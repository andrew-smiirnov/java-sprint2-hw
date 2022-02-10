/* Из-за не особой пригодности для расширения функционала предыдущей версии организации хранения задач/эпиков/подзадач
   было решено передалать трекер задач на основе наследуемых классов от класса простой задачи (Task). Не знаю стоит ли
   делать абстрактный класс задачи, если он будет полностью повторять класс простой задачи?
   Медоды getTask(), getSubtask() и getEpic() организовал пока как консольный вывод содержания объекта.
*/
public class Main {

    public static void main(String[] args) {

        TaskManager taskManager = Managers.getDefault();
        Task task1 = new Task ("Задача №1", "Проверить код", null, TaskStatus.NEW);
        Task task2 = new Task("Задача №2", "Перепроверить код", null, TaskStatus.NEW);
        Epic epic1 = new Epic ("Эпик №1", "Упорядочить код", null, TaskStatus.NEW);
        Epic epic2 = new Epic ("Эпик №2", "Навести порядок в коде", null, TaskStatus.NEW);
        Subtask subtask1 = new Subtask("Подзадача №1", "Убрать лишние пробелы", null, TaskStatus.NEW);
        Subtask subtask2 = new Subtask("Подзадача №2", "Выровнять строки", null, TaskStatus.NEW);
        Subtask subtask3 = new Subtask("Подзадача №1", "Убрать лишние записи", null, TaskStatus.NEW);

        System.out.println("----- Начнём: -----");
        taskManager.printAllTasks();
        taskManager.addTask(task1);
        taskManager.addTask(task2);
        taskManager.addEpic(epic1);
        taskManager.addEpic(epic2);
        taskManager.addSubtask(2, subtask1);
        taskManager.addSubtask(3, subtask2);
        taskManager.addSubtask(3, subtask3);

        System.out.println("----- Задачи/эпики/подзадачи внесены в базу -----");
        taskManager.printAllTasks();

        System.out.println();
        System.out.println("----- Вызов метода getTask, getEpic, getSubtask -----");
        taskManager.getTask(0);
        taskManager.getTask(1);
        taskManager.getEpic(3);
        taskManager.getTask(0);
        taskManager.getSubtask(4);
        taskManager.getSubtask(6);
        taskManager.getTask(1);
        taskManager.getEpic(3);

        System.out.println();
        System.out.println("----- История просмотров getTask, getEpic, getSubtask -----");
        for (Task task : taskManager.history()){ // Печатаю список только для наглядности
            System.out.println(task);
        }

        System.out.println();
        System.out.println("----- Изменим статус задач/эпиков/подзадач -----");

        task1 = new Task("Задача 01", "Проверить код", 0, TaskStatus.DONE);
        taskManager.updateTaskById(task1);
        epic1 = new Epic("Эпик 01", "Упорядочить код", 2, TaskStatus.DONE);
        taskManager.updateEpicById(epic1);
        subtask2 = new Subtask("Подзадача 02", "Выровнять строки", 5, TaskStatus.IN_PROGRESS);
        taskManager.updateSubtaskById(subtask2);
        subtask3 = new Subtask("Подзадача 01", "Убрать лишние записи", 6, TaskStatus.IN_PROGRESS);
        taskManager.updateSubtaskById(subtask3);

        taskManager.printAllTasks();

        System.out.println();
        System.out.println("----- Вызовем ещё несколько задач методом getTask, getEpic, getSubtask -----");

        taskManager.getSubtask(6);
        taskManager.getSubtask(5);
        taskManager.getTask(0);
        taskManager.getEpic(2);

        System.out.println();
        System.out.println("----- История просмотров после изменения статусов -----");
        for (Task task : taskManager.history()){
            System.out.println(task);
        }

        System.out.println();
        System.out.println("----- Удалили несколько задач с ID= 1, 3 и 4 -----");

        taskManager.deleteTaskById(1);
        taskManager.deleteSubtaskById(4);
        taskManager.deleteEpicById(3);

        System.out.println();
        System.out.println("----- История просмотров getTask, getEpic, getSubtask с учетом удалений -----");
        for (Task task : taskManager.history()){
            System.out.println(task);
        }
    }
}
// История сильно поредела так как с эпикам ID=3 удалились и его подзадачи ID=5, ID=6