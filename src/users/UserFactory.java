package users;

import users.employee.Admin;
import users.employee.IndependentResearcher;
import users.employee.Manager;
import users.employee.Teacher;
import users.student.MasterStudent;
import users.student.PhDStudent;
import users.student.Student;

//TODO: Желательно детям юзеров не добавлять филды, если добавлять, то поставить дефолт значение в конструкторе.

public class UserFactory {
    public static User createUser(String type, String id, String name, String email, String password) {
        return switch (type.toLowerCase()) {
            case "admin" -> new Admin(id, name, email, password);
            case "manager" -> new Manager(id, name, email, password);
            case "researcher" -> new IndependentResearcher(id, name, email, password);
            case "teacher" -> new Teacher(id, name, email, password);
            case "master" -> new MasterStudent(id, name, email, password);
            case "phd" -> new PhDStudent(id, name, email, password);
            case "bachelor" -> new Student(id, name, email, password);
            default -> throw new IllegalArgumentException("Unknown user type: " + type);
        };
    }
}

