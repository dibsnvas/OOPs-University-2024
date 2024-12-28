package main.menu.ManagerMenu;

import database.Database;
import localization.auth.AuthLocalization;
import main.menu.AbstractMenu;
import users.User;
import utilities.News;

import java.time.LocalDate;
import java.util.List;
import java.util.Scanner;

public class NewsMenu extends AbstractMenu {
    Scanner scanner = new Scanner(System.in);
    private final Database db = Database.getInstance();
    private final String language;

    public NewsMenu(String language) {
        super(language);
        this.language = language;
        initializeActions();
    }

    @Override
    protected void initializeActions() {
        actions.put(1, this::viewNews);
        actions.put(2, this::addNews);
    }

    @Override
    protected String getMenuTitle() {
        return AuthLocalization.getMessage("NEWS_MENU_TITLE", language);
    }

    @Override
    protected String getActionDescription(int key) {
        return switch (key) {
            case 1 -> AuthLocalization.getMessage("VIEW_EXISTING_NEWS", language);
            case 2 -> AuthLocalization.getMessage("ADD_NEWS", language);
            default -> AuthLocalization.getMessage("UNKNOWN_OPTION", language);
        };
    }

    private void viewNews() {
        db.displayNews();
    }

    private void addNews() {
        System.out.println(AuthLocalization.getMessage("INPUT_NEWS_TITLE", language));
        String newsTitle = scanner.nextLine();
        System.out.println(AuthLocalization.getMessage("INPUT_NEWS_CONTENT", language));
        String newsContent = scanner.nextLine();
        LocalDate newsDate = LocalDate.now();
        System.out.println(AuthLocalization.getMessage("INPUT_NEWS_PRIORITY", language));
        int newsPriority = scanner.nextInt();
        scanner.nextLine(); // Clear the buffer

        News newNews = new News(newsTitle, newsContent, newsDate, newsPriority);
        db.addNews(newNews);
        notifyAllUsers();
    }

    private void notifyAllUsers() {
        List<User> users = db.getAllUsers();
        for (User user : users) {
            user.updateNews();
        }
    }
}
