import java.util.*;
import java.util.concurrent.locks.*;

/*******************************************************************************
 * 
 * Representation of a subway station. Each instance maintains list of
 * Passengers present in the station.
 * 
 * @author skeleton by CS121, completed by Lexi Shewchuk
 * 
 ******************************************************************************/

public class Station extends Entity {

    public Lock stationLock = new ReentrantLock();
    public Condition stationVacant = stationLock.newCondition();

    private static Map<String, Station> stations = new HashMap<>();

    public static Set<String> stations() {
        return stations.keySet();
    }

    // private List<Passenger> passengers = new LinkedList<>();
    private Train currTrain = null;

    public synchronized void print() {
        P.pl("Station " + this.toString());
        P.p("\tcurrentTrain:\t");
        if (currentTrain() != null) {
            P.p(currentTrain().toString());
        }
        P.pl();

        // P.p("\tpassengers:\t");
        // P.list(passengers);
        P.pl();
    }

    private Station(String name) {
        super(name);
    }

    public static Station make(String name) {
        Station existingStation = stations.get(name);
        
        if (existingStation != null) {
            return existingStation;
        }

        Station newStation = new Station(name);
        stations.put(name, newStation);
        return newStation;
    }
    
    public static void reset() {
        // stations = new HashMap<>();
        for (Station s : stations.values()) {
            s.resetStation();
        }
        // stations.clear();
    }

    private void resetStation() {
        // passengers = new LinkedList<>();
        currTrain = null;
    }
    
    public Train currentTrain() {
        return currTrain;
    }

    public void makeVacant() {
        P.pl("    Station --\tmaking " + this + " vacant");
        stationLock.lock();
        P.pl("    Station --\t" + this + " getting " + this + "'s lock");
        currTrain = null;        
        P.pl("    Station --\t" + this + " signalling vacant");
        stationVacant.signalAll();
        stationLock.unlock();
        P.pl("    Station --\t" + this + " releasing " + this + "'s lock");

    }

    public void setCurrentTrain(Train t) {
        currTrain = t;
    }

    // public void addPassenger(Passenger passenger) {
    //     // P.pl("\tStation#addPassenger -- " + passenger.toString() + " at " + this.toString());
    //     passengers.add(passenger);
    // }

    // public void removePassenger(Passenger passenger) {
    //     // P.pl("\tStation#removePassenger -- " + passenger.toString() + " at " + this.toString());
    //     passengers.remove(passenger);
    // }

    

    // public void acquireStationLock() {
    //     // P.pl("\tStation#acquireStationLock -- " + this.toString());
    //     stationLock.lock();
    // }

    // public void releaseStationLock() {
    //     // P.pl("\tStation#releaseStationLock -- " + this.toString());
    //     stationLock.unlock();
    // }

    // public void signalStationLockRelease() {
    //     // P.pl("\tStation#signalStationLockRelease -- " + this.toString());
    //     stationLockCondition.signalAll();
    // }

    // public void awaitStationLock() {
    //     // P.pl("\tStation#awaitStationLock -- " + this.toString());

    //     try {
    //         stationLockCondition.await();
    //     } catch (InterruptedException e) {
    //         throw new RuntimeException("Could not await station lock", e);
    //     }
    // }



}
