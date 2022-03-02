package manager;

import model.Task;

public class TaskNode <T extends Task>{

    public T task;
    public TaskNode<T> next;
    public TaskNode<T> prev;

    public TaskNode(TaskNode<T> prev, T task, TaskNode<T> next) {
        this.task = task;
        this.next = next;
        this.prev = prev;
    }
}
