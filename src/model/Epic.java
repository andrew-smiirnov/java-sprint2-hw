package model;

import java.util.List;
import java.util.ArrayList;
import java.util.Objects;

public class Epic extends Task {
    private String title;  // Наименование задачи
    private String description;  // Описание задачи
    private Integer id;  // Уникальный идентификационный номер задачи
    private TaskStatus status; // Статус задачи
    private List <Integer> subtasks;


    public Epic(String title, String description, Integer id, TaskStatus status) {
        super(title, description, id, status);
        this.title = title;
        this.description = description;
        this.id = id;
        this.status = status;
        subtasks = new ArrayList<>();
    }

    @Override
    public String toString() {
        return "Эпик{" +
                "Название='" + title + '\'' +
                ", Описание='" + description + '\'' +
                ", id=" + id +
                ", Статус=" + status +
                '}';
    }

    public void setSubtasks(List<Integer> subtasks) {
        this.subtasks = subtasks;
    }

    public List<Integer> getSubtasks() {
        return subtasks;
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

    @Override
    public Integer getId() {
        return id;
    }

    @Override
    public void setId(Integer id) {
        this.id = id;
    }

    @Override
    public void setStatus(TaskStatus status) {
        this.status = status;
    }

    @Override
    public TaskStatus getStatus() {
        return status;
    }
}
