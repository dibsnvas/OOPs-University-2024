package utilities;

import users.User;
import utilities.enums.UrgencyLevel;

import java.util.Objects;

public class Complaint {
    private final User complainant;
    private final User accused;
    private final String details;
    private final UrgencyLevel urgency;

    // Empty constructor for frameworks or serialization
    public Complaint() {
        this.complainant = null;
        this.accused = null;
        this.details = null;
        this.urgency = null;
    }

    // Constructor with parameters
    public Complaint(User complainant, User accused, String details, UrgencyLevel urgency) {
        this.complainant = complainant;
        this.accused = accused;
        this.details = details;
        this.urgency = urgency;
    }

    // Getters
    public User getComplainant() {
        return complainant;
    }

    public User getAccused() {
        return accused;
    }

    public String getDetails() {
        return details;
    }

    public UrgencyLevel getUrgency() {
        return urgency;
    }

    // Override toString
    @Override
    public String toString() {
        return "Complaint{" +
                "complainant=" + (complainant != null ? complainant.getName() : "N/A") +
                ", accused=" + (accused != null ? accused.getName() : "N/A") +
                ", details='" + details + '\'' +
                ", urgency=" + urgency +
                '}';
    }

    // Override equals
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Complaint complaint = (Complaint) o;
        return Objects.equals(complainant, complaint.complainant) &&
                Objects.equals(accused, complaint.accused) &&
                Objects.equals(details, complaint.details) &&
                urgency == complaint.urgency;
    }

    // Override hashCode
    @Override
    public int hashCode() {
        return Objects.hash(complainant, accused, details, urgency);
    }
}
