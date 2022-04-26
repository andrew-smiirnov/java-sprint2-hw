package model;

import java.time.*;
import java.time.format.DateTimeFormatter;
import java.util.Objects;
import static java.time.temporal.ChronoUnit.MINUTES;


public abstract class Task {
    protected Integer id;  // Уникальный идентификационный номер задачи
    protected TypeOfTask typeOfTask; // Тип задачи
    protected String title;  // Наименование задачи
    protected String description;  // Описание задачи
    protected TaskStatus status; // Статус задачи
    protected ZonedDateTime startTime;  // Время начала задачи
    protected Duration duration;  // Продолжительность задачи
    protected ZonedDateTime endTime; // Время окончания задачи
    protected DateTimeFormatter backupFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm_O");
    protected DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");


    public Task(Integer id, String title, String description, TaskStatus status, String startTime, String duration) {
        this.title = title;
        this.description = description;
        this.id = id;
        this.status = status;
        if (startTime.length() == 16) {
            this.startTime = ZonedDateTime.of(LocalDateTime.parse(startTime, formatter), ZonedDateTime.now().getZone());
        } else if(startTime.equals("null")) {
            this.startTime = null;
        } else {
            String[] time = startTime.split("_");
            DateTimeFormatter fromFileFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
            this.startTime = ZonedDateTime.of(LocalDateTime.parse(time[0], fromFileFormatter), ZoneId.of(time[1]));
        }
        if(duration.equals("null")){
            this.duration = null;
        } else {
            this.duration = Duration.of(Integer.parseInt(duration), MINUTES);
        }
        if (duration.equals("null") || startTime.equals("null")){
            this.endTime = null;
        } else {
            this.endTime = this.startTime.plus(this.duration);
        }
    }

    public Integer getId() {
        return id;
    }

    public TypeOfTask getTypeOfTask() {
        return typeOfTask;
    }

    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }

    public TaskStatus getStatus() {
        return status;
    }

    public ZonedDateTime getStartTime() {
        return startTime;
    }

    public Duration getDuration() {
        return duration;
    }

    public ZonedDateTime  getEndTime() {
        return endTime;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public void setStatus(TaskStatus status) {
        this.status = status;
    }

    public void setStartTime(ZonedDateTime startTime) {
        this.startTime = startTime;
    }

    public void setDuration(Duration duration) {
        this.duration = duration;
    }

    public String getStartTimeToStringForBackup() {
        if(startTime == null) return "null";
        return backupFormatter.format(startTime);
    }

    public String getEndTimeToStringForBackup() {
        if(endTime == null) return "null";
        return backupFormatter.format(endTime);
    }

    public String getDurationToString() {
        if(duration == null) return "null";
        Integer howLong = (int) duration.toMinutes();
        return howLong.toString();
    }

    @Override
    public String toString() {
        return '{' +
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
