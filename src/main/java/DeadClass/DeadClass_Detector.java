package DeadClass;

import Files_Reader.Dead_Class_Reader;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class DeadClass_Detector {

    /* Steps for detecting dead class/interface
     - read file
     - get class/interface name ( Except "main" )
     - check through every file except itself :
        case I : if count = 0 --> Dead!
        case II : if count >= 1 --> Alive.

        *** don't count when the name is after word "import" or "package"
    */

    private List<String> paths;
    private List<String> name;

    public DeadClass_Detector(String source){

        Dead_Class_Reader reader = new Dead_Class_Reader();

        System.out.println("============== Path from Reader ===============");

        paths = reader.readPath(source);

        //name = getClassName();

        System.out.println("************** Path in detector ******************");
        for(String p : paths){
            System.out.println(p);
        }

    }

    public List<String> getClassName(){
        List<String> tmpName = new ArrayList<>();
        for(String p : paths) {
            File file = new File(p);
            String tmp;
            tmp = getFileName(file);
            tmpName.add(tmp);
        }
        return tmpName;
    }

    public String getFileName(File file){
        return removeExtension(file.getName());
    }

    public static String removeExtension(String file){
        return file.replaceFirst("[.][^.]+$", "");
    }

    public List<String> getName() {
        return name;
    }

}
