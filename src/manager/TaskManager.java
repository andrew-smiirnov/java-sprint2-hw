package manager;

import extentions.ManagerSaveException;
import model.Epic;
import model.SimpleTask;
import model.Subtask;
import model.Task;

import java.util.List;

public interface TaskManager {

    void addSimpleTask(SimpleTask simpleTask) throws ManagerSaveException; // Добавить задачу

    void addEpic(Epic epic) throws ManagerSaveException; // Добавить эпик

    void addSubtask(Integer epicId, Subtask subtask) throws ManagerSaveException; // Добавить подзадачу

    void printAllTasks(); // Печать всех задач

    void pintSimpleTaskById(Integer simpleTaskId); // Печать задачи по ID задачи

    void printSubtaskInsideEpicById(Integer epicId); // Печать подзадач эпика по ID эпика

    void updateSimpleTaskById(SimpleTask simpleTask) throws ManagerSaveException; // Обновление задачи по ID задачи

    void updateSubtaskById(Subtask subtask) throws ManagerSaveException; // Обновление подзадачи по ID подзадачи

    void updateEpicById(Epic epic) throws ManagerSaveException; // Обновление эпика по ID эпика

    void deleteSimpleTaskById(Integer simpleTaskId) throws ManagerSaveException; // Удаление задачи по ID

    void deleteSubtaskById(Integer subtaskId) throws ManagerSaveException; // Удаление подзадачи по ID

    void deleteEpicById(Integer epicId) throws ManagerSaveException; // Удаление эпика по ID

    void deleteAllTasks() throws ManagerSaveException; // Очистка списка задач (удаление всех задач)

    void getSimpleTask(Integer simpleTaskId) throws ManagerSaveException; // Получение задачи по ID

    void getSubtask(Integer subtaskId) throws ManagerSaveException; // Получение подзадачи по ID

    void getEpic(Integer epicId) throws ManagerSaveException; // Получение эпика по ID

    List<Task> history(); // Получение списка истории
}
