package model;

import java.util.Objects;

public class SimpleTask extends Task {
    private String title;  // Наименование задачи
    private String description;  // Описание задачи
    private Integer id;  // Уникальный идентификационный номер задачи
    private TaskStatus status; // Статус задачи

    public SimpleTask(String title, String description, Integer id, TaskStatus status) {
        super(title, description, id, status);
        this.title = title;
        this.description = description;
        this.id = id;
        this.status = status;
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
        return "Задача{" +
                "Название='" + title + '\'' +
                ", Описание='" + description + '\'' +
                ", id=" + id +
                ", Статус=" + status +
                '}';
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

