package users.employee;

import research.ResearchPaper;
import users.Researcher;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;

public class IndependentResearcher extends Employee implements Researcher {
    private final List<ResearchPaper> researchPapers;

    public IndependentResearcher() {
        super();
        this.researchPapers = new ArrayList<>();
    }

    public IndependentResearcher(String id, String name, String username, String password) {
        super(id, name, username, password);
        this.researchPapers = new ArrayList<>();
    }

    public void addResearchPaper(ResearchPaper paper) {
        if (paper == null) {
            throw new IllegalArgumentException("Research paper cannot be null.");
        }
        researchPapers.add(paper);
        researchPapers.sort((a, b) -> Integer.compare(b.getCitations(), a.getCitations())); // Keep list sorted
        System.out.println("Research paper added: " + paper.getTitle());
    }

    @Override
    public List<ResearchPaper> getResearchPapers() {
        return new ArrayList<>(researchPapers);
    }

    @Override
    public int calculateHIndex() {
        researchPapers.sort((a, b) -> Integer.compare(b.getCitations(), a.getCitations()));
        int hIndex = 0;
        for (int i = 0; i < researchPapers.size(); i++) {
            if (researchPapers.get(i).getCitations() >= i + 1) {
                hIndex = i + 1;
            } else {
                break;
            }
        }
        return hIndex;
    }

    @Override
    public void printPapers(Comparator<ResearchPaper> comparator) {
        if (comparator == null) {
            throw new IllegalArgumentException("Comparator cannot be null.");
        }
        researchPapers.stream()
                .sorted(comparator)
                .forEach(paper -> System.out.println("Title: " + paper.getTitle() +
                        ", Citations: " + paper.getCitations()));
    }

    @Override
    public String toString() {
        return "Researcher{" +
                "id='" + getId() + '\'' +
                ", name='" + getName() + '\'' +
                ", email='" + getEmail() + '\'' +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof IndependentResearcher that)) return false;
        if (!super.equals(o)) return false;
        return Objects.equals(researchPapers, that.researchPapers);
    }

    @Override
    public int hashCode() {
        return super.hashCode();
    }
}
