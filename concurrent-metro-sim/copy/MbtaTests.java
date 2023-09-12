import static org.junit.Assert.*;

import java.io.*;
import java.util.*;

import org.junit.*;
import org.junit.rules.TestName;

import com.google.gson.Gson;

public class MbtaTests {

    private MBTA mbta = new MBTA();

    @Rule
    public TestName name = new TestName();

    @Before
    public void separate() {
        P.s();
        P.pl("" + name.getMethodName());
    }

    @Before
    public void reset() {
        mbta.reset();
    }

    @Test
    public void testTest() {
        assertTrue("true should be true", true);
    }

    @Test
    public void testStationEquality() {
        Station s1 = Station.make("s1");
        Station s2 = Station.make("s1");
        assertTrue("Stations are not physically equal", s1 == s2);
        assertTrue("Stations are not physically equal", Station.make("a") == Station.make("a"));
    }

    @Test
    public void testTrainEquality() {
        Train t1 = Train.make("t1");
        Train t2 = Train.make("t1");
        assertTrue("Trains are not physically equal", t1 == t2);
        assertTrue("Train are not physically equal", Train.make("a") == Train.make("a"));

    }

    @Test
    public void testPassengerEquality() {
        Passenger p1 = Passenger.make("p1");
        Passenger p2 = Passenger.make("p1");
        assertTrue("Passengers are not physically equal", p1 == p2);
        assertTrue("Passengers are not physically equal", Passenger.make("a") == Passenger.make("a"));
    }

    @Test
    public void testAddLine() {
        MBTA mbta = new MBTA();
        
        List<String> red = List.of("Davis", "Harvard", "Kendall", "Park",
                                    "Downtown Crossing", "South Station",
                                    "Broadway", "Andrew", "JFK");
        mbta.addLine("red", red);

        assertEquals("Lists not equal", stringToStation(red), Train.make("red").getStations());

        List<Station> stations = Train.make("red").getStations();
        P.p("red:\t");
        for (Station s : stations) {
            P.p(" " + s.toString());
        }
        P.pl();
    }

    private List<Station> stringToStation(List<String> strings) {
        List<Station> stations = new LinkedList<>();
        for (String s : strings) {
            stations.add(Station.make(s));
        }
        assertTrue(stations != null && stations.size() != 0);
        return stations;
        
    }

    @Test
    public void testAddJourney() {

        MBTA mbta = new MBTA();
        
        List<String> carol = List.of("Maverick", "State", "Downtown Crossing",
                                        "Park", "Tufts");
        mbta.addJourney("carol", carol);
        assertEquals("Lists not equal", stringToStation(carol), Passenger.make("carol").getStations());

        
        List<Station> stations = Passenger.make("carol").getStations();
        P.p("carol:\t");
        for (Station s : stations) {
            P.p(" " + s.toString());
        }
        P.pl();
    }

    @Test
    public void testLoadConfigSample() {
        MBTA mbta = new MBTA();
        mbta.loadConfig("C:/Users/lexis/Code/cs121/p5-mbta/sample.json");

        P.pl("LINES");
        for (Train l : mbta.lines()) {
            P.p(l.toString() + ":\t");
            for (Station s : l.getStations()) {
                P.p(" " + s.toString());
            }
            P.pl();
        }

        P.pl("\nJOURNEYS");
        for (Passenger p : mbta.passengers()) {
            P.p(p.toString() + ":\t");
            for (Station s : p.getStations()) {
                P.p(" " + s.toString());
            }
            P.pl();
        }
    }
}
