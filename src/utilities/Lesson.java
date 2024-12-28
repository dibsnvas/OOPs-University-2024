package utilities;

import java.time.DayOfWeek;
import java.util.Objects;

import utilities.enums.LessonType;

public class Lesson {
    private final String name;
    private final LessonType type; 
    private final DayOfWeek day;
    private final String time;

    public Lesson() {
        this.name = null;
        this.type = null;
        this.day = null;
        this.time = null;
    }


    public Lesson(String name, LessonType type, DayOfWeek day, String time) {
        this.name = name;
        this.type = type;
        this.day = day;
        this.time = time;
    }

    public String getName() {
        return name;
    }

    public LessonType getType() {
        return type;
    }

    public DayOfWeek getDay() {
        return day;
    }

    public String getTime() {
        return time;
    }

    @Override
    public String toString() {
        return name + " (" + type + ") - " + day + " " + time;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Lesson lesson)) return false;
        return Objects.equals(getName(), lesson.getName()) && getType() == lesson.getType()
                && getDay() == lesson.getDay() && Objects.equals(getTime(), lesson.getTime());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getName(), getType(), getDay(), getTime());
    }
}
