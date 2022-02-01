import java.util.ArrayList;
import java.util.HashMap;

public class Manager {
    Integer id = 0; // Уникальный идентификационный номер задачи
    HashMap<Integer, ArrayList<Task>> taskMap; // Хеш-таблица всех задач/эпиков/подзадач

    public Manager() {
        taskMap = new HashMap<>();
    }

    public void addTask(Task task) { // Метод для добавления задачи
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

    public void addEpic(Task epic) { // Метод для добавления эпика
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

    public void addSubtask(Integer epicId, Task subtask) { // Метод для добавления подзадачи
        if (taskMap.containsKey(epicId)) {
            if (subtask.isSubtask() && !subtask.isEpic()) {
                ArrayList<Task> taskArray = taskMap.get(epicId);
                subtask.setStatus("NEW");
                subtask.setId(id);
                taskArray.add(subtask);
                ++id;
                changeEpicStatus(epicId);
            }
        } else {
            System.out.println("При добавлениии подзадачи произошла обшибка. Данные не внесены");
        }
    }

    public void printAllTasks() { // Метод для печати всех задач
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

    public void pintTaskById(Integer taskID) { // Метод для печати задачи по ID задачи
        if (!taskMap.isEmpty()) {
            if (taskMap.containsKey(taskID)) {
                ArrayList<Task> taskArray = taskMap.get(taskID);
                Task task = taskArray.get(0);
                System.out.println(task);
            } else {
                for (Integer epicId : taskMap.keySet()) {
                    ArrayList<Task> taskArray = taskMap.get(epicId);
                    for (Task task : taskArray) {
                        if (task.getId() == taskID) {
                            System.out.println(task);
                        }
                    }
                }
            }
        } else {
            System.out.println("Задача с данным ID отсутсвует");
        }
    }

    public void printSubtaskInsideEpicById(Integer epicId) { // Метод для печати подзадач эпика по ID эпика
        if (taskMap.containsKey(epicId)) {
            ArrayList<Task> taskArray = taskMap.get(epicId);
            Task epic = taskArray.get(0);
            if (epic.isEpic()) {
                if (taskArray.size() > 1) {
                    for (Task subtask : taskArray) {
                        if (subtask.getId() != epicId) {
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

    public void updateTaskById(Task task) { // Метод для обновления задачи по ID задачи
        Integer taskID = task.getId();
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

    public void updateEpicById(Task epic) { // Метод для обновления эпика по ID эпика
        Integer epicId = epic.getId();
        if (!taskMap.isEmpty() && taskMap.containsKey(epicId)) {
            if (epic.isEpic() && !epic.isSubtask()) {
                ArrayList<Task> taskArray = taskMap.get(epicId);
                taskArray.set(0, epic);
            }
        } else {
            System.out.println("При обновлениии эпика произошла обшибка. Данные не внесены");
        }
    }

    public void updateSubtaskById(Task subtask) { // Метод для обновления подзадачи по ID подзадачи
        Integer subtaskID = subtask.getId();
        if (!taskMap.isEmpty()) {
            for (Integer epicId : taskMap.keySet()) {
                ArrayList<Task> taskArray = taskMap.get(epicId);
                for (Task oldSubtask : taskArray) {
                    if (oldSubtask.getId().equals(subtaskID)) {
                        if (subtask.isSubtask() && !subtask.isEpic()) {
                            int subtaskPosition = taskArray.indexOf(oldSubtask);
                            taskArray.set(subtaskPosition, subtask);
                            changeEpicStatus(epicId);
                        }
                    }
                }
            }
        } else {
            System.out.println("При обновлениии подзадачи произошла обшибка. Данные не внесены");
        }
    }

    public void deleteTaskById(Integer taskID) { // Метод для удаления задачи любого типа по ID
        if (!taskMap.isEmpty()) {
            if (taskMap.containsKey(taskID)) {
                taskMap.remove(taskID);
            } else {
                for (Integer epicId : taskMap.keySet()) {
                    ArrayList<Task> taskArray = taskMap.get(epicId);
                    for (Task subtask : taskArray) {
                        if (subtask.getId() == taskID) {
                            taskArray.remove(subtask);
                            changeEpicStatus(epicId);
                            break;
                        }
                    }
                }
            }
        } else {
            System.out.println("Задача с данным ID отсутсвует");
        }
    }

    public void deleteAllTasks() { // Метод для удаления всех задач
        if (!taskMap.isEmpty()) {
            taskMap.clear();
        } else {
            System.out.println("Трекер задач пуст");
        }
    }

    private void changeEpicStatus(Integer epicId) { // Метод для проверки и обновления статуса эпика
        ArrayList<Task> taskArray = taskMap.get(epicId);
        if (!taskArray.isEmpty()) {
            Task epic = taskArray.get(0);
            if (taskArray.size() == 1) {
                epic.setStatus("NEW");
                taskMap.put(epicId, taskArray);
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
                    taskMap.put(epicId, taskArray);
                } else if (d == taskArray.size() - 1) {
                    epic.setStatus("DONE");
                    taskMap.put(epicId, taskArray);
                } else if ((n + i + d) == taskArray.size() - 1) {
                    epic.setStatus("IN_PROGRESS");
                    taskMap.put(epicId, taskArray);
                } else {
                    System.out.println("Обнаружена ошибка в статусах");
                }
            }
        }
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }
}


