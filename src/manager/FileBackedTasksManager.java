package manager;

import model.*;

import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.io.*;


public class FileBackedTasksManager extends InMemoryTaskManager {

    protected File file;
    protected Task task;


    public FileBackedTasksManager() {}

    public FileBackedTasksManager(File file) {
        this.file = file;
    }


    public static void main(String[] args) throws ManagerSaveException {

        File file = new File("src/files/history.csv");
        TaskManager restoredManager = new FileBackedTasksManager().loadFromFile(file);

        SimpleTask task3 = new SimpleTask("Задача №3", "Добавить сериализацию", null, TaskStatus.NEW);
        restoredManager.addSimpleTask(task3);
        restoredManager.getSimpleTask(7);

        System.out.println();
        System.out.println("----- Задачи/эпики/подзадачи восстановленного менеджера -----");
        restoredManager.printAllTasks();

        System.out.println();
        System.out.println("----- История просмотров getTask, getEpic, getSubtask восстановленного менеджера -----");
        for (Task task : restoredManager.history()){
            System.out.println("id=" + task.getId() + " , status: " + task.getStatus());
        }
    }

    public static FileBackedTasksManager loadFromFile(File file) throws ManagerSaveException {
        FileBackedTasksManager backupManager = new FileBackedTasksManager(file);
        List<String> taskInLine = new ArrayList<>();
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
        // Если в списке taskInLine есть строки с задачами, то они попадают в backupManager
        if (!taskInLine.isEmpty()) {
            for (int i = 0; i < taskInLine.size(); i++) {
                if (taskInLine.get(i).length() > 0) {
                    String[] taskSplit = taskInLine.get(i).split(",");
                    Integer iD = -1;
                    /* Последнее значение taskId в менеджере задач принимает значение последней задачи из списка
                     * истории, чтобы после восстановления не было перезаписи задач с одинаковыми ID
                     */
                    while (Integer.parseInt(taskSplit[0]) != iD) {
                        iD = backupManager.getNewTaskId() + 1;
                    }
                    if ((taskSplit[1]).equals("SIMPLE_TASK")) {
                        SimpleTask simpleTask = new SimpleTask(taskSplit[2], taskSplit[4], null,
                                TaskStatus.valueOf(taskSplit[3]));
                        backupManager.addSimpleTask(simpleTask);
                    } else if ((taskSplit[1]).equals("EPIC")) {
                        Epic epic = new Epic(taskSplit[2], taskSplit[4], null,
                                TaskStatus.valueOf(taskSplit[3]));
                        backupManager.addEpic(epic);
                    } else if ((taskSplit[1]).equals("SUBTASK")) {
                        Subtask subtask = new Subtask(taskSplit[2], taskSplit[4], null,
                                TaskStatus.valueOf(taskSplit[3]));
                        subtask.setEpicId(Integer.parseInt(taskSplit[5]));
                        backupManager.addSubtask(subtask.getEpicId(), subtask);
                    }
                // если предпоследняя строка пустая, а последняя нет, то восстанавливается история просмотров
                } else if (((i + 2) == taskInLine.size()) && (taskInLine.get(i + 1).length() > 0)) {
                    String[] historyOfGetTaskSplit = taskInLine.get(i + 1).split(",");
                    for (String number : historyOfGetTaskSplit) {
                        if (backupManager.taskMap.containsKey(Integer.parseInt(number))) {
                            backupManager.historyManager.add(backupManager.taskMap.get(Integer.parseInt(number)));
                        }
                    }
                    break;
                }
            }
        }
        return backupManager;
    }

    private String toString(Task task) throws IllegalStateException { //Запись полей задачи в строку
        StringBuilder builder = new StringBuilder();
        builder.append(task.getId() + ",");
        if (task instanceof SimpleTask) {
            builder.append(TypeOfTask.SIMPLE_TASK + ",");
        } else if (task instanceof Subtask) {
            builder.append(TypeOfTask.SUBTASK + ",");
        } else if (task instanceof Epic) {
            builder.append(TypeOfTask.EPIC + ",");
        } else {
            throw new IllegalStateException();
        }
        builder.append(task.getTitle() + ",");
        builder.append(task.getStatus() + ",");
        builder.append(task.getDescription() + ",");
        if (task instanceof Subtask) {
            builder.append(((Subtask) task).getEpicId() + ",");
        }
        return builder.toString();
    }

    public Task taskFromString(String taskString){ // Получить объект типа Task из строки
        String[] taskSplit = taskString.split(",");
        Integer iD = -1;
        while (Integer.parseInt(taskSplit[0]) != iD + 1) {
            iD = getNewTaskId();
        }
        switch (taskSplit[1]) {
            case "SIMPLE_TASK":
                return new SimpleTask(taskSplit[2], taskSplit[4], null, TaskStatus.valueOf(taskSplit[3]));
            case "EPIC":
                return new Epic(taskSplit[2], taskSplit[4], null, TaskStatus.valueOf(taskSplit[3]));
            case "SUBTASK":
                Subtask subtask = new Subtask(taskSplit[2], taskSplit[4], null, TaskStatus.valueOf(taskSplit[3]));
                subtask.setEpicId(Integer.parseInt(taskSplit[5]));
                return subtask;
            default:
                return null;
        }
    }

    public static List<Integer> fromString(String value){ // Получить список ID просмотренных задач из строки
        List<Integer> tasksIdInHistoryList = new ArrayList<>();
        if (!value.isEmpty()){
            String[] tasks = value.split(",");
            for (String taskId : tasks) {
                tasksIdInHistoryList.add(Integer.parseInt(taskId));
            }
        }
        return tasksIdInHistoryList;
    }

    public static String toString(HistoryManager manager) {  // Получить список просмотренных задач из менеджера истории
        List<Task> historyViews = manager.getHistory();
        String tasksInLine = null;
        if (!historyViews.isEmpty()) {
            StringBuilder tasks = new StringBuilder();
            for (Task historyView : historyViews) {
                tasks.append(historyView.getId() + ",");
            }
            tasksInLine = tasks.toString();
        }
        return tasksInLine;
    }

    private void save() { // Метод сериализации менеджера задач
        if (!getTaskMap().isEmpty()) {
            for (Integer key : taskMap.keySet()){
                String taskString = toString(taskMap.get(key));
                try (BufferedWriter historyFile = new BufferedWriter(new FileWriter("src/files/history.tmp",
                        StandardCharsets.UTF_8, true))) {
                    historyFile.write(taskString);
                    historyFile.append('\n');
                } catch (IOException e) {
                    System.out.println ("Произошла ошибка во время чтения файла src/files/history.tmp");
                }
            }
            List<Task> historyViews = history();
            if (!historyViews.isEmpty()) {
                try (BufferedWriter fileWriter = new BufferedWriter
                        (new FileWriter("src/files/history.tmp", StandardCharsets.UTF_8, true))) {
                    fileWriter.append('\n');
                    for (Task historyView : historyViews) {
                        fileWriter.write(historyView.getId() + ",");
                    }
                } catch (IOException e) {
                    System.out.println("Произошла ошибка во время чтения файла src/files/history.tmp");
                }
            }
            // tmp файл используется чтобы не повредить основной файл сериализации до завершения всех операций
            File tmp = new File("src/files/history.tmp");
            String historyFileName = ("src/files/history.csv");
            File historyFile = new File(historyFileName);
            historyFile.delete();
            tmp.renameTo(historyFile);
        }
    }

    @Override
    public void addSimpleTask(SimpleTask simpleTask) {
        super.addSimpleTask(simpleTask);
        this.task = simpleTask;
        save();
    }

    @Override
    public void addEpic(Epic epic) {
        super.addEpic(epic);
        this.task = epic;
        save();
    }

    @Override
    public void addSubtask(Integer epicId, Subtask subtask) {
        super.addSubtask(epicId, subtask);
        this.task = subtask;
        save();
    }

    @Override
    public void getSimpleTask(Integer simpleTaskId) {
        super.getSimpleTask(simpleTaskId);
        save();
    }

    @Override
    public void getSubtask(Integer subtaskId) {
        super.getSubtask(subtaskId);
        save();
    }

    @Override
    public void getEpic(Integer epicId) {
        super.getEpic(epicId);
        save();
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
        save();
    }
}
