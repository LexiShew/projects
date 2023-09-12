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
    public void testVerify() {
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
    public void testVerifyNewMBTA() {
        String filename = "C:\\Users\\lexis\\Code\\cs121\\p5-mbta\\oneTrip.json";

        MBTA mbta = new MBTA();
        mbta.loadConfig(filename);
        mbta.checkStart();
        Log log = new Log();
        Sim.run_sim(mbta, log);
        mbta.checkEnd();

        MBTA mbta2 = new MBTA();
        mbta2.loadConfig(filename);
        Verify.verify(mbta2, log);
    }

    @Test
    public void testVerifiyNewLog() {
        String filename = "C:\\Users\\lexis\\Code\\cs121\\p5-mbta\\oneTrip.json";
        String logfile = "C:\\Users\\lexis\\Code\\cs121\\p5-mbta\\log.json";

        // run sim, print log to log.json
        MBTA mbta = new MBTA();
        mbta.loadConfig(filename);
        mbta.checkStart();
        Log log = new Log();
        Sim.run_sim(mbta, log);
        mbta.checkEnd();

        String s = new LogJson(log).toJson();
        try {
            PrintWriter out = new PrintWriter(logfile);
            out.print(s);
            out.close();
        } catch (FileNotFoundException e) {}

        // use config and log generated above to run verifier
        mbta.reset();
        mbta.loadConfig(filename);
        try {
            Reader r = new BufferedReader(new FileReader(logfile));
            Log log2 = LogJson.fromJson(r).toLog();
            P.pl("verify using new log");
            Verify.verify(mbta, log2);
        } catch (FileNotFoundException e) {
        }
    }

    @Test
    public void testLogOutput() {
        String filename = "C:\\Users\\lexis\\Code\\cs121\\p5-mbta\\oneTrip.json";
        String logfile = "C:\\Users\\lexis\\Code\\cs121\\p5-mbta\\log.json";

        // run sim, print log to log.json
        MBTA mbta = new MBTA();
        mbta.loadConfig(filename);
        mbta.checkStart();
        Log log = new Log();
        Sim.run_sim(mbta, log);
        mbta.checkEnd();
        // String s = new LogJson(log).toJson();
        // try {
        //     PrintWriter out = new PrintWriter(logfile);
        //     out.print(s);
        //     out.close();
        // } catch (Exception e) {}

        mbta.reset();
        mbta.loadConfig(filename);
        Verify.verify(mbta, log);


        // use config and log generated above to run verifier
        MBTA mbta2 = new MBTA();
        mbta2.loadConfig(filename);
        try {
            Reader r = new BufferedReader(new FileReader(logfile));
            Log log2 = LogJson.fromJson(r).toLog();
            Verify.verify(mbta2, log2);
        } catch (FileNotFoundException e) {
        }
    }

    @Test
    public void sameTrack() {
        String filename = "C:\\Users\\lexis\\Code\\cs121\\p5-mbta\\oneTrip.json";

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
