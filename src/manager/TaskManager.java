package manager;

import model.Epic;
import model.SimpleTask;
import model.Subtask;
import model.Task;

import java.util.List;

public interface TaskManager {

    void addSimpleTask(SimpleTask simpleTask); // Добавить задачу

    void addEpic(Epic epic); // Добавить эпик

    void addSubtask(Integer epicId, Subtask subtask); // Добавить подзадачу

    void printAllTasks(); // Печать всех задач

    void pintSimpleTaskById(Integer simpleTaskId); // Печать задачи по ID задачи

    void printSubtaskInsideEpicById(Integer epicId); // Печать подзадач эпика по ID эпика

    void updateSimpleTaskById(SimpleTask simpleTask); // Обновление задачи по ID задачи

    void updateSubtaskById(Subtask subtask); // Обновление подзадачи по ID подзадачи

    void updateEpicById(Epic epic); // Обновление эпика по ID эпика

    void deleteSimpleTaskById(Integer simpleTaskId); // Удаление задачи по ID

    void deleteSubtaskById(Integer subtaskId); // Удаление подзадачи по ID

    void deleteEpicById(Integer epicId); // Удаление эпика по ID

    void deleteAllTasks(); // Очистка списка задач (удаление всех задач)

    void getSimpleTask(Integer simpleTaskId); // Получение задачи по ID

    void getSubtask(Integer subtaskId); // Получение подзадачи по ID

    void getEpic(Integer epicId); // Получение эпика по ID

    public List<Task> history(); // Получение списка истории
}
