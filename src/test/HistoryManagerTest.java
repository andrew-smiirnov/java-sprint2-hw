import manager.HistoryManager;
import manager.InMemoryHistoryManager;
import model.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;


public class HistoryManagerTest {

    HistoryManager historyManager;

    public void getHistoryManager(){
        historyManager = new InMemoryHistoryManager<>();
    }

    @BeforeEach
    public void beforeEach(){
        getHistoryManager();
    }

    @Test
    public void checkVoidHistoryManagerTest(){
        assertEquals (historyManager.getHistory().size(), 0);
        historyManager.add(new SimpleTask(0,"task 1", "desc 1", TaskStatus.NEW));
        historyManager.add(new SimpleTask(1,"task 2", "desc 2", TaskStatus.NEW));
        historyManager.add(new SimpleTask(2,"task 3", "desc 3", TaskStatus.NEW));
        assertEquals (historyManager.getHistory().size(), 3);
        historyManager.clearHistoryList();
        assertEquals (historyManager.getHistory().size(), 0);
    }

    @Test
    public void checkDuplicationInHistoryManagerTest(){
        historyManager.add(new SimpleTask(0,"task 1", "desc 1", TaskStatus.NEW));
        historyManager.add(new SimpleTask(1,"task 2", "desc 2", TaskStatus.NEW));
        historyManager.add(new SimpleTask(2,"task 3", "desc 3", TaskStatus.NEW));
        assertEquals (historyManager.getHistory().get(0).getId(), 0);
        assertEquals (historyManager.getHistory().get(1).getId(), 1);
        assertEquals (historyManager.getHistory().get(2).getId(), 2);
        historyManager.add(new SimpleTask(0,"task 1", "desc 1", TaskStatus.DONE));
        assertEquals (historyManager.getHistory().get(0).getId(), 1);
        assertEquals (historyManager.getHistory().get(1).getId(), 2);
        assertEquals (historyManager.getHistory().get(2).getId(), 0);
        assertEquals (historyManager.getHistory().get(2).getStatus(), TaskStatus.DONE);
    }

    @Test
    public void checkDeleteFromDifferentLocationInHistoryManagerTest() {
        historyManager.add(new SimpleTask(0, "task 1", "desc 1", TaskStatus.NEW));
        historyManager.add(new SimpleTask(1, "task 2", "desc 2", TaskStatus.NEW));
        historyManager.add(new SimpleTask(2, "task 3", "desc 3", TaskStatus.NEW));
        historyManager.add(new SimpleTask(3, "task 4", "desc 4", TaskStatus.NEW));
        historyManager.add(new SimpleTask(4, "task 5", "desc 5", TaskStatus.NEW));
        assertEquals(historyManager.getHistory().get(0).getId(), 0);
        assertEquals(historyManager.getHistory().get(2).getId(), 2);
        assertEquals(historyManager.getHistory().get(4).getId(), 4);
        historyManager.remove(2);
        assertEquals(historyManager.getHistory().get(2).getId(), 3);
        historyManager.remove(0);
        assertEquals(historyManager.getHistory().get(0).getId(), 1);
        historyManager.remove(4);
        assertEquals(historyManager.getHistory().get(1).getId(), 3);
        assertEquals(historyManager.getHistory().size(), 2);
    }
}
