package users.employee;

import users.employee.enums.ManagerType;
import users.student.Student;
import utilities.Schedule;
import utilities.enums.LessonType;

import java.time.DayOfWeek;
import java.util.List;
import java.util.Map;

public class Manager extends Employee {
    private final ManagerType managerType;

    public Manager() {
        super();
        this.managerType = null;
    }

    public Manager(String id, String name, String email, String password) {
        super(id, name, email, password);
        this.managerType = null;
    }

    public Manager(String id, String name, String email, String password, ManagerType managerType) {
        super(id, name, email, password);
        this.managerType = managerType;
    }

    public ManagerType getManagerType() {
        return managerType;
    }

    public void assignLesson(Student student, DayOfWeek day, String lessonName, String lessonTime, LessonType lessonType) {
        if (managerType != ManagerType.OR) {
            System.out.println("Permission denied: Only OR managers can assign lessons.");
            return;
        }

        String formattedLesson = formatLesson(lessonName, lessonTime, lessonType);
        student.addLessonToSchedule(day, formattedLesson);

        System.out.printf("Assigned lesson \"%s\" to %s on %s%n", formattedLesson, student.getName(), day);
    }

    public void updateLessonTime(Student student, DayOfWeek oldDay, String lessonName, DayOfWeek newDay, String newTime) {
        Schedule schedule = student.getSchedule();
        String lessonToUpdate = findLesson(schedule, oldDay, lessonName);

        if (lessonToUpdate == null) {
            System.out.printf("Lesson \"%s\" not found in student's schedule on %s%n", lessonName, oldDay);
            return;
        }

        schedule.removeLesson(oldDay, lessonToUpdate);

        String updatedLesson = lessonName + " (" + newTime + ")";
        schedule.addLesson(newDay, updatedLesson);

        System.out.printf("Updated lesson \"%s\" to new time on %s: %s%n", lessonName, newDay, newTime);
    }

    public void viewStudentSchedule(Student student) {
        Schedule schedule = student.getSchedule();
        Map<DayOfWeek, List<String>> allLessons = schedule.getAllLessons();

        System.out.printf("Schedule for student %s:%n", student.getName());
        for (DayOfWeek day : DayOfWeek.values()) {
            List<String> lessons = allLessons.getOrDefault(day, List.of());
            if (lessons.isEmpty()) {
                System.out.printf("%s: No lessons%n", day);
            } else {
                System.out.printf("%s:%n", day);
                lessons.forEach(lesson -> System.out.printf("  - %s%n", lesson));
            }
        }
    }

    private String formatLesson(String lessonName, String lessonTime, LessonType lessonType) {
        return String.format("%s (%s, %s)", lessonName, lessonType, lessonTime);
    }

    private String findLesson(Schedule schedule, DayOfWeek day, String lessonName) {
        return schedule.getLessons(day).stream()
                .filter(lesson -> lesson.startsWith(lessonName))
                .findFirst()
                .orElse(null);
    }
}
