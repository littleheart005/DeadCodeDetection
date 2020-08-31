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


    public void detect(){

        for(String p : paths){

            File file = new File(p);
            try {
                Scanner sc = new Scanner(file);
                while (sc.hasNextLine()){
                    String line = sc.nextLine();
                    if (line.contains("import") || line.contains("package") ||
                            line.contains("class") || line.contains("interface")){
                        line = sc.nextLine();
                    }
                    searchForName(line);
                    // words.add(word); // add words to array
                    //searchClassName(words);

                }
                sc.close();
            }catch (FileNotFoundException e){
                System.out.print("Error file not found!! : ");
                e.printStackTrace();
            }
        }

    }

    private void searchForName(String line){
        List<Integer> count = new ArrayList<>();


    }

}
