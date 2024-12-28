package users.student;

import database.Database;
import research.ResearchPaper;
import users.Researcher;
import users.employee.Manager;

import java.util.List;
import java.util.Objects;

public class MasterStudent extends Student implements Researcher {

    private final String thesisTopic;

    public MasterStudent() {
        super();
        this.thesisTopic = null;
    }

    public MasterStudent(String id, String name, String email, String password) {
        super(id, name, email, password, null, true);
        this.thesisTopic = null;
    }

    public MasterStudent(String id, String name, String email, String password, String major, boolean isResearcher, String thesisTopic) {
        super(id, name, email, password, major, true);
        this.thesisTopic = thesisTopic;
    }

    public String getThesisTopic() {
        return thesisTopic;
    }

    public void submitThesis(Database db) {
        System.out.println("Submitting thesis: " + thesisTopic);
        // here should be implemented logic to contain the thesis in database
    }

    @Override
    public void addResearchPaper(ResearchPaper paper) {
        super.addResearchPaper(paper);
        System.out.println("Master student added a new research paper: " + paper.getTitle());
    }

    @Override
    public List<ResearchPaper> getResearchPapers() {
        System.out.println("Fetching research papers for Master student...");
        return super.getResearchPapers();
    }

    @Override
    public String toString() {
        return "Master Student{" +
                "id='" + getId() + '\'' +
                ", name='" + getName() + '\'' +
                ", email='" + getEmail() + '\'' +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof MasterStudent that)) return false;
        if (!super.equals(o)) return false;
        return Objects.equals(getThesisTopic(), that.getThesisTopic());
    }

    @Override
    public int hashCode() {
        return super.hashCode();
    }
}
