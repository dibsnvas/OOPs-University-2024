package database;

import research.ResearchPaper;
import users.employee.Admin;
import users.employee.Manager;
import users.employee.enums.ManagerType;
import users.student.Student;
import users.employee.Teacher;
import utilities.Course;
import utilities.News;

import java.time.LocalDate;

public class DataInitializer {
    public static void initialize(Database db) {
        // initial users
        db.addUser(new Student("student1", "magzhan", "magzhan@gmail.com", "password123", "Computer Science", true));
        db.addUser(new Student("student2", "aktanberdi", "aktan@gmail.com", "password456", "Mathematics", false));
        db.addUser(new Student("student3", "tilek", "tiklerx@gmail.com", "password789", "911", false));
        db.addUser(new Teacher("teacher1", "Pakita", "pakita@gmail.com", "professorpass", true, true));
        db.addUser(new Teacher("teacher2", "amanov", "aman@gmail.com", "teacherpass", false, false));
        db.addUser(new Admin("admin1", "admin", "admin@gmail.com", "admin"));
        db.addUser(new Manager("manager1", "madyga", "madyga@gmail.com", "madyga", ManagerType.DEPARTMENT));
        db.addUser(new Manager("manager2", "Madyga", "mseitowa@gmail.com", "managerpass", ManagerType.OR));

        // initial courses
        db.addCourse(new Course("C1", "OOP"));
        db.addCourse(new Course("C2", "Data Structures and Algorithms"));

        // enroll course
        db.addCourseToUser("C1", db.getUser("student1"));
        db.addCourseToUser("C2", db.getUser("student1"));
        db.addCourseToUser("C1", db.getUser("teacher1"));
        db.addCourseToUser("C2", db.getUser("teacher2"));

        // initial papers
        db.addResearchPaperForUser("student1", new ResearchPaper("R1", "Deep Learning", 10));

        db.addResearchPaperForUser("teacher1", new ResearchPaper("R1", "Deep Learning", 10));
        db.addResearchPaperForUser("teacher1", new ResearchPaper("R2", "AI", 15));

        // initial news
        db.addNews(new News("System Update", "The system will be down for maintenance tomorrow.", LocalDate.now(), 1));
        db.addNews(new News("Welcome", "Welcome to the university portal!", LocalDate.now().minusDays(1), 1));
        db.addNews(new News("Research Achievements", "Our researchers published groundbreaking papers!", LocalDate.now().minusDays(2), 2));
    }
}
