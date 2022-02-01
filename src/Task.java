import java.util.Objects;

public class Task {
    private String title;  // Наименование задачи
    private String description;  // Описание задачи
    private Integer id;  // Уникальный идентификационный номер задачи
    private String status; // Статус задачи
    private boolean isEpic; // Статус эпик
    private boolean isSubtask; // Статус подзадачи

    public Task() {
    }

    public Task(String title, String description, Integer id, String status, boolean isEpic, boolean isSubtask) {
        this.title = title;
        this.description = description;
        this.id = id;
        this.status = status;
        this.isEpic = isEpic;
        this.isSubtask = isSubtask;
    }

    public Integer getId() {
        return id;
    }

    public boolean isEpic() {
        return isEpic;
    }

    public boolean isSubtask() {
        return isSubtask;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    @Override
    public String toString() {
        String result = "Ещё не добавлено ни одной задачи";
        if (!isEpic && !isSubtask) {
            result = "Задача {" +
                    " id: " + id +
                    " | Название: " + title +
                    " | Описание: " + description +
                    " | Статус: " + status +
                    " }";
        } else if (isEpic) {
            result = "Эпик {" +
                    " id: " + id +
                    " | Название: " + title +
                    " | Описание: " + description +
                    " | Статус: " + status +
                    " }";
        } else if (isSubtask) {
            result = " Подзадача {" +
                    " id: " + id +
                    " | Название: " + title +
                    " | Описание: " + description +
                    " | Статус: " + status +
                    " }";
        }
        return result;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Task task = (Task) o;
        return isEpic == task.isEpic && isSubtask == task.isSubtask && Objects.equals(title, task.title)
                && Objects.equals(description, task.description) && Objects.equals(id, task.id)
                && Objects.equals(status, task.status);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
