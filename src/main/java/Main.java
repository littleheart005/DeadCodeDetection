
import DeadClass.DeadClassWithAST;
import DeadClass.Detector;
import DeadInterface.DeadInterfaceDetector;
import Util.ASTParser;

public class Main {

    public static void main(String[] args) {

        String source = "/Users/Peeradon/Documents/OpenSourceProject/SpringBoot/spring-boot-master/spring-boot-project/spring-boot/src";

        // ===================== Dead Class & Dead Interface =======================

        long start = System.currentTimeMillis();

        ASTParser astParser = new ASTParser(source);

        DeadClassWithAST deadClassWithAST= new DeadClassWithAST(astParser.cu);
        //deadClassWithAST.printMap();
        DeadInterfaceDetector deadInterfaceDetector = new DeadInterfaceDetector(astParser.cu);
        //deadInterfaceDetector.printMap();

        long end = System.currentTimeMillis();
        float AstTime = (end - start)/1000F;


        //Dead Class Detector with regular expression and line splitting. (Commended all dead interface portion)
//        start = System.currentTimeMillis();
//
//        Detector detector = new Detector(source);
//        detector.detect();
//
//        end = System.currentTimeMillis();
//        float SplitTime = (end - start)/1000F;
//
//        detector.createReport("ArduinoDetection");

        deadClassWithAST.printDeadClass();
        deadClassWithAST.createReport("SpringBoot");
        deadInterfaceDetector.printDeadInterface();
        deadInterfaceDetector.createReport("SpringBoot");

        System.out.println("\n\nDead class and interface with AST. Total elapse time: "+AstTime+" seconds");
        //System.out.println("Dead Class and interface with regex line splitting: Total elapse time: "+SplitTime+" seconds");

    }

}