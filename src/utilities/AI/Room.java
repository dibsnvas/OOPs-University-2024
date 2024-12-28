package utilities.AI;

public class Room {
    private final String roomNumber;
    private final String location;
    private final String department;
    private final String description;

    public Room(String roomNumber, String location, String department, String description) {
        this.roomNumber = roomNumber;
        this.location = location;
        this.department = department;
        this.description = description;
    }

    public String getRoomNumber() {
        return roomNumber;
    }

    public String getLocation() {
        return location;
    }

    public String getDepartment() {
        return department;
    }

    public String getDescription() {
        return description;
    }

    @Override
    public String toString() {
        return "Room " + roomNumber + " (" + department + "): " + description + " at " + location;
    }
}
