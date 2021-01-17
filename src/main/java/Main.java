import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;

import DeadReturn.DeadReturnDetector;

public class Main {

    public static void main(String[] args){

        String source = "/Users/Peeradon/Documents/DesignPatternCode/HF_DP/src/headfirst/command/party/Light.java";

        DeadReturnDetector deadReturnDetector = new DeadReturnDetector(source);

        deadReturnDetector.printMethodName();



//        DeadVariableDetector deadVariableDetector = new DeadVariableDetector(source);
//        Detector detector = new Detector(source);
//        detector.detect();
//        System.out.println();
//        System.out.println(" ================ Dead Class/Interface =============");
//        detector.createReport("Test");



    }

}