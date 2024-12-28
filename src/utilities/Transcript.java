package utilities;

import users.student.Student;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class Transcript {
    private final Student student;

    public Transcript(Student student) {
        this.student = student;
    }

    public double calculateGPA() {
        List<Mark> marks = student.getMarks();
        double totalScore = 0;
        double totalMaxScore = 0;

        for (Mark mark : marks) {
            totalScore += mark.getScore();
            totalMaxScore += Mark.getMaxScore(mark.getMarkType());
        }

        return totalMaxScore > 0 ? (totalScore / totalMaxScore) * 4.0 : 0.0;
    }

    private boolean isRetakeRequired(List<Mark> marks) {
        double firstAttestation = marks.stream()
                .filter(mark -> Mark.FIRST_ATTESTATION.equals(mark.getMarkType()))
                .mapToDouble(Mark::getScore)
                .sum();

        double secondAttestation = marks.stream()
                .filter(mark -> Mark.SECOND_ATTESTATION.equals(mark.getMarkType()))
                .mapToDouble(Mark::getScore)
                .sum();

        return (firstAttestation + secondAttestation) < 30.0;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder("Transcript for " + student.getName() + ":\n");

        Map<String, List<Mark>> marksByCourse = student.getMarks().stream()
                .collect(Collectors.groupingBy(Mark::getCourseId));

        for (Map.Entry<String, List<Mark>> entry : marksByCourse.entrySet()) {
            String courseId = entry.getKey();
            List<Mark> marks = entry.getValue();

            sb.append("Course: ").append(courseId).append("\n");

            for (Mark mark : marks) {
                sb.append("  ").append(mark).append("\n");
            }

            if (isRetakeRequired(marks)) {
                student.addRetake(courseId); // Increment retake count
                sb.append("  Status: RETAKE\n");
            }
        }

        sb.append("GPA: ").append(String.format("%.2f", calculateGPA()));
        return sb.toString();
    }
}
