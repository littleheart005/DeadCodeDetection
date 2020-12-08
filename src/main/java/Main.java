import DeadClass_Interface.Detector;
import DeadVariable.DeadVariableDetector;

import java.io.FileNotFoundException;
import java.util.concurrent.TimeUnit;

public class Main {

    public static void main(String[] args) throws FileNotFoundException {

        String source = "C:\\Users\\birdn\\Desktop\\Project\\Test Project\\Decorator";

        DeadVariableDetector deadVariableDetector = new DeadVariableDetector(source);
        Detector detector = new Detector(source);
        detector.detect();
        System.out.println();
        System.out.println(" ================ Dead Class/Interface =============");
        detector.createReport("Test");

    }

}