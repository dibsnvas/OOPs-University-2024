package users;

import java.io.Serializable;
import java.util.Objects;

public abstract class User implements Serializable {
    private final String id;
    private final String name;
    private final String email;
    private final String hashedPassword;

    // Default constructor for subclasses
    protected User() {
        this.id = null;
        this.name = null;
        this.email = null;
        this.hashedPassword = null;
    }

    // Parameterized constructor
    public User(String id, String name, String email, String password) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.hashedPassword = hashPassword(password);
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getEmail() {
        return email;
    }

    public boolean checkPassword(String password) {
        return this.hashedPassword != null && this.hashedPassword.equals(hashPassword(password));
    }

    private String hashPassword(String password) {
        // Simple SHA-256 hash
        try {
            java.security.MessageDigest digest = java.security.MessageDigest.getInstance("SHA-256");
            byte[] hash = digest.digest(password.getBytes(java.nio.charset.StandardCharsets.UTF_8));
            StringBuilder hexString = new StringBuilder();
            for (byte b : hash) {
                String hex = Integer.toHexString(0xff & b);
                if (hex.length() == 1) hexString.append('0');
                hexString.append(hex);
            }
            return hexString.toString();
        } catch (Exception e) {
            throw new RuntimeException("Error hashing password: " + e.getMessage());
        }
    }

    public void updateNews() {
        System.out.println("User " + this.getName() + " got notified about news.");
    }

    @Override
    public String toString() {
        return "User{" +
                "id='" + id + '\'' +
                ", name='" + name + '\'' +
                ", email='" + email + '\'' +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        User user = (User) o;
        return Objects.equals(id, user.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(getId());
    }
}
