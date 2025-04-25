package JUnit_Mockito_Lab4.UnitTests;

// 1. Unit Tests for Task (Entity)

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import tasks.model.Task;

import static org.junit.jupiter.api.Assertions.*;
import java.util.Date;

public class TaskTest {

    private Task task;
    private Date startDate;
    private Date endDate;

    @BeforeEach
    void setUp() {
        startDate = new Date();
        endDate = new Date(startDate.getTime() + 3600000); // 1 hour later
    }

    @Test
    void testTaskCreationWithSingleTime() {
        task = new Task("Test Task", startDate);

        assertEquals("Test Task", task.getTitle());
        assertEquals(startDate, task.getTime());
        assertFalse(task.isRepeated());
        assertFalse(task.isActive());
    }

    @Test
    void testTaskCreationWithInterval() {
        task = new Task("Repeated Task", startDate, endDate, 3600);

        assertEquals("Repeated Task", task.getTitle());
        assertEquals(startDate, task.getStartTime());
        assertEquals(endDate, task.getEndTime());
        assertEquals(3600, task.getRepeatInterval());
        assertTrue(task.isRepeated());
    }
}