package tasks.validation;

import javafx.scene.control.Alert;
import tasks.model.Task;
import tasks.services.DateService;
import java.time.LocalDate;
import java.util.Date;
import java.util.regex.Pattern;

public class TaskValidator {
    private static final Pattern TIME_PATTERN = Pattern.compile("([01]?[0-9]|2[0-3]):[0-5][0-9]");

    private final DateService dateService;

    public TaskValidator(DateService dateService) {
        this.dateService = dateService;
    }

    public boolean validateTaskInput(String title,
                                     LocalDate startDate, String startTime,
                                     LocalDate endDate, String endTime, String interval, boolean isRepeated) {
        StringBuilder errorMessage = new StringBuilder();

        // Validate title
        if (title == null || title.trim().isEmpty()) {
            errorMessage.append("Title cannot be empty.\n");
        }

        // Validate start date
        if (startDate == null) {
            errorMessage.append("Start date must be selected.\n");
        }

        // Validate start time format
        if (startTime == null || !TIME_PATTERN.matcher(startTime).matches()) {
            errorMessage.append("Start time must be in format HH:MM (e.g., 09:30).\n");
        }

        // If repeated, validate end date, end time, and interval
        if (isRepeated) {
            if (endDate == null) {
                errorMessage.append("End date must be selected for repeated tasks.\n");
            }

            if (endTime == null || !TIME_PATTERN.matcher(endTime).matches()) {
                errorMessage.append("End time must be in format HH:MM (e.g., 09:30).\n");
            }

            if (interval == null || !TIME_PATTERN.matcher(interval).matches()) {
                errorMessage.append("Interval must be in format HH:MM (e.g., 01:30).\n");
            }

            if (startDate != null && endDate != null) {
                try {
                    Date start = dateService.getDateMergedWithTime(startTime, dateService.getDateValueFromLocalDate(startDate));
                    Date end = dateService.getDateMergedWithTime(endTime, dateService.getDateValueFromLocalDate(endDate));

                if (start.after(end)) {
                    errorMessage.append("Start date/time must be before end date/time.\n");
                }

                } catch (IllegalArgumentException e) {
                    errorMessage.append("Start date/time must be before end date/time.\n");
                }
            }
        }

        if (errorMessage.length() > 0) {
            showValidationAlert(errorMessage.toString());
            return false;
        }

        return true;
    }

    public boolean validateTask(Task task) {
        StringBuilder errorMessage = new StringBuilder();

        if (task == null) {
            errorMessage.append("Task cannot be null");
        }

        if (task.getTitle() == null || task.getTitle().trim().isEmpty()) {
            errorMessage.append("Task title cannot be empty");
        }

        if (task.isRepeated() && task.getStartTime().after(task.getEndTime())) {
            errorMessage.append("Start time must be before end time for repeated tasks");
        }

        if (errorMessage.length() > 0) {
            showValidationAlert(errorMessage.toString());
            return false;
        }

        return true;
    }

    private void showValidationAlert(String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Validation Error");
        alert.setHeaderText("Please correct the following errors:");
        alert.setContentText(message);
        alert.showAndWait();
    }
}
