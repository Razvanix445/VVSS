package tasks.services;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import org.apache.log4j.Logger;
import tasks.model.ArrayTaskList;
import tasks.model.Task;
import tasks.model.TasksOperations;

import java.util.Date;

public class TasksService {

    private static final Logger log = Logger.getLogger(TasksService.class.getName());
    private ArrayTaskList tasks;

    public TasksService(ArrayTaskList tasks) {
        this.tasks = tasks;
    }

    public ObservableList<Task> getObservableList(){
        return FXCollections.observableArrayList(tasks.getAll());
    }

    public String getIntervalInHours(Task task){
        int seconds = task.getRepeatInterval();
        int minutes = seconds / DateService.SECONDS_IN_MINUTE;
        int hours = minutes / DateService.MINUTES_IN_HOUR;
        minutes = minutes % DateService.MINUTES_IN_HOUR;
        return formTimeUnit(hours) + ":" + formTimeUnit(minutes);//hh:MM
    }

    public String formTimeUnit(int timeUnit){
        StringBuilder sb = new StringBuilder();
        if (timeUnit < 10) sb.append("0");
        if (timeUnit == 0) sb.append("0");
        else {
            sb.append(timeUnit);
        }
        return sb.toString();
    }

    public int parseFromStringToSeconds(String stringTime){//hh:MM
        if (stringTime == null || stringTime.isEmpty()) {
            log.error("Empty time string provided!");
            throw new IllegalArgumentException("Time string cannot be empty!");
        }

        try {
            String[] units = stringTime.split(":");
            if (units.length != 2) {
                throw new IllegalArgumentException("Time must be in format HH:MM!");
            }

            int hours = Integer.parseInt(units[0]);
            int minutes = Integer.parseInt(units[1]);

            if (hours < 0 || minutes < 0 || minutes >= 60) {
                throw new IllegalArgumentException("Invalid time values: hours must be >= 0, minutes must be between 0-59!");
            }

            return (hours * DateService.MINUTES_IN_HOUR + minutes) * DateService.SECONDS_IN_MINUTE;
        } catch (NumberFormatException e) {
            log.error("Failed to parse time string: " + stringTime);
            throw new IllegalArgumentException("Time values must be numeric!");
        }
    }

    public Iterable<Task> filterTasks(Date start, Date end){
        if (start == null || end == null) {
            log.error("Null date provided for filtering!");
            throw new IllegalArgumentException("Start and end dates cannot be null!");
        }

        if (start.after(end)) {
            log.error("Start date after end date for filtering!");
            throw new IllegalArgumentException("Start date must be before end date!");
        }

        TasksOperations tasksOps = new TasksOperations(getObservableList());
        return tasksOps.incoming(start, end);
    }

    // ADDING CRUD OPERATIONS ON TASKS

    public void addTask(Task task, ObservableList<Task> tasksList) {
        tasksList.add(task);
        saveTasksList(tasksList);
        log.info("Task added: " + task.getTitle());
    }

    public void updateTask(Task oldTask, Task newTask, ObservableList<Task> tasksList) {
        for (int i = 0; i < tasksList.size(); i++) {
            if (oldTask.equals(tasksList.get(i))) {
                tasksList.set(i, newTask);
                saveTasksList(tasksList);
                log.info("Task updated: " + newTask.getTitle());
                return;
            }
        }
        log.warn("Task not found for update: " + oldTask.getTitle());
    }

    public void deleteTask(Task task, ObservableList<Task> tasksList) {
        if (task == null) {
            log.error("Null task provided for deletion");
            throw new IllegalArgumentException("Task cannot be null");
        }

        boolean removed = tasksList.remove(task);
        if (removed) {
            saveTasksList(tasksList);
            log.info("Task deleted: " + task.getTitle());
        } else {
            log.warn("Task not found for deletion");
        }
    }

    private void saveTasksList(ObservableList<Task> tasksList) {
        TaskIO.rewriteFile(tasksList);
    }
}
