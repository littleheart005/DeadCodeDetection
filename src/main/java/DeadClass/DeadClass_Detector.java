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
    private List<String> tmpResult;
    private List<String> tmpPathName;

    public void detect(){

        // initialize count size and value
        count = new int[names.size()];
        for (int i : count){
            i = 0;
        }

        // initialize tmpResult and tmpPathName for storing first search results
        tmpResult = new ArrayList<>();
        tmpPathName = new ArrayList<>();

        // Looping
        for(String p : paths){ // Loop through each file
            File file = new File(p);
            try {
                Scanner sc = new Scanner(file);
                String line;
                while (sc.hasNextLine()){
                    line = sc.nextLine();
                    searchLines(line); // Search in each line again to check for exactly name match
                }
                sc.close();
            }catch (FileNotFoundException e){
                System.out.print("Error file not found!! : ");
                e.printStackTrace();
            }
        }
    }

    // Method for finding exactly class/interface name in each line that are founded in detect()
    private void searchLines(String line){

        // split line into a word
        List<String> words = new ArrayList<>();
        try{
            Scanner sc = new Scanner(line);
            while (sc.hasNext()){
                words.add(sc.next());
            }
        }catch (StringIndexOutOfBoundsException e){
            e.printStackTrace();
        }

        // Search for class/interface name in words
        for(String name : names){
            String obj = name+"();";
            for (int i=0;i<words.size();i++){
                if(i==0){ // Check if the first word of the line is a class/interfaec name?
                    if(words.get(i).matches(name)){
                        count[names.indexOf(name)]++;
                        System.out.println(name + " in line ->  "+line+"  i = "+i+" --> if i=0");
                    }
                }
                else{ // Check the rest words of the line
                    // Check if it is a class or interface declaration
                    if (words.get(i).matches(name)){
                        if(!words.get(i-1).matches("class") && !words.get(i-1).matches("interface")){
                            count[names.indexOf(name)]++;
                            System.out.println(name + " in line ->  "+line+"  i = "+i+" -> if class/interface");
                        }
                    }
                    // Check for the object declarlation
                    if (words.get(i-1).matches("new")){
                        if(words.get(i).contains(name)){
                            count[names.indexOf(name)]++;
                            System.out.println(name + " in line ->  "+line+"  i = "+i+" -> if object");
                        }
                    }
                }
            }
        }

    }

    public List<String> getNames() {
        return names;
    }

    public int[] getCount() {
        return count;
    }

    public List<String> getTmpResult() {
        return tmpResult;
    }


}
