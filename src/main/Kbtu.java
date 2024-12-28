package main;

import database.DataInitializer;
import database.Database;
import localization.auth.AuthLocalization;
import main.menu.ManagerMenu.ManagerMenu;
import main.menu.admin.AdminMenu;
import main.menu.studentMenu.StudentMenu;
import main.menu.teacherMenu.TeacherMenu;
import users.User;
import users.employee.Admin;
import users.employee.Manager;
import users.employee.Teacher;
import users.student.Student;

import java.util.Scanner;

public class Kbtu {
    public static void main(String[] args) {
        // Initialize database
        Database db = Database.getInstance();
        DataInitializer.initialize(db);

        Scanner scanner = new Scanner(System.in);

        // Select language
        String language = selectLanguage(scanner);
        User currentUser;

        while (true) {
            currentUser = authorize(scanner, db, language);
            if (currentUser != null) {
                navigateToRoleMenu(currentUser, language);
            }
        }
    }

    private static String selectLanguage(Scanner scanner) {
        System.out.println("Select your language / Выберите язык / Тілді таңдаңыз:");
        System.out.println("1. English");
        System.out.println("2. Русский");
        System.out.println("3. Қазақша");

        while (true) {
            String input = scanner.nextLine();
            int choice = parseIntegerInput(input);

            switch (choice) {
                case 1:
                    return "EN";
                case 2:
                    return "RU";
                case 3:
                    return "KZ";
                default:
                    System.out.println("Invalid choice. Please select according to their number.");
            }
        }
    }

    private static User authorize(Scanner scanner, Database db, String language) {
        System.out.println(AuthLocalization.getMessage("LOGIN_PROMPT", language));
        System.out.print(AuthLocalization.getMessage("ENTER_EMAIL", language) + " ");
        String email = scanner.nextLine();

        System.out.print(AuthLocalization.getMessage("ENTER_PASSWORD", language) + " ");
        String password = scanner.nextLine();

        User user = db.getUserByEmail(email);

        if (user == null || !user.checkPassword(password)) {
            System.out.println(AuthLocalization.getMessage("INVALID_CREDENTIALS", language));
            return null;
        }

        System.out.println(AuthLocalization.getMessage("WELCOME_USER", language) + " " + user.getName());

        db.displayNews();

        return user;
    }

    private static void navigateToRoleMenu(User currentUser, String language) {

        if (currentUser instanceof Admin admin) {
            new AdminMenu(admin, language).execute();
        } else if (currentUser instanceof Teacher teacher) {
            new TeacherMenu(teacher, language).execute();
        } else if (currentUser instanceof Manager) {
            Manager manager = (Manager) currentUser;
            new ManagerMenu(manager, language).execute();
        } else if (currentUser instanceof Student student) {
            new StudentMenu(student, language).execute();
        } else {
            System.out.println("Unknown role. Exiting.");
        }
    }

    private static int parseIntegerInput(String input) {
        try {
            return Integer.parseInt(input);
        } catch (NumberFormatException e) {
            return -1;
        }
    }
}
