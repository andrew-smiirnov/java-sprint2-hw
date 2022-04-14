/*
* Спасибо за мудрые наставления. Всё исправил)
*/

import extentions.ManagerSaveException;
import manager.*;
import model.*;


public class Main {

    public static void main(String[] args) throws ManagerSaveException {

        TaskManager taskManager  = new FileBackedTasksManager();


        taskManager.addSimpleTask(new SimpleTask (
                "Задача №1",
                "Проверить код",
                TaskStatus.NEW,
                "2022-04-02 10:15",
                "15"));
        taskManager.addSimpleTask(new SimpleTask(
                "Задача №2",
                "Перепроверить код",
                TaskStatus.NEW,
                "2022-04-02 10:30",
                "15"));
        taskManager.addEpic(new Epic (
                "Эпик №1",
                "Упорядочить код",
                TaskStatus.NEW));
        taskManager.addEpic(new Epic (
                "Эпик №2",
                "Навести порядок в коде",
                TaskStatus.NEW));
        taskManager.addSubtask(2, new Subtask(
                "Подзадача №2.1",
                "Описание 1",
                TaskStatus.NEW,
                "2022-04-02 10:45",
                "15"));
        taskManager.addSubtask(2, new Subtask(
                "Подзадача №2.2",
                "Описание 2",
                TaskStatus.NEW));
        taskManager.addSubtask(3, new Subtask(
                "Подзадача №3.1",
                "Описание 1",
                TaskStatus.NEW));
        taskManager.updateSimpleTaskById(new SimpleTask(
                1,
                "Задача №2",
                "Перепроверить код",
                TaskStatus.DONE));
        taskManager.updateSubtaskById(new Subtask(
                4,
                "Подзадача №2.1",
                "Описание 1",
                TaskStatus.DONE));
        taskManager.getEpic(2);
        taskManager.getSimpleTask(1);
        taskManager.getSubtask(5);
        taskManager.getSimpleTask(0);
        taskManager.getEpic(3);
        taskManager.getSubtask(6);
        taskManager.getSubtask(4);

        System.out.println();
        System.out.println("----- История просмотров getTask, getEpic, getSubtask с учетом очистки списка -----");
        for (Task task : taskManager.history()){
            System.out.println("id=" + task.getId() + " " + task.getTypeOfTask() + " ,status: " + task.getStatus());
        }

        System.out.println();
        System.out.println("----- PrioritizedTasks -----");
        for (Task task : taskManager.getPrioritizedTasks()){
            System.out.println(task);
        }
    }
}