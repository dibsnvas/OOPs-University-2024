package main.menu;

import localization.auth.AuthLocalization;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Scanner;

public abstract class AbstractMenu {
    protected final Map<Integer, Runnable> actions = new LinkedHashMap<>();
    private String language;

    protected AbstractMenu(String language) {
        this.language = language;
    }

    protected abstract void initializeActions();

    protected abstract String getMenuTitle();

    protected abstract String getActionDescription(int key);

    public void setLanguage(String language) {
        this.language = language;
    }

    public void execute() {
        Scanner scanner = new Scanner(System.in);

        while (true) {
            try {
                System.out.println("\n" + getMenuTitle());
                actions.forEach((key, value) -> System.out.println(key + ". " + getActionDescription(key)));
                System.out.println("0. " + AuthLocalization.getMessage("BACK", language));

                System.out.print(AuthLocalization.getMessage("SELECT_OPTION", language) + " ");
                String input = scanner.nextLine();
                int choice = parseIntegerInput(input);

                if (choice == 0) {
                    return; // Back to the previous menu
                }

                Runnable action = actions.get(choice);
                if (action != null) {
                    action.run();
                } else {
                    System.out.println(AuthLocalization.getMessage("INVALID_OPTION", language));
                }
            } catch (Exception e) {
                System.out.println(AuthLocalization.getMessage("ERROR_OCCURRED", language) + ": " + e.getMessage());
            }
        }
    }

    private int parseIntegerInput(String input) {
        try {
            return Integer.parseInt(input);
        } catch (NumberFormatException e) {
            System.out.println(AuthLocalization.getMessage("INVALID_INPUT", language));
            return -1; // Return -1 for invalid input
        }
    }
}
