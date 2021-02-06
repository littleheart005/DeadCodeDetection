import DeadClass.*;
import Util.ASTParser;
import java.io.IOException;

public class Main {

    public static void main(String[] args) throws IOException {
        String openSource = "/Users/Peeradon/Documents/OpenSourceProject/iceberg-apache-iceberg-0.11.0/core/src/main";
        String source = "/Users/Peeradon/Documents/DesignPatternCode/HF_DP/src/TestElapse/combined";

        long start = System.currentTimeMillis();
        ASTParser astParser = new ASTParser(openSource);
        DeadClassInterfaceDetector deadClassInterfaceDetector = new DeadClassInterfaceDetector(astParser.cu);
        deadClassInterfaceDetector.detect();
        long end = System.currentTimeMillis();
        float AstTime = (end - start)/1000F;

        deadClassInterfaceDetector.createReport(AstTime);

        // Dead Class Detector with regular expression and line splitting. (Commended all dead interface portion)
//        start = System.currentTimeMillis();
//        Detector detector = new Detector(source);
//        detector.detect();
//        end = System.currentTimeMillis();
//        float SplitTime = (end - start)/1000F;
//        detector.createReport("Sample");

        System.out.println("\n\nDead class and interface with AST. Total elapse time: "+AstTime+" seconds");
//        System.out.println("Dead Class and interface with regex line splitting: Total elapse time: "+SplitTime+" seconds");

//        String source = "C:\\Users\\birdn\\Desktop\\Project\\Test Project\\MementoPattern\\src";
//        ASTParser astParser = new ASTParser(source);
//        ConstructToken constructToken = new ConstructToken(astParser.cu, astParser.location);
//        DeadVariableDetector deadVariableDetector = new DeadVariableDetector(constructToken.getComponentList());
//        deadVariableDetector.createReport("MementoPattern");
    }
}