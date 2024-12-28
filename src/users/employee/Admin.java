package users.employee;

import database.Database;
import users.User;
import users.UserFactory;

import java.io.*;
import java.io.IOException;
import java.util.Objects;
import java.util.logging.FileHandler;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;

public class Admin extends Employee {

    private static final Logger logger = Logger.getLogger(Admin.class.getName());
    private final Database db;

    static {
        try {
            FileHandler fileHandler = new FileHandler("user_actions.log", true);
            fileHandler.setFormatter(new SimpleFormatter());
            logger.addHandler(fileHandler);
            logger.setLevel(Level.INFO);
        } catch (IOException e) {
            System.err.println("Failed to set up file logging: " + e.getMessage());
        }
    }

    public Admin() {
        super();
        this.db = Database.getInstance();
    }

    public Admin(String id, String name, String email, String password) {
        super(id, name, email, password);
        this.db = Database.getInstance();
    }

    /**
     * Add a new user to the system.
     * @param type the type of user (e.g., admin, manager, teacher, etc.)
     * @param id the ID of the new user
     * @param name the name of the new user
     * @param email the email of the new user
     * @param password the password of the new user
     */
    public void addUser(String type, String id, String name, String email, String password) {
        try {
            User newUser = UserFactory.createUser(type, id, name, email, password);
            db.addUser(newUser);
            logger.log(Level.INFO, "User added: {0}", newUser);
        } catch (IllegalArgumentException e) {
            logger.log(Level.WARNING, "Failed to create user: {0}", e.getMessage());
        }
    }

    /**
     * Remove a user from the system by their ID.
     * @param userId the ID of the user to remove
     */
    public void removeUser(String userId) {
        User user = db.getUser(userId);
        if (user != null) {
            db.removeUser(userId);
            logger.log(Level.INFO, "User removed: {0}", user);
        } else {
            logger.log(Level.WARNING, "Attempted to remove a non-existent user: {0}", userId);
        }
    }

    /**
     * Retrieve a user by their ID.
     * @param userId the ID of the user to retrieve
     * @return the User object, or null if not found
     */
    public User getUser(String userId) {
        User user = db.getUser(userId);
        if (user == null) {
            logger.log(Level.WARNING, "No user found with ID: {0}", userId);
        }
        return user;
    }

    /**
     * View all users in the system.
     */
    public void viewAllUsers() {
        logger.log(Level.INFO, "Displaying all users...");
        try {
            Thread.sleep(1000); // 1000 milliseconds = 1 second
        } catch (InterruptedException e) {
            e.fillInStackTrace();
        }
        for (User user : db.getAllUsers()) {
            System.out.println(user);
        }
    }

    /**
     * View system log files.
     */
    public void viewLogFiles() {
        System.out.println("Log files content:");
        try (BufferedReader reader = new BufferedReader(new FileReader("user_actions.log"))) {
            String line;
            while ((line = reader.readLine()) != null) {
                System.out.println(line);
            }
        } catch (IOException e) {
            System.err.println("Error reading log file: " + e.getMessage());
        }
    }

    @Override
    public String toString() {
        return "Admin{" +
                "id='" + getId() + '\'' +
                ", name='" + getName() + '\'' +
                ", email='" + getEmail() + '\'' +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Admin admin = (Admin) o;
        return Objects.equals(getId(), admin.getId());
    }

    @Override
    public int hashCode() {
        return super.hashCode();
    }

}