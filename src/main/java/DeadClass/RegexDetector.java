package DeadClass;

import PathReader.PathReader;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.regex.*;

public class RegexDetector {

    private List<String> paths;
    private List<String> names;
    private int counts[];

    public RegexDetector(String source){
        // Create reader
        PathReader reader = new PathReader();

        // Store file paths
        paths = reader.readPath(source);

        names = readFile_Name(paths);

        counts = new int[names.size()];
        for(int count : counts){
            count = 0;
        }

    }

    private List<String> readFile_Name(List<String> paths) {
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

    private String getFileName(File file){
        return removeExtension(file.getName());
    }

    private static String removeExtension(String file){
        return file.replaceFirst("[.][^.]+$", "");
    }

    public void  detect(){
        for (String path : paths){
            File file = new File(path);
            try {
                Scanner sc = new Scanner(file);
                while (sc.hasNext()){
                    String line = sc.nextLine();
                    searchLines(line);
                }
                sc.close();
            }catch (FileNotFoundException e){
                e.printStackTrace();
            }
        }
        System.out.println("\n");
        for(int i=0;i<names.size();i++){
            System.out.println("Class name: "+names.get(i)+" -> count : "+counts[i]);
        }
    }

    private void searchLines(String line){

        //System.out.println(line);

        //Case I : implements or extends?
        for(String name : names){
            String extendPattern = ".*extends\\s"+name+"\\s.*";
            String implementPattern = ".*implements.*"+name+"\\s.*";

            //create Pattern instance
            Pattern pattern = Pattern.compile(extendPattern);
            //create Matcher instance
            Matcher matcher = pattern.matcher(line);

            //check for extends
            while (matcher.find()){
                System.out.println("Case I --> Found "+name+" at : "+line);
                counts[names.indexOf(name)]++;
            }

            pattern = Pattern.compile(implementPattern);
            matcher = pattern.matcher(line);

            //check for implements
            while (matcher.find()){
                System.out.println("Case I --> Found "+name+" at : "+line);
                counts[names.indexOf(name)]++;
            }

        }

        //Case II : type declaration
        for(String name : names){
            String typePattern = "^\\s*"+name+"\\s.+;";

            Pattern pattern = Pattern.compile(typePattern);
            Matcher matcher = pattern.matcher(line);

            while (matcher.find()){
                System.out.println("Case II (1) --> Found "+name+" at : "+line);
                counts[names.indexOf(name)]++;
            }


            String methodPattern = name+"\\s.+\\(.*\\)"; // --> Still not work!!

            pattern = Pattern.compile(methodPattern);
            matcher = pattern.matcher(line);
            while (matcher.find()){
                System.out.println("Case II (2) --> Found "+name+" at : "+line);
                counts[names.indexOf(name)]++;
            }
        }

        //Case III : object creation
        for(String name : names){
            String objPattern = "new\\s{1}"+name+"(.*);";

            Pattern pattern = Pattern.compile(objPattern);
            Matcher matcher = pattern.matcher(line);

            while (matcher.find()){
                System.out.println("Case III --> Found "+name+" at : "+line);
                counts[names.indexOf(name)]++;
            }
        }



    }

    public int[] getCounts() {
        return counts;
    }

    public List<String> getNames() {
        return names;
    }
}
