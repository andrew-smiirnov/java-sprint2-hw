package manager;

import model.Task;

import java.util.ArrayList;
import java.util.List;

public class InMemoryHistoryManager implements HistoryManager {
    public static List<Task> historyList;

    public InMemoryHistoryManager() {
        historyList = new ArrayList<>();
    }

    @Override
    public void add(Task task) { // Добавление задачи в список просмотров
        historyList.add(task);
        if(historyList.size() > 10){
            historyList.remove(0);
        }
    }

    @Override
    public List<Task> getHistory() {  //Получение списка просмотренных задач
        return historyList;
    }

    @Override
    public void updateHistoryList(Integer taskId){
        if (!historyList.isEmpty()) {
            for (int i = 0; i < historyList.size(); i++ ) {
                Task task = historyList.get(i);
                 if (task.getId() == taskId){
                historyList.remove(task);
                }
            }
        }
    }

    @Override
    public void clearHistoryList(){
        historyList.clear();
    }
}
