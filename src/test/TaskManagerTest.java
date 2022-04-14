import manager.TaskManager;
import model.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;


import static org.junit.jupiter.api.Assertions.*;

public abstract class TaskManagerTest<T extends TaskManager> {

    protected T taskManager;

    public abstract void getTaskManager();

    @BeforeEach
    public abstract void beforeEach();


    @Test
    public void addUpdateAndDeleteSimpleTaskInOrdinaryWorkTaskManagerTest() {
        taskManager.addSimpleTask(new SimpleTask ("task 1", "desc 1", TaskStatus.NEW));
        SimpleTask simpleTask = taskManager.getSimpleTask(0);
        assertEquals(simpleTask.getId(), 0);
        assertEquals(taskManager.getSimpleTask(0).getStatus(), TaskStatus.NEW);
        taskManager.updateSimpleTaskById(new SimpleTask (0, "task 1", "desc 1", TaskStatus.DONE));
        assertEquals(taskManager.getSimpleTask(0).getStatus(), TaskStatus.DONE);
        taskManager.deleteSimpleTaskById(0);
        assertNull(taskManager.getSimpleTask(0), "В трекере задач нет задач");
    }

    @Test
    public void addUpdateAndDeleteEpicInOrdinaryWorkTaskManagerTest() {
        taskManager.addEpic(new Epic ("epic 1", "desc 1", TaskStatus.NEW));
        Epic epic = taskManager.getEpic(0);
        assertEquals(epic.getId(), 0);
        taskManager.updateEpicById(new Epic (0,"epic 1", "desc 1", TaskStatus.DONE));
        assertEquals(taskManager.getEpic(0).getStatus(), TaskStatus.NEW);
        taskManager.deleteEpicById(0);
        assertNull(taskManager.getSimpleTask(0), "В трекере задач нет задач");
    }

    @Test
    public void addUpdateAndDeleteSubtaskInOrdinaryWorkTaskManagerTest() {
        taskManager.addEpic(new Epic ("epic 1", "desc 1", TaskStatus.NEW));
        taskManager.addSubtask(0, new Subtask("subtask 1.1", "desc 1.1", TaskStatus.NEW));

        assertEquals(taskManager.getEpic(0).getSubtasks().size(), 1);
        assertEquals(taskManager.getSubtask(1).getStatus(), TaskStatus.NEW);
        assertEquals(taskManager.getSubtask(1).getEpicId(), 0);
        taskManager.updateSubtaskById(new Subtask(1,"subtask 1.1", "desc 1.1", TaskStatus.IN_PROGRESS));
        assertEquals(taskManager.getEpic(0).getStatus(), TaskStatus.IN_PROGRESS);
        assertEquals(taskManager.getSubtask(1).getStatus(), TaskStatus.IN_PROGRESS);
        taskManager.deleteSubtaskById(1);
        assertNull(taskManager.getSubtask(1), "В трекере задач нет задач");
    }

    @Test
    public void changeEpicStatusInOrdinaryWorkTaskManagerTest() {
        taskManager.addEpic(new Epic ("epic 1", "desc 1", TaskStatus.NEW));
        taskManager.addSubtask(0, new Subtask("subtask 1.1", "desc 1.1", TaskStatus.NEW));
        taskManager.addSubtask(0, new Subtask("subtask 1.2", "desc 1.2", TaskStatus.NEW));

        assertEquals(taskManager.getEpic(0).getSubtasks().size(), 2);
        assertEquals(taskManager.getEpic(0).getStatus(), TaskStatus.NEW);
        taskManager.updateSubtaskById(new Subtask(1,"subtask 1.1", "desc 1.1", TaskStatus.IN_PROGRESS));
        assertEquals(taskManager.getEpic(0).getStatus(), TaskStatus.IN_PROGRESS);
        taskManager.updateSubtaskById(new Subtask(2,"subtask 1.2", "desc 1.2", TaskStatus.DONE));
        assertEquals(taskManager.getEpic(0).getStatus(), TaskStatus.IN_PROGRESS);
        taskManager.updateSubtaskById(new Subtask(1,"subtask 1.1", "desc 1.1", TaskStatus.DONE));
        assertEquals(taskManager.getEpic(0).getStatus(), TaskStatus.DONE);
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
    public void addDifferentTasksWithNotNullStartTimeInOrdinaryWorkTaskManagerTest() {
        taskManager.addSimpleTask(new SimpleTask ("task 1","desc 1", TaskStatus.NEW,
                "2022-04-02 10:15",
                "15"));
        taskManager.addEpic(new Epic ("epic 1", "desc 1", TaskStatus.NEW));
        taskManager.addSubtask(1, new Subtask("subtask 1.1", "desc 1.1", TaskStatus.NEW,
                "2022-04-02 10:30",
                "15"));
        List<Integer> id = Arrays.asList(0, 2);
        assertEquals (taskManager.getPrioritizedTasks().stream().map(Task::getId).collect(Collectors.toList()), id);
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


