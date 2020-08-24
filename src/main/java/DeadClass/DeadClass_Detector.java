package DeadClass;

import Files_Reader.Dead_Class_Reader;
import java.util.*;

public class DeadClass_Detector {

    /* Steps for detecting dead class/interface
     - read file --> Done !!
     - get class/interface name ( Except "main" ) --> Done !!
     - create map for storing result <(String)Class_name, (Boolean)Deadcode?> --> Done !!

     - check through every file except itself :
        case I : if count = 0 --> Dead!
        case II : if count >= 1 --> Alive.

        *** don't count when the name is after word "import" or "package"
    */

    public DeadClass_Detector(String source){

        // Create reader
        Dead_Class_Reader reader = new Dead_Class_Reader();

        // Store path
        List<String> paths = reader.readPath(source);

        // Store name
        List<String> names = reader.readFile_Name(paths);

        System.out.println("************** Classes name ******************");
        for(String p : names){
            System.out.println(p);
        }

        // Map name from String to map
        Map<String, Boolean> maps = toMap(names);

    }

    public Map<String, Boolean> toMap(List<String> strings){

        Map<String, Boolean> maps = new LinkedHashMap<>();
        for (String string : strings) {
            maps.put(string, false);
        }

        System.out.println("========== Linked Hash Maps ========");
        for(Map.Entry<String ,Boolean> m : maps.entrySet()) {
            System.out.print(m.getKey() + " : ");
            System.out.println(m.getValue());
        }

        return maps;
    }

    public void detect(){

    }

}
