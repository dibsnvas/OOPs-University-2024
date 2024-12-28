package utilities;

import java.io.Serializable;

public class Mark implements Serializable {
    public static final String FIRST_ATTESTATION = "1st Attestation";
    public static final String SECOND_ATTESTATION = "2nd Attestation";
    public static final String FINAL = "Final";

    private final String studentId;
    private final String courseId;
    private final String markType;
    private final double score;

    public Mark(String studentId, String courseId, String markType, double score) {
        if (!markType.equals(FIRST_ATTESTATION) && !markType.equals(SECOND_ATTESTATION) && !markType.equals(FINAL)) {
            throw new IllegalArgumentException("Invalid mark type.");
        }
        if (score < 0 || score > getMaxScore(markType)) {
            throw new IllegalArgumentException("Invalid score for " + markType + ": " + score);
        }
        this.studentId = studentId;
        this.courseId = courseId;
        this.markType = markType;
        this.score = score;
    }

    public String getStudentId() {
        return studentId;
    }

    public String getCourseId() {
        return courseId;
    }

    public String getMarkType() {
        return markType;
    }

    public double getScore() {
        return score;
    }

    public static double getMaxScore(String markType) {
        return switch (markType) {
            case FIRST_ATTESTATION, SECOND_ATTESTATION -> 30.0;
            case FINAL -> 40.0;
            default -> throw new IllegalArgumentException("Invalid mark type.");
        };
    }

    @Override
    public String toString() {
        return "Course ID: " + courseId + ", Type: " + markType + ", Score: " + score;
    }
}
