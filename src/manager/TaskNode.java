package manager;

import model.Task;

public class TaskNode <T extends Task>{

    private T task;
    private TaskNode<T> next;
    private TaskNode<T> prev;

    public TaskNode(TaskNode<T> prev, T task, TaskNode<T> next) {
        this.task = task;
        this.next = next;
        this.prev = prev;
    }

    public T getTask() {
        return task;
    }

    public void setTask(T task) {
        this.task = task;
    }

    public TaskNode<T> getNext() {
        return next;
    }

    public void setNext(TaskNode<T> next) {
        this.next = next;
    }

    public TaskNode<T> getPrev() {
        return prev;
    }

    public void setPrev(TaskNode<T> prev) {
        this.prev = prev;
    }
}
