package utilities.comparator;

import utilities.Complaint;

import java.util.Comparator;

public class UrgencyComparator implements Comparator<Complaint> {
    @Override
    public int compare(Complaint c1, Complaint c2) {
        if (c1.getUrgency() == null && c2.getUrgency() == null) return 0;
        if (c1.getUrgency() == null) return 1;
        if (c2.getUrgency() == null) return -1;
        return c2.getUrgency().ordinal() - c1.getUrgency().ordinal();
    }
}

