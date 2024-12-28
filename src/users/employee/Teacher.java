package users.employee;

import database.Database;
import research.ResearchPaper;
import users.Researcher;
import users.student.Student;
import utilities.Complaint;
import utilities.enums.UrgencyLevel;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class Teacher extends Employee implements Researcher {
    private final boolean isProfessor;
    private boolean isResearcher;
    private final List<ResearchPaper> researchPapers;
    private static final Database db = Database.getInstance();

    public Teacher() {
        super();
        this.isProfessor = false;
        this.isResearcher = false;
        this.researchPapers = null;
    }

    public Teacher(String id, String name, String email, String password) {
        super(id, name, email, password);
        this.isProfessor = false;
        this.isResearcher = false;
        this.researchPapers = null;
    }

    public Teacher(String id, String name, String email, String password, boolean isProfessor, boolean isResearcher) {
        super(id, name, email, password);
        this.isProfessor = isProfessor;
        this.isResearcher = isResearcher;
        this.researchPapers = isResearcher ? new ArrayList<>() : null;
    }

    /**
     * Method for a Teacher to send a complaint to a Manager.
     *
     * @param managerId   The ID of the Manager to whom the complaint is addressed.
     * @param student     The Student being complained about.
     * @param description The description of the complaint.
     * @param urgency     The urgency level of the complaint.
     */
    public void sendComplaint(String managerId, Student student, String description, UrgencyLevel urgency) {
        Complaint complaint = new Complaint(this, student, description, urgency);
        db.addComplaint(managerId, complaint);
        System.out.println("Complaint successfully sent to Manager with ID: " + managerId);
    }

    public boolean isProfessor() {
        return isProfessor;
    }

    public boolean isResearcher() {
        return isResearcher;
    }

    public void applyForResearcher() {
        this.isResearcher = true;
        System.out.println("You are now a researcher!");
    }

    @Override
    public void addResearchPaper(ResearchPaper paper) {
        if (!isResearcher) throw new IllegalStateException("Teacher is not a researcher.");
        researchPapers.add(paper);
    }

    @Override
    public List<ResearchPaper> getResearchPapers() {
        if (!isResearcher) throw new IllegalStateException("Teacher is not a researcher.");
        return researchPapers;
    }

    @Override
    public int calculateHIndex() {
        if (!isResearcher) throw new IllegalStateException("Teacher is not a researcher.");
        researchPapers.sort((a, b) -> Integer.compare(b.getCitations(), a.getCitations()));

        int hIndex = 0;
        for (int i = 0; i < researchPapers.size(); i++) {
            if (researchPapers.get(i).getCitations() >= i + 1) {
                hIndex = i + 1;
            } else break;
        }
        return hIndex;
    }

    @Override
    public void printPapers(java.util.Comparator<ResearchPaper> comparator) {
        if (!isResearcher) throw new IllegalStateException("Teacher is not a researcher.");
        researchPapers.stream().sorted(comparator).forEach(System.out::println);
    }

    @Override
    public String toString() {
        return "Teacher{" +
                "id='" + getId() + '\'' +
                ", name='" + getName() + '\'' +
                ", email='" + getEmail() + '\'' +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Teacher teacher)) return false;
        if (!super.equals(o)) return false;
        return isProfessor == teacher.isProfessor &&
                isResearcher == teacher.isResearcher &&
                Objects.equals(researchPapers, teacher.researchPapers);
    }

    @Override
    public int hashCode() {
        return super.hashCode();
    }

}
