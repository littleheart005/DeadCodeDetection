package DeadClass;

import Path_Reader.Path_Reader;

import java.io.File;
import java.io.FileNotFoundException;
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

    private  List<String> paths;
    private  List<String> names;
    private  Map<String, Boolean> maps;

    public DeadClass_Detector(String source){

        // Create reader
        Path_Reader reader = new Path_Reader();

        // Store file paths
        paths = reader.readPath(source);

        // Store class/interface names
        names = readFile_Name(paths);
        /*
        System.out.println("************** Classes name ******************");
        for(String p : names){
            System.out.println(p);
        }*/

        // Map name from String to map
        maps = toMap(names);

    }

    public Map<String, Boolean> toMap(List<String> strings){

        Map<String, Boolean> maps = new LinkedHashMap<>();
        for (String string : strings) {
            maps.put(string, false);
        }
         /*
        System.out.println("========== Linked Hash Maps ========");
        for(Map.Entry<String ,Boolean> m : maps.entrySet()) {
            System.out.print(m.getKey() + " : ");
            System.out.println(m.getValue());
        }*/

        return maps;
    }

    public List<String> readFile_Name(List<String> paths) {
        /* Next step, this method should return a list 'word' to caller
         * for using in various method */
        List<String> names = new ArrayList<>();
        for(String p : paths){

            File file = new File(p);
            String tmp;
            tmp = getFileName(file);
            names.add(tmp);
        }
        return names;
    }

    public String getFileName(File file){
        return removeExtension(file.getName());
    }

    public static String removeExtension(String file){
        return file.replaceFirst("[.][^.]+$", "");
    }


    private int[] count;
    private List<String> result;

    private LineResult lineResult;

    public void detect(){

        // initialize count size and value
        count = new int[names.size()];
        for (int i : count){
            i = 0;
        }

        // initialize result
        result = new ArrayList<>();

        // initialize object for storing data
        lineResult = new LineResult();

        // Looping
        for(String p : paths){ // Loop through each file
            File file = new File(p);
            try {
                Scanner sc = new Scanner(file);
                String line;

                var className = new ClassName(); // create object for storing result

                while (sc.hasNextLine()){
                    line = sc.nextLine();
                    for (int i=0;i<names.size();i++){
                        if(line.contains(names.get(i))){
                            count[i]++;
                            result.add(names.get(i)+" --> "+line+" --> "+file.getName());
                        }
                    }
                }
                sc.close();
            }catch (FileNotFoundException e){
                System.out.print("Error file not found!! : ");
                e.printStackTrace();
            }
        }

    }

    public List<String> getNames() {
        return names;
    }

    public int[] getCount() {
        return count;
    }

    public List<String> getResult() {
        return result;
    }
}
