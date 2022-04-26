import manager.InMemoryHistoryManager;
import manager.InMemoryTaskManager;
import model.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.util.*;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class InMemoryTaskManagerTest extends TaskManagerTest<InMemoryTaskManager> {

    @Override
    public void initializeTaskManager() {
        taskManager = new InMemoryTaskManager(new InMemoryHistoryManager<>());
    }

    @Override
    @BeforeEach
    public void beforeEach(){
        initializeTaskManager();
    }

    @Override
    @Test
    public void getTaskMapForDifferentTasksWithNotNullStartTimeInOrdinaryWorkTaskManagerTest() {

        assertEquals (taskManager.getAllTasks().size(), 0);
        taskManager.addSimpleTask(new SimpleTask ("task 1","desc 1", TaskStatus.NEW,
                "2022-04-02 10:15",
                "15"));
        taskManager.addEpic(new Epic("epic 1", "descE 1", TaskStatus.NEW));
        taskManager.addSubtask(1, new Subtask("subT 1.1", "desc 1.1", TaskStatus.NEW,
                "2022-04-02 10:30",
                "15"));
        assertEquals (taskManager.getAllTasks().size(), 3);
        taskManager.addSimpleTask(new SimpleTask ("task 2","desc 2", TaskStatus.NEW,
                "2022-04-01 11:45",
                "30"));
        taskManager.addSimpleTask(new SimpleTask ("task 3","desc 3", TaskStatus.NEW,
                "2022-03-02 14:00",
                "90"));
        List<Integer> id = Arrays.asList(0, 1, 2, 3, 4);
        Map<Integer, Task> taskMap = taskManager.getAllTasks();
        List<Integer> taskId = new ArrayList<>();
        for (Integer key : taskMap.keySet()) {
            taskId.add(key);
        }
        assertEquals (taskId, id);
    }

    @Override
    @Test
    public void loadFromFileTest(){}
}

