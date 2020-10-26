package DeadClass;

import PathReader.PathReader;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Detector {

    private final List<String> paths;

    // Map for storing dead class/interface name and count result Ex. <Main:0>  key = name , value = count
    private final Map<String,Integer> classResult = new HashMap<>();
    private final Map<String,Integer> interfaceResult = new HashMap<>();

    // Map for storing number of line and line


    public Detector(String source) {
        PathReader reader = new PathReader();
        // Store files paths
        paths = reader.readPath(source);

        readFile_Name(paths);
    }

    private void readFile_Name(List<String> paths) {
        for(String p : paths){
            File file = new File(p);
            try {
                Scanner sc = new Scanner(file);
                while (sc.hasNext()) {   // read a line
                    String line = sc.nextLine();
                    getClassInterfaceName(line, file);
                }
                sc.close();
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
        }
    }

    public void  detect() {
        // Read files using paths
        for (String path : paths){
            File file = new File(path); // create a file
            try {
                Scanner sc = new Scanner(file);
                while (sc.hasNext()){   // read a line
                    //Working portion
                    String line = sc.nextLine();
                    searchDeadClass(line);
                    searchDeadInterface(line);
                }
                sc.close();
            }catch (FileNotFoundException e){
                e.printStackTrace();
            }
        }

    }

    // =======================================================================================

    public void testLoc(){
        for (String path : paths){
            File file = new File(path); // create a file
            System.out.println(path+"\n");
            try {
                Scanner sc = new Scanner(file);
                int i = 1;

                while (sc.hasNext()){   // read a line

                    Map<Integer,String> loc = new HashMap<>();
                    loc.put(i,sc.nextLine());
                    i = i+1;

                    for(Map.Entry<Integer,String> m : loc.entrySet()){
                        System.out.println("line : "+m.getKey()+" --> "+m.getValue());
                    }
                }
                sc.close();
            }catch (FileNotFoundException e){
                e.printStackTrace();
            }
            System.out.println();
        }
    }

    // =============================================================================================

    private void searchDeadClass(String line) {
        // Checking a line with 3 cases condition
        for (Map.Entry<String,Integer> map : classResult.entrySet()) {
            // Case I : implements or extends
            caseI(map.getKey(),line,classResult);

            caseII(map.getKey(),line,classResult);

            caseIII(map.getKey(),line,classResult);
        }
    }

    private void searchDeadInterface(String line) {
        // Checking a line with 3 cases condition
        for (Map.Entry<String,Integer> map : interfaceResult.entrySet()) {
            // Case I : implements or extends
            caseI(map.getKey(),line,interfaceResult);

            caseII(map.getKey(),line,interfaceResult);

            caseIII(map.getKey(),line,interfaceResult);
        }
    }


    /* Additional cases to be count
    --> private Subject weatherData; --> Subject
    --> public CurrentCondition(Subject Weather) --> Subject
    --> private ArrayList<Observer>observers;  --> Observer
     */


    private void caseI(String name, String line,Map<String,Integer> map){
        /*
            Case I : Checking for using name as a implementation ex. implements , extends
        */
        // Define regex patterns
        String extendPattern = ".*extends\\s"+name+"\\s.*";
        String implementPattern = ".*implements.*"+name+"\\s.*";

        // Checking for extends
        Pattern pattern = Pattern.compile(extendPattern); // create pattern object to use regex
        Matcher matcher = pattern.matcher(line); // create matcher object to match regex with line
        while (matcher.find()){
            System.out.println("Case I --> Found "+name+" at : "+line);
            increaseCount(map,name);
        }

        // Checking for implements
        pattern = Pattern.compile(implementPattern);
        matcher = pattern.matcher(line);
        while (matcher.find()){
            System.out.println("Case I --> Found "+name+" at : "+line);
            increaseCount(map,name);
        }
    }

    private void caseII(String name, String line,Map<String,Integer> map){
        /*
            Case II:
            - Checking if the name is used for type declaration ex. Pizza pizza;
            - Checking if the name is used to create method ex. public Pizza orderPizza();
        */

       // PizzaStore ny = new NYPizzaStore();

        // Define regex patterns
        String typePattern = "^[\\s]*[^/]*"+name+"\\s.+;";     // pattern for type declaration
        String methodPattern = name+".+\\(.*\\)";       // pattern for method creation

        // Checking Type declaration
        Pattern pattern = Pattern.compile(typePattern);
        Matcher matcher = pattern.matcher(line);
        while (matcher.find() && !line.contains("abstract")){
            System.out.println("Case II (Type) --> Found "+name+" at : "+line);
            increaseCount(map,name);
        }

        // Checking Method
        pattern = Pattern.compile(methodPattern);
        matcher = pattern.matcher(line);
        // Checking if the line contains "class" or "="
        while (matcher.find() && !line.contains("class") && !line.contains("=")) {
            System.out.println("Case II (Method) --> Found " + name + " at : " + line);
            increaseCount(map,name);
        }
    }

    private void caseIII(String name, String line,Map<String,Integer> map){
        /*
            Case III : Checking object creation ex. Pizza pizza = new Pizza();
        */

        // Define pattern
        String objPattern = "=\\s*new\\s*"+name+"(.*);";


        // Checking for object creation
        Pattern pattern = Pattern.compile(objPattern);
        Matcher matcher = pattern.matcher(line);
        while (matcher.find()){
            System.out.println("Case III --> Found "+name+" at : "+line);
            increaseCount(map,name);
        }
    }


    public void getClassInterfaceName(String line, File file){
        if(line.contains("class")){
            classResult.put(getFileName(file),0);
        }
        if(line.contains("interface")){
            interfaceResult.put(getFileName(file),0);
        }
    }

    private String getFileName(File file){
        return removeExtension(file.getName());
    }

    private static String removeExtension(String file){
        return file.replaceFirst("[.][^.]+$", "");
    }

    public void printMap(){

        System.out.println("\n ========== Classes ===========");
        for(Map.Entry<String ,Integer> m : classResult.entrySet()) {
            //System.out.print(m.getKey() + " : "+m.getValue());
            if(m.getValue()==0){
                System.out.println("Class "+m.getKey()+" has no reference.");
            }
        }

        System.out.println("\n ========== Interfaces ===========");
        for(Map.Entry<String ,Integer> m : interfaceResult.entrySet()) {
            //System.out.print(m.getKey() + " : "+m.getValue());
            if(m.getValue()==0){
                System.out.println("Interface "+m.getKey()+" has no reference.");
            }
        }

    }

    private void increaseCount(Map<String,Integer> map, String key){
        map.put(key,map.get(key)+1);
    }


}
