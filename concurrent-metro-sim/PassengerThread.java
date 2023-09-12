public class PassengerThread extends Thread {
    private MBTA mbta;
    private Log log;
    private Passenger passenger;

    public PassengerThread(Passenger passenger, MBTA mbta, Log log) {
        this.mbta = mbta;
        this.log = log;
        this.passenger = passenger;
    }

    public String name() {
        return passenger.toString();
    }

    public void run() {
        // P.pl("\tPassengerThread#run -- passenger = " + passenger.toString());

        // run until this passenger is at their final destination
        while (!passenger.arrived()) {

            Train passengerTrain = passenger.currentTrain();

            if (passengerTrain != null) {
                // Passenger is on a Train

                Station deboardStation = passenger.deboardingStation();
                
                P.pl("    PassengerThread -- " + this.name() + " locking " + deboardStation + "'s passengerLock");
                deboardStation.passengerLock.lock();

                try {
                    while (deboardStation != passengerTrain.currentStation()) {
                        P.pl("    PassengerThread -- " + this.name() + " awaiting train " + passengerTrain + "'s arrival at " + deboardStation);
                        deboardStation.trainArrived.await();
                    }

                    // P.pl(" PassengerThread -- " + this.name() + " got " +
                    // passengerTrain.toString() + "'s lock again");

                    if (passengerTrain.currentStation() == deboardStation) {

                        P.pl("    PassengerThread -- " + this.name() + " locking " + deboardStation + "'s stationLock");
                        deboardStation.stationLock.lock();
                        deboardStation.addPassenger(passenger);
                        passenger.deboard();
                        
                        // acquired train lock -- passenger can deboard
                        log.passenger_deboards(passenger, passengerTrain, passengerTrain.currentStation());
                        
                        P.pl("    PassengerThread -- " + this.name() + " locking " + deboardStation + "'s stationLock");
                        P.pl("    PassengerThread -- " + this.name() + " locking " + deboardStation + "'s passengerLock");
                        deboardStation.stationLock.unlock();

                        deboardStation.passengerLock.unlock();
                    }

                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                } finally {
                    P.pl("    PassengerThread -- " + this.name() + " locking " + deboardStation + "'s passengerLock");
                    deboardStation.passengerLock.unlock();
                }

                // // remove passenger from train, add to station
                // passengerTrain.removePassenger(passenger);
                // passengerTrain.currentStation().addPassenger(passenger);
                // // P.pl("\tPassengerThread -- " + passenger.toString() + " deboarded " +
                // // passengerTrain.toString() + " at " + passengerTrain.currentStation());

                // // release the lock to other passengers or to the train so it can move
                // // P.pl(" PassengerThread -- " + this.name() + " releasing " +
                // passengerTrain.toString() + "'s lock");
                // passengerTrain.signalTrainLockRelease();
                // passengerTrain.releaseTrainLock();

            } else {
                // Passenger is in the station
                Station currentStation = passenger.boardingStation();

                // lock into station to let passengers on
                currentStation.passengerLock.lock();
                // currentStation.print();

                Train trainToBoard = null;
                for (Train t : mbta.lines()) {
                    if (t.getStations().contains(passenger.boardingStation()) &&
                            t.getStations().contains(passenger.deboardingStation())) {
                        trainToBoard = t;
                    }
                }

                // P.pl(" PassengerThread -- " + this.name() + " acquiring " +
                // trainToBoard.toString() + "'s lock");
                // trainToBoard.acquireTrainLock();

                try {
                while (currentStation.currentTrain() != trainToBoard) {
                    // P.pl(" PassengerThread -- " + this.name() + " awaiting " +
                    // trainToBoard.toString() + "'s lock");
                    currentStation.trainArrived.await();
                }
                // P.pl(" PassengerThread -- " + this.name() + " got " + trainToBoard.toString()
                // + "'s lock again");

                // got the right train -- board then release train's lock

                // remove passenger from the station, add to train
                // trainToBoard.addPassenger(passenger);

                currentStation.stationLock.lock();
                passenger.boardTrain(trainToBoard);
                currentStation.removePassenger(passenger);
                log.passenger_boards(passenger, trainToBoard, currentStation);
                currentStation.stationLock.unlock();
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            } finally {
                currentStation.passengerLock.unlock();

            }
                // P.pl("\tPassengerThread -- " + passenger.toString() + " boarded " +
                // trainToBoard.toString() + " at " + currentStation.toString());
                // P.pl(" PassengerThread -- " + this.name() + " releasing " +
                // trainToBoard.toString() + "'s lock");
                // release the lock to other passenger or to the train
                // trainToBoard.signalTrainLockRelease();
                // trainToBoard.releaseTrainLock();

            }

        }

    }
}
