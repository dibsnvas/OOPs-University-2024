package main.menu.admin;

import database.Database;
import localization.auth.AuthLocalization;
import users.User;
import users.employee.Admin;

import java.util.Scanner;

import main.menu.AbstractMenu;

public class AdminMenu extends AbstractMenu {
    private final Admin admin;
    private final Database db = Database.getInstance();
    private final String language;

    public AdminMenu(Admin admin, String language) {
        super(language);
        this.admin = admin;
        this.language = language;
        initializeActions();
    }

    @Override
    protected void initializeActions() {
        actions.put(1, admin::viewAllUsers);
        actions.put(2, this::addUser);
        actions.put(3, this::removeUser);
        actions.put(4, admin::viewLogFiles);
        actions.put(5, this::getUserById);
    }

    @Override
    protected String getMenuTitle() {
        return AuthLocalization.getMessage("ADMIN_MENU_TITLE", language);
    }

    @Override
    protected String getActionDescription(int key) {
        return switch (key) {
            case 1 -> AuthLocalization.getMessage("VIEW_ALL_USERS", language);
            case 2 -> AuthLocalization.getMessage("ADD_NEW_USER", language);
            case 3 -> AuthLocalization.getMessage("REMOVE_USER", language);
            case 4 -> AuthLocalization.getMessage("VIEW_LOG_FILES", language);
            case 5 -> AuthLocalization.getMessage("GET_USER_BY_ID", language);
            default -> AuthLocalization.getMessage("UNKNOWN_OPTION", language);
        };
    }

    private void addUser() {
        Scanner scanner = new Scanner(System.in);

        System.out.print(AuthLocalization.getMessage("ENTER_USER_TYPE", language) + ": ");
        String type = scanner.nextLine();

        System.out.print(AuthLocalization.getMessage("ENTER_USER_ID", language) + ": ");
        String id = scanner.nextLine();

        System.out.print(AuthLocalization.getMessage("ENTER_USER_NAME", language) + ": ");
        String name = scanner.nextLine();

        System.out.print(AuthLocalization.getMessage("ENTER_USER_EMAIL", language) + ": ");
        String email = scanner.nextLine();

        System.out.print(AuthLocalization.getMessage("ENTER_USER_PASSWORD", language) + ": ");
        String password = scanner.nextLine();

        admin.addUser(type, id, name, email, password);
        System.out.println(AuthLocalization.getMessage("USER_ADDED_SUCCESS", language));
    }

    private void removeUser() {
        Scanner scanner = new Scanner(System.in);

        System.out.print(AuthLocalization.getMessage("ENTER_USER_ID_TO_REMOVE", language) + ": ");
        String userId = scanner.nextLine();

        admin.removeUser(userId);
        System.out.println(AuthLocalization.getMessage("USER_REMOVED_SUCCESS", language));
    }

    private void getUserById() {
        Scanner scanner = new Scanner(System.in);

        System.out.print(AuthLocalization.getMessage("ENTER_USER_ID_TO_RETRIEVE", language) + ": ");
        String userId = scanner.nextLine();

        User user = admin.getUser(userId);
        if (user != null) {
            System.out.println(user);
        } else {
            System.out.println(AuthLocalization.getMessage("USER_NOT_FOUND", language) + ": " + userId);
        }
    }
}
