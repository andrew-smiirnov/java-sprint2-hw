package manager;

import model.Task;
import java.util.List;

public interface HistoryManager {

    void add(Task task); //Дабавление задачи в список

    void remove(int id); // Удалении задачи из списка истории

    List<Task> getHistory(); // Получение списка просмотренных задач

    void clearHistoryList();
}
