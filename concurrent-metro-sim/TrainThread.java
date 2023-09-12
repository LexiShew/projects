public class TrainThread extends Thread {
    private MBTA mbta;
    private Train train;
    private Log log;

    public TrainThread(Train train, MBTA mbta, Log log) {
        this.mbta = mbta;
        this.train = train;
        this.log = log;
    }

    public String name() {
        return train.toString();
    }

    private void stay() {
        try {
            // P.pl("pause");
            sleep(500);
        } catch (InterruptedException e) {
            throw new RuntimeException("Thread couldn't sleep", e);
        }
    }

    public void run() {

        // lock into starting station
        // Station currentStation = train.currentStation();
        // currentStation.stationLock.lock();;
        // while (currentStation.currentTrain() != train) {
        // currentStation.stationVacant.signalAll();
        // }
        // P.pl(" TrainThread -- " + this.name() + " got " + currentStation.toString() +
        // "'s lock again");

        // run trains until all passengers are at their final destination
        while (!mbta.allPassengersArrived()) {
            
            Station currentStation = train.currentStation();
            currentStation.stationLock.lock();
            
            Station nextStation = train.nextStation();
            P.pl("    TrainThread -- " + this.name() + " moving from " + currentStation + " to " + nextStation);
            // lock into starting station
            P.pl("    TrainThread -- " + this.name() + " locking " + currentStation + "'s stationLock");
            P.pl("    TrainThread -- " + this.name() + " locking " + nextStation + "'s stationLock");
            nextStation.stationLock.lock();
            P.pl("    TrainThread -- " + this.name() + " locking " + nextStation + "'s passengerLock");
            nextStation.passengerLock.lock();

            // don't let passengers get off while moving

            // lock into next station

            // start moving train, signalling that the current station is vacant
            currentStation.moveTrain();

            try {
                while (nextStation.currentTrain() != null) {
                    P.pl("    TrainThread -- " + this.name() + " awaiting vacant station " + nextStation);
                    nextStation.stationVacant.await();
                }
                nextStation.unloadPassengersFromTrain(train);
                log.train_moves(train, currentStation, nextStation);

            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            } finally {
                P.pl("    TrainThread -- " + this.name() + " unlocking " + currentStation + "'s stationLock");
                currentStation.stationLock.unlock();
                P.pl("    TrainThread -- " + this.name() + " unlocking " + nextStation + "'s stationLock");
                nextStation.stationLock.unlock();
                P.pl("    TrainThread -- " + this.name() + " unlocking " + nextStation + "'s passengerLock");
                nextStation.passengerLock.unlock();
            }
            stay();
            // P.pl(" TrainThread -- " + this.name() + " got " + nextStation.toString() +
            // "'s lock again");

            // release previous station now that train has moved
            // lock into station, and signal to all passengers they can deboard


            // // while train is moving, acquire it's own lock to prevent passengers from
            // // boarding/deboarding prematurely
            // // P.pl(" TrainThread -- " + this.name() + " acquiring " + train.toString() +
            // "'s lock");
            // train.acquireTrainLock();

            // // move train
            // log.train_moves(train, currentStation, nextStation);
            // train.moveTrain();
            // // P.pl("\tTrainThread -- " + train.toString() + " moving to " +
            // train.nextStation());

            // // train has moved, so release its lock to give to passengers
            // // P.pl(" TrainThread -- " + this.name() + " releasing " + train.toString() +
            // "'s lock");
            // train.signalTrainLockRelease();
            // train.releaseTrainLock();

            // // early exit?
            // // if (train.passengers().isEmpty()) {
            // // stay();
            // // }

            // // loop will restart, with the previous "nextStation" as the "currentStation"

        }

        // all passengers have arrived, so release the "currentStation" lock
        // P.pl(" TrainThread -- finished!");
        // if (nextStation != null) {
        // nextStation.signalStationLockRelease();
        // nextStation.stationLock.unlock();
        // }
        // P.pl(" TrainThread -- " + this.name() + " releasing " +
        // currentStation.toString() + "'s lock");
        // currentStation.stationLock.unlock();
    }
}
