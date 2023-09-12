import java.util.*;

/*******************************************************************************
 * 
 * Representation of a passenger.
 * 
 * @author skeleton by CS121, completed by Lexi Shewchuk
 * 
 ******************************************************************************/

public class Passenger extends Entity {

    private static Map<String, Passenger> passengers = new HashMap<>();

    public static Set<String> passengers() {
        return passengers.keySet();
    }

    private List<Station> stations = null;
    private int currentPos = 0;
    private Train currentTrain = null;
    // private Station currentStation = null;

    public synchronized void print() {
        P.pl("Passenger " + this.toString());
        if (currentTrain == null) {
            P.pl("\tcurrentTrain:\tnull");
        } else {
            P.pl("\tcurrentTrain:\t" + currentTrain.toString());
        }
        P.pl("\tboardingStation:\t" + boardingStation().toString());
        if (deboardingStation() == null) {
            P.pl("\tdeboardingStation:\tnull");
        } else {
            P.pl("\tdeboardingStation:\t" + deboardingStation().toString());
        }
        // P.pl("\tdeboardingStation:\t" + deboardingStation().toString());
        P.p("\tstations:\t");
        P.list(stations);
        P.pl();
    }

    private Passenger(String name) {
        super(name);
    }

    public static Passenger make(String name) {
        Passenger existingPassenger = passengers.get(name);

        if (existingPassenger != null) {
            return existingPassenger;
        }

        Passenger newPassenger = new Passenger(name);
        passengers.put(name, newPassenger);
        return newPassenger;
    }

    public static void reset() {
        // passengers = new HashMap<>();
        for (Passenger p : passengers.values()) {
            p.resetPassenger();
        } 
        // passengers.clear();
    }

    private void resetPassenger() {
        stations = new LinkedList<>();
        currentPos = 0;
        currentTrain = null;
    }

    public Train currentTrain() {
        return currentTrain;
    }

    public Station boardingStation() {
        return currentStation();
    }

    private Station currentStation() {
        return stations.get(currentPos);
    }

    public Station deboardingStation() {
        if (arrived()) {
            // P.pl("\t" + this.toString() + " arrived at " + currentStation().toString());
            return null;
        } else {
            // P.pl("\t" + this.toString() + "'s current station is " + currentStation().toString() + " and will deboard at "
                    // + (stations.get(stations.indexOf(currentStation()) + 1)).toString());
            return stations.get(stations.indexOf(currentStation()) + 1);
        }
    }

    public synchronized void boardTrain(Train train) {
        this.currentTrain = train;
    }

    public synchronized void deboard() {
        currentPos++;
        this.currentTrain = null;
    }

    public boolean arrived() {
        return currentStation() == stations.get(stations.size() - 1);
    }

    public void setStations(List<Station> stations) {
        this.stations = stations;
        // currentStation = stations.get(0);
        // stations.get(0).addPassenger(this);
        currentPos = 0;
    }

    public List<Station> getStations() {
        return Collections.unmodifiableList(stations);
    }

}
