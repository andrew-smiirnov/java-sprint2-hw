import java.util.ArrayList;
import java.util.List;

public class InMemoryHistoryManager implements HistoryManager {
    public static List<Task> historyTaskManager;

    public InMemoryHistoryManager() {
        historyTaskManager = new ArrayList<>();
    }

    @Override
    public void add(Task task) { // Добавление задачи в список просмотров
        historyTaskManager.add(task);
        if(historyTaskManager.size() > 10){
            historyTaskManager.remove(0);
        }
    }

    @Override
    public List<Task> getHistory() {  //Получение списка просмотренных задач
        return historyTaskManager;
    }

    @Override
    public void updateHistoryList(Integer taskId){
        if (!historyTaskManager.isEmpty()) {
            for (int i = 0; i < historyTaskManager.size(); i++ ) {
                Task task = historyTaskManager.get(i);
                 if (task.getId() == taskId){
                historyTaskManager.remove(task);
                }
            }
        }
    }

    @Override
    public void clearHistoryList(){
        historyTaskManager.clear();
    }
}
