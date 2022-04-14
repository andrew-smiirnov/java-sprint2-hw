package manager;

import extentions.ManagerSaveException;
import model.*;

import java.time.ZonedDateTime;
import java.util.*;
import java.util.stream.Collectors;


public class InMemoryTaskManager implements TaskManager {

    protected HistoryManager historyManager;
    protected Integer taskId; // Уникальный идентификационный номер задачи
    protected Map<Integer, Task> taskMap; // Хеш-таблица всех задач/эпиков/подзадач
    protected Set<Task> listTasksSortedByTime; // Список отсортированных по времени задач и подзадач


    public InMemoryTaskManager() {
        taskMap = new HashMap<>();
        historyManager = Managers.getDefaultHistory();
        taskId = 0;
        listTasksSortedByTime = new TreeSet<>(treeSetComparator);
    }

    public Map<Integer, Task> getTaskMap() {
        return taskMap;
    }

    public void addSimpleTask(SimpleTask simpleTask) { // Добавить новую задачу
        if (simpleTask.getId() != null) {
            System.out.println("При добавлениии задачи произошла обшибка. Данные не внесены");
            return;
        }
        if (!isFreeTimeForTask(simpleTask)) {
            System.out.println("Задачи пересекаются по времени. Попробуйте задать другое время");
            return;
        }
        simpleTask.setId(getNewTaskId());
        taskMap.put(simpleTask.getId(), simpleTask);
        listTasksSortedByTime.add(simpleTask);
    }

    public void addEpic(Epic epic) { // Добавить новый эпик
        if (epic.getId() == null) {
            epic.setId(getNewTaskId());
            taskMap.put(epic.getId(), epic);
        } else {
            System.out.println("При добавлениии эпика произошла обшибка. Данные не внесены");
        }
    }

    public void addSubtask(Integer epicId, Subtask subtask) { // Добавить новую подзадачу
        if (!taskMap.containsKey(epicId)) {
            System.out.println("Эпик с данным ID отсутсвует");
            return;
        }
        if (!isFreeTimeForTask(subtask)) {
            System.out.println("Задачи пересекаются по времени. Попробуйте задать другое время");
            return;
        }
        subtask.setId(getNewTaskId());
        subtask.setEpicId(epicId);
        taskMap.put(subtask.getId(), subtask);
        listTasksSortedByTime.add(subtask);
        Epic epic = (Epic) taskMap.get(epicId);
        List<Integer> subtasks = epic.getSubtasks();
        subtasks.add(subtask.getId());
        changeEpicStatus(epicId);
        if (subtask.getStartTime() != null) {
            updateEpicTimeSortedList(epicId);
        }
    }

    public void updateEpicTimeSortedList(Integer epicId) {
        Epic epic = (Epic) taskMap.get(epicId);
        List<Integer> subtasks = epic.getSubtasks();
        if (subtasks.isEmpty()) {
            epic.setStartTime(null);
            epic.setDuration(null);
            epic.setEndTime(null);
        } else if (subtasks.size() == 1) {
            Subtask subtaskOne = (Subtask) taskMap.get(subtasks.get(0));
            epic.setStartTime(subtaskOne.getStartTime());
            epic.setEndTime(subtaskOne.getEndTime());
        } else {
            ZonedDateTime epicStartTime = taskMap.get(subtasks.get(0)).getStartTime();
            ZonedDateTime epicEndTime = taskMap.get(subtasks.get(0)).getEndTime();
            for (Integer subtaskId : subtasks) {
                Subtask subtask = (Subtask) taskMap.get(subtaskId);
                if (epicStartTime.isAfter(subtask.getStartTime())) {
                    epicStartTime = subtask.getStartTime();
                }
                if (epicEndTime.isBefore(subtask.getEndTime())) {
                    epicEndTime = subtask.getEndTime();
                }
            }
            epic.setStartTime(epicStartTime);
            epic.setEndTime(epicEndTime);
        }
    }

    public void updateSimpleTaskById(SimpleTask simpleTask) { // Обновление задачи по ID задачи
        if (taskMap.isEmpty()) {
            System.out.println("В трекере задач нет задач");
            return;
        }
        if (!taskMap.containsKey(simpleTask.getId())) {
            System.out.println("При обновлениии задачи произошла обшибка. Данные не внесены");
            return;
        }
        if (!isFreeTimeForTask(simpleTask)) {
            System.out.println("Задачи пересекаются по времени. Попробуйте задать другое время");
            return;
        }
        listTasksSortedByTime.remove(taskMap.get(simpleTask.getId()));
        taskMap.put(simpleTask.getId(), simpleTask);
        listTasksSortedByTime.add(simpleTask);
    }

    public void updateEpicById(Epic epic) { // Ообновление эпика по ID эпика
        if (taskMap.isEmpty()) {
            System.out.println("В трекере задач нет задач");
            return;
        }
        if (taskMap.containsKey(epic.getId())) {
            Epic oldEpic = (Epic) taskMap.get(epic.getId());
            List<Integer> subtasks = oldEpic.getSubtasks();
            epic.setSubtasks(subtasks);
            changeEpicStatus(epic.getId());
            taskMap.put(epic.getId(), epic);
        } else {
            System.out.println("Эпик с данным id не найден. Данные не внесены");
        }
    }

    public void updateSubtaskById(Subtask subtask) { // Обновления подзадачи по ID подзадачи
        if (taskMap.isEmpty()) {
            System.out.println("В трекере задач нет эпиков");
            return;
        }
        if (!taskMap.containsKey(subtask.getId())) {
            System.out.println("Подзадача с данным ID отсутсвует");
            return;
        }
        if (!isFreeTimeForTask(subtask)) {
            System.out.println("Задачи пересекаются по времени. Попробуйте задать другое время");
            return;
        }
        listTasksSortedByTime.remove(taskMap.get(subtask.getId()));
        Subtask oldSubtask = (Subtask) taskMap.get(subtask.getId());
        subtask.setEpicId(oldSubtask.getEpicId());
        taskMap.put(subtask.getId(), subtask);
        listTasksSortedByTime.add(subtask);
        changeEpicStatus(subtask.getEpicId());
        if (subtask.getStartTime() != null) {
            updateEpicTimeSortedList(subtask.getEpicId());
        }
    }

    public Boolean isFreeTimeForTask(Task newTask) {
        if (newTask.getStartTime() == null) {
            return true;
        }
        Boolean isTimeFree = false;
        List<Task> allTasks = getPrioritizedTasks().
                stream().
                filter(Task -> Task.getStartTime() != null).
                collect(Collectors.toList());
        int index = -1;
        int first = 0;
        int last = allTasks.size() - 1;
        while (first <= last) {
            int middle = (first + last) / 2;
            if (allTasks.get(middle).getStartTime().isBefore(newTask.getEndTime()) &&
                    allTasks.get(middle).getEndTime().isAfter(newTask.getStartTime())
            ) {
                index = middle;
                break;
            } else if (allTasks.get(middle).getEndTime().minusSeconds(30).isBefore(newTask.getStartTime())) {
                first = middle + 1;
            } else if (allTasks.get(middle).getStartTime().isAfter(newTask.getEndTime().minusSeconds(30))) {
                last = middle - 1;
            }
        }
        if (index == -1) isTimeFree = true;
        return isTimeFree;
    }

    public void deleteSimpleTaskById(Integer simpleTaskId) { // Удаление задачи по ID
        if (taskMap.isEmpty()) {
            System.out.println("Трекер задач пуст");
            return;
        }
        if (!taskMap.containsKey(simpleTaskId)) {
            System.out.println("Задача с данным ID отсутсвует");
            return;
        }
        Task task = taskMap.get(simpleTaskId);
        if (task.getTypeOfTask().equals(TypeOfTask.SIMPLE_TASK)) {
            listTasksSortedByTime.remove(task);
            taskMap.remove(simpleTaskId);
            historyManager.remove(simpleTaskId);
        } else {
            System.out.println("Данный ID не принадлежит задаче");
        }
    }

    public void deleteSubtaskById(Integer subtaskId) { // Удаление подзадачи по ID
        if (taskMap.isEmpty()) {
            System.out.println("Трекер задач пуст");
            return;
        }
        if (!taskMap.containsKey(subtaskId)) {
            System.out.println("Подзадача с данным ID отсутсвует");
            return;
        }
        Task task = taskMap.get(subtaskId);
        if (task.getTypeOfTask().equals(TypeOfTask.SUBTASK)) {
            Subtask subtask = (Subtask) taskMap.get(subtaskId);
            Epic epic = (Epic) taskMap.get(subtask.getEpicId());
            List<Integer> subtasks = epic.getSubtasks();
            subtasks.remove(subtaskId);
            listTasksSortedByTime.remove(taskMap.get(subtaskId));
            if (subtask.getStartTime() != null) {
                updateEpicTimeSortedList(subtask.getEpicId());
            }
            taskMap.remove(subtaskId);
            historyManager.remove(subtaskId);
        } else {
            System.out.println("Данный ID не принадлежит подзадаче");
        }
    }

    public void deleteEpicById(Integer epicId) { // Удаление эпика по ID
        if (taskMap.isEmpty()) {
            System.out.println("Трекер задач пуст");
            return;
        }
        if (!taskMap.containsKey(epicId)) {
            System.out.println("Задача с данным ID отсутсвует");
            return;
        }
        Task task = taskMap.get(epicId);
        if (task.getTypeOfTask().equals(TypeOfTask.EPIC)) {
            Epic epic = (Epic) taskMap.get(epicId);
            List<Integer> subtasks = epic.getSubtasks();
            for (Integer subtaskId : subtasks) {
                listTasksSortedByTime.remove(taskMap.get(subtaskId));
                taskMap.remove(subtaskId);
                historyManager.remove(subtaskId);
            }
            historyManager.remove(epicId);
            taskMap.remove(epicId);
        } else {
            System.out.println("Данный ID не принадлежит эпику");
        }
    }

    public void deleteAllTasks() throws ManagerSaveException { // Удаление всех задач
        if (!taskMap.isEmpty()) {
            taskMap.clear();
            historyManager.clearHistoryList();
            listTasksSortedByTime.clear();
        } else {
            System.out.println("Трекер задач пуст");
        }
    }

    public SimpleTask getSimpleTask(Integer simpleTaskId){  // Получение задачи по ID
        if (taskMap.isEmpty()) {
            System.out.println("В трекере задач нет задач");
            return null;
        }
        if (!taskMap.containsKey(simpleTaskId)) {
            System.out.println("Задача с данным ID отсутсвует");
            return null;
        }
        SimpleTask task = (SimpleTask) taskMap.get(simpleTaskId);
        if (task.getTypeOfTask().equals(TypeOfTask.SIMPLE_TASK)) {
            historyManager.add(task);
        } else {
            System.out.println("ID не принадлежит задаче");
        }
        return task;
    }

    public Subtask getSubtask(Integer subtaskId){  // Получение подзадачи по ID
        if (taskMap.isEmpty()) {
            System.out.println("В трекере задач нет задач");
            return null;
        }
        if (!taskMap.containsKey(subtaskId)) {
            System.out.println("Подзадача с данным ID отсутсвует");
            return null;
        }
        Subtask subtask = (Subtask) taskMap.get(subtaskId);
        if (subtask.getTypeOfTask().equals(TypeOfTask.SUBTASK)) {
            historyManager.add(subtask);
        } else {
            System.out.println("ID не принадлежит подзадаче");
        }
        return subtask;
    }

    public Epic getEpic(Integer epicId) {  // Получение эпика по ID
        if (taskMap.isEmpty()) {
            System.out.println("В трекере задач нет задач");
            return null;
        }
        if (!taskMap.containsKey(epicId)) {
            System.out.println("Эпик с данным ID отсутсвует");
            return null;
        }
        Epic epic = (Epic) taskMap.get(epicId);
        if (epic.getTypeOfTask().equals(TypeOfTask.EPIC)) {
            historyManager.add(epic);
        } else {
            System.out.println("ID не принадлежит эпику");
        }
        return epic;
    }

    private void changeEpicStatus(Integer epicId) { // Обновление статуса эпика
        Epic epic = (Epic) taskMap.get(epicId);
        List<Integer> subtasksId = epic.getSubtasks();
        if (!subtasksId.isEmpty()) {
            if (subtasksId.size() == 1) {
                Subtask subtask = (Subtask) taskMap.get(subtasksId.get(0));
                epic.setStatus(subtask.getStatus());
            } else {
                int n = 0;
                int i = 0;
                int d = 0;
                for (Integer subtaskId : subtasksId) {
                    Subtask subtask = (Subtask) taskMap.get(subtaskId);
                    switch (subtask.getStatus()) {
                        case NEW:
                            ++n;
                            break;
                        case IN_PROGRESS:
                            ++i;
                            break;
                        case DONE:
                            ++d;
                            break;
                        default:
                            System.out.println("Обнаружен ошибочный статус");
                    }
                }
                if (n == subtasksId.size()) {
                    epic.setStatus(TaskStatus.NEW);
                } else if (d == subtasksId.size()) {
                    epic.setStatus(TaskStatus.DONE);
                } else if ((n + i + d) == subtasksId.size()) {
                    epic.setStatus(TaskStatus.IN_PROGRESS);
                } else {
                    System.out.println("Обнаружена ошибка в статусах эпика" + epicId);
                }
            }
        }
    }

    public Integer getNewTaskId() {  // Обновление уникального ID для задач
        return taskId++;
    }

    public List<Task> history() {  // Получение списка истории
        return historyManager.getHistory();
    }

    public Set<Task> getPrioritizedTasks() {
        return listTasksSortedByTime;
    }

    static Comparator<Task> treeSetComparator = new Comparator<Task>() {
        @Override
        public int compare(Task o1, Task o2) {
            if(o1.getId() == o2.getId()) return 0;
            if(o1.getStartTime() == null || o2.getStartTime() == null){
                return o1.getId().compareTo(o2.getId());
            }
            if(o2.getStartTime() == null) return -1;
            return o1.getStartTime().compareTo(o2.getStartTime());
        }
    };
}


