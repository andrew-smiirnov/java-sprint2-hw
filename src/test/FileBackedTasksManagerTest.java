import manager.FileBackedTasksManager;
import manager.TaskManager;
import model.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;



public class FileBackedTasksManagerTest extends TaskManagerTest<FileBackedTasksManager> {

    @Override
    public void getTaskManager() {
        File file = new File("src/files/test.csv");
        taskManager = new FileBackedTasksManager(file);
    }

    @Override
    @BeforeEach
    public void beforeEach(){
        getTaskManager();
        taskManager.deleteAllTasks();
    }

    @Override
    @Test
    public void getTaskMapForDifferentTasksWithNotNullStartTimeInOrdinaryWorkTaskManagerTest() {

        assertEquals (taskManager.getTaskMap().size(), 0);
        taskManager.addSimpleTask(new SimpleTask ("task 1","desc 1", TaskStatus.NEW,
                "2022-04-02 10:15",
                "15"));
        taskManager.addEpic(new Epic("epic 1", "descE 1", TaskStatus.NEW));
        taskManager.addSubtask(1, new Subtask("subT 1.1", "desc 1.1", TaskStatus.NEW,
                "2022-04-02 10:30",
                "15"));

        assertEquals (taskManager.getTaskMap().size(), 3);
        taskManager.addSimpleTask(new SimpleTask ("task 2","desc 2", TaskStatus.NEW,
                "2022-04-01 11:45",
                "30"));
        taskManager.addSimpleTask(new SimpleTask ("task 3","desc 3", TaskStatus.NEW,
                "2022-03-02 14:00",
                "90"));
        List<Integer> id = Arrays.asList(0, 1, 2, 3, 4);
        Map<Integer, Task> taskMap = taskManager.getTaskMap();
        List<Integer> taskId = new ArrayList<>();
        for (Integer key : taskMap.keySet()) {
            taskId.add(key);
        }
        assertEquals (taskId, id);
    }

    @Override
    @Test
    public void loadFromFileTest() {
        taskManager.addSimpleTask(new SimpleTask("task 1", "desc 1", TaskStatus.NEW,
                "2022-04-02 10:15",
                "15"));

        taskManager.addEpic(new Epic("epic 1", "descE 1", TaskStatus.NEW));
        taskManager.addSubtask(1, new Subtask("subT 1.1", "desc 1.1", TaskStatus.NEW,
                "2022-04-02 10:30",
                "15"));
        assertEquals(taskManager.getTaskMap().size(), 3);
        File backupFile = new File("src/files/test.csv");
        TaskManager backedManager = new FileBackedTasksManager().loadFromFile(backupFile);
        backedManager.addSimpleTask(new SimpleTask ("task 2","desc 2", TaskStatus.DONE));
        assertEquals (backedManager.getSimpleTask(3).getStatus(), TaskStatus.DONE);
    }
}

