package main.menu.ResearcherMenu;

import research.ResearchPaper;

import java.util.List;
import java.util.Scanner;
import database.Database;

import main.menu.AbstractMenu;
import localization.auth.AuthLocalization;

public class ResearcherMenu extends AbstractMenu {
    private final String userID;
    private final List<ResearchPaper> researchPapers;
    private final Database db = Database.getInstance();
    private final String language;

    public ResearcherMenu(String userID, String language) {
        super(language);
        this.userID = userID;
        this.language = language;
        this.researchPapers = db.getResearchPapersForUser(userID);
        initializeActions();
    }

    @Override
    protected void initializeActions() {
        actions.put(1, this::viewResearchPapers);
        actions.put(2, this::addResearchPaper);
        actions.put(3, this::viewSpecificResearchPaper);
    }

    @Override
    protected String getMenuTitle() {
        return AuthLocalization.getMessage("RESEARCHER_MENU_TITLE", language);
    }

    @Override
    protected String getActionDescription(int key) {
        return switch (key) {
            case 1 -> AuthLocalization.getMessage("VIEW_ALL_RESEARCH", language);
            case 2 -> AuthLocalization.getMessage("ADD_RESEARCH", language);
            case 3 -> AuthLocalization.getMessage("VIEW_SPECIFIC_RESEARCH", language);
            default -> AuthLocalization.getMessage("UNKNOWN_OPTION", language);
        };
    }

    private void viewResearchPapers() {
        System.out.println("\n" + AuthLocalization.getMessage("ALL_RESEARCH", language));
        if (researchPapers.isEmpty()) {
            System.out.println(AuthLocalization.getMessage("NO_RESEARCH_AVAILABLE", language));
            return;
        }
        researchPapers.forEach(paper ->
                System.out.println(AuthLocalization.getMessage("RESEARCH_DETAILS", language)
                        .replace("{ID}", paper.getId())
                        .replace("{TITLE}", paper.getTitle())
                        .replace("{CITATIONS}", String.valueOf(paper.getCitations()))
                )
        );
    }

    private void addResearchPaper() {
        Scanner scanner = new Scanner(System.in);

        System.out.print(AuthLocalization.getMessage("ENTER_RESEARCH_ID", language) + " ");
        String id = scanner.nextLine();

        System.out.print(AuthLocalization.getMessage("ENTER_RESEARCH_TITLE", language) + " ");
        String title = scanner.nextLine();

        System.out.print(AuthLocalization.getMessage("ENTER_RESEARCH_CITATIONS", language) + " ");
        int citations = Integer.parseInt(scanner.nextLine());

        ResearchPaper paper = new ResearchPaper(id, title, citations);
        researchPapers.add(paper);

        System.out.println(AuthLocalization.getMessage("RESEARCH_ADDED_SUCCESS", language));
    }

    private void viewSpecificResearchPaper() {
        Scanner scanner = new Scanner(System.in);

        System.out.print(AuthLocalization.getMessage("ENTER_RESEARCH_ID_TO_VIEW", language) + " ");
        String id = scanner.nextLine();

        ResearchPaper paper = researchPapers.stream()
                .filter(p -> p.getId().equals(id))
                .findFirst()
                .orElse(null);

        if (paper == null) {
            System.out.println(AuthLocalization.getMessage("RESEARCH_NOT_FOUND", language));
        } else {
            System.out.println("\n" + AuthLocalization.getMessage("RESEARCH_DETAILS_HEADER", language));
            System.out.println(AuthLocalization.getMessage("RESEARCH_DETAILS", language)
                    .replace("{ID}", paper.getId())
                    .replace("{TITLE}", paper.getTitle())
                    .replace("{CITATIONS}", String.valueOf(paper.getCitations()))
            );
        }
    }
}
