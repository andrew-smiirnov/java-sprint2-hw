/* Привет)
 * Добавил, дополнительное поле с типом задачи, что позволило немного сократить код в FileBackedTasksManager.java
 * CSV не стал трогать. Если не критично, то пусть будет простой тип без усложнений.
 */

import manager.*;
import model.*;

public class Main {

    public static void main(String[] args) throws ManagerSaveException {

        TaskManager taskManager = new FileBackedTasksManager();
        SimpleTask task1 = new SimpleTask (null, "Задача №1", "Проверить код", TaskStatus.NEW);
        SimpleTask task2 = new SimpleTask(null, "Задача №2", "Перепроверить код", TaskStatus.NEW);
        Epic epic1 = new Epic (null, "Эпик №1", "Упорядочить код", TaskStatus.NEW);
        Epic epic2 = new Epic (null, "Эпик №2", "Навести порядок в коде", TaskStatus.NEW);
        Subtask subtask1 = new Subtask(null, "Подзадача №1", "Убрать лишние пробелы", TaskStatus.NEW);
        Subtask subtask2 = new Subtask(null, "Подзадача №2", "Выровнять строки", TaskStatus.NEW);
        Subtask subtask3 = new Subtask(null, "Подзадача №1", "Убрать лишние записи", TaskStatus.NEW);

        System.out.println("----- Начнём: -----");
        taskManager.printAllTasks();
        taskManager.addSimpleTask(task1);
        taskManager.addSimpleTask(task2);
        taskManager.addEpic(epic1);
        taskManager.addEpic(epic2);
        taskManager.addSubtask(2, subtask1);
        taskManager.addSubtask(2, subtask2);
        taskManager.addSubtask(3, subtask3);

        System.out.println("----- Задачи/эпики/подзадачи внесены в базу -----");
        taskManager.printAllTasks();

        System.out.println();
        System.out.println("----- Вызов метода getTask, getEpic, getSubtask -----");
        taskManager.getSimpleTask(0);
        taskManager.getSimpleTask(1);
        taskManager.getEpic(2);
        taskManager.getEpic(3);
        taskManager.getSubtask(4);
        taskManager.getSubtask(5);
        taskManager.getSubtask(6);


        System.out.println();
        System.out.println("----- История просмотров getTask, getEpic, getSubtask -----");
        for (Task task : taskManager.history()){ // Печатаю список только для наглядности
            System.out.println("id=" + task.getId() + " , status: " + task.getStatus());
        }

        System.out.println();
        System.out.println("----- Вызовем ещё несколько задач методом getTask, getEpic, getSubtask -----");

        taskManager.getSubtask(6);
        taskManager.getSubtask(5);
        taskManager.getSimpleTask(0);
        taskManager.getEpic(2);

        System.out.println();
        System.out.println("----- Удалили задачу с id: 0, эпик id = 2 + подзадачи эпика id: 4, 5, 6  -----");

        taskManager.deleteSimpleTaskById(0);
        taskManager.deleteEpicById(2);


        System.out.println();
        System.out.println("----- Оставшиеся задачи/эпики/подзадачи -----");
        taskManager.printAllTasks();

        System.out.println();
        System.out.println("----- История просмотров getTask, getEpic, getSubtask с учетом очистки списка -----");
        for (Task task : taskManager.history()){
            System.out.println("id=" + task.getId() + " , status: " + task.getStatus());
        }
    }
}