import DeadVariable.ConstructComponent;

import DeadClass.DeadClassWithAST;
import DeadClass.Detector;
import DeadInterface.DeadInterfaceDetector;
import DeadVariable.DeadVariableDetector;
import Util.ASTParser;

import java.io.IOException;

public class Main {

    public static void main(String[] args) throws IOException {

        /*String source = "C:\\Users\\Punch\\Desktop\\DeadCode_workspace\\OpenSource\\Arduino\\arduino-core\\src";

        // ===================== Dead Class & Dead Interface =======================
        long start = System.currentTimeMillis();

        ASTParser astParser = new ASTParser(source);

        DeadClassWithAST deadClassWithAST= new DeadClassWithAST(astParser.cu);
        deadClassWithAST.printMap();
        deadClassWithAST.printDeadClass();
        deadClassWithAST.createReport("Arduino_2");

        DeadInterfaceDetector deadInterfaceDetector = new DeadInterfaceDetector(astParser.cu);
        deadInterfaceDetector.printMap();
        deadInterfaceDetector.printDeadInterface();
        deadInterfaceDetector.createReport("Arduino");

        long end = System.currentTimeMillis();
        float AstTime = (end - start)/1000F;

        // Dead Class Detector with regular expression and line splitting. (Commended all dead interface portion)
        start = System.currentTimeMillis();

        Detector detector = new Detector(source);
        detector.detect();

        end = System.currentTimeMillis();
        float SplitTime = (end - start)/1000F;

        detector.createReport("ArduinoDetection");

        System.out.println("\n\nDead class and interface with AST. Total elapse time: "+AstTime+" seconds");
        System.out.println("Dead Class and interface with regex line splitting: Total elapse time: "+SplitTime+" seconds");*/


        String source = "C:\\Users\\birdn\\Desktop\\Project\\Test Project\\StrategyPattern";
        ASTParser astParser = new ASTParser(source);
        ConstructComponent constructComponent = new ConstructComponent(astParser.cu, astParser.location);
        DeadVariableDetector deadVariableDetector = new DeadVariableDetector(constructComponent.getComponentList());
        deadVariableDetector.createReport("StrategyPattern");
    }
}