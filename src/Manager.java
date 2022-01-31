import java.util.ArrayList;
import java.util.HashMap;

public class Manager {
    static Integer id = 0; // Уникальный идентификационный номер задачи
    static HashMap<Integer, ArrayList<Task>> taskMap; // Хеш-таблица всех задач/эпиков/подзадач

    public Manager() {
        taskMap = new HashMap<>();
    }

    public static void addTask(Task task) { // Метод для добавления задачи
        if (!task.isEpic() && !task.isSubtask()) {
            task.setStatus("NEW");
            task.setId(id);
            ArrayList<Task> taskArray = new ArrayList<>();
            taskArray.add(task);
            taskMap.put(id, taskArray);
            ++id;
        } else {
            System.out.println("При добавлениии задачи произошла обшибка. Данные не внесены");
        }
    }

    public static void addEpic(Task epic) { // Метод для добавления эпика
        if (epic.isEpic() && !epic.isSubtask()) {
            epic.setStatus("NEW");
            epic.setId(id);
            ArrayList<Task> taskArray = new ArrayList<>();
            taskArray.add(epic);
            taskMap.put(id, taskArray);
            ++id;
        } else {
            System.out.println("При добавлениии эпика произошла обшибка. Данные не внесены");
        }
    }

    public static void addSubtask(Integer epicID, Task subtask) { // Метод для добавления подзадачи
        if (taskMap.containsKey(epicID)) {
            if (subtask.isSubtask() && !subtask.isEpic()) {
                ArrayList<Task> taskArray = taskMap.get(epicID);
                subtask.setStatus("NEW");
                subtask.setId(id);
                taskArray.add(subtask);
                ++id;
                changeEpicStatus(epicID);
            }
        } else {
            System.out.println("При добавлениии подзадачи произошла обшибка. Данные не внесены");
        }
    }

    public static void printAllTasks() { // Метод для печати всех задач
        if (!taskMap.isEmpty()) {
            for (Integer keyTask : taskMap.keySet()) {
                ArrayList<Task> taskArray = taskMap.get(keyTask);
                for (Task task : taskArray) {
                    System.out.println(task);
                }
            }
        } else {
            System.out.println("Трекер задач пуст");
        }
    }

    public static void pintTaskByID(Integer taskID) { // Метод для печати задачи по ID задачи
        if (!taskMap.isEmpty()) {
            if (taskMap.containsKey(taskID)) {
                ArrayList<Task> taskArray = taskMap.get(taskID);
                Task task = taskArray.get(0);
                System.out.println(task);
            } else {
                for (Integer epicID : taskMap.keySet()) {
                    ArrayList<Task> taskArray = taskMap.get(epicID);
                    for (Task task : taskArray) {
                        if (task.getID() == taskID) {
                            System.out.println(task);
                        }
                    }
                }
            }
        } else {
            System.out.println("Задача с данным ID отсутсвует");
        }
    }

    public static void printSubtaskInsideEpicByID(Integer epicID) { // Метод для печати подзадач эпика по ID эпика
        if (taskMap.containsKey(epicID)) {
            ArrayList<Task> taskArray = taskMap.get(epicID);
            Task epic = taskArray.get(0);
            if (epic.isEpic()) {
                if (taskArray.size() > 1) {
                    for (Task subtask : taskArray) {
                        if (subtask.getID() != epicID) {
                            System.out.println(subtask);
                        }
                    }
                } else {
                    System.out.println("Данный эпик не содержит подзадач");
                }
            } else {
                System.out.println("Данный ID принадлежит задаче");
            }
        } else {
            System.out.println("Эпик с данным ID отсутсвует");
        }
    }

    public static void updateTaskByID(Task task) { // Метод для обновления задачи по ID задачи
        Integer taskID = task.getID();
        if (!taskMap.isEmpty() && taskMap.containsKey(taskID)) {
            ArrayList<Task> taskArray = taskMap.get(taskID);
            Task oldTask = taskArray.get(0);
            if (!oldTask.isEpic() && !oldTask.isSubtask()) {
                taskArray.set(0, task);
            }
        } else {
            System.out.println("При обновлениии задачи произошла обшибка. Данные не внесены");
        }
    }

    public static void updateEpicByID(Task epic) { // Метод для обновления эпика по ID эпика
        Integer epicID = epic.getID();
        if (!taskMap.isEmpty() && taskMap.containsKey(epicID)) {
            if (epic.isEpic() && !epic.isSubtask()) {
                ArrayList<Task> taskArray = taskMap.get(epicID);
                taskArray.set(0, epic);
            }
        } else {
            System.out.println("При обновлениии эпика произошла обшибка. Данные не внесены");
        }
    }

    public static void updateSubtaskByID(Task subtask) { // Метод для обновления подзадачи по ID подзадачи
        Integer subtaskID = subtask.getID();
        if (!taskMap.isEmpty()) {
            for (Integer epicID : taskMap.keySet()) {
                ArrayList<Task> taskArray = taskMap.get(epicID);
                for (Task oldSubtask : taskArray) {
                    if (oldSubtask.getID().equals(subtaskID)) {
                        if (subtask.isSubtask() && !subtask.isEpic()) {
                            int subtaskPosition = taskArray.indexOf(oldSubtask);
                            taskArray.set(subtaskPosition, subtask);
                            changeEpicStatus(epicID);
                        }
                    }
                }
            }
        } else {
            System.out.println("При обновлениии подзадачи произошла обшибка. Данные не внесены");
        }
    }

    public static void deleteTaskByID(Integer taskID) { // Метод для удаления задачи любого типа по ID
        if (!taskMap.isEmpty()) {
            if (taskMap.containsKey(taskID)) {
                taskMap.remove(taskID);
            } else {
                for (Integer epicID : taskMap.keySet()) {
                    ArrayList<Task> taskArray = taskMap.get(epicID);
                    for (Task subtask : taskArray) {
                        if (subtask.getID() == taskID) {
                            taskArray.remove(subtask);
                            changeEpicStatus(epicID);
                            break;
                        }
                    }
                }
            }
        } else {
            System.out.println("Задача с данным ID отсутсвует");
        }
    }

    public static void deleteAllTasks() { // Метод для удаления всех задач
        if (!taskMap.isEmpty()) {
            taskMap.clear();
        } else {
            System.out.println("Трекер задач пуст");
        }
    }

    private static void changeEpicStatus(Integer epicID) { // Метод для проверки и обновления статуса эпика
        ArrayList<Task> taskArray = taskMap.get(epicID);
        if (!taskArray.isEmpty()) {
            Task epic = taskArray.get(0);
            if (taskArray.size() == 1) {
                epic.setStatus("NEW");
                taskMap.put(epicID, taskArray);
            } else {
                int n = 0;
                int i = 0;
                int d = 0;
                for (Task subtask : taskArray) {
                    if (subtask.isSubtask()) {
                        switch (subtask.getStatus()) {
                            case ("NEW"):
                                ++n;
                                break;
                            case ("IN_PROGRESS"):
                                ++i;
                                break;
                            case ("DONE"):
                                ++d;
                                break;
                            default:
                                System.out.println("Обнаружен ошибочный статус");
                        }
                    }
                }
                if (n == taskArray.size() - 1) {
                    epic.setStatus("NEW");
                    taskMap.put(epicID, taskArray);
                } else if (d == taskArray.size() - 1) {
                    epic.setStatus("DONE");
                    taskMap.put(epicID, taskArray);
                } else if ((n + i + d) == taskArray.size() - 1) {
                    epic.setStatus("IN_PROGRESS");
                    taskMap.put(epicID, taskArray);
                } else {
                    System.out.println("Обнаружена ошибка в статусах");
                }
            }
        }
    }
}


