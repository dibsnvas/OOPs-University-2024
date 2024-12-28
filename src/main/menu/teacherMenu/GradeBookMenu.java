package main.menu.teacherMenu;

import database.Database;
import localization.auth.AuthLocalization;
import main.menu.AbstractMenu;
import users.student.Student;
import utilities.Mark;

import java.util.List;
import java.util.Scanner;

public class GradeBookMenu extends AbstractMenu {
    private final Database db = Database.getInstance();
    private final String courseId;
    private final String language;

    public GradeBookMenu(String courseId, String language) {
        super(language);
        this.courseId = courseId;
        this.language = language;
        initializeActions();
    }

    @Override
    protected void initializeActions() {
        actions.put(1, this::viewStudents);
        actions.put(2, this::addMarkToStudent);
        actions.put(3, this::viewStudentMarks);
    }

    @Override
    protected String getMenuTitle() {
        return AuthLocalization.getMessage("GRADEBOOK_MENU_TITLE", language) + ": " + courseId;
    }

    @Override
    protected String getActionDescription(int key) {
        return switch (key) {
            case 1 -> AuthLocalization.getMessage("VIEW_STUDENTS", language);
            case 2 -> AuthLocalization.getMessage("ADD_MARK", language);
            case 3 -> AuthLocalization.getMessage("VIEW_STUDENT_MARKS", language);
            default -> AuthLocalization.getMessage("UNKNOWN_OPTION", language);
        };
    }

    private void viewStudents() {
        List<Student> students = getStudentsForCourse(courseId);

        if (students.isEmpty()) {
            System.out.println(AuthLocalization.getMessage("NO_STUDENTS", language));
            return;
        }

        System.out.println("\n" + AuthLocalization.getMessage("STUDENTS_FOR_COURSE", language) + " " + courseId + ":");
        for (Student student : students) {
            System.out.println("ID: " + student.getId() + ", " + AuthLocalization.getMessage("NAME", language) + ": " + student.getName());
        }
    }

    private void addMarkToStudent() {
        Scanner scanner = new Scanner(System.in);

        System.out.print(AuthLocalization.getMessage("ENTER_STUDENT_ID", language) + ": ");
        String studentId = scanner.nextLine();

        System.out.println(AuthLocalization.getMessage("SELECT_MARK_TYPE", language));
        System.out.println("1. " + AuthLocalization.getMessage("FIRST_ATTESTATION", language));
        System.out.println("2. " + AuthLocalization.getMessage("SECOND_ATTESTATION", language));
        System.out.println("3. " + AuthLocalization.getMessage("FINAL", language));
        int markTypeChoice = scanner.nextInt();

        String markType;
        switch (markTypeChoice) {
            case 1 -> markType = Mark.FIRST_ATTESTATION;
            case 2 -> markType = Mark.SECOND_ATTESTATION;
            case 3 -> markType = Mark.FINAL;
            default -> {
                System.out.println(AuthLocalization.getMessage("INVALID_CHOICE", language));
                return;
            }
        }

        System.out.print(AuthLocalization.getMessage("ENTER_SCORE", language) + " (Max: " + Mark.getMaxScore(markType) + "): ");
        double score = scanner.nextDouble();

        if (score < 0 || score > Mark.getMaxScore(markType)) {
            System.out.println(AuthLocalization.getMessage("INVALID_SCORE", language) + " 0 - " + Mark.getMaxScore(markType) + ".");
            return;
        }

        addMark(studentId, courseId, markType, score);
    }

    private void viewStudentMarks() {
        List<Student> students = getStudentsForCourse(courseId);

        if (students.isEmpty()) {
            System.out.println(AuthLocalization.getMessage("NO_STUDENTS", language));
            return;
        }

        System.out.println("\n" + AuthLocalization.getMessage("MARKS_FOR_COURSE", language) + " " + courseId + ":");

        for (Student student : students) {
            System.out.println(AuthLocalization.getMessage("STUDENT_ID", language) + ": " + student.getId() +
                    ", " + AuthLocalization.getMessage("NAME", language) + ": " + student.getName());

            List<Mark> courseMarks = student.getMarks().stream()
                    .filter(mark -> mark.getCourseId().equals(courseId))
                    .toList();

            if (courseMarks.isEmpty()) {
                System.out.println("  " + AuthLocalization.getMessage("NO_MARKS_FOUND", language));
            } else {
                courseMarks.forEach(mark -> System.out.println("  " + mark));
            }
        }
    }

    public List<Student> getStudentsForCourse(String courseId) {
        List<String> studentIds = db.getStudentsForCourse(courseId);
        return studentIds.stream()
                .map(db::getUser)
                .filter(user -> user instanceof Student)
                .map(user -> (Student) user)
                .toList();
    }

    public void addMark(String studentId, String courseId, String markType, double score) {
        Mark mark = new Mark(studentId, courseId, markType, score);
        Student student = (Student) db.getUser(studentId);

        if (student == null) {
            System.out.println(AuthLocalization.getMessage("STUDENT_NOT_FOUND", language));
            return;
        }

        student.addMark(mark);
        db.addMark(mark);

        System.out.println(AuthLocalization.getMessage("MARK_ADDED_SUCCESS", language) +
                " " + AuthLocalization.getMessage("STUDENT_ID", language) + ": " + studentId +
                ", " + AuthLocalization.getMessage("COURSE_ID", language) + ": " + courseId);
    }
}
