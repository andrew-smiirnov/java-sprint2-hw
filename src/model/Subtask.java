package model;

import java.util.Objects;

public class Subtask extends Task {
    private String title;  // Наименование задачи
    private final TypeOfTask typeOfTask = TypeOfTask.SUBTASK; // Тип задачи
    private String description;  // Описание задачи
    private Integer id;  // Уникальный идентификационный номер задачи
    private TaskStatus status; // Статус задачи
    private Integer epicId;


    public Subtask(Integer id, String title, String description, TaskStatus status) {
        super(id,title, description, status);
        this.title = title;
        this.description = description;
        this.id = id;
        this.status = status;
    }

    public Integer getEpicId() {
        return epicId;
    }
    public void setEpicId(Integer epicId) {
        this.epicId = epicId;
    }

    @Override
    public String toString() {
        return "{" +
                "id: " + id +
                ", " + typeOfTask +
                ", Название='" + title + '\'' +
                ", Описание='" + description + '\'' +
                ", Статус=" + status + '}';
    }

    public TypeOfTask getTypeOfTask() {
        return typeOfTask;
    }

    @Override
    public Integer getId() {
        return id;
    }

    @Override
    public void setId(Integer id) {
        this.id = id;
    }

    @Override
    public TaskStatus getStatus() {
        return status;
    }

    @Override
    public void setStatus(TaskStatus status) {
        this.status = status;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        Subtask subtask = (Subtask) o;
        return id == subtask.id && Objects.equals(title, subtask.title)
                && Objects.equals(description, subtask.description) && status == subtask.status;
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), id);
    }
}