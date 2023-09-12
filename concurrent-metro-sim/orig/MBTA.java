import static org.junit.Assert.assertEquals;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;

import com.google.gson.Gson;

/*******************************************************************************
 * 
 * Representation of the MBTA's subway system. Maintains state of stations,
 * trains, and passengers, and validates conditions of a simulation.
 * 
 * @author skeleton by CS121, completed by Lexi Shewchuk
 * 
 ******************************************************************************/

public class MBTA {

    private List<Train> lines = new LinkedList<>();
    private List<Passenger> passengers = new LinkedList<>();

    /**
     * return a list of the active trains/lines
     * 
     * @return List of Trains/lines in the setup
     */
    public List<Train> lines() {
        return lines;
    }

    /**
     * returns a list of the passengers present in the simulation
     * 
     * @return List of Passengers in the setup
     */
    public List<Passenger> passengers() {
        return passengers;
    }

    /**
     * Creates an initially empty simulation
     */
    public MBTA() {
    }

    /**
     * Adds a new transit line with given name and stations
     * 
     * @param name     the of the line
     * @param stations list of the stations on the line
     */
    public synchronized void addLine(String name, List<String> stations) {
        Train t = Train.make(name);
        lines.add(t);

        List<Station> newStations = new ArrayList<>();
        for (String s : stations) {
            newStations.add(Station.make(s));
        }
        t.setStations(newStations);
    }

    /**
     * Adds a new planned journey to the simulation
     * 
     * @param name     name of the passenger
     * @param stations list of stations on the passenger's trip
     */
    public synchronized void addJourney(String name, List<String> stations) {
        Passenger p = Passenger.make(name);
        passengers.add(p);

        List<Station> newStations = new ArrayList<>();
        for (String s : stations) {
            newStations.add(Station.make(s));
        }
        // P.pl("MBTA#addJourney");
        p.setStations(newStations);
        // journeys.put(name, newStations);
    }

    /**
     * Return normally if initial simulation conditions are satisfied,
     * otherwise raises an exception
     * 
     * @throws RuntimeException if initial configuration is invalid
     */
    public void checkStart() {
        // make sure trains start at the first station on their route
        for (Train t : lines) {
            if (t.currentStation() != t.getStations().get(0)) {
                throw new RuntimeException("Trains must start at the first station of their line");
            }
        }

        // make sure passengers start at the first station of their trip
        for (Passenger p : passengers) {
            if (p.boardingStation() != p.getStations().get(0)) {
                throw new RuntimeException("Passengers must start at the first station of their journey");
            }
        }
    }

    /**
     * Return normally if final simulation conditions are satisfied,
     * otherwise raises an exception
     * 
     * @throws RuntimeException if final configuration is invalid
     */
    public void checkEnd() {
        if (!allPassengersArrived()) {
            throw new RuntimeException("Passengers must end at the final station of their journey");
        }
    }

    /**
     * checks if all Passengers have arrived at their final destinations
     * 
     * @return true if all Passengers are at their final Station, otherwise false
     */
    public synchronized boolean allPassengersArrived() {
        // P.p("\tMBTA#allPassengersArrived -- ");
        // make sure passengers end at the last station of their trip
        for (Passenger p : passengers) {
            if (!p.arrived()) {
                // P.pl("false");
                return false;
            }
        }
        // P.pl("true");
        return true;
    }

    /**
     * reset to an empty simulation
     */
    public synchronized void reset() {
        lines.clear();
        passengers.clear();

        Train.reset();
        Passenger.reset();
        Station.reset();
    }

    /**
     * sets up the initial MBTA configuration by reading from a given config file
     * 
     * @param filename the name of the config file to be read from
     * @throws IOException       if file could not be read
     * @throws RuntimeExceptions if file is not a valid config file
     */
    public synchronized void loadConfig(String filename) {
        Gson gson = new Gson();

        JsonObject maps = null;
        BufferedReader br;
        try {
            br = new BufferedReader(new FileReader(filename));
            maps = gson.fromJson(br, JsonObject.class);
            br.close();

        } catch (IOException e) {
            e.printStackTrace();
        }

        if (maps == null) {
            throw new RuntimeException("Could not read configuration file");
        }

        for (String name : maps.lines.keySet()) {
            addLine(name, maps.lines.get(name));
        }
        for (String name : maps.trips.keySet()) {
            addJourney(name, maps.trips.get(name));
        }
    }

    /**
     * Object that temporarily stores the lines and trips from a config file
     */
    private class JsonObject {
        Map<String, List<String>> lines;
        Map<String, List<String>> trips;
    }
}
