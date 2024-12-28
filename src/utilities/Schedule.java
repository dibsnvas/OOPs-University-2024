package utilities;

import java.io.Serializable;
import java.time.DayOfWeek;
import java.util.*;

public class Schedule implements Serializable {

    private Map<DayOfWeek, Vector<String>> weeklySchedule;

    public Schedule() {
        weeklySchedule = new HashMap<>();
        for (DayOfWeek day : DayOfWeek.values()) {
            weeklySchedule.put(day, new Vector<>());
        }
    }

    public void addLesson(DayOfWeek day, String lesson) {
        weeklySchedule.get(day).add(lesson);
    }

    public boolean removeLesson(DayOfWeek day, String lesson) {
        List<String> lessons = weeklySchedule.get(day);
        if (lessons != null) {
            return lessons.remove(lesson);
        }
        return false;
    }

    public List<String> getLessons(DayOfWeek day) {
        return weeklySchedule.getOrDefault(day, new Vector<>());
    }

    public Map<DayOfWeek, List<String>> getAllLessons() {
        Map<DayOfWeek, List<String>> result = new HashMap<>();
        for (DayOfWeek day : weeklySchedule.keySet()) {
            result.put(day, new ArrayList<>(weeklySchedule.get(day)));
        }
        return result;
    }

    @Override
    public String toString() {
        StringBuilder scheduleString = new StringBuilder("Weekly Schedule:\n");
        for (DayOfWeek day : DayOfWeek.values()) {
            scheduleString.append(day.name())
                          .append(": ")
                          .append(weeklySchedule.get(day).isEmpty() ? "No lessons" : weeklySchedule.get(day))
                          .append("\n");
        }
        return scheduleString.toString();
    }
}
