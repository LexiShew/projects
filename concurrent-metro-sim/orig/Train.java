import java.util.*;
import java.util.concurrent.locks.*;

/*******************************************************************************
 * 
 * Representation of a subway train. Each instance maintains a set of
 * passengers, who can be added or removed from the Train
 * 
 * @author skeleton by CS121, completed by Lexi Shewchuk
 * 
 ******************************************************************************/

public class Train extends Entity {
    private enum Direction {
        FORWARD, BACKWARD
    }

    public static Set<String> trains() {
        return trains.keySet();
    }

    public Lock trainLock = new ReentrantLock();
    public Condition trainArrived = trainLock.newCondition();

    private static Map<String, Train> trains = new HashMap<>();

    private List<Station> stations = new ArrayList<>();
    private List<Passenger> passengers = new ArrayList<>();
    private int currPos = 0;
    private Direction currDirection = Direction.FORWARD;

    public synchronized void print() {

        P.pl("Train " + this.toString());
        if (currentStation() == null) {
            P.pl("\tcurrentStation:\tnull");
        } else {
            P.pl("\tcurrentStation:\t" + currentStation().toString());
        }
        if (nextStation() == null) {
            P.pl("\tnextStation:\tnull");
        } else {
            P.pl("\tnextStation:\t" + nextStation().toString());
        }
        P.pl("\tcurrDirection:\t" + currDirection.toString());

        P.p("\tstations:\t");
        P.list(stations);

        P.p("\tpassengers:\t");
        P.list(passengers);
        P.pl();
    }

    private Train(String name) {
        super(name);
    }

    public static Train make(String name) {
        Train existingTrain = trains.get(name);

        if (existingTrain != null) {
            return existingTrain;
        }

        Train newTrain = new Train(name);
        trains.put(name, newTrain);
        return newTrain;
    }

    public static void reset() {
        // trains = new HashMap<>();
        for (Train t : trains.values()) {
            t.resetTrain();
        }
        // trains.clear();
    }

    private void resetTrain() {
        stations = new ArrayList<>();
        // passengers.clear();
        currPos = 0;
        currDirection = Direction.FORWARD;
    }

    public void deboardPassengers() {
        P.pl("    Train --\tdeboarding " + this);
        P.pl("    Station --\t" + this + " releasing " + this + "'s lock");
        trainLock.lock();
        currentStation().setCurrentTrain(this);
        trainArrived.signalAll();
        P.pl("    Train --\t" + this + " signalling arrived");
        trainLock.unlock();
        P.pl("    Train --\t" + this + " getting " + this + "'s lock");
    }

    public synchronized void moveTrain() {
        currentStation().makeVacant();
        currPos = nextPos();
        currentStation().setCurrentTrain(this);
    }

    private synchronized int nextPos() {
        int lastStationPos = stations.size() - 1;

        // if at the end of the line, switch direction
        if (currPos == 0) {
            currDirection = Direction.FORWARD;
        } else if (currPos == lastStationPos) {
            currDirection = Direction.BACKWARD;
        }

        // update current position depending on direction
        if (currDirection == Direction.FORWARD) {
            return currPos + 1;
        } else {
            return currPos - 1;
        }
    }

    public Station nextStation() {
        return stations.get(nextPos());
    }

    public Station currentStation() {
        return stations.get(currPos);
    }

    public void setStations(List<Station> stations) {
        this.stations = stations;
        currPos = 0;
        currentStation().setCurrentTrain(this);
    }

    public List<Station> getStations() {
        return Collections.unmodifiableList(stations);
    }

    public void addPassenger(Passenger passenger) {
        passengers.add(passenger);
        // P.pl("\tTrain#addPassenger -- " + passenger.toString() + " boarded " + this.toString());
    }

    public void removePassenger(Passenger passenger) {
        passengers.remove(passenger);
        // P.pl("\tTrain#removePassenger -- " + passenger.toString() + " deboarded " + this.toString());
    }

    public List<Passenger> passengers() {
        return Collections.unmodifiableList(passengers);
    }

    

    // public void acquireTrainLock() {
    //     // P.pl("\tTrain#acquireTrainLock -- " + this.toString());
    //     trainLock.lock();
    // }

    // public void releaseTrainLock() {
    //     // P.pl("\tTrain#releaseTrainLock -- " + this.toString());
    //     trainLock.unlock();
    // }

    // public void signalTrainLockRelease() {
    //     // P.pl("\tTrain#signalTrainLockRelease -- " + this.toString());
    //     trainLockCondition.signalAll();
    // }

    // public void awaitTrainLock() {
    //     // P.pl("\tTrain#awaitTrainLock -- " + this.toString());

    //     try {
    //         trainLockCondition.await();
    //     } catch (InterruptedException e) {
    //         throw new RuntimeException("Could not await train lock", e);
    //     }
    // }
}
