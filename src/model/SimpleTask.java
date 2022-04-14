package model;

import java.util.Objects;


public class SimpleTask extends Task {

    public SimpleTask(String title, String description, TaskStatus status) {
        super(null, title, description, status, "null", "null");
        this.typeOfTask = TypeOfTask.SIMPLE_TASK;
    }

    public SimpleTask(Integer id, String title, String description, TaskStatus status) {
        super(id, title, description, status, "null", "null");
        this.typeOfTask = TypeOfTask.SIMPLE_TASK;
    }

    public SimpleTask(String title, String description, TaskStatus status, String startTime, String duration) {
        super(null, title, description, status, startTime, duration);
        this.typeOfTask = TypeOfTask.SIMPLE_TASK;
    }

    public SimpleTask(Integer id, String title, String description, TaskStatus status, String startTime, String duration) {
        super(id, title, description, status, startTime, duration);
        this.typeOfTask = TypeOfTask.SIMPLE_TASK;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        SimpleTask task = (SimpleTask) o;
        return Objects.equals(title, task.title) && Objects.equals(description, task.description)
                && Objects.equals(id, task.id) && status == task.status;
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), id);
    }
}