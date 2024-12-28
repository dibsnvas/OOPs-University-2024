package users.employee;

import database.Database;
import users.User;
import utilities.Message;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public abstract class Employee extends User {
    private final List<Message> receivedMessages;
    private final List<Message> sentMessages;
    private final Database db;

    public Employee() {
        this(null, null, null, null);
    }

    public Employee(String id, String name, String username, String password) {
        super(id, name, username, password);
        this.db = Database.getInstance();
        this.receivedMessages = new ArrayList<>(db.getMessagesForRecipient(this));
        this.sentMessages = new ArrayList<>(db.getMessagesFromSender(this));
    }

    public List<Message> getReceivedMessages() {
        return new ArrayList<>(receivedMessages); // Return a copy to prevent external modifications
    }

    public List<Message> getSentMessages() {
        return new ArrayList<>(sentMessages); // Return a copy to prevent external modifications
    }

    public void sendMessage(Employee recipient, String content) {
        if (recipient == null || content == null || content.trim().isEmpty()) {
            throw new IllegalArgumentException("Recipient and content must not be null or empty.");
        }

        Message message = new Message();
        message.setSender(this);
        message.setRecipient(recipient);
        message.setContent(content);
        message.setTimeStamp(java.time.LocalDateTime.now());
        message.setRead(false);

        this.getSentMessages().add(message);
        recipient.getReceivedMessages().add(message);
    }

    public void readReceivedMessages() {
        System.out.println("Received Messages:");
        for (Message message : receivedMessages) {
            System.out.println("From: " + message.getSender().getName());
            System.out.println("Content: " + message.getContent());
            System.out.println("Sent time: " + message.getTimeStamp());
            System.out.println("Status: " + (message.isRead() ? "Read" : "Unread"));
            System.out.println("-----");
            message.markAsRead();
            db.updateMessageStatus(message); // Update read status in the database
        }
    }

    public void readSentMessages() {
        System.out.println("Sent Messages:");
        for (Message message : sentMessages) {
            System.out.println("To: " + message.getRecipient().getName());
            System.out.println("Content: " + message.getContent());
            System.out.println("Sent time: " + message.getTimeStamp());
            System.out.println("Status: " + (message.isRead() ? "Read" : "Unread"));
            System.out.println("-----");
        }
    }

    @Override
    public String toString() {
        return "Employee{" +
                "receivedMessages=" + receivedMessages.size() +
                ", sentMessages=" + sentMessages.size() +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Employee employee = (Employee) o;
        return Objects.equals(getId(), employee.getId());
    }

    @Override
    public int hashCode() {
        return super.hashCode();
    }

}
