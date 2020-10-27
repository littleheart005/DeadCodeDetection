import DeadClass.Detector;

import javax.xml.bind.JAXBException;
import java.io.FileNotFoundException;

public class Main {

    public static void main(String[] args) throws JAXBException, FileNotFoundException {

        String source = "D:\\work\\PreTest\\src\\FactoryPattern";

        Detector detector = new Detector(source);

        detector.detect();

    }

}