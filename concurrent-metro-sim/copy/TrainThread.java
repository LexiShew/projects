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
        Station currentStation = train.currentStation();
        currentStation.acquireStationLock();
        while (currentStation.currentTrain() != train) {
            currentStation.awaitStationLock();
        }
        // P.pl("    TrainThread -- " + this.name() + " got " + currentStation.toString() + "'s lock again");


        Station nextStation = null;
        // run trains until all passengers are at their final destination
        while (!mbta.allPassengersArrived()) {

            currentStation = train.currentStation();
            // pause before moving
            stay();

            // release current station, even tho train isn't locked into next station
            currentStation.signalStationLockRelease();
            currentStation.releaseStationLock();

            // lock into next station
            nextStation = train.nextStation();
            // P.pl("\tTrainThread -- " + train.toString() + " at " + currentStation);
            // P.pl("    TrainThread -- " + this.name() + " acquiring " + nextStation.toString() + "'s lock");

            // while the next station isn't free, wait
            nextStation.acquirePassengerLock();
            nextStation.acquireStationLock();
            currentStation.acquireStationLock();

            currentStation.moveTrain();

            while (nextStation.currentTrain() != null) {
                // P.pl("    TrainThread -- " + this.name() + " awaiting " + nextStation.toString() + "'s lock");
                nextStation.awaitVacantStation();
            }
            // P.pl("    TrainThread -- " + this.name() + " got " + nextStation.toString() + "'s lock again");



            // while train is moving, acquire it's own lock to prevent passengers from
            // boarding/deboarding prematurely
            // P.pl("    TrainThread -- " + this.name() + " acquiring " + train.toString() + "'s lock");
            train.acquireTrainLock();

            // move train
            log.train_moves(train, currentStation, nextStation);
            train.moveTrain();
            // P.pl("\tTrainThread -- " + train.toString() + " moving to " + train.nextStation());

            // train has moved, so release its lock to give to passengers
            // P.pl("    TrainThread -- " + this.name() + " releasing " + train.toString() + "'s lock");
            train.signalTrainLockRelease();
            train.releaseTrainLock();

            // early exit?
            // if (train.passengers().isEmpty()) {
            //     stay();
            // }
            
            // loop will restart, with the previous "nextStation" as the "currentStation"
            
        }

        // all passengers have arrived, so release the "currentStation" lock
        // P.pl("    TrainThread -- finished!");
        if (nextStation != null) {
            nextStation.signalStationLockRelease();
            nextStation.releaseStationLock();
        }
        // P.pl("    TrainThread -- " + this.name() + " releasing " + currentStation.toString() + "'s lock");
        // currentStation.releaseStationLock();
    }
}
