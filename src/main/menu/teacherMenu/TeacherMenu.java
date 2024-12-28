package main.menu.teacherMenu;

import database.Database;
import localization.auth.AuthLocalization;
import main.menu.AbstractMenu;
import main.menu.ResearcherMenu.ResearcherMenu;
import users.employee.Teacher;
import users.student.Student;
import utilities.Course;
import utilities.enums.UrgencyLevel;

import java.util.Map;
import java.util.Scanner;

public class TeacherMenu extends AbstractMenu {
    private final Database db = Database.getInstance();
    private final Teacher teacher;
    private final String language;

    public TeacherMenu(Teacher teacher, String language) {
        super(language);
        this.teacher = teacher;
        this.language = language;
        initializeActions();
    }

    @Override
    protected void initializeActions() {
        actions.put(1, this::showTeacherStatus);
        actions.put(2, this::openGradeBook);

        if (teacher.isResearcher()) {
            actions.put(3, this::openResearcherMenu);
        }
        actions.put(4, this::sendComplaint);
    }

    @Override
    protected String getMenuTitle() {
        return AuthLocalization.getMessage("TEACHER_MENU_TITLE", language);
    }

    private static final Map<Integer, String> actionDescriptions = Map.of(
            1, "View Status",
            2, "Open Grade Book",
            3, "Open Researcher Menu",
            4, "Send a Complaint"
    );

    @Override
    protected String getActionDescription(int key) {
        return switch (key) {
            case 1 -> AuthLocalization.getMessage("VIEW_STATUS", language);
            case 2 -> AuthLocalization.getMessage("OPEN_GRADE_BOOK", language);
            case 3 -> AuthLocalization.getMessage("OPEN_RESEARCHER_MENU", language);
            case 4 -> AuthLocalization.getMessage("SEND_COMPLAINT", language);
            default -> AuthLocalization.getMessage("UNKNOWN_OPTION", language);
        };
    }

    private void showTeacherStatus() {
        System.out.println("\n" + AuthLocalization.getMessage("TEACHER_STATUS", language));
        System.out.println(AuthLocalization.getMessage("NAME", language) + ": " + teacher.getName());
        System.out.println(AuthLocalization.getMessage("PROFESSOR", language) + ": " + (teacher.isProfessor() ? AuthLocalization.getMessage("YES", language) : AuthLocalization.getMessage("NO", language)));
        System.out.println(AuthLocalization.getMessage("RESEARCHER", language) + ": " + (teacher.isResearcher() ? AuthLocalization.getMessage("YES", language) : AuthLocalization.getMessage("NO", language)));
    }

    private void openGradeBook() {
        Scanner scanner = new Scanner(System.in);
        System.out.print(AuthLocalization.getMessage("ENTER_COURSE_ID", language) + ": ");
        String courseId = scanner.nextLine().trim();
        Course course = db.getCourse(courseId);

        if (course == null) {
            System.out.println(AuthLocalization.getMessage("INVALID_COURSE_ID", language));
            return;
        }

        GradeBookMenu gradeBookMenu = new GradeBookMenu(courseId, language);
        gradeBookMenu.execute();
    }

    private void openResearcherMenu() {
        ResearcherMenu researcherMenu = new ResearcherMenu(teacher.getId(), language);
        researcherMenu.execute();
    }

    private void sendComplaint() {
        Scanner scanner = new Scanner(System.in);

        System.out.print(AuthLocalization.getMessage("ENTER_ACCUSED_ID", language) + ": ");
        String accusedId = scanner.nextLine().trim();
        var accused = db.getUser(accusedId);

        if (!(accused instanceof Student)) {
            System.out.println(AuthLocalization.getMessage("ACCUSED_NOT_FOUND", language));
            return;
        }

        System.out.print(AuthLocalization.getMessage("ENTER_COMPLAINT_DETAILS", language) + ": ");
        String details = scanner.nextLine().trim();

        System.out.println(AuthLocalization.getMessage("SELECT_URGENCY_LEVEL", language));
        System.out.println("1. " + AuthLocalization.getMessage("LOW", language));
        System.out.println("2. " + AuthLocalization.getMessage("MEDIUM", language));
        System.out.println("3. " + AuthLocalization.getMessage("HIGH", language));
        int urgencyChoice = scanner.nextInt();
        UrgencyLevel urgencyLevel;

        switch (urgencyChoice) {
            case 1 -> urgencyLevel = UrgencyLevel.LOW;
            case 2 -> urgencyLevel = UrgencyLevel.MEDIUM;
            case 3 -> urgencyLevel = UrgencyLevel.HIGH;
            default -> {
                System.out.println(AuthLocalization.getMessage("INVALID_CHOICE", language));
                urgencyLevel = UrgencyLevel.MEDIUM;
            }
        }

        scanner.nextLine(); // Clear buffer
        System.out.print(AuthLocalization.getMessage("ENTER_MANAGER_ID", language) + ": ");
        String managerId = scanner.nextLine().trim();

        teacher.sendComplaint(managerId, (Student) accused, details, urgencyLevel);
        System.out.println(AuthLocalization.getMessage("COMPLAINT_SENT", language));
    }
}
