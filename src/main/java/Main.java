import DeadClassInterface.*;
import Util.ASTParser;
import java.io.IOException;

public class Main {

    public static void main(String[] args) throws IOException {

        String openSource = "/Users/Peeradon/Documents/OpenSourceProject/DetectedProject/Arduino-1.8.13/arduino-core";

//       Dead Class/Interface Detector
        long start = System.currentTimeMillis();

        ASTParser astParser = new ASTParser(openSource);
        DeadClassInterfaceDetector deadClassInterfaceDetector = new DeadClassInterfaceDetector(astParser.cu);
        deadClassInterfaceDetector.detect();

        long end = System.currentTimeMillis();
        float AstTime = (end - start)/1000F;

        deadClassInterfaceDetector.setReportName("Arduino");
        deadClassInterfaceDetector.createReport(AstTime);
        System.out.println("\n\nDead class and interface total elapse time: "+AstTime+" seconds");



        // Dead Class Detector with regular expression and line splitting. (Commended all dead interface portion)
//        start = System.currentTimeMillis();
//        Detector detector = new Detector(source2);
//        detector.detect();
//        end = System.currentTimeMillis();
//        float SplitTime = (end - start)/1000F;
//        detector.createReport("Sample");
//        System.out.println("Dead Class and interface with regex line splitting: Total elapse time: "+SplitTime+" seconds");
    }
}