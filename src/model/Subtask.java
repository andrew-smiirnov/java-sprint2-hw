package model;

import java.util.Objects;


public class Subtask extends Task {
    private Integer epicId;


    public Subtask(String title, String description, TaskStatus status) {
        super(null, title, description, status, "null", "null");
        this.typeOfTask = TypeOfTask.SUBTASK;
    }

    public Subtask(Integer id, String title, String description, TaskStatus status){
        super(id, title, description, status, "null", "null");
        this.typeOfTask = TypeOfTask.SUBTASK;
    }
    public Subtask(String title, String description, TaskStatus status, String startTime, String duration){
        super(null, title, description, status, startTime, duration);
        this.typeOfTask = TypeOfTask.SUBTASK;
    }

    public Subtask(Integer id, String title, String description, TaskStatus status, String startTime, String duration){
        super(id, title, description, status, startTime, duration);
        this.typeOfTask = TypeOfTask.SUBTASK;
    }

    public Integer getEpicId() {
        return epicId;
    }

    public void setEpicId(Integer epicId) {
        this.epicId = epicId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        Subtask subtask = (Subtask) o;
        return Objects.equals(epicId, subtask.epicId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), id);
    }


}