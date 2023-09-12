import java.util.*;

public class DeboardEvent implements Event {
    public final Passenger p;
    public final Train t;
    public final Station s;

    public DeboardEvent(Passenger p, Train t, Station s) {
        this.p = p;
        this.t = t;
        this.s = s;
    }

    public boolean equals(Object o) {
        if (o instanceof DeboardEvent e) {
            return p.equals(e.p) && t.equals(e.t) && s.equals(e.s);
        }
        return false;
    }

    public int hashCode() {
        return Objects.hash(p, t, s);
    }

    public String toString() {
        return "Passenger " + p + " deboards " + t + " at " + s;
    }

    public List<String> toStringList() {
        return List.of(p.toString(), t.toString(), s.toString());
    }

    public void replayAndCheck(MBTA mbta) {
        P.pl("DeboardEvent#replayAndCheck");

        // check that passenger deboarding station, train's current station, and
        //  given station are all equal
        if (p.deboardingStation() != t.currentStation() || t.currentStation() != s) {
            throw new RuntimeException(
                    "Train's current Station, Passenger's deboarding Station, and given Station must all be equal");
        }
        t.removePassenger(p);
        // s.addPassenger(p);
        p.deboard();
        
        s.print();
        t.print();
        p.print();
    }
}
