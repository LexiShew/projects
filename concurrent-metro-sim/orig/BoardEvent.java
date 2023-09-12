import java.util.*;

public class BoardEvent implements Event {
    public final Passenger p;
    public final Train t;
    public final Station s;

    public BoardEvent(Passenger p, Train t, Station s) {
        this.p = p;
        this.t = t;
        this.s = s;
    }

    public boolean equals(Object o) {
        if (o instanceof BoardEvent e) {
            return p.equals(e.p) && t.equals(e.t) && s.equals(e.s);
        }
        return false;
    }

    public int hashCode() {
        return Objects.hash(p, t, s);
    }

    public String toString() {
        return "Passenger " + p + " boards " + t + " at " + s;
    }

    public List<String> toStringList() {
        return List.of(p.toString(), t.toString(), s.toString());
    }

    public void replayAndCheck(MBTA mbta) {
        P.pl("BoardEvent#replayAndCheck");
        // check that passenger boarding station, train's current station, and
        // given station are all equal

        s.print();
        t.print();
        p.print();
        if (p.boardingStation() != t.currentStation() || t.currentStation() != s) {
            P.pl("BoardEvent\n\tp.boardingStation(): " + p.boardingStation().toString());
            P.pl("\tt.currentStation(): " + t.currentStation().toString());
            P.pl("\ts: " + s.toString());
            throw new RuntimeException(
                    "Train's current Station, Passenger's boarding Station, and given Station must all be equal");
        }
        // s.removePassenger(p);
        t.addPassenger(p);
        p.boardTrain(t);

    }
}
