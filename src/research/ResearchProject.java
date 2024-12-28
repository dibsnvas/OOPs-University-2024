package research;

import users.Researcher;
import users.User;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class ResearchProject {
    private String topic;
    private List<ResearchPaper> publishedPapers;
    private List<Researcher> participants;

    public ResearchProject() {
        this.topic = null;
        this.publishedPapers = new ArrayList<>();
        this.participants = new ArrayList<>();
    }

    public ResearchProject(String topic) {
        this.topic = topic;
        this.publishedPapers = new ArrayList<>();
        this.participants = new ArrayList<>();
    }

    public void addParticipant(User user) throws Exception {
        if (!(user instanceof Researcher)) {
            throw new Exception("Only researchers can join a ResearchProject.");
        }
        participants.add((Researcher) user);
    }

    public void addResearchPaper(ResearchPaper paper) {
        publishedPapers.add(paper);
    }

    public List<Researcher> getParticipants() {
        return participants;
    }

    public List<ResearchPaper> getPublishedPapers() {
        return publishedPapers;
    }

    @Override
    public String toString() {
        return "ResearchProject{" +
                "topic='" + topic + '\'' +
                ", publishedPapers=" + publishedPapers +
                ", participants=" + participants +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof ResearchProject that)) return false;
        return Objects.equals(topic, that.topic) && Objects.equals(getPublishedPapers(), that.getPublishedPapers())
                && Objects.equals(getParticipants(), that.getParticipants());
    }

    @Override
    public int hashCode() {
        return Objects.hash(topic, getPublishedPapers(), getParticipants());
    }
}
