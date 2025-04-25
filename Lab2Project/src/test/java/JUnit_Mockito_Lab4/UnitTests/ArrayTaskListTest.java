package JUnit_Mockito_Lab4.UnitTests;

// 2. Unit Tests for ArrayTaskList (Repository)

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import tasks.model.Task;
import tasks.model.ArrayTaskList;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class ArrayTaskListTest {

    private ArrayTaskList taskList;
    private Task mockTask1;
    private Task mockTask2;

    @BeforeEach
    void setUp() {
        taskList = new ArrayTaskList();
        mockTask1 = mock(Task.class);
        mockTask2 = mock(Task.class);
    }

    @Test
    void testAddTask() {
        taskList.add(mockTask1);

        assertEquals(1, taskList.size());
        assertEquals(mockTask1, taskList.getTask(0));
    }

    @Test
    void testRemoveTask() {
        taskList.add(mockTask1);
        taskList.add(mockTask2);

        boolean removed = taskList.remove(mockTask1);

        assertTrue(removed);
        assertEquals(1, taskList.size());
        assertEquals(mockTask2, taskList.getTask(0));
    }
}