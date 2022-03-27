package model;

import java.util.Objects;

public class SimpleTask extends Task {
    private String title;  // Наименование задачи
    private final TypeOfTask typeOfTask = TypeOfTask.SIMPLE_TASK; // Тип задачи
    private String description;  // Описание задачи
    private Integer id;  // Уникальный идентификационный номер задачи
    private TaskStatus status; // Статус задачи

    public SimpleTask(Integer id, String title, String description, TaskStatus status) {
        super(id, title, description, status);
        this.title = title;
        this.description = description;
        this.id = id;
        this.status = status;
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
    public String toString() {
        return "{" +
                "id: " + id +
                ", " + typeOfTask +
                ", Название='" + title + '\'' +
                ", Описание='" + description + '\'' +
                ", Статус=" + status + '}';
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

