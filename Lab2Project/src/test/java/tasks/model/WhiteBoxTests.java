package tasks.model;

import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

import java.util.Calendar;
import java.util.Date;

public class WhiteBoxTests {

    private Task task;
    private Calendar calendar;

    @Before
    public void setUp() {
        calendar = Calendar.getInstance();
    }

    // Helper method to create dates
    private Date createDate(int year, int month, int day, int hour, int minute) {
        calendar.set(year, month, day, hour, minute, 0);
        calendar.set(Calendar.MILLISECOND, 0);
        return calendar.getTime();
    }

    /**
     * Test for Path F02_P01: [1(T) - 2 - 17]
     * Condition: current.after(end) || current.equals(end) = TRUE
     */
    @Test
    public void testPath_P01_CurrentAfterOrEqualsEnd() {
        // Arrange
        Date startTime = createDate(2023, Calendar.JANUARY, 1, 10, 0);
        Date endTime = createDate(2023, Calendar.JANUARY, 10, 10, 0);
        int interval = 24 * 60 * 60; // One day in seconds
        task = new Task("Test Task", startTime, endTime, interval);
        task.setActive(true);
        Date currentTime = createDate(2023, Calendar.JANUARY, 10, 10, 0); // Equal to end time

        // Act
        Date nextTime = task.nextTimeAfter(currentTime);

        // Assert
            assertNull("Should return null when current equals end", nextTime);
    }

    /**
     * Test for Path F02_P02: [1(F) - 3(T) - 4 - 5(T) - 6 - 17]
     * Condition: isRepeated() && isActive() = TRUE, current.before(start) = TRUE
     */
    @Test
    public void testPath_P02_RepeatedActiveBeforeStart() {
        // Arrange
        Date startTime = createDate(2023, Calendar.JANUARY, 1, 10, 0);
        Date endTime = createDate(2023, Calendar.JANUARY, 10, 10, 0);
        int interval = 24 * 60 * 60; // One day in seconds
        task = new Task("Test Task", startTime, endTime, interval);
        task.setActive(true);
        Date currentTime = createDate(2022, Calendar.DECEMBER, 31, 10, 0);

        // Act
        Date nextTime = task.nextTimeAfter(currentTime);

        // Assert
        assertEquals("Should return start time when current is before start",
                startTime, nextTime);
    }

    /**
     * Test for Path F02_P03: [1(F) - 3(T) - 4 - 5(F) - 7(T) - 8(T) - 9(T) - 10 - 17]
     * Condition: isRepeated() && isActive() = TRUE, current.before(start) = FALSE,
     *          (current.after(start) || current.equals(start)) && (current.before(end) || current.equals(end)) = TRUE,
     *          current.equals(timeAfter) = TRUE
     */
    @Test
    public void testPath_P03_RepeatedActiveEqualsTimeAfter() {
        // Arrange
        Date startTime = createDate(2023, Calendar.JANUARY, 1, 10, 0);
        Date endTime = createDate(2023, Calendar.JANUARY, 10, 10, 0);
        int interval = 24 * 60 * 60; // One day in seconds
        task = new Task("Test Task", startTime, endTime, interval);
        task.setActive(true);
        Date currentTime = createDate(2023, Calendar.JANUARY, 1, 10, 0); // Equal to start time
        Date expectedNext = createDate(2023, Calendar.JANUARY, 2, 10, 0); // start + interval

        // Act
        Date nextTime = task.nextTimeAfter(currentTime);

        // Assert
        assertEquals("Should return start+interval when current equals start",
                expectedNext, nextTime);
    }

    /**
     * Test for Path F02_P04: [1(F) - 3(T) - 4 - 5(F) - 7(T) - 8(T) - 9(F) - 11(T) - 12 - 17]
     * Condition: isRepeated() && isActive() = TRUE, current.before(start) = FALSE,
     *          (current.after(start) || current.equals(start)) && (current.before(end) || current.equals(end)) = TRUE,
     *          current.equals(timeAfter) = FALSE,
     *          current.after(timeBefore) && current.before(timeAfter) = TRUE
     */
    @Test
    public void testPath_P04_RepeatedActiveBetweenTimepoints() {
        // Arrange
        Date startTime = createDate(2023, Calendar.JANUARY, 1, 10, 0);
        Date endTime = createDate(2023, Calendar.JANUARY, 10, 10, 0);
        int interval = 24 * 60 * 60; // One day in seconds
        task = new Task("Test Task", startTime, endTime, interval);
        task.setActive(true);
        Date currentTime = createDate(2023, Calendar.JANUARY, 2, 15, 0); // Between intervals
        Date expectedNext = createDate(2023, Calendar.JANUARY, 2, 10, 0); // timeBefore (day 2)

        // Act
        Date nextTime = task.nextTimeAfter(currentTime);

        // Assert
        assertEquals("Should return timeBefore when current is between timepoints",
                expectedNext, nextTime);
    }

    /**
     * Test for Path F02_P05: [1(F) - 3(T) - 4 - 5(F) - 7(T) - 8(T) - 9(F) - 11(F) - 13 - 8(F) - 14(T) - 15 - 17]
     * This is a complex path with loop exit, then !isRepeated() && current.before(time) && isActive() = TRUE
     */
    @Test
    public void testPath_P05_LoopExitsThenNonRepeatedBeforeTask() {
        // Arrange
        Date startTime = createDate(2023, Calendar.JANUARY, 1, 10, 0);
        Date endTime = createDate(2023, Calendar.JANUARY, 3, 10, 0);
        int interval = 24 * 60 * 60; // One day in seconds
        task = new Task("Test Task", startTime, endTime, interval);
        task.setActive(true);

        // First loop through all intervals
        task.setTime(createDate(2023, Calendar.JANUARY, 4, 10, 0)); // Make it non-repeated
        Date currentTime = createDate(2023, Calendar.JANUARY, 2, 23, 59); // Cause loop to exit
        Date expectedNext = createDate(2023, Calendar.JANUARY, 4, 10, 0); // task.time

        // Act
        Date nextTime = task.nextTimeAfter(currentTime);

        // Assert
        assertEquals("Should return task time for non-repeated task after loop exit",
                expectedNext, nextTime);
    }

    /**
     * Test for Path F02_P06: [1(F) - 3(T) - 4 - 5(F) - 7(F) - 14(T) - 15 - 17]
     * Condition: isRepeated() && isActive() = TRUE initially, then task changed to non-repeated
     *          with !isRepeated() && current.before(time) && isActive() = TRUE
     */
    @Test
    public void testPath_P06_RepeatedActiveOutsideRangeThenNonRepeatedBefore() {
        // Arrange
        Date startTime = createDate(2023, Calendar.JANUARY, 1, 10, 0);
        Date endTime = createDate(2023, Calendar.JANUARY, 10, 10, 0);
        int interval = 24 * 60 * 60; // One day in seconds
        task = new Task("Test Task", startTime, endTime, interval);
        task.setActive(true);

        // Modify to non-repeated
        task.setTime(createDate(2023, Calendar.JANUARY, 15, 10, 0));
        Date currentTime = createDate(2023, Calendar.JANUARY, 11, 10, 0);
        Date expectedNext = createDate(2023, Calendar.JANUARY, 15, 10, 0);

        // Act
        Date nextTime = task.nextTimeAfter(currentTime);

        // Assert
        assertEquals("Should return task time for non-repeated task when current is before it",
                expectedNext, nextTime);
    }

    /**
     * Test for Path F02_P07: [1(F) - 3(T) - 4 - 5(F) - 7(F) - 14(F) - 16 - 17]
     * Condition: isRepeated() && isActive() = TRUE initially, then task changed to non-repeated and inactive
     *          with !isRepeated() && current.before(time) && isActive() = FALSE
     */
    @Test
    public void testPath_P07_RepeatedActiveOutsideRangeThenNonRepeatedInactive() {
        // Arrange
        Date startTime = createDate(2023, Calendar.JANUARY, 1, 10, 0);
        Date endTime = createDate(2023, Calendar.JANUARY, 10, 10, 0);
        int interval = 24 * 60 * 60; // One day in seconds
        task = new Task("Test Task", startTime, endTime, interval);
        task.setActive(true);

        // Modify to non-repeated and inactive
        task.setTime(createDate(2023, Calendar.JANUARY, 15, 10, 0));
        task.setActive(false);
        Date currentTime = createDate(2023, Calendar.JANUARY, 14, 10, 0);

        // Act
        Date nextTime = task.nextTimeAfter(currentTime);

        // Assert
        assertNull("Should return null for inactive non-repeated task", nextTime);
    }

    /**
     * Test for Path F02_P08: [1(F) - 3(F) - 14(T) - 15 - 17]
     * Condition: isRepeated() && isActive() = FALSE,
     *          !isRepeated() && current.before(time) && isActive() = TRUE
     */
    @Test
    public void testPath_P08_NonRepeatedActiveBeforeTask() {
        // Arrange
        Date taskTime = createDate(2023, Calendar.JANUARY, 1, 10, 0);
        task = new Task("Test Task", taskTime);
        task.setActive(true);
        Date currentTime = createDate(2022, Calendar.DECEMBER, 31, 10, 0);

        // Act
        Date nextTime = task.nextTimeAfter(currentTime);

        // Assert
        assertEquals("Should return task time for non-repeated active task when current is before it",
                taskTime, nextTime);
    }

    /**
     * Test for Path F02_P09: [1(F) - 3(F) - 14(F) - 16 - 17]
     * Condition: isRepeated() && isActive() = FALSE,
     *          !isRepeated() && current.before(time) && isActive() = FALSE
     */
    @Test
    public void testPath_P09_NonRepeatedInactive() {
        // Arrange
        Date taskTime = createDate(2023, Calendar.JANUARY, 1, 10, 0);
        task = new Task("Test Task", taskTime);
        task.setActive(false);
        Date currentTime = createDate(2022, Calendar.DECEMBER, 31, 10, 0);

        // Act
        Date nextTime = task.nextTimeAfter(currentTime);

        // Assert
        assertNull("Should return null for non-repeated inactive task", nextTime);
    }
}