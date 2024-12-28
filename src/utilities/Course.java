package utilities;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class Course implements Serializable {
    private final String courseId;
    private final String courseName;
    private String description;
    private List<Lesson> lessons;

    // Constructor without description
    public Course() {
        this.courseId = null;
        this.courseName = null;
        this.description = null;
        this.lessons = new ArrayList<>();
    }

    public Course(String courseId, String courseName) {
        this.courseId = courseId;
        this.courseName = courseName;
        this.description = "";
        this.lessons = new ArrayList<>();
    }

    // Constructor with description
    public Course(String courseId, String courseName, String description) {
        this.courseId = courseId;
        this.courseName = courseName;
        this.description = description;
        this.lessons = new ArrayList<>();
    }

    public String getCourseId() {
        return courseId;
    }

    public String getCourseName() {
        return courseName;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public List<Lesson> getLessons() {
        return lessons;
    }

    public void addLesson(Lesson lesson) {
        lessons.add(lesson); // Safely add lessons since lessons is initialized
    }

    @Override
    public String toString() {
        return "Course{" +
                "courseId='" + courseId + '\'' +
                ", courseName='" + courseName + '\'' +
                ", description='" + description + '\'' +
                ", lessons=" + lessons +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Course course)) return false;
        return Objects.equals(getCourseId(), course.getCourseId())
                && Objects.equals(getCourseName(), course.getCourseName())
                && Objects.equals(getDescription(), course.getDescription())
                && Objects.equals(getLessons(), course.getLessons());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getCourseId(), getCourseName(), getDescription(), getLessons());
    }
}
