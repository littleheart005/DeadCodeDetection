import DeadClass.DeadClassDetector;
import DeadVariable.DeadVariableDetector;

import javax.xml.bind.JAXBException;
import java.io.FileNotFoundException;
import java.util.List;

public class Main {

    public static void main(String[] args) throws JAXBException, FileNotFoundException {

        //========================== Working portion ==============================

        String source = "D:\\Year 3\\Software Design\\PreTest\\src\\FactoryPattern";

        /*DeadClassDetector detector = new DeadClassDetector(source);
        detector.detect();


        int[] count = detector.getCount();
        System.out.println("Count length : "+count.length);
        List<String> names = detector.getNames();
        System.out.println("Name size : "+names.size());


        for(int i=0;i<names.size();i++){
            System.out.println("Class name: "+names.get(i)+" -> count : "+count[i]);
        }*/

        //========================== Working portion ==============================

        String source1 = "D:\\intelliJ\\TestProject\\src";
        DeadVariableDetector detector1 = new DeadVariableDetector(source);
        System.out.println("Variable name after reading all files in directory");
        System.out.println(detector1.getVariableName());

        // =========================  Testing portion ===============================

        /*Map<String, Boolean> map = new HashMap<>();
        map.put("Duck",false);
        map.put("Chicken",false);
        map.put("Cat",true);




        // Map traversal
        for(Map.Entry<String ,Boolean> m : map.entrySet()){
            System.out.print(m.getKey()+" : ");
            System.out.println(m.getValue());
        } */


    }

}