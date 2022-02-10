import java.util.List;

public interface TaskManager {

    public void addTask(Task task); // Добавить задачу

    public void addEpic(Epic epic); // Добавить эпик

    public void addSubtask(Integer epicId, Subtask subtask); // Добавить подзадачу

    public void printAllTasks(); // Печать всех задач

    public void pintTaskById(Integer taskID); // Печать задачи по ID задачи

    public void printSubtaskInsideEpicById(Integer epicId); // Печать подзадач эпика по ID эпика

    public void updateTaskById(Task task); // Обновление задачи по ID задачи

    public void updateSubtaskById(Subtask subtask); // Обновление подзадачи по ID подзадачи

    public void updateEpicById(Epic epic); // Обновление эпика по ID эпика

    public void deleteTaskById(Integer taskID); // Удаление задачи по ID

    public void deleteSubtaskById(Integer subtaskID); // Удаление подзадачи по ID

    public void deleteEpicById(Integer epicID); // Удаление эпика по ID

    public void deleteAllTasks(); // Очистка списка задач (удаление всех задач)

    public void getTask(Integer taskID); // Получение задачи по ID

    public void getSubtask(Integer subtaskID); // Получение подзадачи по ID

    public void getEpic(Integer epicID); // Получение эпика по ID

    public List<Task> history(); // Получение списка истории
}
