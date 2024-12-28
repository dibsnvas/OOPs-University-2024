package main.menu.studentMenu;

import database.Database;
import localization.auth.AuthLocalization;
import main.menu.AbstractMenu;
import users.student.Student;
import utilities.Course;

import java.util.List;
import java.util.Scanner;

public class CourseMenu extends AbstractMenu {
    private final Student student;
    private final Database db = Database.getInstance();
    private final String language;

    public CourseMenu(Student student, String language) {
        super(language);
        this.student = student;
        this.language = language;
        initializeActions();
    }

    @Override
    protected void initializeActions() {
        actions.put(1, this::viewAvailableCourses);
        actions.put(2, this::viewEnrolledCourses);
        actions.put(3, this::applyForCourse);
    }

    @Override
    protected String getMenuTitle() {
        return AuthLocalization.getMessage("COURSE_MENU_TITLE", language);
    }

    @Override
    protected String getActionDescription(int key) {
        return switch (key) {
            case 1 -> AuthLocalization.getMessage("VIEW_AVAILABLE_COURSES", language);
            case 2 -> AuthLocalization.getMessage("VIEW_ENROLLED_COURSES", language);
            case 3 -> AuthLocalization.getMessage("APPLY_FOR_COURSE", language);
            default -> AuthLocalization.getMessage("UNKNOWN_OPTION", language);
        };
    }

    private void viewAvailableCourses() {
        System.out.println("\n" + AuthLocalization.getMessage("AVAILABLE_COURSES_TITLE", language));
        List<Course> courses = db.getAllCourses();
        if (courses.isEmpty()) {
            System.out.println(AuthLocalization.getMessage("NO_COURSES_AVAILABLE", language));
        } else {
            courses.forEach(course ->
                    System.out.println(course.getCourseId() + ": " + course.getCourseName())
            );
        }
    }

    private void applyForCourse() {
        Scanner scanner = new Scanner(System.in);
        System.out.print(AuthLocalization.getMessage("ENTER_COURSE_ID", language) + " ");
        String courseId = scanner.nextLine();

        try {
            db.addCourseApplication(courseId, student.getId());
            System.out.println(AuthLocalization.getMessage("COURSE_APPLICATION_SUCCESS", language));
        } catch (IllegalArgumentException e) {
            System.out.println(AuthLocalization.getMessage("ERROR_PREFIX", language) + " " + e.getMessage());
        }
    }

    private void viewEnrolledCourses() {
        System.out.println("\n" + AuthLocalization.getMessage("ENROLLED_COURSES_TITLE", language));
        var courses = db.getCoursesForStudent(student.getId());
        if (courses.isEmpty()) {
            System.out.println(AuthLocalization.getMessage("NO_ENROLLED_COURSES", language));
        } else {
            courses.forEach(course ->
                    System.out.println("- " + course.getCourseName())
            );
        }
    }
}
