package utilities.AI;

import java.util.List;
import java.util.stream.Collectors;

public class AIService {

    public static String chatGPT(String message) {
    	RoomService roomService = new RoomService("/home/diana/Downloads/oop_final-main (3)/oop_final-main/src/utilities/resources/room.csv");

        if (message.toLowerCase().contains("room")) {
            String roomNumber = extractRoomNumber(message);
            Room room = roomService.findRoomByNumber(roomNumber);
            return (room != null) ? room.toString() : "Room " + roomNumber + " not found.";
        }

        if (message.toLowerCase().contains("department")) {
            String department = extractDepartmentName(message);
            List<Room> rooms = roomService.findRoomsByDepartment(department);
            return rooms.isEmpty()
                    ? "No rooms found for department " + department
                    : rooms.stream().map(Room::toString).collect(Collectors.joining("\n"));
        }

        return "I'm sorry, I couldn't understand your request.";
    }

    private static String extractRoomNumber(String message) {
        return message.replaceAll("\\D", "").trim(); 
    }

    private static String extractDepartmentName(String message) {
        return message.replace("department", "").trim(); 
    }
}
