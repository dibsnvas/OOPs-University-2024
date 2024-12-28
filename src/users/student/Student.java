package users.student;

import research.ResearchPaper;
import users.Researcher;
import users.User;
import utilities.Course;
import utilities.Mark;
import utilities.Schedule;
import utilities.Transcript;
import utilities.enums.Organizations;

import java.time.DayOfWeek;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class Student extends User implements Researcher {
    private String major;
    private boolean isResearcher;
    private final List<ResearchPaper> researchPapers;
    private Schedule schedule;
    private final List<Course> registeredCourses;
    private final List<Mark> marks;
    private final Set<Organizations> joinedOrganizations;
    private final Map<String, Integer> retakeCount;

    public Student() {
        super();
        this.major = null;
        this.isResearcher = false;
        this.researchPapers = null;
        this.schedule = new Schedule();
        this.registeredCourses = new ArrayList<>();
        this.marks = new ArrayList<>();
        this.joinedOrganizations = new HashSet<>();
        this.retakeCount = new HashMap<>();
    }


    public Student(String id, String name, String email, String password) {
        super(id, name, email, password);
        this.major = major;
        this.isResearcher = false;
        this.researchPapers = null;
        this.schedule = new Schedule();
        this.registeredCourses = new ArrayList<>();
        this.marks = new ArrayList<>();
        this.joinedOrganizations = new HashSet<>();
        this.retakeCount = new HashMap<>();
    }

    public Student(String id, String name, String email, String password, String major, boolean isResearcher) {
        super(id, name, email, password);
        this.major = major;
        this.isResearcher = isResearcher;
        this.researchPapers = isResearcher ? new ArrayList<>() : null;
        this.schedule = new Schedule();
        this.registeredCourses = new ArrayList<>();
        this.marks = new ArrayList<>();
        this.joinedOrganizations = new HashSet<>();
        this.retakeCount = new HashMap<>();
    }

    public boolean isResearcher() {
        return isResearcher;
    }

    public void applyForResearcher() {
        this.isResearcher = true;
        System.out.println("You are now a researcher!");
    }

    public Schedule getSchedule() {
        return schedule;
    }

    public void addLessonToSchedule(DayOfWeek day, String lesson) {
        this.schedule.addLesson(day, lesson);
    }

    public void viewSchedule() {
        System.out.println("Schedule for " + this.getName() + ":");
        System.out.println(this.schedule);
    }

    public void registerForCourse(Course course) {
        if (!registeredCourses.contains(course)) {
            registeredCourses.add(course);
        }
    }

    public List<Course> getRegisteredCourses() {
        return registeredCourses;
    }
    
    public void addMark(Mark mark) {
        marks.add(mark);
    }

    public List<Mark> getMarks() {
        return marks;
    }
    public Transcript getTranscript() {
        return new Transcript(this); 
    }
    
    public Set<Organizations> getJoinedOrganizations() {
        return joinedOrganizations;
    }

    public void joinOrganization(Organizations organization) {
        if (joinedOrganizations.contains(organization)) {
            System.out.println("You are already a member of " + organization.name());
        } else {
            joinedOrganizations.add(organization);
            System.out.println("Successfully joined " + organization.name());
        }
    }

    public void leaveOrganization(Organizations organization) {
        if (joinedOrganizations.contains(organization)) {
            joinedOrganizations.remove(organization);
            System.out.println("You have left " + organization.name());
        } else {
            System.out.println("You are not a member of " + organization.name());
        }
    }

    public void viewOrganizations() {
        if (joinedOrganizations.isEmpty()) {
            System.out.println("You are not a member of any organizations.");
        } else {
            System.out.println("You are a member of the following organizations:");
            for (Organizations org : joinedOrganizations) {
                System.out.println("- " + org.name());
            }
        }
    }
    public void addRetake(String courseId) {
        retakeCount.put(courseId, retakeCount.getOrDefault(courseId, 0) + 1);

        if (retakeCount.get(courseId) > 3) {
            dismissStudent();
        }
    }

    public int getRetakeCount(String courseId) {
        return retakeCount.getOrDefault(courseId, 0);
    }

    private void dismissStudent() {
        System.out.println("Student " + getName() + " has been dismissed for exceeding retakes.");
        // Add any additional dismissal logic here (e.g., remove from database)
    }

    
    @Override
    public void addResearchPaper(ResearchPaper paper) {
        if (!isResearcher) throw new IllegalStateException("Student is not a researcher.");
        researchPapers.add(paper);
    }

    @Override
    public List<ResearchPaper> getResearchPapers() {
        if (!isResearcher) throw new IllegalStateException("Student is not a researcher.");
        return researchPapers;
    }

    @Override
    public int calculateHIndex() {
        if (!isResearcher) throw new IllegalStateException("Student is not a researcher.");
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
        if (!isResearcher) throw new IllegalStateException("Student is not a researcher.");
        researchPapers.stream().sorted(comparator).forEach(System.out::println);
    }

    public String getMajor() {
        return major;
    }
}
