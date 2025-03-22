package tasks.services;

import org.apache.log4j.Logger;

import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.regex.Pattern;

public class DateService {
    public static final int SECONDS_IN_MINUTE = 60;
    public static final int MINUTES_IN_HOUR = 60;
    public static final int HOURS_IN_A_DAY = 24;

    private static final Logger log = Logger.getLogger(DateService.class.getName());
    private static final Pattern TIME_PATTERN = Pattern.compile("([01]?[0-9]|2[0-3]):[0-5][0-9]");

    private TasksService service;

    public DateService(TasksService service){
        this.service = service;
    }

    public static LocalDate getLocalDateValueFromDate(Date date){//for setting to DatePicker - requires LocalDate
        if (date == null) {
            log.error("Null date provided to getLocalDateValueFromDate!");
            throw new IllegalArgumentException("Date cannot be null!");
        }
        return date.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();

    }
    public Date getDateValueFromLocalDate(LocalDate localDate){//for getting from DatePicker
        if (localDate == null) {
            log.error("Null localDate provided to getDateValueFromLocalDate");
            throw new IllegalArgumentException("LocalDate cannot be null");
        }
        Instant instant = Instant.from(localDate.atStartOfDay(ZoneId.systemDefault()));
        return Date.from(instant);
    }

    public Date getDateMergedWithTime(String time, Date noTimeDate) {//to retrieve Date object from both DatePicker and time field
        if (time == null || time.isEmpty()) {
            log.error("Empty time string provided to getDateMergedWithTime");
            throw new IllegalArgumentException("Time string cannot be empty");
        }

        if (noTimeDate == null) {
            log.error("Null date provided to getDateMergedWithTime");
            throw new IllegalArgumentException("Date cannot be null");
        }

        if (!TIME_PATTERN.matcher(time).matches()) {
            log.error("Invalid time format: " + time);
            throw new IllegalArgumentException("Time must be in format HH:MM");
        }

        String[] units = time.split(":");
        int hour = Integer.parseInt(units[0]);
        int minute = Integer.parseInt(units[1]);

        // The hour cannot be 24 or the minute cannot be 60, so >= instead of >.
        if (hour >= HOURS_IN_A_DAY || minute >= MINUTES_IN_HOUR) {
            log.error("Invalid time values: hour=" + hour + ", minute=" + minute);
            throw new IllegalArgumentException("Time unit exceeds bounds: hours must be 0-23, minutes must be 0-59");
        }

        Calendar calendar = GregorianCalendar.getInstance();
        calendar.setTime(noTimeDate);
        calendar.set(Calendar.HOUR_OF_DAY, hour);
        calendar.set(Calendar.MINUTE, minute);
        return calendar.getTime();
    }

    public String getTimeOfTheDayFromDate(Date date){//to set in detached time field
        if (date == null) {
            log.error("Null date provided to getTimeOfTheDayFromDate");
            throw new IllegalArgumentException("Date cannot be null");
        }

        Calendar calendar = GregorianCalendar.getInstance();
        calendar.setTime(date);
        int hours = calendar.get(Calendar.HOUR_OF_DAY);
        int minutes = calendar.get(Calendar.MINUTE);

        return service.formTimeUnit(hours) + ":" + service.formTimeUnit(minutes);
    }

    public boolean isTimeValid(String timeString) {
        return timeString != null && TIME_PATTERN.matcher(timeString).matches();
    }

    public boolean isDateRangeValid(Date startDate, Date endDate) {
        if (startDate == null || endDate == null) {
            return false;
        }
        return !startDate.after(endDate);
    }
}
