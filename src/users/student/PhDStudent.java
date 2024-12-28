package users.student;

import research.ResearchPaper;
import users.Researcher;

import java.util.List;
import java.util.Objects;

public class PhDStudent extends Student implements Researcher {
    private final String dissertationTopic;
    private final String advisorName;

    public PhDStudent() {
        super();
        this.dissertationTopic = null;
        this.advisorName = null;
    }

    public PhDStudent(String id, String name, String email, String password) {
        super(id, name, email, password, null, true);
        this.dissertationTopic = null;
        this.advisorName = null;
    }

    public PhDStudent(String id, String name, String email, String password, String major, boolean isResearcher, String dissertationTopic, String advisorName) {
        super(id, name, email, password, major, true);
        this.dissertationTopic = dissertationTopic;
        this.advisorName = advisorName;
    }

    public String getDissertationTopic() {
        return dissertationTopic;
    }

    public String getAdvisorName() {
        return advisorName;
    }

    public void defendDissertation() {
        System.out.println("Defending dissertation: " + dissertationTopic + " under advisor: " + advisorName);
    }

    @Override
    public void addResearchPaper(ResearchPaper paper) {
        super.addResearchPaper(paper);
        System.out.println("PhD student added a new research paper: " + paper.getTitle());
    }

    @Override
    public List<ResearchPaper> getResearchPapers() {
        System.out.println("Fetching research papers for PhD student...");
        return super.getResearchPapers();
    }

    @Override
    public int calculateHIndex() {
        System.out.println("Calculating H-Index for PhD student...");
        return super.calculateHIndex();
    }

    @Override
    public String toString() {
        return "PhD Student{" +
                "id='" + getId() + '\'' +
                ", name='" + getName() + '\'' +
                ", email='" + getEmail() + '\'' +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof PhDStudent that)) return false;
        if (!super.equals(o)) return false;
        return Objects.equals(getDissertationTopic(), that.getDissertationTopic())
                && Objects.equals(getAdvisorName(), that.getAdvisorName());
    }

    @Override
    public int hashCode() {
        return super.hashCode();
    }
}
