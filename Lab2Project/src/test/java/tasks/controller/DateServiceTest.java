package tasks.controller;

import org.junit.jupiter.api.*;
import tasks.model.ArrayTaskList;
import tasks.services.DateService;
import tasks.services.TasksService;

import static org.junit.jupiter.api.Assertions.*;
import java.util.Date;
import java.util.Calendar;

public class DateServiceTest {

    private DateService dateService;
    private TasksService tasksService;

    private ArrayTaskList arrayTaskList;
    @BeforeAll
    static void setupAll() {
        System.out.println("Starting DateService tests...");
    }

    @BeforeEach
    void setup() {
        arrayTaskList = new ArrayTaskList();
        tasksService = new TasksService(arrayTaskList);
        dateService = new DateService(tasksService);
    }

    // ECP Valid Case 1: Normal time format
    @DisplayName("Test valid time format - ECP")
    @Test
    void testValidTimeFormat() {
        Date date = new Date();
        Date result = dateService.getDateMergedWithTime("08:30", date);
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(result);
        assertEquals(8, calendar.get(Calendar.HOUR_OF_DAY));
        assertEquals(30, calendar.get(Calendar.MINUTE));
    }

    // ECP Invalid Case 1: Incorrect time format
    @DisplayName("Test invalid time format - ECP")
    @Test
    void testInvalidTimeFormat() {
        Date date = new Date();
        assertThrows(IllegalArgumentException.class, () -> {
            dateService.getDateMergedWithTime("-1:3", date);
        });
    }

    // ECP Invalid Case 2: Hour exceeds bounds
    @DisplayName("Test time exceeds bounds - ECP")
    @Test
    void testTimeExceedsBounds() {
        Date date = new Date();
        assertThrows(IllegalArgumentException.class, () -> {
            dateService.getDateMergedWithTime("25:00", date);
        });
    }

    // ECP Invalid Case 3: Minute exceeds bounds
    @DisplayName("Test minute exceeds bounds - ECP")
    @Test
    void testMinuteExceedsBounds() {
        Date date = new Date();
        assertThrows(IllegalArgumentException.class, () -> {
            dateService.getDateMergedWithTime("23:61", date);
        });
    }

    // BVA Valid Case 1: Lower boundary of time
    @DisplayName("Test time at lower boundary - BVA")
    @Test
    void testTimeAtLowerBoundary() {
        Date date = new Date();
        Date result = dateService.getDateMergedWithTime("00:00", date);
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(result);
        assertEquals(0, calendar.get(Calendar.HOUR_OF_DAY));
        assertEquals(0, calendar.get(Calendar.MINUTE));
    }

    // BVA Valid Case 2: Upper boundary of time
    @DisplayName("Test time at upper boundary - BVA")
    @Test
    void testTimeAtUpperBoundary() {
        Date date = new Date();
        Date result = dateService.getDateMergedWithTime("23:59", date);
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(result);
        assertEquals(23, calendar.get(Calendar.HOUR_OF_DAY));
        assertEquals(59, calendar.get(Calendar.MINUTE));
    }

    // BVA Invalid Case 1: Below lower boundary
    @DisplayName("Test hour below lower boundary - BVA")
    @Test
    void testHourBelowLowerBoundary() {
        Date date = new Date();
        assertThrows(IllegalArgumentException.class, () -> {
            dateService.getDateMergedWithTime("-1:30", date);
        });
    }

    // BVA Invalid Case 2: Above upper boundary
    @DisplayName("Test minute above upper boundary - BVA")
    @Test
    void testMinuteAboveUpperBoundary() {
        Date date = new Date();
        assertThrows(IllegalArgumentException.class, () -> {
            dateService.getDateMergedWithTime("23:60", date);
        });
    }

    @AfterEach
    void tearDown() {
        System.out.println("Test finished.");
    }

    @AfterAll
    static void tearDownAll() {
        System.out.println("All DateService tests completed.");
    }
}