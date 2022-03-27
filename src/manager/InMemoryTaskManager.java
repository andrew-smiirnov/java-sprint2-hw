package manager;

import model.*;
import java.util.List;
import java.util.HashMap;
import java.util.Map;

public class InMemoryTaskManager implements TaskManager {

    protected HistoryManager historyManager;
    protected Integer taskId; // Уникальный идентификационный номер задачи
    protected Map<Integer, Task> taskMap; // Хеш-таблица всех задач/эпиков/подзадач


    public InMemoryTaskManager() {
        taskMap = new HashMap<>();
        historyManager = Managers.getDefaultHistory();
        taskId = 0;
    }

    public Map<Integer, Task> getTaskMap() {
        return taskMap;
    }

    public void addSimpleTask(SimpleTask simpleTask) throws ManagerSaveException { // Добавить новую задачу
        if (simpleTask.getId() == null) {
            simpleTask.setId(getNewTaskId());
            taskMap.put(simpleTask.getId(), simpleTask);
        } else {
            System.out.println("При добавлениии задачи произошла обшибка. Данные не внесены");
        }
    }

    public void addEpic(Epic epic) throws ManagerSaveException { // Добавить новый эпик
        if (epic.getId() == null) {
            epic.setStatus(TaskStatus.NEW);
            epic.setId(getNewTaskId());
            taskMap.put(epic.getId(), epic);
        } else {
            System.out.println("При добавлениии эпика произошла обшибка. Данные не внесены");
        }
    }

    public void addSubtask(Integer epicId, Subtask subtask) throws ManagerSaveException { // Добавить новую подзадачу
        if (taskMap.containsKey(epicId)) {
            subtask.setId(getNewTaskId());
            subtask.setEpicId(epicId);
            taskMap.put(subtask.getId(), subtask);
            Epic epic = (Epic) taskMap.get(epicId);
            List<Integer> subtasks = epic.getSubtasks();
            subtasks.add(subtask.getId());
            changeEpicStatus(epicId);
        } else {
            System.out.println("Эпик с данным ID отсутсвует");
        }
    }

    public void printAllTasks() { // Печать всех задач
        if (taskMap.isEmpty()) {
            System.out.println("В трекере задач нет задач");
            return;
        }
        for (Integer keyTask : taskMap.keySet()) {
            Task task = taskMap.get(keyTask);
            System.out.println(task);
        }
    }

    public void pintSimpleTaskById(Integer simpleTaskId) { // Печати задачи по ID задачи
        if (taskMap.isEmpty()) {
            System.out.println("В трекере задач нет задач");
            return;
        }
        if (taskMap.containsKey(simpleTaskId)) {
            SimpleTask simpleTask = (SimpleTask) taskMap.get(simpleTaskId);
            System.out.println(simpleTask);
        } else {
            System.out.println("Задача с данным ID отсутсвует");
        }
    }

    public void printSubtaskInsideEpicById(Integer epicId) { // Печать подзадач эпика по ID эпика
        if (taskMap.isEmpty()) {
            System.out.println("В трекере задач нет задач");
            return;
        }
        if (taskMap.containsKey(epicId)) {
            Epic epic = (Epic) taskMap.get(epicId);
            List<Integer> subtasks = epic.getSubtasks();
            if (!subtasks.isEmpty()) {
                for (Integer subtaskId : subtasks) {
                    Subtask subtask = (Subtask) taskMap.get(subtaskId);
                    System.out.println(subtask);
                }
            } else {
                System.out.println("Данный эпик не содержит подзадач");
            }
        } else {
            System.out.println("Эпик с данным ID отсутсвует");
        }
    }

    public void updateSimpleTaskById(SimpleTask simpleTask) throws ManagerSaveException { // Обновление задачи по ID задачи
        if (taskMap.isEmpty()) {
            System.out.println("В трекере задач нет задач");
            return;
        }
        if (taskMap.containsKey(simpleTask.getId())) {
            taskMap.put(simpleTask.getId(), simpleTask);
        } else {
            System.out.println("При обновлениии задачи произошла обшибка. Данные не внесены");
        }
    }

    public void updateEpicById(Epic epic) throws ManagerSaveException { // Ообновление эпика по ID эпика
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

    public void updateSubtaskById(Subtask subtask) throws ManagerSaveException { // Обновления подзадачи по ID подзадачи
        if (taskMap.isEmpty()) {
            System.out.println("В трекере задач нет эпиков");
            return;
        }
        if (taskMap.containsKey(subtask.getId())) {
            Subtask oldSubtask = (Subtask) taskMap.get(subtask.getId());
            subtask.setEpicId(oldSubtask.getEpicId());
            taskMap.put(subtask.getId(), subtask);
            changeEpicStatus(subtask.getEpicId());
        } else {
            System.out.println("Подзадача с данным ID отсутсвует");
        }
    }

    public void deleteSimpleTaskById(Integer simpleTaskId) throws ManagerSaveException { // Удаление задачи по ID
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
            taskMap.remove(simpleTaskId);
            historyManager.remove(simpleTaskId);
        } else {
            System.out.println("Данный ID не принадлежит задаче");
        }
    }

    public void deleteSubtaskById(Integer subtaskId) throws ManagerSaveException { // Удаление подзадачи по ID
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
            taskMap.remove(subtaskId);
            historyManager.remove(subtaskId);
            changeEpicStatus(epic.getId());
        } else {
            System.out.println("Данный ID не принадлежит подзадаче");
        }
    }

    public void deleteEpicById(Integer epicId) throws ManagerSaveException { // Удаление эпика по ID
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
                historyManager.remove(subtaskId);
                taskMap.remove(subtaskId);
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
        } else {
            System.out.println("Трекер задач пуст");
        }
    }

    public void getSimpleTask(Integer simpleTaskId) throws ManagerSaveException {  // Получение задачи по ID
        if (taskMap.isEmpty()) {
            System.out.println("В трекере задач нет задач");
            return;
        }
        if (!taskMap.containsKey(simpleTaskId)) {
            System.out.println("Задача с данным ID отсутсвует");
            return;
        }
        Task task = taskMap.get(simpleTaskId);
        if (task.getTypeOfTask().equals(TypeOfTask.SIMPLE_TASK)) {
            System.out.println(task);
            historyManager.add(task);
        } else {
            System.out.println("ID не принадлежит задаче");
        }
    }

    public void getSubtask(Integer subtaskId) throws ManagerSaveException {  // Получение подзадачи по ID
        if (taskMap.isEmpty()) {
            System.out.println("В трекере задач нет задач");
            return;
        }
        if (!taskMap.containsKey(subtaskId)) {
            System.out.println("Подзадача с данным ID отсутсвует");
            return;
        }
        Task task = taskMap.get(subtaskId);
        if (task.getTypeOfTask().equals(TypeOfTask.SUBTASK)) {
            Subtask subtask = (Subtask) task;
            System.out.println(subtask);
            historyManager.add(subtask);
        } else {
            System.out.println("ID не принадлежит подзадаче");
        }
    }

    public void getEpic(Integer epicId) throws ManagerSaveException {  // Получение эпика по ID
        if (taskMap.isEmpty()) {
            System.out.println("В трекере задач нет задач");
            return;
        }
        if (!taskMap.containsKey(epicId)) {
            System.out.println("Эпик с данным ID отсутсвует");
            return;
        }
        Task task = taskMap.get(epicId);
        if (task.getTypeOfTask().equals(TypeOfTask.EPIC)) {
            System.out.println(task);
            historyManager.add(task);
        } else {
            System.out.println("ID не принадлежит эпику");
        }
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
                } else if (d == subtasksId.size() - 1) {
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
}


