package manager;

import model.*;
import java.util.*;

public class InMemoryHistoryManager <T> implements HistoryManager {

    public InMemoryHistoryManager() {
    }

    private TaskNode<Task> first; // Указатель на первый элемент списка. Он же first

    private TaskNode<Task> last; //Указатель на последний элемент списка. Он же last

    private final Map<Integer, TaskNode<Task>> historyMap = new HashMap<>(); // Мапа для ускорения работы хендмейд LinkedList


    @Override
    public void add(Task task) {  // Добавить задачу в историю просмотров
        if (task == null){
            System.out.println("Передана пустая задача");
            return;
        }
        if (historyMap.containsKey(task.getId())) {
            remove(task.getId());
            linkLast(task);
        } else {
            linkLast(task);
        }
        while (historyMap.size() > 10){
            removeNode(first);
        }
    }

    @Override
    public List<Task> getHistory() {  //  Получить список задач
        List<Task> historyList = new ArrayList<>();
        for (TaskNode<Task> i = first; i != null; i = i.getNext()) {
           historyList.add(i.getTask());
        }
        return historyList;
    }

    @Override
    public void remove(int id) { // Удалить ноду из списка просмотров по id задачи
        if (historyMap.containsKey(id)) {
            removeNode(historyMap.get(id));
            historyMap.remove(id);
        }
    }

    @Override
    public void clearHistoryList() { // Очистка списка просмотренных задач
        for (TaskNode<Task> i = first; i != null; ){
            TaskNode<Task> next = i.getNext();
            i.setTask(null);
            i.setNext(null);
            i.setPrev(null);
            i = next;
        }
        first = last = null;
        }

    public void linkLast(Task task) {  // Добавить ноду в конец списка
        if (historyMap.size() == 0) {
            final TaskNode<Task> f = first;
            final TaskNode<Task> firstNode = new TaskNode<>(null, task, f);
            first = firstNode;
            if (f == null){
                last = firstNode;
            } else {
                f.setPrev(firstNode);
            }
            historyMap.put(task.getId(), first);
        } else {
            final TaskNode<Task> l = last;
            final TaskNode<Task> lastNode = new TaskNode<>(l, task, null);
            last = lastNode;
            if (l == null) {
                first = lastNode;
            } else {
                l.setNext(lastNode);
            }
            historyMap.put(task.getId(), lastNode);
            }
    }

    public void removeNode(TaskNode<Task> taskNode) { // Удалить ноду
        if (taskNode != null && historyMap.size() > 0) {
        // Удалить первую ноду
            if (taskNode == first) {
                final TaskNode<Task> next = taskNode.getNext();
                taskNode.setTask(null);
                taskNode.setNext(null);
                first = next;
                if (next == null) {
                    last = null;
                } else {
                    next.setPrev(null);
                }
            } else if (taskNode == last) {
            // Удалить последнюю ноду
                final Task task = taskNode.getTask();
                final TaskNode<Task> prev = taskNode.getPrev();
                taskNode.setTask(null);
                taskNode.setPrev(null);
                last = prev;
                if (prev == null) {
                    first = null;
                } else {
                    prev.setNext(null);
                }
            } else {
            // Удалить ноду в середине списка
                taskNode.getPrev().setNext(taskNode.getNext());
                taskNode.getNext().setPrev(taskNode.getPrev());
                taskNode.setTask(null);
            }
        }
    }
}

