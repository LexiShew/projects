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
        mbta.loadConfig("sample.json");
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
    public void testAVerifierValid() {
        //Train.make("red").debugPrint();
        mbta.addLine("red", new ArrayList<String>(Arrays.asList("Lechmere", "Park", "Penny")));
        mbta.addJourney("Bob", new ArrayList<String>(Arrays.asList("Lechmere", "Park"))); 
        
        logger.train_moves(Train.make("red"), Station.make("Lechmere"), Station.make("Park"));
        
        Verify.verify(mbta, logger);
    }
    
    @Test
    public void testBoardTrain() {
        //Train.make("red").debugPrint();
        
        // mbta.debugPrintState();
        mbta.addLine("red", new ArrayList<String>(Arrays.asList("Lechmere", "Park", "Penny")));
        mbta.addJourney("Bob", new ArrayList<String>(Arrays.asList("Lechmere", "Park"))); 
                
        logger.passenger_boards(Passenger.make("Bob"), Train.make("red"), Station.make("Lechmere"));
        logger.train_moves(Train.make("red"), Station.make("Lechmere"), Station.make("Park"));
        
        Verify.verify(mbta, logger);
        //Train.make("red").debugPrint();
    }
    
    @Test 
    public void testMoveAlongLine() {   
        logger.train_moves(Train.make("red"), Station.make("Lechmere"), Station.make("Park"));
        logger.train_moves(Train.make("red"), Station.make("Park"), Station.make("Penny"));      
        
        Verify.verify(mbta, logger) ;
    }
    
    @Test
    public void testTwoLines(){
        mbta.addLine("blue", new ArrayList<String>(Arrays.asList("Tufts", "Park", "Dirt")));       
        logger.train_moves(Train.make("red"), Station.make("Lechmere"), Station.make("Park"));
        logger.train_moves(Train.make("red"), Station.make("Park"), Station.make("Penny"));           
        logger.train_moves(Train.make("blue"), Station.make("Tufts"), Station.make("Park"));   
        
        Verify.verify(mbta, logger);
    }

   // @Test 
    public void testInvalidMove() {
        logger.train_moves(Train.make("red"), Station.make("Tufts"), Station.make("Park"));
        // passenger tries to board when train is not at station 
        
        Verify.verify(mbta, logger);
    }
    
    @Test 
    public void testInvalidDeboard() {
        logger.passenger_deboards(Passenger.make("Bob"), Train.make("red"), Station.make("Park"));
        
        Verify.verify(mbta, logger);
    }
    
    @Test
    public void testInvalidBoard() {
        logger.passenger_boards(Passenger.make("Bob"), Train.make("red"), Station.make("Park"));
        
        Verify.verify(mbta, logger);
    }
    
    @Test 
    public void checkFromJson() {
        String name = "sample.json";
        MBTA mbta = new MBTA();
        mbta.loadConfig(name);
    }
}
