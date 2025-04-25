package JUnit_Mockito_Lab4.UnitTests;

// 3. Unit Tests for TasksService (Service)

import javafx.collections.ObservableList;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import tasks.model.ArrayTaskList;
import tasks.model.Task;
import tasks.services.TasksService;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
import java.util.ArrayList;
import java.util.Arrays;

public class TasksServiceTest {

    private TasksService tasksService;
    private ArrayTaskList mockTaskList;
    private Task mockTask;

    @BeforeEach
    void setUp() {
        mockTaskList = mock(ArrayTaskList.class);
        mockTask = mock(Task.class);
        tasksService = new TasksService(mockTaskList);
    }

    @Test
    void testGetObservableList() {
        ArrayList<Task> tasksList = new ArrayList<>();
        tasksList.add(mockTask);
        when(mockTaskList.getAll()).thenReturn(tasksList);

        ObservableList<Task> result = tasksService.getObservableList();

        assertEquals(1, result.size());
        assertEquals(mockTask, result.get(0));
    }

    @Test
    void testParseFromStringToSeconds() {
        int result = tasksService.parseFromStringToSeconds("01:30");

        assertEquals(5400, result); // 1 hour 30 minutes = 5400 seconds
    }
}