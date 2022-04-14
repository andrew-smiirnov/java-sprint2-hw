package manager;

import model.Epic;
import model.SimpleTask;
import model.Subtask;
import model.Task;

import java.util.List;
import java.util.Set;

public interface TaskManager {

    void addSimpleTask(SimpleTask simpleTask); // Добавить задачу

    void addEpic(Epic epic); // Добавить эпик

    void addSubtask(Integer epicId, Subtask subtask); // Добавить подзадачу

    void updateSimpleTaskById(SimpleTask simpleTask); // Обновление задачи по ID задачи

    void updateSubtaskById(Subtask subtask); // Обновление подзадачи по ID подзадачи

    void updateEpicById(Epic epic); // Обновление эпика по ID эпика

    void deleteSimpleTaskById(Integer simpleTaskId); // Удаление задачи по ID

    void deleteSubtaskById(Integer subtaskId); // Удаление подзадачи по ID

    void deleteEpicById(Integer epicId); // Удаление эпика по ID

    void deleteAllTasks(); // Очистка списка задач (удаление всех задач)

    SimpleTask getSimpleTask(Integer simpleTaskId); // Получение задачи по ID

    Subtask getSubtask(Integer subtaskId); // Получение подзадачи по ID

    Epic getEpic(Integer epicId); // Получение эпика по ID

    List<Task> history(); // Получение списка истории

    Set<Task> getPrioritizedTasks(); // Получение отсортированного по времени списка задач
}
