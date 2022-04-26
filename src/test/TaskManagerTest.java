import manager.TaskManager;
import model.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.time.Duration;
import java.time.LocalDateTime;
import java.time.ZonedDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;


import static org.junit.jupiter.api.Assertions.*;

public abstract class TaskManagerTest<T extends TaskManager> {

    protected T taskManager;

    public abstract void initializeTaskManager() throws IOException;

    @BeforeEach
    public abstract void beforeEach() throws IOException;


    @Test
    public void addUpdateAndDeleteSimpleTaskInOrdinaryWorkTaskManagerTest() {
        taskManager.addSimpleTask(new SimpleTask ("task 1", "desc 1", TaskStatus.NEW));
        assertEquals(taskManager.getSimpleTask(0).get().getId(), 0);
        assertEquals(taskManager.getSimpleTask(0).get().getStartTime(), null);
        assertEquals(taskManager.getSimpleTask(0).get().getDuration(), null);
        assertEquals(taskManager.getSimpleTask(0).get().getStartTimeToStringForBackup(), "null");
        assertEquals(taskManager.getSimpleTask(0).get().getDurationToString(), "null");
        assertEquals(taskManager.getSimpleTask(0).get().getStatus(), TaskStatus.NEW);
        taskManager.updateSimpleTaskById(new SimpleTask (0, "task 1", "desc 1", TaskStatus.DONE));
        assertEquals(taskManager.getSimpleTask(0).get().getStatus(), TaskStatus.DONE);
        taskManager.deleteSimpleTaskById(0);
        assertNull(taskManager.getSimpleTask(0), "В трекере задач нет задач");
    }

    @Test
    public void addUpdateAndDeleteEpicInOrdinaryWorkTaskManagerTest() {
        taskManager.addEpic(new Epic ("epic 1", "desc 1", TaskStatus.NEW));
        assertEquals(taskManager.getEpic(0).get().getId(), 0);
        assertEquals(taskManager.getEpic(0).get().getStartTime(), null);
        assertEquals(taskManager.getEpic(0).get().getEndTime(), null);
        assertEquals(taskManager.getEpic(0).get().getStartTimeToStringForBackup(), "null");
        taskManager.updateEpicById(new Epic (0,"epic 1", "desc 1", TaskStatus.DONE));
        assertEquals(taskManager.getEpic(0).get().getStatus(), TaskStatus.NEW);
        taskManager.deleteEpicById(0);
        assertNull(taskManager.getSimpleTask(0), "В трекере задач нет задач");
    }

    @Test
    public void addUpdateAndDeleteSubtaskInOrdinaryWorkTaskManagerTest() {
        taskManager.addEpic(new Epic ("epic 1", "desc 1", TaskStatus.NEW));
        taskManager.addSubtask(0, new Subtask("subtask 1.1", "desc 1.1", TaskStatus.NEW));
        assertEquals(taskManager.getEpic(0).get().getSubtasks().size(), 1);
        assertEquals(taskManager.getSubtask(1).get().getStatus(), TaskStatus.NEW);
        assertEquals(taskManager.getSubtask(1).get().getStartTime(), null);
        assertEquals(taskManager.getSubtask(1).get().getDuration(), null);
        assertEquals(taskManager.getSubtask(1).get().getStartTimeToStringForBackup(), "null");
        assertEquals(taskManager.getSubtask(1).get().getDurationToString(), "null");
        assertEquals(taskManager.getSubtask(1).get().getEpicId(), 0);
        taskManager.updateSubtaskById(new Subtask(1,"subtask 1.1", "desc 1.1", TaskStatus.IN_PROGRESS));
        assertEquals(taskManager.getEpic(0).get().getStatus(), TaskStatus.IN_PROGRESS);
        assertEquals(taskManager.getSubtask(1).get().getStatus(), TaskStatus.IN_PROGRESS);
        taskManager.deleteSubtaskById(1);
        assertNull(taskManager.getSubtask(1), "В трекере задач нет задач");
    }

    @Test
    public void changeEpicStatusInOrdinaryWorkTaskManagerTest() {
        taskManager.addEpic(new Epic ("epic 1", "desc 1", TaskStatus.NEW));
        taskManager.addSubtask(0, new Subtask("subtask 1.1", "desc 1.1", TaskStatus.NEW));
        taskManager.addSubtask(0, new Subtask("subtask 1.2", "desc 1.2", TaskStatus.NEW));
        assertEquals(taskManager.getEpic(0).get().getSubtasks().size(), 2);
        assertEquals(taskManager.getEpic(0).get().getStatus(), TaskStatus.NEW);
        taskManager.updateSubtaskById(new Subtask(1,"subtask 1.1", "desc 1.1", TaskStatus.IN_PROGRESS));
        assertEquals(taskManager.getEpic(0).get().getStatus(), TaskStatus.IN_PROGRESS);
        taskManager.updateSubtaskById(new Subtask(2,"subtask 1.2", "desc 1.2", TaskStatus.DONE));
        assertEquals(taskManager.getEpic(0).get().getStatus(), TaskStatus.IN_PROGRESS);
        taskManager.updateSubtaskById(new Subtask(1,"subtask 1.1", "desc 1.1", TaskStatus.DONE));
        assertEquals(taskManager.getEpic(0).get().getStatus(), TaskStatus.DONE);
        taskManager.deleteAllTasks();
        assertNull(taskManager.getSubtask(1), "В трекере задач нет задач");
        assertNull(taskManager.getSubtask(2), "В трекере задач нет задач");
        assertNull(taskManager.getEpic(0), "В трекере задач нет задач");
    }

    @Test
    public void getHistoryInOrdinaryWorkTaskManagerTest() {
        taskManager.addSimpleTask(new SimpleTask ("task 1", "desc 1", TaskStatus.IN_PROGRESS));
        taskManager.addEpic(new Epic ("epic 1", "desc 1", TaskStatus.NEW));
        taskManager.addSubtask(1, new Subtask("subtask 1.1", "desc 1.1", TaskStatus.DONE));
        taskManager.getEpic(1);
        taskManager.getSimpleTask(0);
        taskManager.getSubtask(2);
        taskManager.getEpic(1);
        List<Integer> id = Arrays.asList(0, 2 ,1);
        assertEquals (taskManager.history().stream().map(Task::getId).collect(Collectors.toList()), id);
    }

    @Test
    public void getAllTasksTaskManagerTest() {
        assertEquals (taskManager.getAllTasks().size(), 0);
        taskManager.addSimpleTask(new SimpleTask ("task 1", "desc 1", TaskStatus.IN_PROGRESS));
        taskManager.addEpic(new Epic ("epic 1", "desc 1", TaskStatus.NEW));
        assertEquals (taskManager.getAllTasks().size(), 2);
        assertEquals (taskManager.getSubtasksIdOfEpic(1).size(), 0);
        taskManager.addSubtask(1, new Subtask("subtask 1.1", "desc 1.1", TaskStatus.DONE));
        taskManager.addSubtask(1, new Subtask("subtask 1.2", "desc 1.2", TaskStatus.NEW));
        assertEquals (taskManager.getSubtasksIdOfEpic(1).size(), 2);
        assertEquals (taskManager.getAllTasks().size(), 4);
    }

    @Test
    public void addDifferentTasksWithNotNullStartTimeInOrdinaryWorkTaskManagerTest() {
        taskManager.addSimpleTask(new SimpleTask ("task 1","desc 1", TaskStatus.NEW,
                "2022-04-02 10:15",
                "15"));
        taskManager.getSimpleTask(0).get().setStartTime(ZonedDateTime.parse("2022-04-02T10:10:00+03:00"));
        taskManager.getSimpleTask(0).get().setDuration(Duration.parse("PT10M"));
        assertEquals(taskManager.getSimpleTask(0).get().getStartTimeToStringForBackup(), "2022-04-02 10:10_GMT+3");
        assertEquals(taskManager.getSimpleTask(0).get().getDurationToString(), "10");
        taskManager.addEpic(new Epic ("epic 1", "desc 1", TaskStatus.NEW));
        taskManager.addSubtask(1, new Subtask("subtask 1.1", "desc 1.1", TaskStatus.NEW,
                "2022-04-02 10:30",
                "15"));
        List<Integer> id = Arrays.asList(0, 2);
        assertEquals (taskManager.getPrioritizedTasks().stream().map(Task::getId).collect(Collectors.toList()), id);
        taskManager.updateSimpleTaskById(new SimpleTask (0,"task 1","desc 1", TaskStatus.IN_PROGRESS,
                "2022-04-02 10:15",
                "15"));
        assertEquals (taskManager.getSimpleTask(0).get().getStatus(), TaskStatus.IN_PROGRESS);
        taskManager.addSimpleTask(new SimpleTask ("task 2","desc 2", TaskStatus.NEW,
                "2022-04-02 10:25",
                "25"));
        taskManager.addSimpleTask(new SimpleTask ("task 3","desc 3", TaskStatus.NEW,
                "2022-04-02 10:00",
                "90"));
        assertEquals (taskManager.getPrioritizedTasks().stream().map(Task::getId).collect(Collectors.toList()), id);
    }

    public abstract void getTaskMapForDifferentTasksWithNotNullStartTimeInOrdinaryWorkTaskManagerTest();

    public abstract void loadFromFileTest();
}


