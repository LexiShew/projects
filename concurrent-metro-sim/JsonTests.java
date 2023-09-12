
import static org.junit.Assert.*;

import java.io.*;
import java.util.List;
import java.util.Map;

import org.junit.*;
import org.junit.rules.TestName;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;

public class JsonTests {

    
    @Rule
    public TestName name = new TestName();

    @Before
    public void separate() {
        P.s();
        P.p("" + name.getMethodName());
    }

    @Test
    public void testTest() {
        assertTrue("true should be true", true);
    }

    @Test(expected = JsonSyntaxException.class)
    public void invalidJsonTest() {
        List bad = List.of("a", "b");
        
        Gson gson = new Gson();
        String json = gson.toJson(bad);

        Map nope = gson.fromJson(json, Map.class);
    }

    @Test
    public void gsonTest1() {
        Gson gson = new Gson();
        String jsonLines = gson.toJson(lines);
        String jsonTrips = gson.toJson(trips);

        Map newLines = gson.fromJson(jsonLines, Map.class);
        Map newTrips = gson.fromJson(jsonTrips, Map.class);

        assertEquals(lines, newLines);
        assertEquals(trips, newTrips);
    }

    @Test
    public void gsonTest2() {
        TestJson tj = new TestJson();
        tj.lines = lines;
        tj.trips = trips;

        Gson gson = new Gson();
        String json = gson.toJson(tj);

        TestJson newTj = gson.fromJson(json, TestJson.class);

        assertEquals(lines, newTj.lines);
        assertEquals(trips, newTj.trips);
    }

    @Test
    public void gsonTest3() {

        TestJson tj = new TestJson();
        tj.lines = lines;
        tj.trips = trips;

        Gson gson = new Gson();
        String json = gson.toJson(tj);

        BufferedWriter bw;
        try {
            bw = new BufferedWriter(new FileWriter("C:\\Users\\lexis\\Code\\cs121\\p5-mbta\\gsonTest1.json"));
            bw.write(json);
            bw.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

        BufferedReader br;
        try {
            br = new BufferedReader(new FileReader("C:\\Users\\lexis\\Code\\cs121\\p5-mbta\\gsonTest1.json"));
            TestJson newTj = gson.fromJson(br, TestJson.class);
            assertEquals(lines, newTj.lines);
            assertEquals(trips, newTj.trips);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private class TestJson {
        Map lines;
        Map trips;
    }

    private Map lines = Map.of(
            "red",
            List.of("Davis", "Harvard", "Kendall", "Park",
                    "Downtown Crossing", "South Station",
                    "Broadway", "Andrew", "JFK"),
            "orange",
            List.of("Ruggles", "Back Bay",
                    "Tufts Medical Center", "Chinatown",
                    "Downtown Crossing", "State",
                    "North Station", "Sullivan"),
            "green",
            List.of("Tufts", "East Sommerville", "Lechmere",
                    "North Station", "Government Center",
                    "Park", "Boylston", "Arlington",
                    "Copley"),
            "blue",
            List.of("Tufts", "East Sommerville", "Lechmere",
                    "North Station", "Government Center", "Park",
                    "Boylston", "Arlington", "Copley"));

    private Map trips = Map.of(
            "Bob",  List.of("Park", "Tufts"),
            "Alice", List.of("Davis", "Kendall"),
            "Carol", List.of("Maverick", "State",
                    "Downtown Crossing", "Park",
                    "Tufts"));


}
