package manager;

import model.Task;

import java.util.List;

public interface HistoryManager {

    void add(Task task); //Дабавление задачи в список

    void updateHistoryList(Integer taskId); // Обновление истории просмотра при удалении задачи

    List<Task> getHistory(); // Получение списка просмотренных задач

    void clearHistoryList(); // Очистка истории просмотров
}
