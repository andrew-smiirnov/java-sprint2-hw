package model;

import java.util.Objects;

public abstract class Task {
    private String title;  // Наименование задачи
    private String description;  // Описание задачи
    private Integer id;  // Уникальный идентификационный номер задачи
    private TaskStatus status; // Статус задачи

    public Task(String title, String description, Integer id, TaskStatus status) {
        this.title = title;
        this.description = description;
        this.id = id;
        this.status = status;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public TaskStatus getStatus() {
        return status;
    }

    public void setStatus(TaskStatus status) {
        this.status = status;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Task task = (Task) o;
        return Objects.equals(title, task.title) && Objects.equals(description, task.description)
                && id.equals(task.id)
                && status == task.status;
    }

    @Override
    public int hashCode() {
        return Objects.hash(title, description, id, status);
    }
}
