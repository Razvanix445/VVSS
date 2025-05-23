package tasks.model;

import javafx.collections.ObservableList;

import java.util.*;

public class TasksOperations {
    public ArrayList<Task> tasks;

    public TasksOperations(ObservableList<Task> tasksList){
        tasks=new ArrayList<>();
        tasks.addAll(tasksList);
    }
    public Iterable<Task> incoming(Date start, Date end){
        System.out.println(start);
        System.out.println(end);

        if (start == null) {
            System.out.println("Data de start este null!");
            return new ArrayList<>();
        }
        if (end == null) {
            System.out.println("Data de end este null!");
            return new ArrayList<>();
        }
        if (start.after(end)) {
            System.out.println("Data de start este după data de end!");
            return new ArrayList<>();
        }

        ArrayList<Task> incomingTasks = new ArrayList<>();
        for (Task t : tasks) {
            Date nextTime = t.nextTimeAfter(start);
            if (nextTime != null && (nextTime.before(end) || nextTime.equals(end))) {
                incomingTasks.add(t);
                System.out.println(t.getTitle());
            }
        }
        return incomingTasks;
    }

    public SortedMap<Date, Set<Task>> calendar( Date start, Date end){
        Iterable<Task> incomingTasks = incoming(start, end);
        TreeMap<Date, Set<Task>> calendar = new TreeMap<>();

        for (Task t : incomingTasks){
            Date nextTimeAfter = t.nextTimeAfter(start);
            while (nextTimeAfter!= null && (nextTimeAfter.before(end) || nextTimeAfter.equals(end))){
                if (calendar.containsKey(nextTimeAfter)){
                    calendar.get(nextTimeAfter).add(t);
                }
                else {
                    HashSet<Task> oneDateTasks = new HashSet<>();
                    oneDateTasks.add(t);
                    calendar.put(nextTimeAfter,oneDateTasks);
                }
                nextTimeAfter = t.nextTimeAfter(nextTimeAfter);
            }
        }
        return calendar;
    }
}

