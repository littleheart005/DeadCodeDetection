import DeadClass_Interface.Detector;

import java.util.concurrent.TimeUnit;

public class Main {

    public static void main(String[] args) {

        String source = "C:\\Users\\Punch\\Desktop\\DeadCode_workspace\\run_test\\src\\strategypattern";

        Detector detector = new Detector(source);
        detector.detect();
        detector.createReport("StrategyPattern");


    }

}