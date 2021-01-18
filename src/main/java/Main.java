import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;

import DeadReturn.DeadReturnDetector;
import DeadVariable.VariableNameDetector;
import Util.ASTParser;

public class Main {

    public static void main(String[] args) throws FileNotFoundException {

        /*String source = "/Users/Peeradon/Documents/DesignPatternCode/HF_DP/src/headfirst/command/party/Light.java";

        DeadReturnDetector deadReturnDetector = new DeadReturnDetector(source);

        deadReturnDetector.printMethodName();*/

        String birdSource = "C:\\Users\\birdn\\Desktop\\Project\\new test project\\StrategyPattern";
        ASTParser astParser = new ASTParser(birdSource);
        VariableNameDetector variableNameDetector = new VariableNameDetector(astParser.cu);

//        DeadVariableDetector deadVariableDetector = new DeadVariableDetector(source);
//        Detector detector = new Detector(source);
//        detector.detect();
//        System.out.println();
//        System.out.println(" ================ Dead Class/Interface =============");
//        detector.createReport("Test");



    }

}