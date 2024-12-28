package utilities.AI;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class RoomService {
    private final List<Room> rooms;

    public RoomService(String filePath) {
        this.rooms = new ArrayList<>();
        loadRooms(filePath);
    }

    private void loadRooms(String filePath) {
        try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
            String line;
            boolean isFirstLine = true; // Skip the header
            while ((line = br.readLine()) != null) {
                if (isFirstLine) {
                    isFirstLine = false;
                    continue;
                }
                String[] parts = line.split(",");
                if (parts.length == 4) {
                    rooms.add(new Room(parts[0].trim(), parts[1].trim(), parts[2].trim(), parts[3].trim()));
                }
            }
        } catch (IOException e) {
            System.err.println("Error loading rooms: " + e.getMessage());
        }
    }

    public Room findRoomByNumber(String roomNumber) {
        return rooms.stream()
                .filter(room -> room.getRoomNumber().equalsIgnoreCase(roomNumber))
                .findFirst()
                .orElse(null);
    }

    public List<Room> findRoomsByDepartment(String department) {
        return rooms.stream()
                .filter(room -> room.getDepartment().equalsIgnoreCase(department))
                .collect(Collectors.toList());
    }
}
