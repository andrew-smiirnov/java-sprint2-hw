import manager.Managers;
import manager.TaskManager;
import model.Epic;
import model.Subtask;
import model.TaskStatus;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class EpicChangeStatusTest {

    protected TaskManager taskManager;

    @BeforeEach
    public void voidTaskManager() {
    taskManager = Managers.getDefault();
    }

    @Test
    void isTaskManagerEmpty() {
        assertNull(taskManager.getEpic(0), "В трекере задач нет задач");
    }

    @Test
    void checkEpicStatusWithStatusNewForSubtasks(){
        Epic epic = new Epic ("Эпик", "Описание эпика", TaskStatus.NEW);
        List<Subtask> subtasks = generateList3SubtasksWithStatus(
                TaskStatus.NEW,
                TaskStatus.NEW,
                TaskStatus.NEW
        );
        taskManager.addEpic(epic);
        for(Subtask subtask : subtasks){
            taskManager.addSubtask(0, subtask);
        }
        assertEquals(epic.getStatus(), TaskStatus.NEW);
    }

    @Test
    void checkEpicStatusWithStatusDoneForSubtasks(){
        Epic epic = new Epic ("Эпик", "Описание эпика", TaskStatus.DONE);
        List<Subtask> subtasks = generateList3SubtasksWithStatus(
                TaskStatus.DONE,
                TaskStatus.DONE,
                TaskStatus.DONE
        );
        taskManager.addEpic(epic);
        for(Subtask subtask : subtasks){
            taskManager.addSubtask(0, subtask);
        }
        assertEquals(epic.getStatus(), TaskStatus.DONE);
    }

    @Test
    void checkEpicStatusWithStatusNewAndDoneForSubtasks(){
        Epic epic = new Epic ("Эпик", "Описание эпика", TaskStatus.DONE);
        List<Subtask> subtasks = generateList3SubtasksWithStatus(
                TaskStatus.NEW,
                TaskStatus.DONE,
                TaskStatus.NEW
        );
        taskManager.addEpic(epic);
        for(Subtask subtask : subtasks){
            taskManager.addSubtask(0, subtask);
        }
        assertEquals(epic.getStatus(), TaskStatus.IN_PROGRESS);
    }

    @Test
    void checkEpicStatusWithStatusInProgressForSubtasks(){
        Epic epic = new Epic ("Эпик", "Описание эпика", TaskStatus.DONE);
        List<Subtask> subtasks = generateList3SubtasksWithStatus(
                TaskStatus.IN_PROGRESS,
                TaskStatus.IN_PROGRESS,
                TaskStatus.IN_PROGRESS
        );
        taskManager.addEpic(epic);
        for(Subtask subtask : subtasks){
            taskManager.addSubtask(0, subtask);
        }
        assertEquals(epic.getStatus(), TaskStatus.IN_PROGRESS);
    }

    private List<Subtask> generateList3SubtasksWithStatus(TaskStatus first, TaskStatus second, TaskStatus third){
        List<Subtask> subtaskList = new ArrayList<>();
        subtaskList.add(0, new Subtask("subtask 1", "description 1", first));
        subtaskList.add(1, new Subtask("subtask 2", "description 2", second));
        subtaskList.add(2, new Subtask("subtask 3", "description 3", third));
        return subtaskList;
    }
}