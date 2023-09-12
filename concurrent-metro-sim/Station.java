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
    public Lock passengerLock = new ReentrantLock();
    public Condition trainArrived = passengerLock.newCondition();

    private static Map<String, Station> stations = new HashMap<>();

    public static Set<String> stations() {
        return stations.keySet();
    }

    private List<Passenger> passengers = new LinkedList<>();
    private Train currTrain = null;

    public synchronized void print() {
        P.pl("Station " + this.toString());
        P.p("\tcurrentTrain:\t");
        if (currTrain != null) {
            P.p(currTrain.toString());
        }
        P.pl();

        P.p("\tpassengers:\t");
        P.list(passengers);
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
        passengers = new LinkedList<>();
        currTrain = null;
    }

    public void moveTrain() {
        stationLock.lock();
        currTrain = null;
        P.pl("    Station -- " + this + " moving train + signaling vacancy");
        stationVacant.signalAll();
        stationLock.unlock();
    } 

    public void unloadPassengersFromTrain(Train t) {
        passengerLock.lock();
        currTrain = t;
        P.pl("    Station -- " + t + " arrived at " + this + "signaling");
        trainArrived.signalAll();
        passengerLock.unlock();
    }
    
    public Train currentTrain() {
        return currTrain;
    }

    public void makeVacant() {
        currTrain = null;
    }

    public void setCurrentTrain(Train t) {
        currTrain = t;
    }

    public void addPassenger(Passenger passenger) {
        // P.pl("\tStation#addPassenger -- " + passenger.toString() + " at " + this.toString());
        passengerLock.lock();
        passengers.add(passenger);
        passengerLock.unlock();
    }

    public void removePassenger(Passenger passenger) {
        // P.pl("\tStation#removePassenger -- " + passenger.toString() + " at " + this.toString());
        passengerLock.lock();
        passengers.remove(passenger);
        passengerLock.unlock();
    }

    
    

    // public void acquireStationLock() {
    //     // P.pl("\tStation#acquireStationLock -- " + this.toString());
    //     stationLock.lock();
    // }

    // public void releaseStationLock() {
    //     // P.pl("\tStation#releaseStationLock -- " + this.toString());
    //     stationLock.unlock();
    // }

    // public void signalStationVacant() {
    //     // P.pl("\tStation#signalStationLockRelease -- " + this.toString());
    //     stationVacantCondition.signalAll();
    // }

    // public void awaitVacantStation() {
    //     // P.pl("\tStation#awaitStationLock -- " + this.toString());

    //     try {
    //         stationVacantCondition.await();
    //     } catch (InterruptedException e) {
    //         throw new RuntimeException("Could not await station lock", e);
    //     }
    // }

    
    

    // public void acquirePassengerLock() {
    //     // P.pl("\tStation#acquirepassengerLock -- " + this.toString());
    //     passengerLock.lock();
    // }

    // public void releasePassengerLock() {
    //     // P.pl("\tStation#releasepassengerLock -- " + this.toString());
    //     passengerLock.unlock();
    // }

    // public void signalTrainArrival() {
    //     // P.pl("\tStation#signalpassengerLockRelease -- " + this.toString());
    //     trainArrivedCondition.signalAll();
    // }

    // public void awaitTrainArrival() {
    //     // P.pl("\tStation#awaitpassengerLock -- " + this.toString());

    //     try {
    //         trainArrivedCondition.await();
    //     } catch (InterruptedException e) {
    //         throw new RuntimeException("Could not await station lock", e);
    //     }
    // }



}
