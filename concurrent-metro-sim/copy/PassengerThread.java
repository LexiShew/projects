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
                // check if the train's current Station is the destination

                // P.pl("\tPassengerThread -- " + passenger.toString() + " on " +
                // passengerTrain.toString());
                // P.pl("    PassengerThread -- " + this.name() + " acquiring " + passengerTrain.toString() + "'s lock");
                passengerTrain.acquireTrainLock();

                while (passenger.deboardingStation() != passengerTrain.currentStation()) {
                    // P.pl("    PassengerThread -- " + this.name() + " awaiting " + passengerTrain.toString()
                            // + "'s lock");
                    passengerTrain.awaitTrainLock();
                }
                // P.pl("    PassengerThread -- " + this.name() + " got " + passengerTrain.toString() + "'s lock again");

                // acquired train lock -- passenger can deboard
                log.passenger_deboards(passenger, passengerTrain, passengerTrain.currentStation());

                // remove passenger from train, add to station
                passengerTrain.removePassenger(passenger);
                passengerTrain.currentStation().addPassenger(passenger);
                passenger.deboard();
                // P.pl("\tPassengerThread -- " + passenger.toString() + " deboarded " +
                // passengerTrain.toString() + " at " + passengerTrain.currentStation());

                // release the lock to other passengers or to the train so it can move
                // P.pl("    PassengerThread -- " + this.name() + " releasing " + passengerTrain.toString() + "'s lock");
                passengerTrain.signalTrainLockRelease();
                passengerTrain.releaseTrainLock();

            } else {
                // passenger is at a Station -- board train

                // make sure the next train to come along actually goes to the passenger's next
                // stop
                // P.pl("\tPassengerThread -- " + passenger.toString() + " at " +
                // passenger.boardingStation().toString());
                // passenger.print();

                Train trainToBoard = null;
                Station currentStation = passenger.boardingStation();
                // currentStation.print();

                for (Train t : mbta.lines()) {
                    if (t.getStations().contains(passenger.boardingStation()) &&
                        t.getStations().contains(passenger.deboardingStation())) {
                        trainToBoard = t;
                    }
                }

                // P.pl("    PassengerThread -- " + this.name() + " acquiring " + trainToBoard.toString() + "'s lock");
                trainToBoard.acquireTrainLock();

                while (currentStation.currentTrain() != trainToBoard) {
                    // P.pl("    PassengerThread -- " + this.name() + " awaiting " + trainToBoard.toString() + "'s lock");
                    trainToBoard.awaitTrainLock();
                }
                // P.pl("    PassengerThread -- " + this.name() + " got " + trainToBoard.toString() + "'s lock again");

                // got the right train -- board then release train's lock
                log.passenger_boards(passenger, trainToBoard, currentStation);

                // remove passenger from the station, add to train
                currentStation.removePassenger(passenger);
                trainToBoard.addPassenger(passenger);
                passenger.boardTrain(trainToBoard);

                // P.pl("\tPassengerThread -- " + passenger.toString() + " boarded " +
                // trainToBoard.toString() + " at " + currentStation.toString());
                // P.pl("    PassengerThread -- " + this.name() + " releasing " + trainToBoard.toString() + "'s lock");
                // release the lock to other passenger or to the train
                trainToBoard.signalTrainLockRelease();
                trainToBoard.releaseTrainLock();

            }

        }

    }
}
