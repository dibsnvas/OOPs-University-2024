package main.menu.studentMenu;

import database.Database;
import localization.auth.AuthLocalization;
import main.menu.AbstractMenu;
import main.menu.ResearcherMenu.ResearcherMenu;
import users.student.Student;
import utilities.Course;
import utilities.Lesson;
import utilities.AI.AIService;
import utilities.enums.Organizations;

import java.util.List;
import java.util.Scanner;

public class StudentMenu extends AbstractMenu {
    private final Student student;
    private final String language;

    public StudentMenu(Student student, String language) {
        super(language);
        this.student = student;
        this.language = language;
        initializeActions();
    }

    @Override
    protected void initializeActions() {
        actions.put(1, this::viewMarks);
        actions.put(2, this::registerForCourse);
        actions.put(3, this::viewResearchPapers);
        actions.put(4, this::viewSchedule);
        actions.put(5, this::viewAvailableLessons);
        actions.put(6, this::addLessonToSchedule);
        actions.put(7, this::viewTranscript);
        if (student.isResearcher()) {
            actions.put(8, this::openResearcherMenu);
        }
        actions.put(9, this::talkToGuideon);
        actions.put(10, this::manageOrganizations);
    }

    @Override
    protected String getMenuTitle() {
        return AuthLocalization.getMessage("STUDENT_MENU_TITLE", language);
    }

    @Override
    protected String getActionDescription(int key) {
        return switch (key) {
            case 1 -> AuthLocalization.getMessage("VIEW_MARKS", language);
            case 2 -> AuthLocalization.getMessage("REGISTER_FOR_COURSE", language);
            case 3 -> AuthLocalization.getMessage("VIEW_RESEARCH_PAPERS", language);
            case 4 -> AuthLocalization.getMessage("VIEW_SCHEDULE", language);
            case 5 -> AuthLocalization.getMessage("VIEW_AVAILABLE_LESSONS", language);
            case 6 -> AuthLocalization.getMessage("ADD_LESSON_TO_SCHEDULE", language);
            case 7 -> AuthLocalization.getMessage("VIEW_TRANSCRIPT", language);
            case 8 -> AuthLocalization.getMessage("OPEN_RESEARCHER_MENU", language);
            case 9 -> AuthLocalization.getMessage("TALK_TO_GUIDEON_BOT", language);
            case 10 -> AuthLocalization.getMessage("MANAGE_ORGANIZATIONS", language);
            default -> AuthLocalization.getMessage("UNKNOWN_OPTION", language);
        };
    }

    private void viewMarks() {
        System.out.println("\n" + AuthLocalization.getMessage("YOUR_MARKS", language));
        if (student.getMarks().isEmpty()) {
            System.out.println(AuthLocalization.getMessage("NO_MARKS", language));
            return;
        }

        student.getMarks().forEach(mark ->
                System.out.println(mark.getCourseId() + " (" + mark.getMarkType() + "): " + mark.getScore()));
    }

    private void registerForCourse() {
        Scanner scanner = new Scanner(System.in);
        System.out.println("\n" + AuthLocalization.getMessage("AVAILABLE_COURSES", language));

        Database db = Database.getInstance();
        List<Course> courses = db.getAllCourses();

        for (int i = 0; i < courses.size(); i++) {
            System.out.println((i + 1) + ". " + courses.get(i).getCourseName());
        }

        System.out.print(AuthLocalization.getMessage("SELECT_COURSE", language) + " ");
        int courseIndex = scanner.nextInt() - 1;

        if (courseIndex >= 0 && courseIndex < courses.size()) {
            Course selectedCourse = courses.get(courseIndex);
            student.registerForCourse(selectedCourse);
            System.out.println(AuthLocalization.getMessage("COURSE_REGISTER_SUCCESS", language)
                    + selectedCourse.getCourseName());
        } else {
            System.out.println(AuthLocalization.getMessage("INVALID_SELECTION", language));
        }
    }

    private void viewResearchPapers() {
        System.out.println("\n" + AuthLocalization.getMessage("YOUR_RESEARCH_PAPERS", language));
        student.getResearchPapers().forEach(System.out::println);
    }

    private void viewSchedule() {
        System.out.println("\n" + AuthLocalization.getMessage("YOUR_SCHEDULE", language));
        student.viewSchedule();
    }

    private void viewAvailableLessons() {
        System.out.println("\n" + AuthLocalization.getMessage("AVAILABLE_LESSONS", language));
        List<Course> registeredCourses = student.getRegisteredCourses();

        if (registeredCourses.isEmpty()) {
            System.out.println(AuthLocalization.getMessage("NO_REGISTERED_COURSES", language));
            return;
        }

        for (Course course : registeredCourses) {
            System.out.println(AuthLocalization.getMessage("COURSE_PREFIX", language) + course.getCourseName());
            for (Lesson lesson : course.getLessons()) {
                System.out.println("  - " + lesson);
            }
        }
    }

    private void addLessonToSchedule() {
        System.out.println("\n" + AuthLocalization.getMessage("AVAILABLE_LESSONS", language));
        List<Course> registeredCourses = student.getRegisteredCourses();

        if (registeredCourses.isEmpty()) {
            System.out.println(AuthLocalization.getMessage("NO_REGISTERED_COURSES", language));
            return;
        }

        for (int i = 0; i < registeredCourses.size(); i++) {
            System.out.println((i + 1) + ". " + registeredCourses.get(i).getCourseName());
        }

        Scanner scanner = new Scanner(System.in);
        System.out.print(AuthLocalization.getMessage("SELECT_COURSE_TO_VIEW", language) + " ");
        int courseIndex = scanner.nextInt() - 1;

        if (courseIndex >= 0 && courseIndex < registeredCourses.size()) {
            Course selectedCourse = registeredCourses.get(courseIndex);
            List<Lesson> lessons = selectedCourse.getLessons();
            for (int i = 0; i < lessons.size(); i++) {
                System.out.println((i + 1) + ". " + lessons.get(i));
            }

            System.out.print(AuthLocalization.getMessage("SELECT_LESSON_TO_ADD", language) + " ");
            int lessonIndex = scanner.nextInt() - 1;

            if (lessonIndex >= 0 && lessonIndex < lessons.size()) {
                Lesson selectedLesson = lessons.get(lessonIndex);
                student.addLessonToSchedule(selectedLesson.getDay(), selectedLesson.toString());
                System.out.println(AuthLocalization.getMessage("LESSON_ADD_SUCCESS", language));
            } else {
                System.out.println(AuthLocalization.getMessage("INVALID_SELECTION", language));
            }
        } else {
            System.out.println(AuthLocalization.getMessage("INVALID_SELECTION", language));
        }
    }

    private void talkToGuideon() {
        Scanner scanner = new Scanner(System.in);
        System.out.println("\n" + AuthLocalization.getMessage("GUIDEON_BOT_TITLE", language));
        System.out.println(AuthLocalization.getMessage("GUIDEON_PROMPT", language));

        while (true) {
            System.out.print(AuthLocalization.getMessage("YOU", language) + " ");
            String userInput = scanner.nextLine();
            if (userInput.equalsIgnoreCase("exit")) {
                System.out.println(AuthLocalization.getMessage("GUIDEON_EXIT", language));
                break;
            }

            String response = AIService.chatGPT(userInput);
            System.out.println(AuthLocalization.getMessage("GUIDEON", language) + ": " + response);
        }
    }

    private void openResearcherMenu() {
        System.out.println("\n" + AuthLocalization.getMessage("RESEARCHER_MENU", language));
        ResearcherMenu researcherMenu = new ResearcherMenu(student.getId(), language);
        researcherMenu.execute();
    }

    private void viewTranscript() {
        System.out.println("\n" + AuthLocalization.getMessage("TRANSCRIPT", language));
        System.out.println(student.getTranscript());
    }

    private void manageOrganizations() {
        Scanner scanner = new Scanner(System.in);
        System.out.println("\n" + AuthLocalization.getMessage("MANAGE_ORGANIZATIONS_TITLE", language));
        System.out.println("1. " + AuthLocalization.getMessage("JOIN_ORGANIZATION", language));
        System.out.println("2. " + AuthLocalization.getMessage("LEAVE_ORGANIZATION", language));
        System.out.println("3. " + AuthLocalization.getMessage("VIEW_JOINED_ORGANIZATIONS", language));
        System.out.println("0. " + AuthLocalization.getMessage("BACK_TO_MENU", language));

        System.out.print(AuthLocalization.getMessage("SELECT_OPTION", language) + " ");
        int choice = scanner.nextInt();
        scanner.nextLine(); // Consume newline

        switch (choice) {
            case 1 -> joinOrganization(scanner);
            case 2 -> leaveOrganization(scanner);
            case 3 -> viewJoinedOrganizations();
            case 0 -> System.out.println(AuthLocalization.getMessage("RETURNING_TO_MENU", language));
            default -> System.out.println(AuthLocalization.getMessage("INVALID_SELECTION", language));
        }
    }

    private void joinOrganization(Scanner scanner) {
        System.out.println("\n" + AuthLocalization.getMessage("AVAILABLE_ORGANIZATIONS", language));
        for (Organizations org : Organizations.values()) {
            System.out.println("- " + org);
        }
        System.out.print(AuthLocalization.getMessage("ENTER_ORGANIZATION_NAME", language) + " ");
        String orgName = scanner.nextLine().toUpperCase();

        try {
            Organizations organization = Organizations.valueOf(orgName);
            student.joinOrganization(organization);
            System.out.println(AuthLocalization.getMessage("ORGANIZATION_JOIN_SUCCESS", language));
        } catch (IllegalArgumentException e) {
            System.out.println(AuthLocalization.getMessage("INVALID_ORGANIZATION_NAME", language));
        }
    }

    private void leaveOrganization(Scanner scanner) {
        System.out.println("\n" + AuthLocalization.getMessage("JOINED_ORGANIZATIONS", language));
        student.getJoinedOrganizations().forEach(org -> System.out.println("- " + org));
        System.out.print(AuthLocalization.getMessage("ENTER_ORGANIZATION_NAME", language) + " ");
        String orgName = scanner.nextLine().toUpperCase();

        try {
            Organizations organization = Organizations.valueOf(orgName);
            student.leaveOrganization(organization);
            System.out.println(AuthLocalization.getMessage("ORGANIZATION_LEAVE_SUCCESS", language));
        } catch (IllegalArgumentException e) {
            System.out.println(AuthLocalization.getMessage("INVALID_ORGANIZATION_NAME", language));
        }
    }

    private void viewJoinedOrganizations() {
        System.out.println("\n" + AuthLocalization.getMessage("JOINED_ORGANIZATIONS", language));
        if (student.getJoinedOrganizations().isEmpty()) {
            System.out.println(AuthLocalization.getMessage("NO_JOINED_ORGANIZATIONS", language));
        } else {
            student.getJoinedOrganizations().forEach(org -> System.out.println("- " + org));
        }
    }
}
