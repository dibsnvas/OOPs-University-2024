package utilities;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.Objects;

public class News implements Serializable, Comparable<News> {
    private static final long serialVersionUID = 1L;

    private String title;
    private String content;
    private LocalDate date;
    private int priority;

    public News() {
        this.title = null;
        this.content = null;
        this.date = null;
        this.priority = 0;
    }

    public News(String title, String content, LocalDate date, int priority) {
        this.title = title;
        this.content = content;
        this.date = date;
        this.priority = priority;
    }

    public String getTitle() {
        return title;
    }

    public String getContent() {
        return content;
    }

    public LocalDate getDate() {
        return date;
    }

    public int getPriority() {
        return priority;
    }

    @Override
    public String toString() {
        return "[" + date + "] " + title + ": " + content;
    }

    @Override
    public int compareTo(News other) {
        // sort by priority(new ones are first)
        if (this.priority != other.priority) {
            return Integer.compare(other.priority, this.priority);
        }
        return other.date.compareTo(this.date);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof News news)) return false;
        return getPriority() == news.getPriority() && Objects.equals(getTitle(), news.getTitle())
                && Objects.equals(getContent(), news.getContent()) && Objects.equals(getDate(), news.getDate());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getTitle(), getContent(), getDate(), getPriority());
    }
}
