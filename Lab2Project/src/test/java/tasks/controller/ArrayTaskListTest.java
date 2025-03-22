package tasks.controller;

import org.junit.jupiter.api.*;
import static org.junit.jupiter.api.Assertions.*;
import tasks.model.ArrayTaskList;
import tasks.model.Task;
import java.util.Date;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class ArrayTaskListTest {
    ArrayTaskList taskList;

    @BeforeAll
    static void initAll() {
        System.out.println("Starting tests...");
    }

    @BeforeEach
    void setUp() {
        taskList = new ArrayTaskList();
    }

    @AfterEach
    void tearDown() {
        taskList = null;
    }

    @AfterAll
    static void tearDownAll() {
        System.out.println("All tests completed.");
    }

    @Test
    @Order(1)
    @DisplayName("ECP - Add valid task")
    void addValidTask() {
        // Arrange
        Task task = new Task("Test Task", new Date(10000));

        // Act
        taskList.add(task);

        // Assert
        assertEquals(1, taskList.size(), "Size should be 1 after adding a valid task");
    }

    @Test
    @Order(2)
    @DisplayName("ECP - Add null task throws NullPointerException")
    void addNullTask() {
        // Arrange
        Task task = null;

        // Act & Assert
        assertThrows(NullPointerException.class, () -> taskList.add(task), "Adding null should throw NullPointerException");
    }

    @Test
    @Order(3)
    @DisplayName("BVA - Add task when list is at initial capacity")
    void addTaskAtInitialCapacity() {
        // Arrange
        for (int i = 0; i < 9; i++) {
            taskList.add(new Task("Task " + i, new Date(i * 1000L)));
        }
        Task boundaryTask = new Task("Boundary Task", new Date(9000));

        // Act
        taskList.add(boundaryTask);

        // Assert
        assertEquals(10, taskList.size(), "Size should be 10 after adding at initial capacity");
    }

    @Test
    @Order(4)
    @DisplayName("BVA - Add task when list exceeds initial capacity")
    void addTaskExceedingInitialCapacity() {
        // Arrange
        for (int i = 0; i < 10; i++) {
            taskList.add(new Task("Task " + i, new Date(i * 1000L)));
        }
        Task boundaryTask = new Task("Exceed Task", new Date(10000));

        // Act
        taskList.add(boundaryTask);

        // Assert
        assertEquals(11, taskList.size(), "Size should be 11 after exceeding initial capacity");
    }

    @Test
    @Order(5)
    @DisplayName("ECP - Add duplicate task")
    void addDuplicateTask() {
        // Arrange
        Task task = new Task("Duplicate Task", new Date(5000));
        taskList.add(task);

        // Act
        taskList.add(task);

        // Assert
        assertEquals(2, taskList.size(), "Size should be 2 after adding duplicate task");
    }
}
