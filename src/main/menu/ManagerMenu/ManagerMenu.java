package main.menu.ManagerMenu;

import main.menu.AbstractMenu;
import localization.auth.AuthLocalization;
import users.User;
import users.employee.Manager;
import users.student.Student;
import users.employee.enums.*;
import utilities.Complaint;
import utilities.enums.*;

import java.time.DayOfWeek;
import java.util.List;
import java.util.Scanner;

import database.Database;

public class ManagerMenu extends AbstractMenu {
    private final Manager currentManager;
    private final String language;
    private final Database db = Database.getInstance();

    public ManagerMenu(Manager currentManager, String language) {
        super(language);
        if (currentManager.getManagerType() != ManagerType.OR && currentManager.getManagerType() != ManagerType.DEPARTMENT) {
            throw new IllegalArgumentException(AuthLocalization.getMessage("ACCESS_DENIED", language));
        }
        this.currentManager = currentManager;
        this.language = language;
        initializeActions();
    }

    @Override
    protected void initializeActions() {
        actions.put(1, this::approveRegistrations);
        actions.put(2, this::assignTeachers);
        actions.put(3, this::assignLessonToStudent);
        actions.put(4, this::updateLessonTimeForStudent);
        actions.put(5, this::viewComplaints);
        actions.put(6, this::openNewsMenu);
    }

    @Override
    protected String getMenuTitle() {
        return AuthLocalization.getMessage("MANAGER_MENU_TITLE", language);
    }

    @Override
    protected String getActionDescription(int key) {
        return switch (key) {
            case 1 -> AuthLocalization.getMessage("APPROVE_REGISTRATIONS", language);
            case 2 -> AuthLocalization.getMessage("ASSIGN_TEACHERS", language);
            case 3 -> AuthLocalization.getMessage("ASSIGN_LESSON_TO_STUDENT", language);
            case 4 -> AuthLocalization.getMessage("UPDATE_LESSON_TIME", language);
            case 5 -> AuthLocalization.getMessage("VIEW_COMPLAINTS", language);
            case 6 -> AuthLocalization.getMessage("OPEN_NEWS_MENU", language);
            default -> AuthLocalization.getMessage("UNKNOWN_OPTION", language);
        };
    }

    private void approveRegistrations() {
        System.out.println(AuthLocalization.getMessage("APPROVING_REGISTRATIONS", language));
        // Add logic for approving registrations
    }

    private void assignTeachers() {
        System.out.println(AuthLocalization.getMessage("ASSIGNING_TEACHERS", language));
        // Add logic for assigning teachers
    }

    private void assignLessonToStudent() {
        Scanner scanner = new Scanner(System.in);
        System.out.print(AuthLocalization.getMessage("ENTER_STUDENT_ID", language) + " ");
        String studentId = scanner.nextLine();
        Student student = (Student) Database.getInstance().getUser(studentId);

        if (student == null) {
            System.out.println(AuthLocalization.getMessage("STUDENT_NOT_FOUND", language));
            return;
        }

        System.out.print(AuthLocalization.getMessage("ENTER_DAY", language) + " ");
        String day = scanner.nextLine().toUpperCase();
        System.out.print(AuthLocalization.getMessage("ENTER_LESSON_NAME", language) + " ");
        String lessonName = scanner.nextLine();
        System.out.print(AuthLocalization.getMessage("ENTER_LESSON_TIME", language) + " ");
        String lessonTime = scanner.nextLine();
        System.out.print(AuthLocalization.getMessage("ENTER_LESSON_TYPE", language) + " ");
        String lessonTypeInput = scanner.nextLine().toUpperCase();

        LessonType lessonType;
        if (lessonTypeInput.equals("L")) {
            lessonType = LessonType.LECTURE;
        } else if (lessonTypeInput.equals("P")) {
            lessonType = LessonType.PRACTICE;
        } else {
            System.out.println(AuthLocalization.getMessage("INVALID_LESSON_TYPE", language));
            return;
        }

        try {
            currentManager.assignLesson(student, DayOfWeek.valueOf(day), lessonName, lessonTime, lessonType);
            System.out.println(AuthLocalization.getMessage("LESSON_ASSIGNED_SUCCESS", language) + " " +
                    lessonName + " (" + lessonType + ", " + lessonTime + ") " +
                    AuthLocalization.getMessage("TO_STUDENT", language) + " " +
                    student.getName() + " " +
                    AuthLocalization.getMessage("ON_DAY", language) + " " + day);
        } catch (IllegalArgumentException e) {
            System.out.println(AuthLocalization.getMessage("INVALID_DAY", language));
        }
    }

    private void updateLessonTimeForStudent() {
        Scanner scanner = new Scanner(System.in);
        System.out.print(AuthLocalization.getMessage("ENTER_STUDENT_ID", language) + " ");
        String studentId = scanner.nextLine();
        Student student = (Student) Database.getInstance().getUser(studentId);

        if (student == null) {
            System.out.println(AuthLocalization.getMessage("STUDENT_NOT_FOUND", language));
            return;
        }

        System.out.print(AuthLocalization.getMessage("ENTER_CURRENT_DAY", language) + " ");
        String oldDay = scanner.nextLine().toUpperCase();
        System.out.print(AuthLocalization.getMessage("ENTER_LESSON_NAME", language) + " ");
        String lesson = scanner.nextLine();
        System.out.print(AuthLocalization.getMessage("ENTER_NEW_DAY", language) + " ");
        String newDay = scanner.nextLine().toUpperCase();
        System.out.print(AuthLocalization.getMessage("ENTER_NEW_TIME", language) + " ");
        String newTime = scanner.nextLine();

        try {
            currentManager.updateLessonTime(student, DayOfWeek.valueOf(oldDay), lesson, DayOfWeek.valueOf(newDay), newTime);
            System.out.println(AuthLocalization.getMessage("LESSON_UPDATED_SUCCESS", language));
        } catch (IllegalArgumentException e) {
            System.out.println(AuthLocalization.getMessage("INVALID_DAY", language));
        }
    }

    private void viewComplaints() {
        if (currentManager.getManagerType() != ManagerType.DEPARTMENT) {
            System.out.println(AuthLocalization.getMessage("PERMISSION_DENIED", language));
            return;
        }

        List<Complaint> complaints = db.getComplaintsForUser(currentManager.getId());

        if (complaints.isEmpty()) {
            System.out.println(AuthLocalization.getMessage("NO_COMPLAINTS", language));
        } else {
            System.out.println(AuthLocalization.getMessage("COMPLAINTS_FOR_MANAGER", language) + " " + currentManager.getId() + ":");
            complaints.forEach(System.out::println);
        }
    }

    private void openNewsMenu() {
        NewsMenu newsMenu = new NewsMenu(language);
        newsMenu.execute();
    }
}
