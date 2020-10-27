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

    //
    private final Map<String,Integer> classDeclareLine = new HashMap<>();
    private final Map<String,Integer> interfaceDeclareLine = new HashMap<>();


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


    public void detect(){
        for (String path : paths){
            File file = new File(path); // create a file

            try {
                Scanner sc = new Scanner(file);
                int i = 1;

                while (sc.hasNext()){   // read a line

                    Map<Integer,String> loc = new HashMap<>();
                    loc.put(i,sc.nextLine());

                    getClassInt(sc.nextLine(),i);

                    searchClass(i,loc.get(i),path); // sending no.of line ,line and path to searching method
                    searchInterface(i,loc.get(i),path);
                    i = i+1;
                }
                sc.close();
            }catch (FileNotFoundException e){
                e.printStackTrace();
            }
        }

    }

    private void searchClass(int number,String line,String path) {
        // Checking a line with 5 cases condition
        for (Map.Entry<String,Integer> map : classResult.entrySet()) {

            caseI(map.getKey(),number,line,classResult,path);

            caseII(map.getKey(),number,line,classResult,path);

            caseIII(map.getKey(),number,line,classResult,path);

            caseIV(map.getKey(),number,line,classResult,path);

            caseV(map.getKey(),number,line,classResult,path);

        }
    }

    private void searchInterface(int number,String line,String path) {
        // Checking a line with 5 cases condition
        for (Map.Entry<String,Integer> map : interfaceResult.entrySet()) {

            caseI(map.getKey(),number,line,interfaceResult,path);

            caseII(map.getKey(),number,line,interfaceResult,path);

            caseIII(map.getKey(),number,line,interfaceResult,path);

            caseIV(map.getKey(),number,line,interfaceResult,path);

            caseV(map.getKey(),number,line,interfaceResult,path);

        }
    }

    private void caseI(String name, int number,String line,Map<String,Integer> map,String path){
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
            /*System.out.println("Case I (Extend) --> Found "+name+" at line : "+number+" in "+path);
            System.out.println(line);*/
            increaseCount(map,name);
        }

        // Checking for implements
        pattern = Pattern.compile(implementPattern);
        matcher = pattern.matcher(line);
        while (matcher.find()){
            /*System.out.println("Case I (Implement) --> Found "+name+" at line : "+number+" in "+path);
            System.out.println(line);*/
            increaseCount(map,name);
        }
    }

    private void caseII(String name,int number, String line,Map<String,Integer> map,String path){
        /*
            Case II:
            - Checking if the name is used for type declaration ex. Pizza pizza;
            - Checking if the name is used to create method ex. public Pizza orderPizza();
        */

        // PizzaStore ny = new NYPizzaStore();

        // Define regex patterns
        String typePattern = "^[\\s]*[^/]*"+name+"\\s[^)]+;";     // pattern for type declaration
        String methodPattern = name+".+\\(.*\\)";       // pattern for method creation


        // Checking Type declaration
        Pattern pattern = Pattern.compile(typePattern);
        Matcher matcher = pattern.matcher(line);
        while (matcher.find() && !line.contains("abstract")){
           /* System.out.println("Case II (Type) --> Found "+name+" at Line : "+number+" in "+path);
            System.out.println(line);*/
            increaseCount(map,name);
        }

        // Checking Method
        pattern = Pattern.compile(methodPattern);
        matcher = pattern.matcher(line);
        // Checking if the line contains "class" or "="
        while (matcher.find() && !line.contains("class") && !line.contains("=")) {
           /* System.out.println("Case II (Method) --> Found "+name+" at Line : "+number+" in "+path);
            System.out.println(line);*/
            increaseCount(map,name);
        }

    }

    private void caseIII(String name,int number, String line,Map<String,Integer> map,String path){
        /*
            Case III : Checking object creation ex. Pizza pizza = new Pizza();
        */
        // PizzaStore ny = new NYPizzaStore();

        // Define pattern
        String objPattern = "=\\s*new\\s*"+name+"(.*);"; //ex. Pizza pizza = new Pizza();
        String objAssPattern = "^[\\s]*"+name+"\\s+.+\\s*=\\s*.+\\(.*\\);"; // ex. Pizza pizza = ny.orderPizza("cheese");

        // Checking for object creation
        Pattern pattern = Pattern.compile(objPattern);
        Matcher matcher = pattern.matcher(line);
        while (matcher.find() && line.contains("new")){
           /* System.out.println("Case III (Object) --> Found "+name+" at Line : "+number+" in "+path);
            System.out.println(line);*/
            increaseCount(map,name);
        }

        pattern = Pattern.compile(objAssPattern);
        matcher = pattern.matcher(line);
        while (matcher.find() && !line.contains("return")){
           /* System.out.println("Case III (Object with Initialize) --> Found "+name+" at Line : "+number+" in "+path);
            System.out.println(line);*/
            increaseCount(map,name);
        }
    }

    private void caseIV(String name,int number, String line,Map<String,Integer> map,String path) {
        /* Case IV
            - Checking if the name is used for parameter's type.  ex. public CurrentCondition(Subject Weather)
        */

        String paraTypePattern = ".*\\("+name+".+\\)"; //pattern for parameter's type

        // Checking parameter type
        Pattern pattern = Pattern.compile(paraTypePattern);
        Matcher matcher = pattern.matcher(line);
        while (matcher.find()){
           /* System.out.println("Case IV (Parameter) --> Found "+name+" at Line : "+number+" in "+path);
            System.out.println(line);*/
            increaseCount(map,name);
        }
    }

    private void caseV(String name,int number, String line,Map<String,Integer> map,String path) {
        /* Case IV
            - Checking if the name is used for Array,List or other data collections.
                ex. private ArrayList<Observer>observers;
        */

        String arrayPattern = ".+<"+name+">.+;"; //pattern for parameter's type

        // Checking parameter type
        Pattern pattern = Pattern.compile(arrayPattern);
        Matcher matcher = pattern.matcher(line);
        while (matcher.find()){
           /* System.out.println("Case V (DataCollection) --> Found "+name+" at Line : "+number+" in "+path);
            System.out.println(line);*/
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
            System.out.print(m.getKey() + " : "+m.getValue());
            if(m.getValue()==0){
                System.out.print("\t\tClass "+m.getKey()+" has no reference.");
            }
            System.out.println();
        }
        System.out.println("\n ========== Interfaces ===========");
        for(Map.Entry<String ,Integer> m : interfaceResult.entrySet()) {
            System.out.print(m.getKey() + " : "+m.getValue());
            if(m.getValue()==0){
                System.out.print("\t\tInterface "+m.getKey()+" has no reference.");
            }
            System.out.println();
        }
    }

    private void increaseCount(Map<String,Integer> map, String key){
        map.put(key,map.get(key)+1);
    }

    // ===========================================================================

    private void getClassInt(String line, int number){

        String classPattern = "^[\\s]*.+class\\s+.+";
        String interfacePattern = "^[\\s]*.+interface\\s+.+";

        Pattern pattern = Pattern.compile(classPattern); // create pattern object to use regex
        Matcher matcher = pattern.matcher(line); // create matcher object to match regex with line

        while (matcher.find()){
            classDeclareLine.put(line,number);
        }

        pattern = Pattern.compile(interfacePattern);
        matcher = pattern.matcher(line);
        while (matcher.find()){
            interfaceDeclareLine.put(line,number);
        }

    }

    // ===========================================================================

}
