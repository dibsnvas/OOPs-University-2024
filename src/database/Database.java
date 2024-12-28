package database;

import research.ResearchPaper;
import users.Researcher;
import users.User;
import users.employee.Teacher;
import users.student.Student;
import utilities.*;
import utilities.comparator.UrgencyComparator;

import java.io.*;
import java.util.*;

public class Database implements Serializable {
    private static final long serialVersionUID = 1L;

    //singleton
    private static Database instance;

    private Map<String, User> users;
    private Map<String, Course> courses;
    private Map<String, List<String>> courseApplications;
    private Map<String, List<String>> courseStudents;
    private Map<String, List<String>> courseTeachers;
    private Map<String, List<ResearchPaper>> researchPapersByUser;
    private List<News> news;
    private Map<String, List<Mark>> marksByStudent;
    private Map<String, List<Message>> receivedMessagesByUser;
    private Map<String, List<Message>> sentMessagesByUser;
    private final Map<String, List<Complaint>> complaints;
    private List<StudentOrganization> studentOrganizations;
    private final Map<String, List<ResearchPaper>> allResearchPapers;

    private final UrgencyComparator urgencyComparator = new UrgencyComparator();


    private Database() {
        users = new HashMap<>();
        courses = new HashMap<>();
        courseApplications = new HashMap<>();
        courseStudents = new HashMap<>();
        courseTeachers = new HashMap<>();
        researchPapersByUser = new HashMap<>();
        news = new ArrayList<>();
        marksByStudent = new HashMap<>();
        receivedMessagesByUser = new HashMap<>();
        sentMessagesByUser = new HashMap<>();
        complaints = new HashMap<>();
        allResearchPapers = new HashMap<>();

    }

    public static Database getInstance() {
        if (instance == null) {
            instance = loadDatabase();
            if (instance == null) {
                instance = new Database();
            }
        }
        return instance;
    }



    // serialization
    public static void saveDatabase() {
        try (ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream("database.ser"))) {
            out.writeObject(instance);
            System.out.println("Database successfully saved.");
        } catch (IOException e) {
            System.err.println("Error saving database: " + e.getMessage());
        }
    }

    private static Database loadDatabase() {
        try (ObjectInputStream in = new ObjectInputStream(new FileInputStream("database.ser"))) {
            System.out.println("Database successfully loaded.");
            return (Database) in.readObject();
        } catch (IOException | ClassNotFoundException e) {
            System.err.println("Error loading database: " + e.getMessage());
            return null;
        }
    }



    //user manipulations
    public void addUser(User user) {
        users.put(user.getId(), user);
    }

    public void removeUser(String userId) {
        if (users.containsKey(userId)) {
            users.remove(userId);
            System.out.println("User " + userId + " successfully removed.");
        } else {
            System.out.println("User " + userId + " does not exist.");
        }
    }

    public User getUser(String userId) {
        return users.get(userId);
    }

    public User getUserByEmail(String email) {
        for (User user : users.values()) {
            if (user.getEmail().equalsIgnoreCase(email)) {
                return user;
            }
        }
        return null;
    }

    public List<User> getAllUsers() {
        return new ArrayList<>(users.values());
    }




    // course manipulations
    public void addCourse(Course course) {
        if (courses.containsKey(course.getCourseId())) {
            System.err.println("Course with ID " + course.getCourseId() + " already exists.");
            return;
        }
        courses.put(course.getCourseId(), course);
    }

    public Course getCourse(String courseId) {
        return courses.get(courseId);
    }

    public List<Course> getAllCourses() {
        return new ArrayList<>(courses.values());
    }

    public void addCourseApplication(String courseId, String studentId) {
        courseApplications.computeIfAbsent(courseId, k -> new ArrayList<>()).add(studentId);
        System.out.println("Application submitted for course: " + courseId + " by student: " + studentId);
    }

    public List<String> getApplicationsForCourse(String courseId) {
        return courseApplications.getOrDefault(courseId, Collections.emptyList());
    }

    public void approveApplication(String courseId, String studentId) {
        List<String> applications = courseApplications.get(courseId);
        if (applications != null && applications.remove(studentId)) {
            System.out.println("Application approved for student: " + studentId + " in course: " + courseId);
        } else {
            System.out.println("No application found for student: " + studentId + " in course: " + courseId);
        }
    }

    public void removeCourse(String courseId) {
        if (courses.remove(courseId) != null) {
            System.out.println("Course " + courseId + " successfully removed.");
        } else {
            System.err.println("Course " + courseId + " does not exist.");
        }
    }



    //users interaction with courses
    public void enrollStudent(String courseId, String studentId) {
        List<String> students = courseStudents.get(courseId);
        if (students != null && !students.contains(studentId)) {
            students.add(studentId);
        }
    }

    public void assignTeacher(String courseId, String teacherId) {
        List<String> teachers = courseTeachers.get(courseId);
        if (teachers != null && !teachers.contains(teacherId)) {
            teachers.add(teacherId);
        }
    }

    public void registerStudentToCourse(String courseId, String studentId) {
        courseStudents.computeIfAbsent(courseId, k -> new ArrayList<>()).add(studentId);
        System.out.println("Student " + studentId + " registered to course " + courseId);
    }

    public List<String> getStudentsForCourse(String courseId) {
        return courseStudents.getOrDefault(courseId, Collections.emptyList());
    }

    public List<String> getTeachersForCourse(String courseId) {
        return courseTeachers.getOrDefault(courseId, Collections.emptyList());
    }

    public List<Course> getCoursesForStudent(String studentId) {
        List<Course> result = new ArrayList<>();
        for (Map.Entry<String, List<String>> entry : courseStudents.entrySet()) {
            if (entry.getValue().contains(studentId)) {
                result.add(courses.get(entry.getKey()));
            }
        }
        return result;
    }

    public List<Course> getCoursesForTeacher(String teacherId) {
        List<Course> result = new ArrayList<>();
        for (Map.Entry<String, List<String>> entry : courseTeachers.entrySet()) {
            if (entry.getValue().contains(teacherId)) {
                result.add(courses.get(entry.getKey()));
            }
        }
        return result;
    }

    public void addCourseToUser(String courseId, User user) {
        // if course exists
        if (!courses.containsKey(courseId)) {
            System.out.println("Course with ID " + courseId + " does not exist.");
            return;
        }

        if (user instanceof Student student) {
            courseStudents.computeIfAbsent(courseId, k -> new ArrayList<>()).add(student.getId());
            System.out.println("Student " + student.getName() + " successfully enrolled in course " + courseId + ".");
        }
        else if (user instanceof Teacher teacher) {
            courseTeachers.computeIfAbsent(courseId, k -> new ArrayList<>()).add(teacher.getId());
            System.out.println("Teacher " + teacher.getName() + " successfully assigned to course " + courseId + ".");
        } else {
            System.out.println("Only students or teachers can be assigned to courses.");
        }
    }



    // paper manipulations
    public void addResearchPaperForUser(String userId, ResearchPaper paper) {
        researchPapersByUser
                .computeIfAbsent(userId, k -> new ArrayList<>())
                .add(paper);
    }

    public List<ResearchPaper> getResearchPapersForUser(String userId) {
        return researchPapersByUser.getOrDefault(userId, Collections.emptyList());
    }

    public void removeResearchPaperForUser(String userId, String paperId) {
        List<ResearchPaper> userPapers = researchPapersByUser.get(userId);
        if (userPapers != null) {
            boolean removed = userPapers.removeIf(paper -> paper.getId().equals(paperId));
            if (!removed) {
                System.out.println("Research paper with ID " + paperId + " not found for user " + userId);
            }
            if (userPapers.isEmpty()) {
                researchPapersByUser.remove(userId); // delete if list is empty
            }
        } else {
            System.out.println("No research papers found for user " + userId);
        }
    }



    // researchPaper requirements
    public List<ResearchPaper> getAllResearchPapersSorted(Comparator<ResearchPaper> comparator) {
        List<ResearchPaper> papers = new ArrayList<>();
        for(User user : users.values()) {
            if(user instanceof Researcher researcher) {
                papers.addAll(researcher.getResearchPapers());
            }
        }
        papers.sort(comparator);
        return papers;
    }

    public Researcher getTopCitedResearcher() {
        Researcher topResearcher = null;
        int maxCitations = 0;

        for(User user : users.values()) {
            if(user instanceof Researcher researcher) {
                int totalCitations = researcher
                        .getResearchPapers()
                        .stream()
                        .mapToInt(ResearchPaper::getCitations)
                        .sum();
                if(totalCitations > maxCitations) {
                    maxCitations = totalCitations;
                    topResearcher = researcher;
                }
            }
        }
        return topResearcher;
    }



    //news manipulations
    public List<News> getNews() {
        return news;
    }

    public List<News> getSortedNews() {
        List<News> sortedNews = new ArrayList<>(news);
        sortedNews.sort(News::compareTo); // compare to from news
        return sortedNews;
    }

    public void displayNews() {
        List<News> sortedNews = getSortedNews();
        System.out.println("\n--- Latest News ---");
        if (sortedNews.isEmpty()) {
            System.out.println("No news available.");
        } else {
            sortedNews.forEach(System.out::println);
        }
    }

    public void addNews(News newNews) {
        news.add(newNews);
    }

    public void removeNews(News targetNews) {
        news.remove(targetNews);
    }



    // teacher needs and marks
    public void addMark(Mark mark) {
        marksByStudent.computeIfAbsent(mark.getStudentId(), k -> new ArrayList<>()).add(mark);
        System.out.println("Mark added successfully for Student ID: " + mark.getStudentId() + " in Course ID: " + mark.getCourseId());
    }

    public List<Mark> getMarksForStudent(String studentId) {
        return marksByStudent.getOrDefault(studentId, Collections.emptyList());
    }

    public List<Mark> getMarksForStudentInCourse(String studentId, String courseId) {
        return getMarksForStudent(studentId).stream()
                .filter(mark -> mark.getCourseId().equals(courseId))
                .toList();
    }

    // Message manipulations
    // Retrieve messages for a specific recipient
    public List<Message> getMessagesForRecipient(User recipient) {
        return receivedMessagesByUser.getOrDefault(recipient.getId(), new ArrayList<>());
    }

    // Retrieve messages from a specific sender
    public List<Message> getMessagesFromSender(User sender) {
        return sentMessagesByUser.getOrDefault(sender.getId(), new ArrayList<>());
    }

    // Update the read status of a message
    public void updateMessageStatus(Message message) {
        List<Message> receivedMessages = receivedMessagesByUser.get(message.getRecipient().getId());
        if (receivedMessages != null) {
            for (Message msg : receivedMessages) {
                if (msg.equals(message)) {
                    msg.setRead(message.isRead());
                    return;
                }
            }
        }
        throw new IllegalArgumentException("Message not found in received messages for recipient: " + message.getRecipient().getId());
    }

    // Add a new message (helper method for sending messages)
    public void addMessage(Message message) {
        sentMessagesByUser.computeIfAbsent(message.getSender().getId(), k -> new ArrayList<>()).add(message);
        receivedMessagesByUser.computeIfAbsent(message.getRecipient().getId(), k -> new ArrayList<>()).add(message);
    }

    public void addComplaint(String userId, Complaint complaint) {
        complaints.computeIfAbsent(userId, k -> new ArrayList<>()).add(complaint);
        complaints.get(userId).sort(urgencyComparator);
    }

    // Retrieve complaints for a specific user by their ID
    public List<Complaint> getComplaintsForUser(String userId) {
        return complaints.getOrDefault(userId, Collections.emptyList());
    }

    // Remove a specific complaint for a user
    public void removeComplaint(String userId, Complaint complaint) {
        List<Complaint> userComplaints = complaints.get(userId);
        if (userComplaints != null && userComplaints.remove(complaint)) {
            System.out.println("Complaint removed for user ID: " + userId);
            if (userComplaints.isEmpty()) {
                complaints.remove(userId); // Remove the key if the list becomes empty
            }
        } else {
            System.out.println("Complaint not found for user ID: " + userId);
        }
    }

    // Remove all complaints for a user
    public void removeAllComplaintsForUser(String userId) {
        if (complaints.remove(userId) != null) {
            System.out.println("All complaints removed for user ID: " + userId);
        } else {
            System.out.println("No complaints found for user ID: " + userId);
        }
    }

    public List<StudentOrganization> getStudentOrganizations() {
        return studentOrganizations;
    }

    public void collectAllResearchPapers() {
        List<ResearchPaper> allPapers = new ArrayList<>();
        for (User user : users.values()) {
            if (user instanceof Researcher researcher) {
                allPapers.addAll(researcher.getResearchPapers());
            }
        }
        allResearchPapers.put("all", allPapers);
    }





    public void addStudentOrganization(StudentOrganization organization) {
        studentOrganizations.add(organization);
        System.out.println("Organization " + organization.getOrgName() + " added successfully.");
    }

    public StudentOrganization findOrganizationByName(String orgName) {
        return studentOrganizations.stream()
                .filter(org -> org.getOrgName().equalsIgnoreCase(orgName))
                .findFirst()
                .orElse(null);
    }

    public void removeStudentOrganization(String orgName) {
        studentOrganizations.removeIf(org -> org.getOrgName().equalsIgnoreCase(orgName));
        System.out.println("Organization " + orgName + " removed successfully.");
    }

    public List<Complaint> getAllComplaints() {
        return complaints.values().stream()
                .flatMap(List::stream)
                .sorted(urgencyComparator)
                .toList();
    }

}
