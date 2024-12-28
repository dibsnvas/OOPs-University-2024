package utilities;

import users.User;

import java.time.LocalDateTime;
import java.util.Objects;

public class Message {
    private User sender;
    private User recipient;
    private String content;
    private LocalDateTime timeStamp;
    private boolean isRead;

    public Message() {
        this.sender = null;
        this.recipient = null;
        this.content = "";
        this.timeStamp = LocalDateTime.now();
        this.isRead = false;
    }

    public Message(User sender, User recipient, String content) {
        this.sender = sender;
        this.recipient = recipient;
        this.content = content;
        this.timeStamp = LocalDateTime.now();
        this.isRead = false;
    }

    public User getSender() {
        return sender;
    }

    public void setSender(User sender) {
        this.sender = sender;
    }

    public User getRecipient() {
        return recipient;
    }

    public void setRecipient(User recipient) {
        this.recipient = recipient;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public LocalDateTime getTimeStamp() {
        return timeStamp;
    }

    public void setTimeStamp(LocalDateTime timeStamp) {
        this.timeStamp = timeStamp;
    }

    public boolean isRead() {
        return isRead;
    }

    public void setRead(boolean isRead) {
        this.isRead = isRead;
    }

    public void markAsRead() {
        this.isRead = true;
    }

    @Override
    public String toString() {
        return "Message{" +
                "sender=" + sender.getName() +
                ", recipient=" + recipient.getName() +
                ", content='" + content + '\'' +
                ", timeStamp=" + timeStamp +
                ", isRead=" + isRead +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Message message)) return false;
        return isRead() == message.isRead() && Objects.equals(getSender(), message.getSender())
                && Objects.equals(getRecipient(), message.getRecipient())
                && Objects.equals(getContent(), message.getContent())
                && Objects.equals(getTimeStamp(), message.getTimeStamp());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getSender(), getRecipient(), getContent(), getTimeStamp(), isRead());
    }
}
