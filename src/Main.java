import java.util.Scanner;

public class Main {

    public static void main(String[] args) {
        Manager manager = new Manager();
        Task task1 = new Task("Задача №1", "Проверить код",
                manager.getId(), "NEW", false, false);
        Task task2 = new Task("Задача №2", "Перепроверить код",
                manager.getId(), "NEW", false, false);
        Task epic1 = new Task("Эпик №1", "Упорядочить код",
                manager.getId(), "NEW", true, false);
        Task epic2 = new Task("Эпик №2", "Навести порядок в коде",
                manager.getId(), "NEW", true, false);
        Task subtask1 = new Task("Подзадача №1", "Убрать лишние пробелы",
                manager.getId(), "NEW", false, true);
        Task subtask2 = new Task("Подзадача №2", "Выровнять строки",
                manager.getId(), "NEW", false, true);
        Task subtask3 = new Task("Подзадача №1", "Убрать лишние записи",
                manager.getId(), "NEW", false, true);

        manager.addTask(task1);
        manager.addTask(task2);
        manager.addEpic(epic1);
        manager.addEpic(epic2);
        manager.addSubtask(2, subtask1);
        manager.addSubtask(2, subtask2);
        manager.addSubtask(3, subtask3);

        System.out.println("----- Задачи/эпики/подзадачи внесены в базу -----");
        manager.printAllTasks();

        task1 = new Task("Задача 01", "Проверить код", 0, "DONE", false, false);
        manager.updateTaskById(task1);
        task2 = new Task("Задача 02", "Перепроверить код", 1, "IN_PROGRESS", false, false);
        manager.updateTaskById(task2);
        epic1 = new Task("Эпик 01", "Упорядочить код", 2, "DONE", true, false);
        manager.updateEpicById(epic1);
        epic2 = new Task("Эпик 02", "Навести порядок в коде", 3, "DONE", true, false);
        manager.updateEpicById(epic2);
        subtask1 = new Task("Подзадача 01", "Убрать лишние пробелы", 4, "DONE", false, true);
        manager.updateSubtaskById(subtask1);
        subtask2 = new Task("Подзадача 02", "Выровнять строки", 5, "IN_PROGRESS", false, true);
        manager.updateSubtaskById(subtask2);
        subtask3 = new Task("Подзадача 01", "Убрать лишние записи", 6, "IN_PROGRESS", false, true);
        manager.updateSubtaskById(subtask3);

        System.out.println();
        System.out.println("----- Изменим статус задач/эпиков/подзадач -----");
        manager.printAllTasks();

        manager.deleteTaskById(1);
        manager.deleteTaskById(3);
        manager.deleteTaskById(5);

        System.out.println();
        System.out.println("----- Удалим несколько задач/эпиков/подзадач -----");
        manager.printAllTasks();
    }
}