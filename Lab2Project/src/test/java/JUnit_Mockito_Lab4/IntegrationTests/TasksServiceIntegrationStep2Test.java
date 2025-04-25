package JUnit_Mockito_Lab4.IntegrationTests;

// Integration Test for R with S (Step 2)

import javafx.collections.ObservableList;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import tasks.model.ArrayTaskList;
import tasks.model.Task;
import tasks.services.TasksService;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class TasksServiceIntegrationStep2Test {

    private TasksService tasksService;
    private ArrayTaskList realTaskList;
    private Task mockTask1;
    private Task mockTask2;

    @BeforeEach
    void setUp() {
        realTaskList = new ArrayTaskList(); // Real repository
        mockTask1 = mock(Task.class); // Mock entities
        mockTask2 = mock(Task.class);
        tasksService = new TasksService(realTaskList);
    }

    @Test
    void testServiceWithRealRepositoryAddAndGet() {
        realTaskList.add(mockTask1);
        realTaskList.add(mockTask2);

        ObservableList<Task> result = tasksService.getObservableList();

        assertEquals(2, result.size());
        assertTrue(result.contains(mockTask1));
        assertTrue(result.contains(mockTask2));
    }

    @Test
    void testServiceWithRealRepositoryRemoveAndGet() {
        realTaskList.add(mockTask1);
        realTaskList.add(mockTask2);
        realTaskList.remove(mockTask1);

        ObservableList<Task> result = tasksService.getObservableList();

        assertEquals(1, result.size());
        assertFalse(result.contains(mockTask1));
        assertTrue(result.contains(mockTask2));
    }
}