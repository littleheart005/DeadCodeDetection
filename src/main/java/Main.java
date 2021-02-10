import DeadVariable.DeadVariableDetector;
import TokenGenerator.ConstructFileToken;
import Util.ASTParser;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Main {

    public static void main(String[] args) throws IOException {

        /*String source = "/Users/Peeradon/Documents/DesignPatternCode/HF_DP/src/TestElapse/combined";

        // ===================== Dead Class & Dead Interface =======================
        long start = System.currentTimeMillis();

        ASTParser astParser = new ASTParser(source);

        DeadClassWithAST deadClassWithAST= new DeadClassWithAST(astParser.cu);
        deadClassWithAST.createReport();

        DeadInterfaceDetector deadInterfaceDetector = new DeadInterfaceDetector(astParser.cu);
        deadInterfaceDetector.createReport();

        long end = System.currentTimeMillis();

        float AstTime = (end - start)/1000F;

        // Dead Class Detector with regular expression and line splitting. (Commended all dead interface portion)
        start = System.currentTimeMillis();

        Detector detector = new Detector(source);
        detector.detect();

        end = System.currentTimeMillis();
        float SplitTime = (end - start)/1000F;

        detector.createReport("Sample");

        //System.out.println("\n\nDead class and interface with AST. Total elapse time: "+AstTime+" seconds");
        System.out.println("Dead Class and interface with regex line splitting: Total elapse time: "+SplitTime+" seconds");*/

        String source = "C:\\Users\\birdn\\Desktop\\Project\\openSource\\Arduino-master\\arduino-core\\src";
//        String source = "C:\\Users\\birdn\\Desktop\\Project\\Test Project\\DecoratorPattern";
//        String source = "C:\\Users\\birdn\\Desktop\\Project\\Test Project\\FactoryPattern";
//        String source = "C:\\Users\\birdn\\Desktop\\Project\\Test Project\\MementoPattern\\src";
//        String source = "C:\\Users\\birdn\\Desktop\\Project\\Test Project\\ObserverPattern";
//        String source = "C:\\Users\\birdn\\Desktop\\Project\\Test Project\\StrategyPattern";

        long start = System.currentTimeMillis();

        ASTParser astParser = new ASTParser(source);
        ConstructFileToken constructFileToken = new ConstructFileToken(astParser.cu);
        DeadVariableDetector deadVariableDetector = new DeadVariableDetector(constructFileToken.getFileTokenList());

        long end = System.currentTimeMillis();
        float AstTime = (end - start)/1000F;

        deadVariableDetector.createReport("StrategyPattern");

        System.out.println("Dead Variable with AST. Total elapse time: " + AstTime + " seconds");
    }
}