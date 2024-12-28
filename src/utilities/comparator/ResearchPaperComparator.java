package utilities.comparator;

import research.ResearchPaper;

import java.util.Comparator;

public class ResearchPaperComparator {
    public static Comparator<ResearchPaper> sortByCitations() {
        return Comparator.comparingInt(ResearchPaper::getCitations).reversed();
    }

    public static Comparator<ResearchPaper> sortByLength() {
        return Comparator.comparingInt(paper -> paper.getTitle().length());
    }

}
