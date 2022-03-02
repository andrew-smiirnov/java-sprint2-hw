package manager;

import model.*;

import java.util.*;

public class InMemoryHistoryManager <T> implements HistoryManager {

    public InMemoryHistoryManager() {
    }

    private TaskNode<Task> first; // Указатель на первый элемент списка. Он же first

    private TaskNode<Task> last; //Указатель на последний элемент списка. Он же last

    private int size = 0; // Размер списка

    private final Map<Integer, TaskNode<Task>> history = new HashMap<>(); // Мапа для ускорения работы хендмейд LinkedList


    @Override
    public void add(Task task) {  // Добавить задачу в историю просмотров
        if (task == null){
            System.out.println("Передана пустая задача");
            return;
        }
        if (history.containsKey(task.getId())) {
            remove(task.getId());
            linkLast(task);
        } else {
            linkLast(task);
        }
    }

    @Override
    public List<Task> getHistory() {  //  Получить список задач
        List<Task> historyList = new ArrayList<>();
        for (TaskNode<Task> i = first; i != null; i = i.next) {
           historyList.add(i.task);
        }
        return historyList;
    }

    @Override
    public void remove(int id) { // Удалить ноду из списка просмотров по id задачи
        if (history.containsKey(id)) {
            removeNode(history.get(id));
            history.remove(id);
        }
    }

    @Override
    public void clearHistoryList() { // Очистка списка просмотренных задач
        for (TaskNode<Task> i = first; i != null; ){
            TaskNode<Task> next = i.next;
            i.task = null;
            i.next = null;
            i.prev = null;
            i = next;
        }
        first = last = null;
        size = 0;
    }

    public void linkLast(Task task) {  // Добавить ноду в конец списка
        if (size == 0) {
            final TaskNode<Task> f = first;
            final TaskNode<Task> firstNode = new TaskNode<>(null, task, f);
            first = firstNode;
            if (f == null){
                last = firstNode;
            } else {
                f.prev = firstNode;
            }
            history.put(task.getId(), first);
            size++;
        } else {
            final TaskNode<Task> l = last;
            final TaskNode<Task> lastNode = new TaskNode<>(l, task, null);
            last = lastNode;
            if (l == null) {
                first = lastNode;
            } else {
                l.next = lastNode;
            }
            history.put(task.getId(), lastNode);
            size++;
        }
    }

    public void removeNode(TaskNode<Task> taskNode) { // Удалить ноду
        if (taskNode != null && size > 0) {
        // Удалить первую ноду
            if (taskNode == first) {
                final TaskNode<Task> next = taskNode.next;
                taskNode.task = null;
                taskNode.next = null;
                first = next;
                if (next == null) {
                    last = null;
                } else {
                    next.prev = null;
                }
                size--;
            } else if (taskNode == last) {
            // Удалить последнюю ноду
                final Task task = taskNode.task;
                final TaskNode<Task> prev = taskNode.prev;
                taskNode.task = null;
                taskNode.prev = null;
                last = prev;
                if (prev == null) {
                    first = null;
                } else {
                    prev.next = null;
                }
                size--;
            } else {
            // Удалить ноду в середине списка
                taskNode.prev.next = taskNode.next;
                taskNode.next.prev = taskNode.prev;
                taskNode.task = null;
                size--;
            }
        }
    }
}

