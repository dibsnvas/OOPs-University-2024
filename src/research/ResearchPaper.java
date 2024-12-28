package research;

import java.io.Serializable;
import java.util.Objects;

public class ResearchPaper implements Serializable {
    private String id;
    private String title;
    private int citations;

    public ResearchPaper() {
        this.id = null;
        this.title = null;
        this.citations = 0;
    }


    public ResearchPaper(String id, String title, int citations) {
        this.id = id;
        this.title = title;
        this.citations = citations;
    }

    public String getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public int getCitations() {
        return citations;
    }

    @Override
    public String toString() {
        return "ResearchPaper{" +
                "id='" + id + '\'' +
                ", title='" + title + '\'' +
                ", citations=" + citations +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof ResearchPaper that)) return false;
        return getCitations() == that.getCitations() && Objects.equals(getId(), that.getId())
                && Objects.equals(getTitle(), that.getTitle());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getId(), getTitle(), getCitations());
    }
}
