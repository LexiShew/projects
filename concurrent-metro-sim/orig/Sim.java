import java.io.*;
import java.util.*;

public class Sim {

    public static void run_sim(MBTA mbta, Log log) {
        // P.pl("\tSim#run_sim\n");

        Set<Thread> threads = new HashSet<Thread>();

        // thread for each Train
        for (Train t : mbta.lines()) {
            // threads.put(t.toString(), );
            threads.add(new TrainThread(t, mbta, log));
        }

        // P.p("lines:\t\t");
        // P.list(mbta.lines());

        // Thread for each Passenger
        for (Passenger p : mbta.passengers()) {
            // threads.put(p.toString(), new PassengerThread(p, mbta, log));
            threads.add(new PassengerThread(p, mbta, log));
        }

        // P.p("\npassengers:\t");
        // P.list(mbta.passengers());

        // start simulation
        // for (String t : threads.keySet()) {
        for (Thread t : threads) {
            // if (t instanceof PassengerThread) {
            //     P.pl("\n\tSim -- starting thread " + ((PassengerThread) t).name());

            // }
            // if (t instanceof TrainThread) {
            //     P.pl("\tSim -- starting thread " + ((TrainThread) t).name());
            // }

            t.start();
        }

        // finish simulation by joining all threads
        try {
            for (Thread t : threads) {
                t.join();
            }
        } catch (InterruptedException e) {
            throw new RuntimeException("Could not join threads\n", e);
        }
    }

    public static void main(String[] args) throws Exception {
        if (args.length != 1) {
            System.out.println("usage: ./sim <config file>");
            System.exit(1);
        }

        MBTA mbta = new MBTA();
        mbta.loadConfig(args[0]);

        Log log = new Log();

        run_sim(mbta, log);

        String s = new LogJson(log).toJson();
        PrintWriter out = new PrintWriter("log.json");
        out.print(s);
        out.close();

        mbta.reset();
        mbta.loadConfig(args[0]);
        Verify.verify(mbta, log);
    }
}
