import DeadClass_Interface.Detector;

import java.util.concurrent.TimeUnit;

public class Main {

    public static void main(String[] args) {

        String source = "C:\\Users\\birdn\\Desktop\\Project\\Test Project\\Decorator";

        Detector detector = new Detector(source);
        detector.detect();
        //detector.printFound();
        detector.createReport("Test");

    }

}