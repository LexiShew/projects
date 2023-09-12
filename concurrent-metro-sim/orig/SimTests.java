import static org.junit.Assert.*;

import java.io.*;
import java.util.List;
import java.util.Map;

import org.junit.*;
import org.junit.rules.TestName;

import com.google.gson.Gson;

public class SimTests {

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
    public void testOneTrip() {
        String filename = "C:\\Users\\lexis\\Code\\cs121\\p5-mbta\\oneTrip.json";

        MBTA mbta = new MBTA();
        mbta.loadConfig(filename);
        mbta.checkStart();
        Log log = new Log();
        Sim.run_sim(mbta, log);
        mbta.checkEnd();

        mbta.reset();
        mbta.loadConfig(filename);
        Verify.verify(mbta, log);
        // this log ^ is connected to the old mbta config
    }

    @Test
    public void testOneTransfer() {
        String filename = "C:\\Users\\lexis\\Code\\cs121\\p5-mbta\\oneTransfer.json";

        MBTA mbta = new MBTA();
        mbta.loadConfig(filename);
        mbta.checkStart();
        Log log = new Log();
        Sim.run_sim(mbta, log);
        mbta.checkEnd();

        mbta.reset();
        mbta.loadConfig(filename);
        Verify.verify(mbta, log);
        // this log ^ is connected to the old mbta config
    }

    @Test
    public void testSample() {
        String filename = "C:\\Users\\lexis\\Code\\cs121\\p5-mbta\\sample.json";

        MBTA mbta = new MBTA();
        mbta.loadConfig(filename);
        mbta.checkStart();
        Log log = new Log();
        Sim.run_sim(mbta, log);
        mbta.checkEnd();

        mbta.reset();
        mbta.loadConfig(filename);
        Verify.verify(mbta, log);
        // this log ^ is connected to the old mbta config
    }

    @Test
    public void sameTrack() {
        String filename = "C:\\Users\\lexis\\Code\\cs121\\p5-mbta\\samePath.json";

        // run sim, print log to log.json
        MBTA mbta = new MBTA();
        mbta.loadConfig(filename);
        mbta.checkStart();
        Log log = new Log();
        Sim.run_sim(mbta, log);
        mbta.checkEnd();

        mbta.reset();
        mbta.loadConfig(filename);
        Verify.verify(mbta, log);
    }
}
