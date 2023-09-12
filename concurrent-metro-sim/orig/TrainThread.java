public class TrainThread extends Thread {
    private MBTA mbta;
    private Train train;
    private Log log;

    public TrainThread(Train train, MBTA mbta, Log log) {
        this.mbta = mbta;
        this.train = train;
        this.log = log;
    }

    // public String name() {
    //     return train.toString();
    // }

    private void stay() {
        try {
            P.pl("pause");
            sleep(500);
        } catch (InterruptedException e) {
            throw new RuntimeException("Thread couldn't sleep", e);
        }
    }

    public void run() {
        P.pl("\tTrainThread -- train =  " + train);
        // train.print();

        // get the current station lock, next station lock, and train lock
        Station currentStation = train.currentStation();
        Station nextStation = train.nextStation();

        P.pl("    TrainThread --\t" + train + "'s currentStation: " + currentStation);
        P.pl("    TrainThread --\t" + train + "'s nextStation:    " + nextStation);
        

        currentStation.stationLock.lock();
        train.trainLock.lock();
        nextStation.stationLock.lock();

        P.pl("    TrainThread --\t" + train + " requesting " + currentStation + "'s lock");
        P.pl("    TrainThread --\t" + train + " requesting " + nextStation + "'s lock");
        P.pl("    TrainThread --\t" + train + " requesting " + train + "'s lock");

        // train leaves currentStation
        currentStation.makeVacant();

        try {
            // wait for the next station to free up
            while (currentStation.currentTrain() != train) {
                nextStation.stationVacant.await();
            }
            
            // got to next station
            log.train_moves(train, currentStation, nextStation);

            // signal that passengers can deboard
            train.deboardPassengers();

        } catch (InterruptedException e) {
            throw new RuntimeException("Thread interrupted awaiting station vacancy at " + currentStation, e); 
        } finally {
            currentStation.stationLock.unlock();
            nextStation.stationLock.unlock();
            train.trainLock.unlock();
        }

        stay();
    }
}
