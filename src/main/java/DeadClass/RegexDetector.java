package DeadClass;

import PathReader.PathReader;

import java.awt.*;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.*;
import java.util.List;
import java.util.regex.*;

public class RegexDetector {

    private final List<String> paths;
    private final List<String> names;
    private final int[] counts;

    private final List<String> className = new ArrayList<>();
    private final List<String> interfaceName = new ArrayList<>();


    public RegexDetector(String source){
        // Create reader
        PathReader reader = new PathReader();
        // Store files paths
        paths = reader.readPath(source);
        // Get files name
        names = readFile_Name(paths);
        // Create count variable
        counts = new int[names.size()];

    }

    private List<String> readFile_Name(List<String> paths) {

        List<String> names = new ArrayList<>();
        for(String p : paths){

            File file = new File(p);
            String tmp;
            tmp = getFileName(file);
            names.add(tmp);
        }
        return names;
    }

    private String getFileName(File file){
        return removeExtension(file.getName());
    }

    private static String removeExtension(String file){
        return file.replaceFirst("[.][^.]+$", "");
    }

    public void  detect(){
        // Read files using paths
        for (String path : paths){
            File file = new File(path); // create a file
            try {
                Scanner sc = new Scanner(file);
                while (sc.hasNext()){   // read a line
                    String line = sc.nextLine();
                    searchLines(line); // search in a line
                }
                sc.close();
            }catch (FileNotFoundException e){
                e.printStackTrace();
            }
        }

        Map<String,Integer> tmp = new HashMap<>();
        for (int i=0;i<counts.length;i++){
            tmp.put(names.get(i),counts[i]);
        }
        for(Map.Entry<String ,Integer> m : tmp.entrySet()) {
            System.out.print(m.getKey() + " : ");
            System.out.println(m.getValue());
        }

    }

    private void searchLines(String line){
        // Checking a line with 3 cases condition
        for(String name : names){

            // Case I : implements or extends
            caseI(name,line);

            // Case II : type declaration
            caseII(name,line);

            // Case III : object creation
            caseIII(name,line);
        }
    }

    private void caseI(String name, String line){
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
            counts[names.indexOf(name)]++;
        }

        // Checking for implements
        pattern = Pattern.compile(implementPattern);
        matcher = pattern.matcher(line);
        while (matcher.find()){
            System.out.println("Case I --> Found "+name+" at : "+line);
            counts[names.indexOf(name)]++;
        }
    }

    private void caseII(String name, String line){
        /*
            Case II:
            - Checking if the name is used for type declaration ex. Pizza pizza;
            - Checking if the name is used to create method ex. public Pizza orderPizza();
        */

        // Define regex patterns
        String typePattern = "^\\s*"+name+"\\s.+;";     // pattern for type declaration
        String methodPattern = name+".+\\(.*\\)";       // pattern for method creation

        // Checking Type declaration
        Pattern pattern = Pattern.compile(typePattern);
        Matcher matcher = pattern.matcher(line);
        while (matcher.find()){
            System.out.println("Case II (1) --> Found "+name+" at : "+line);
            counts[names.indexOf(name)]++;
        }

        // Checking Method
        pattern = Pattern.compile(methodPattern);
        matcher = pattern.matcher(line);
        // Checking if the line contains "class" or "="
        while (matcher.find() && !line.contains("class") && !line.contains("=")) {
            System.out.println("Case II (2) --> Found " + name + " at : " + line);
            counts[names.indexOf(name)]++;
        }
    }

    private void caseIII(String name, String line){
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
            counts[names.indexOf(name)]++;
        }
    }


    public void printResult(){
        System.out.println("Classes ");
        for (String c : className){
            System.out.println(c);
        }
        System.out.println("================================");
        System.out.println("Intefaces ");
        for (String i : interfaceName){
            System.out.println(i);
        }
    }


    // put this method in path's for loop in detect()

    public void getClassInterfaceName(String line, File file){

        if(line.contains("class")){
            this.className.add(getFileName(file));
        }
        if(line.contains("interface")){
            this.interfaceName.add(getFileName(file));
        }

    }

}
