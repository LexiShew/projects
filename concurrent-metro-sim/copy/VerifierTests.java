import static org.junit.Assert.*;

import java.io.*;
import java.util.*;

import org.junit.*;
import org.junit.rules.TestName;
import org.junit.rules.Verifier;

import com.google.gson.Gson;

public class VerifierTests {

    @Rule
    public TestName name = new TestName();

    @Before
    public void separate() {
        P.s();
        P.pl("" + name.getMethodName());
    }

    @Test
    public void testTest() {
        assertTrue("true should be true", true);
    }

    @Test
    public void testOneMove() {
        MBTA mbta = new MBTA();
        mbta.addLine("t1", List.of("s1", "s2"));
        mbta.addJourney("p1", List.of("s1", "s2"));
        P.pl("start");
        Passenger p1 = Passenger.make("p1");
        Train t1 = Train.make("t1");
        Station s1 = Station.make("s1");
        Station s2 = Station.make("s2");
        p1.print();
        t1.print();
        s1.print();
        s2.print();
        P.pl("\n\n");
        mbta.checkStart();
        
        

        BoardEvent be = new BoardEvent(p1, t1, s1);
        MoveEvent me = new MoveEvent(t1, s1, s2);
        DeboardEvent de = new DeboardEvent(p1, t1, s2);
        List<Event> events = new LinkedList<>(Arrays.asList(be, me, de));
        Log log = new Log(events);

        Verify.verify(mbta, log);
        P.pl("end");
        p1.print();
        t1.print();
        s1.print();
        s2.print();

        mbta.checkEnd();
    }
}
