import java.util.List;

public interface HistoryManager {

    public void add(Task task); //Дабавление задачи в список

    public void updateHistoryList(Integer taskId); // Обновление истории просмотра при удалении задачи

    public List<Task> getHistory(); // Получение списка просмотренных задач

    public void clearHistoryList(); // Очистка истории просмотров
}
