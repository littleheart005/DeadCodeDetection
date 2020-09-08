import DeadClass.DeadClass_Detector;
import File_Writer.Writer_Demo;

import javax.xml.bind.JAXBException;
import java.util.ArrayList;
import java.util.List;

public class Main {

    public static void main(String[] args) throws JAXBException {

        //========================== Working portion ==============================

        String source = "D:\\Year 3\\Software Design\\PreTest\\src\\FactoryPattern";

        DeadClass_Detector detector = new DeadClass_Detector(source);
        detector.detect();


        int[] count = detector.getCount();
        System.out.println("Count length : "+count.length);
        List<String> names = detector.getNames();
        System.out.println("Name size : "+names.size());


        for(int i=0;i<names.size();i++){
            System.out.println("Class name: "+names.get(i)+" -> count : "+count[i]);
        }
        System.out.println();
        List<String> result = detector.getResult();
        for(String re : result){
            System.out.println(re);
        }


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