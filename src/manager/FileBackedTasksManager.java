package manager;

import extentions.ManagerSaveException;
import model.*;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.*;


public class FileBackedTasksManager extends InMemoryTaskManager {

    protected File file;


    public FileBackedTasksManager() {
        this.file = new File("src/files/history.csv");
    }

    public FileBackedTasksManager(File file) {
        this.file = file;
    }

    public FileBackedTasksManager(File file, HashMap<Integer, Task> taskMap, HistoryManager historyManager,
                                  Set<Task> listTasksSortedByTime, Integer taskId) {
        super();
        this.file = file;
        this.taskMap = taskMap;
        this.historyManager = historyManager;
        this.listTasksSortedByTime = listTasksSortedByTime;
        this.taskId = taskId;
    }


    public static FileBackedTasksManager loadFromFile(File file) throws ManagerSaveException {
        HashMap<Integer, Task> taskMap = new HashMap<>();
        HistoryManager history = new InMemoryHistoryManager();
        List<String> taskInLine = new ArrayList<>();
        Integer taskId = -1;
        Set <Task> timeSortedList = new TreeSet<>(treeSetComparator);
        // Считаем все строки из CSV файла в список taskInLine
        if (file.canRead()) {
            try (BufferedReader fileReader = new BufferedReader(new FileReader(file, StandardCharsets.UTF_8))) {
                while (fileReader.ready()) {
                    taskInLine.add(fileReader.readLine());
                }
            } catch (IOException e) {
                throw new ManagerSaveException("Произошла ошибка во время чтения файла " + file.getPath(), e);
            }
        }
        // Если в списке taskInLine есть строки с задачами, то они попадают в мапу с задачами
        if (!taskInLine.isEmpty()) {
            for (int i = 0; i < taskInLine.size(); i++) {
                if (taskInLine.get(i).length() > 1) {
                    String[] taskSplit = taskInLine.get(i).split(",");
                    taskMap.put(Integer.parseInt(taskSplit[0]), taskFromString(taskInLine.get(i)));
                    taskId = Integer.parseInt(taskSplit[0]);
                    // Все подзадачи добавляются в список подзадач эпика
                    if (taskSplit.length == 8) {
                        Epic epic = (Epic) taskMap.get(Integer.parseInt(taskSplit[7]));
                        List<Integer> subtasks = epic.getSubtasks();
                        subtasks.add(Integer.parseInt(taskSplit[0]));
                    }
                } else break;
            }
            // если предпоследняя строка пустая, а последняя нет, то восстанавливается история просмотров
            if (taskInLine.get(taskInLine.size() - 2).isEmpty() && !taskInLine.get(taskInLine.size() - 1).isEmpty()) {
                String[] historyViewsId = taskInLine.get(taskInLine.size() - 1).split(",");
                for (String taskNum : historyViewsId) {
                    if (taskMap.containsKey(Integer.parseInt(taskNum))) {
                        history.add(taskMap.get(Integer.parseInt(taskNum)));
                    }
                }
            }
            // Если в мапе есть задачи, то они попадают в сортированный по времени список
            if (!taskMap.isEmpty()) {
                for (Task task : taskMap.values()) {
                    if (!task.getTypeOfTask().equals(TypeOfTask.EPIC)) {
                        timeSortedList.add(task);
                    }
                }
            }
        }
        return new FileBackedTasksManager(file, taskMap, history, timeSortedList, (taskId + 1));
    }

    private String toString(Task task) throws IllegalStateException { //Запись полей задачи в строку
        StringBuilder builder = new StringBuilder();
        builder.append(task.getId() + ",");
        builder.append(task.getTypeOfTask() + ",");
        builder.append(task.getTitle() + ",");
        builder.append(task.getStatus() + ",");
        builder.append(task.getDescription() + ",");
        builder.append(task.getStartTimeToStringForBackup() + ",");
        builder.append(task.getDurationToString() + ",");
        if (task.getTypeOfTask().equals(TypeOfTask.SUBTASK)) {
            builder.append(((Subtask) task).getEpicId() + ",");
        }
        return builder.toString();
    }

    public static Task taskFromString(String taskString){ // Получить объект типа Task из строки
        String[] taskSplit = taskString.split(",");
        switch (taskSplit[1]) {
            case "SIMPLE_TASK":
                return new SimpleTask(Integer.parseInt(taskSplit[0]), taskSplit[2], taskSplit[4],
                        TaskStatus.valueOf(taskSplit[3]), taskSplit[5], taskSplit[6]);
            case "EPIC":
                return new Epic(Integer.parseInt(taskSplit[0]), taskSplit[2], taskSplit[4],
                        TaskStatus.valueOf(taskSplit[3]), taskSplit[5], taskSplit[6]);
            case "SUBTASK":
                Subtask subtask = new Subtask(Integer.parseInt(taskSplit[0]), taskSplit[2], taskSplit[4],
                        TaskStatus.valueOf(taskSplit[3]), taskSplit[5], taskSplit[6]);
                subtask.setEpicId(Integer.parseInt(taskSplit[7]));
                return subtask;
            default:
                return null;
        }
    }

    public static String toString(HistoryManager manager) {  // Получить список просмотренных задач из менеджера истории
        List<Task> historyViews = manager.getHistory();
        String tasksInLine = "";
        if (!historyViews.isEmpty()) {
            StringBuilder tasks = new StringBuilder();
            for (Task historyView : historyViews) {
                tasks.append(historyView.getId() + ",");
            }
            tasksInLine = tasks.toString();
        }
        return tasksInLine;
    }

    private void save()  { // Метод сериализации менеджера задач
        if (!getTaskMap().isEmpty()) {
            try (BufferedWriter fileWriter = new BufferedWriter(new FileWriter("src/files/history.tmp",
                    StandardCharsets.UTF_8, true))) {
                for (Integer key : taskMap.keySet()) {
                    fileWriter.write(toString(taskMap.get(key)));
                    fileWriter.append('\n');
                }
                fileWriter.append('\n');
                fileWriter.append(toString(historyManager));
            } catch (IOException e) {
                throw new ManagerSaveException("Не удалось записать информацию о задачах в файл history.tmp", e);
            }
            // tmp файл используется чтобы не повредить основной файл сериализации до завершения всех операций
            File tmp = new File("src/files/history.tmp");
            file.delete();
            tmp.renameTo(file);
        }
    }

    @Override
    public void addSimpleTask(SimpleTask simpleTask) {
        super.addSimpleTask(simpleTask);
        save();
    }

    @Override
    public void addEpic(Epic epic) {
        super.addEpic(epic);
        save();
    }

    @Override
    public void addSubtask(Integer epicId, Subtask subtask) {
        super.addSubtask(epicId, subtask);
        save();
    }

    @Override
    public SimpleTask getSimpleTask(Integer simpleTaskId) {
        SimpleTask simpleTask = super.getSimpleTask(simpleTaskId);
        save();
        return simpleTask;
    }

    @Override
    public Subtask getSubtask(Integer subtaskId) {
        Subtask subtask = super.getSubtask(subtaskId);
        save();
        return subtask;
    }

    @Override
    public Epic getEpic(Integer epicId) {
        Epic epic = super.getEpic(epicId);
        save();
        return epic;
    }

    @Override
    public void updateSimpleTaskById(SimpleTask simpleTask) {
        super.updateSimpleTaskById(simpleTask);
        save();
    }

    @Override
    public void updateSubtaskById(Subtask subtask) {
        super.updateSubtaskById(subtask);
        save();
    }

    @Override
    public void updateEpicById(Epic epic) {
        super.updateEpicById(epic);
        save();
    }

    @Override
    public void deleteSimpleTaskById(Integer simpleTaskId) {
        super.deleteSimpleTaskById(simpleTaskId);
        save();
    }

    @Override
    public void deleteSubtaskById(Integer subtaskId) {
        super.deleteSubtaskById(subtaskId);
        save();
    }

    @Override
    public void deleteEpicById(Integer epicId) {
        super.deleteEpicById(epicId);
        save();
    }

    @Override
    public void deleteAllTasks() {
        super.deleteAllTasks();
        file.delete();
    }
}
