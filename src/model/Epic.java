package model;

import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class Epic extends Task {
    private List <Integer> subtasks;


    public Epic(String title, String description, TaskStatus status) {
        super(null, title, description, status, "null", "null");
        this.status = TaskStatus.NEW;
        this.typeOfTask = TypeOfTask.EPIC;
        subtasks = new ArrayList<>();
    }

    public Epic(Integer id, String title, String description, TaskStatus status) {
        super(id, title, description, status, "null", "null");
        this.status = TaskStatus.NEW;
        this.typeOfTask = TypeOfTask.EPIC;
        subtasks = new ArrayList<>();
    }

    public Epic(String title, String description, TaskStatus status, String startTime, String duration) {
        super(null, title, description, status, startTime, duration);
        this.status = TaskStatus.NEW;
        this.typeOfTask = TypeOfTask.EPIC;
        subtasks = new ArrayList<>();
    }

    public Epic(Integer id, String title, String description, TaskStatus status, String startTime, String duration) {
        super(id, title, description, status, startTime, duration);
        this.typeOfTask = TypeOfTask.EPIC;
        subtasks = new ArrayList<>();
    }

    public void setSubtasks(List<Integer> subtasks) {
        this.subtasks = subtasks;
    }

    public List<Integer> getSubtasks() {
        return subtasks;
    }

    public void setStartTime(ZonedDateTime startTime){
        this.startTime = startTime;
    }

    public void setEndTime(ZonedDateTime endTime){
        this.endTime = endTime;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        Epic epic = (Epic) o;
        return Objects.equals(title, epic.title) && Objects.equals(description, epic.description)
                && Objects.equals(id, epic.id) && status == epic.status;
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), id);
    }
}
