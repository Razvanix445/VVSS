package JUnit_Mockito_Lab4.IntegrationTests;

// Integration Test for E with S+R (Step 3)

import javafx.collections.ObservableList;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import tasks.model.ArrayTaskList;
import tasks.model.Task;
import tasks.services.TasksService;

import static org.junit.jupiter.api.Assertions.*;
import java.util.Date;

public class TasksServiceIntegrationStep3Test {

    private TasksService tasksService;
    private ArrayTaskList realTaskList;
    private Task realTask1;
    private Task realTask2;

    @BeforeEach
    void setUp() {
        realTaskList = new ArrayTaskList(); // Real repository
        Date now = new Date();
        realTask1 = new Task("Task 1", now); // Real entities
        realTask2 = new Task("Task 2", now, new Date(now.getTime() + 3600000), 1800);
        tasksService = new TasksService(realTaskList);
    }

    @Test
    void testFullIntegrationAddAndGet() {
        realTaskList.add(realTask1);
        realTaskList.add(realTask2);

        ObservableList<Task> result = tasksService.getObservableList();

        assertEquals(2, result.size());
        assertEquals("Task 1", result.get(0).getTitle());
        assertEquals("Task 2", result.get(1).getTitle());
    }

    @Test
    void testFullIntegrationIntervalFormatting() {
        realTaskList.add(realTask2);

        String intervalString = tasksService.getIntervalInHours(realTask2);

        assertEquals("00:30", intervalString); // 1800 seconds = 30 minutes
    }
}