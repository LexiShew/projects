import java.io.*;
import java.util.*;

public class Sim {

    public static void run_sim(MBTA mbta, Log log) {
        List<Thread> threads = new LinkedList<>();

        // thread for each Train
        for (Train t : mbta.lines()) {
            threads.add(new TrainThread(t, mbta, log));
        }

        // Thread for each Passenger
        for (Passenger p : mbta.passengers()) {
            threads.add(new PassengerThread(p, mbta, log));
        }
        
        // start simulation
        for (Thread t : threads) {
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
