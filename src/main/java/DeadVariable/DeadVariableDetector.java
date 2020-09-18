package DeadVariable;

import Path_Reader.Path_Reader;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.StringTokenizer;

public class DeadVariableDetector {
    private List<String> paths;
    private List<File> files;
    private List<String> variableName = new ArrayList<>();

    public DeadVariableDetector(String source) throws FileNotFoundException {
        Path_Reader pathReader = new Path_Reader();
        paths = pathReader.readPath(source);
        files = readFile(paths);

        for (int i=0; i<files.size(); i++){
            System.out.println("file name : " + files.get(i).getName());
            readVariableFromFile(files.get(i));
        }
    }

    public List<File> readFile(List<String> paths) {
        List<File> files = new ArrayList<>();
        for(String p : paths){
            File file = new File(p);
            files.add(file);
        }
        return files;
    }

    public void readVariableFromFile(File file) throws FileNotFoundException {
        Integer i1 = 1;
        Scanner scanner1 = new Scanner(file);
        while (scanner1.hasNextLine()){
            System.out.println("line " + i1 + " " + scanner1.nextLine());
            i1++;
        }
        System.out.println();

        char[] function = new char[] {'(',')'};

        Integer i = 1;
        Scanner scanner = new Scanner(file);
        while(scanner.hasNextLine()){
            String temp = scanner.nextLine();

            //check if line contains 'function'
            if (containsAny(temp, function)){
                System.out.println("line " + i + " found 'function'");
                i++;
            }

            //check if line is class / interface
            else if (temp.contains("class") || temp.contains("interface")){
                System.out.println("line " + i + " found 'class' // 'interface'");
                i++;
            }

            //check if line contains 'datatype'
            else if (temp.contains("String") || temp.contains("char") || temp.contains("Integer") || temp.contains("int") ||
                    temp.contains("byte") || temp.contains("short") || temp.contains("long") || temp.contains("float") ||
                    temp.contains("double") || temp.contains("boolean")){
                System.out.println("line " + i + " found 'datatype'");

                StringTokenizer token = new StringTokenizer(temp);
                List<String> str = new ArrayList<>();

                for (int j=0; token.hasMoreTokens(); j++){
                    str.add(token.nextToken());
                }

                for (int j=0; j<str.size(); j++){
                    if (str.get(j).equals("private") || str.get(j).equals("public") || str.get(j).equals("protected")){
                        str.remove(j);
                    }
                }

                variableName.add(str.get(1).substring(0, str.get(1).length()-1));
                i++;
            }

            //check if line is space
            else {
                i++;
            }
        }
        scanner.close();
    }

    public static boolean containsAny(String str, char[] searchChars) {
        if (str == null || str.length() == 0 || searchChars == null || searchChars.length == 0) {
            return false;
        }
        for (int i = 0; i < str.length(); i++) {
            char ch = str.charAt(i);
            for (int j = 0; j < searchChars.length; j++) {
                if (searchChars[j] == ch) {
                    return true;
                }
            }
        }
        return false;
    }

    public List<String> getVariableName(){
        return this.variableName;
    }

}
