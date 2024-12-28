package users;

import research.ResearchPaper;

import java.util.List;

public interface Researcher {
    void addResearchPaper(ResearchPaper paper);
    List<ResearchPaper> getResearchPapers();
    int calculateHIndex();
    void printPapers(java.util.Comparator<ResearchPaper> comparator);
}
