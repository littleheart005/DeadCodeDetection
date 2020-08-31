import DeadClass.DeadClass_Detector;

import javax.xml.bind.JAXBException;

public class Main {

    public static void main(String[] args) throws JAXBException {

        //========================== Working portion ==============================

        String source = "D:\\Year 3\\Software Design\\PreTest\\src";

        DeadClass_Detector detector = new DeadClass_Detector(source);



        /* Testing writer
        Writer_Demo demo = new Writer_Demo();
        demo.write(); */

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