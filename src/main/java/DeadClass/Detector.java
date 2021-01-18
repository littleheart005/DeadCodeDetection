package DeadClass;

import PathReader.PathReader;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Detector {

    private final List<String> paths;

    // Map for storing dead class/interface name and count result Ex. <Main:0>  key = name , value = count
    private final Map<String,Integer> classResult = new HashMap<>();
    private final Map<String,Integer> interfaceResult = new HashMap<>();

    ClassDB classes = new ClassDB();
    InterfaceDB interfaces = new InterfaceDB();


    public Detector(String source) {
        PathReader reader = new PathReader();
        // Store files paths
        paths = reader.readPath(source);

        readFileName(paths);
    }

    private void readFileName(List<String> paths) {
        for(String path : paths){
            File file = new File(path);
            try {
                Scanner sc = new Scanner(file);
                while (sc.hasNext()) {   // read a line
                    String line = sc.nextLine();
                    getClassInterfaceName(line,file,path);
                }
                sc.close();
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
        }
    }

    public void getClassInterfaceName(String line, File file,String path){
        if(line.contains("class")){
            classes.addClassName(getFileName(file));
            classes.addClassPath(path);
            classes.setCount();
        }
        if(line.contains("interface")){
            interfaces.addInterfaceName(getFileName(file));
            interfaces.addInterfacePath(path);
            interfaces.setCount();
        }
    }

    private String getFileName(File file){
        return removeExtension(file.getName());
    }

    private static String removeExtension(String file){
        return file.replaceFirst("[.][^.]+$", "");
    }

    public void detect(){
        for (String path : paths){
            File file = new File(path); // create a file

            try {
                Scanner sc = new Scanner(file);
                int i = 1;
                Map<Integer,String> loc = new HashMap<>(); // keep line of code and line number

                while (sc.hasNext()){   // read a line

                    String tmp = sc.nextLine();

                    loc.put(i,tmp);

                    getClassInt(tmp,i);

                    searchClass(i,loc.get(i),path); // sending no.of line ,line and path to searching method
                    searchInterface(i,loc.get(i),path);
                    i = i+1;
                }
                sc.close();
            }catch (IOException e){
                e.printStackTrace();
            }
        }

    }

    // Getting Class/Interface deaclaration line, both number and line's string.[
    private void getClassInt(String line, int number){

        String classPattern = "^[\\s]*.+class\\s+.+";
        String interfacePattern = "^[\\s]*.+interface\\s+.+";

        Pattern pattern = Pattern.compile(classPattern); // create pattern object to use regex
        Matcher matcher = pattern.matcher(line); // create matcher object to match regex with line

        while (matcher.find()){
            classes.addLine(number);
            classes.addDeclareLine(line);
        }

        pattern = Pattern.compile(interfacePattern);
        matcher = pattern.matcher(line);
        while (matcher.find()){
            interfaces.addLine(number);
            interfaces.addDeclareLine(line);
        }

    }

    private void searchClass(int number,String line,String path) {
        /* Method for searching deadclass.
        Parameter List
        -> number = no. of the line.
        -> line = line.
        -> path = path of the file.
        */
        int index = 0;
        for (String name : classes.getClassName()){

            caseI(name,index,number,line,path,'C');
            caseII(name,index,number,line,path,'C');
            caseIII(name,index,number,line,path,'C');
            caseIV(name,index,number,line,path,'C');
            caseV(name,index,number,line,path,'C');

            index = index + 1;
        }
    }

    private void searchInterface(int number,String line,String path){

        int index = 0;
        for (String name : interfaces.getInterfaceName()){
            caseI(name,index,number,line,path,'I');
            caseII(name,index,number,line,path,'I');
            caseIII(name,index,number,line,path,'I');
            caseIV(name,index,number,line,path,'I');
            caseV(name,index,number,line,path,'I');

            index = index + 1;
        }
    }

    private List<String> foundResult = new ArrayList<>();

    // case I -> extend/implement
    private void caseI(String name,int index,int number,String line,String path,Character type){

        /*Case I : Checking for using name as a implementation ex. implements , extends*/

        // Define regex patterns
        String extendPattern = "(.*extends.+)"+name+"(.*)";
        String implementPattern = "(.*implements.+)"+name+"(.*)";

        // Checking for extends
        Pattern pattern = Pattern.compile(extendPattern); // create pattern object to use regex
        Matcher matcher = pattern.matcher(line); // create matcher object to match regex with line
        while (matcher.find()){
            if (type == 'C'){
                classes.increaseCount(index);
                foundResult.add("class : "+name+" founded case : extend");
            }
        }

        // Checking for implements
        pattern = Pattern.compile(implementPattern);
        matcher = pattern.matcher(line);
        while (matcher.find()){
            if (type == 'I'){
                interfaces.increaseCount(index);
                foundResult.add("interface : "+name+" founded case : implement");
            }
        }
    }

    // case II -> type&method declaration
    private void caseII(String name,int index,int number, String line,String path,Character type){
       /*
        Case II:
        - Checking if the name is used for type declaration ex. Pizza pizza;
        - Checking if the name is used to create method ex. public Pizza orderPizza();
        */

        // Define regex patterns
        String typePattern = "(^[\\s]*[^/\"]*)"+name+"(\\s[^)]+;)";     // pattern for type declaration
        String methodPattern = name+"(.+\\(.*\\))";       // pattern for method creation


        // Checking Type declaration
        Pattern pattern = Pattern.compile(typePattern);
        Matcher matcher = pattern.matcher(line);
        while (matcher.find() && !line.contains("abstract")){
           /* System.out.println("Case II (Type) --> Found "+name+" at Line : "+number+" in "+path);
            System.out.println(line);
            System.out.print("Found case II ");*/
            if (type == 'C'){
                classes.increaseCount(index);
                foundResult.add("class : "+name+" founded case : type declaration");
            }else if (type == 'I'){
                interfaces.increaseCount(index);
                foundResult.add("interface : "+name+" founded case : type declaration");
            }
        }

        // Checking Method
        pattern = Pattern.compile(methodPattern);
        matcher = pattern.matcher(line);
        // Checking if the line contains "class" or "="
        while (matcher.find() && !line.contains("class") && !line.contains("=")) {
           /* System.out.println("Case II (Method) --> Found "+name+" at Line : "+number+" in "+path);
            System.out.println(line);
            System.out.print("Found case II ");*/
            if (type == 'C'){
                classes.increaseCount(index);
                foundResult.add("class : "+name+" founded case : method declaration");
            }else if (type == 'I'){
                interfaces.increaseCount(index);
                foundResult.add("interface : "+name+" founded case : method declaration");
            }
        }

    }

    // case III -> object creation
    private void caseIII(String name,int index,int number, String line,String path,Character type){
        /*
        Case III : Checking object creation ex. Pizza pizza = new Pizza();
        */
        // PizzaStore ny = new NYPizzaStore();

        // Define pattern
        String objPattern = "(new\\s*)"+name+"(.*)"; //ex. Pizza pizza = new Pizza();
        String objAssPattern = "(^[\\s]*)"+name+"(\\s+.+\\s*=\\s*.+\\(.*\\);)"; // ex. Pizza pizza = ny.orderPizza("cheese");

        // Checking for object creation
        Pattern pattern = Pattern.compile(objPattern);
        Matcher matcher = pattern.matcher(line);
        while (matcher.find() && line.contains("new")){
            if (type == 'C'){
                classes.increaseCount(index);
                foundResult.add("class : "+name+" founded case : object creation");
            }else if (type == 'I'){
                interfaces.increaseCount(index);
                foundResult.add("interface : "+name+" founded case : object creation");
            }
        }

        pattern = Pattern.compile(objAssPattern);
        matcher = pattern.matcher(line);
        while (matcher.find() && !line.contains("return")){
            if (type == 'C'){
                classes.increaseCount(index);
                foundResult.add("class : "+name+" founded case : object assignment");
            }else if (type == 'I'){
                interfaces.increaseCount(index);
                foundResult.add("interface : "+name+" founded case : object assignment");
            }
        }
    }

    // case IV -> parameter
    private void caseIV(String name,int index,int number, String line,String path,Character type) {
        /* Case IV
        - Checking if the name is used for parameter's type.  ex. public CurrentCondition(Subject Weather)
        */

        String paraTypePattern = "(.*\\()"+name+"(.+\\))"; //pattern for parameter's type

        // Checking parameter type
        Pattern pattern = Pattern.compile(paraTypePattern);
        Matcher matcher = pattern.matcher(line);
        while (matcher.find()){
            if (type == 'C'){
                classes.increaseCount(index);
                foundResult.add("class : "+name+" founded case : parameter");
            }else if (type == 'I'){
                interfaces.increaseCount(index);
                foundResult.add("interface : "+name+" founded case : parameter");
            }
        }
    }

    // case V -> array
    private void caseV(String name,int index,int number, String line,String path,Character type) {
        /* Case IV
        - Checking if the name is used for Array,List or other data collections.
        ex. private ArrayList<Observer>observers;
        */

        String arrayPattern = "(.+<)"+name+"(>.+;)"; //pattern for parameter's type

        // Checking parameter type
        Pattern pattern = Pattern.compile(arrayPattern);
        Matcher matcher = pattern.matcher(line);
        while (matcher.find()){
           /* System.out.println("Case V (DataCollection) --> Found "+name+" at Line : "+number+" in "+path);
            System.out.println(line)
            System.out.print("Found case V ");*/
            if (type == 'C'){
                classes.increaseCount(index);
                foundResult.add("class : "+name+" founded case : array");
            }else if (type == 'I'){
                interfaces.increaseCount(index);
                foundResult.add("interface : "+name+" founded case : array");
            }
        }
    }

    // Object for create output
    private Output output = new Output();

    // Create text output file
    public void createReport(String fileName){
        output.createFile(fileName);
        output.write(classes,interfaces);
    }

    // Printing founded result with cases
    public void printFound(){
        for (String found : foundResult){
            System.out.println(found);
        }
        System.out.println("");
    }

}
