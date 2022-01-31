import java.util.Scanner;

public class Main {

    public static void main(String[] args) {
        Manager manager = new Manager();
        Task task1 = new Task("Задача №1", "Проверить код",
                Manager.id, "NEW", false, false);
        Task task2 = new Task("Задача №2", "Перепроверить код",
                Manager.id, "NEW", false, false);
        Task epic1 = new Task("Эпик №1", "Упорядочить код",
                Manager.id, "NEW", true, false);
        Task epic2 = new Task("Эпик №2", "Навести порядок в коде",
                Manager.id, "NEW", true, false);
        Task subtask1 = new Task("Подзадача №1", "Убрать лишние пробелы",
                Manager.id, "NEW", false, true);
        Task subtask2 = new Task("Подзадача №2", "Выровнять строки",
                Manager.id, "NEW", false, true);
        Task subtask3 = new Task("Подзадача №1", "Убрать лишние записи",
                Manager.id, "NEW", false, true);

        Manager.addTask(task1);
        Manager.addTask(task2);
        Manager.addEpic(epic1);
        Manager.addEpic(epic2);
        Manager.addSubtask(2, subtask1);
        Manager.addSubtask(2, subtask2);
        Manager.addSubtask(3, subtask3);

        System.out.println("----- Задачи/эпики/подзадачи внесены в базу -----");
        Manager.printAllTasks();

        task1 = new Task("Задача 01", "Проверить код", 0, "DONE", false, false);
        manager.updateTaskByID(task1);
        task2 = new Task("Задача 02", "Перепроверить код", 1, "IN_PROGRESS", false, false);
        manager.updateTaskByID(task2);
        epic1 = new Task("Эпик 01", "Упорядочить код", 2, "DONE", true, false);
        Manager.updateEpicByID(epic1);
        epic2 = new Task("Эпик 02", "Навести порядок в коде", 3, "DONE", true, false);
        Manager.updateEpicByID(epic2);
        subtask1 = new Task("Подзадача 01", "Убрать лишние пробелы", 4, "DONE", false, true);
        Manager.updateSubtaskByID(subtask1);
        subtask2 = new Task("Подзадача 02", "Выровнять строки", 5, "IN_PROGRESS", false, true);
        Manager.updateSubtaskByID(subtask2);
        subtask3 = new Task("Подзадача 01", "Убрать лишние записи", 6, "IN_PROGRESS", false, true);
        Manager.updateSubtaskByID(subtask3);

        System.out.println();
        System.out.println("----- Изменим статус задач/эпиков/подзадач -----");
        Manager.printAllTasks();

        Manager.deleteTaskByID(1);
        Manager.deleteTaskByID(3);
        Manager.deleteTaskByID(5);

        System.out.println();
        System.out.println("----- Удалим несколько задач/эпиков/подзадач -----");
        Manager.printAllTasks();
    }
}