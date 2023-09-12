import static org.junit.Assert.*;
import org.junit.*;
import com.google.gson.*;

import java.util.ArrayList;
import java.util.Arrays;

public class MiscTests {
    
    public static MBTA mbta = new MBTA();
    public static Log logger = new Log();
    
  
    @Test
    public void testToJson() {
        // mbta.debugPrintState();
        mbta.loadConfig("C:\\Users\\lexis\\Code\\cs121\\p5-mbta\\sample.json");
        System.out.println(mbta.lines());
        System.out.println(mbta.passengers());
        // mbta.debugPrintState();
        System.out.println("reset mbta");
        mbta.reset();
        // mbta.debugPrintState();
        
    }
    
    @Test
    public void testSimpleTravel() {
        logger = new Log();
        
        mbta.addLine("a", new ArrayList<String>(Arrays.asList("A", "B", "C")));
        mbta.addLine("b", new ArrayList<String>(Arrays.asList("X", "B", "Z")));        
        
        logger.train_moves(Train.make("a"), Station.make("A"), Station.make("B"));        
        logger.train_moves(Train.make("a"), Station.make("B"), Station.make("C"));
        logger.train_moves(Train.make("b"), Station.make("X"), Station.make("B"));    
        logger.train_moves(Train.make("b"), Station.make("B"), Station.make("Z"));     
        logger.train_moves(Train.make("a"), Station.make("C"), Station.make("B"));
        logger.train_moves(Train.make("a"), Station.make("B"), Station.make("A"));
        
        Verify.verify(mbta, logger);
    }
    
    @Before public void reset() {
        mbta.reset();
        logger = new Log();
        
        System.out.println("-----------------------before test---------------------");
        // mbta.debugPrintState();
        System.out.println("-----------------------done with before test---------------------");
    }
    
    @Test
    public void testBoardTrain() {
        //Train.make("red").debugPrint();
        
        // mbta.debugPrintState();

        Log log = new Log();
        MBTA mbta = new MBTA();

        mbta.addLine("red", new ArrayList<String>(Arrays.asList("Lechmere", "Park", "Penny")));
        mbta.addJourney("Bob", new ArrayList<String>(Arrays.asList("Lechmere", "Park"))); 
                
        log.passenger_boards(Passenger.make("Bob"), Train.make("red"), Station.make("Lechmere"));
        log.train_moves(Train.make("red"), Station.make("Lechmere"), Station.make("Park"));
        log.passenger_deboards(Passenger.make("Bob"), Train.make("red"), Station.make("Park"));
        
        Verify.verify(mbta, log);
        //Train.make("red").debugPrint();
    }
}