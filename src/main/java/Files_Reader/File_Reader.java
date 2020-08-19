package Files_Reader;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class File_Reader {

    public List<String> readPath(String source){

        List<String> paths = new ArrayList<>();
        File folder = new File(source);
        File[] files = folder.listFiles();

        for (File file : files) {
            if (file.isDirectory()) { // Checking if the current file is directory or not.
                String path = file.getAbsolutePath();
                readPath(path);
            } else {
                paths.add(file.getAbsolutePath());
            }
        }
        return paths;
    }

    public void readFile(List<String> paths){

        for(String p : paths){

            File file = new File(p);

            try {
                Scanner sc = new Scanner(file);
                while (sc.hasNext()){

                // Override and edit this part to match each dead code type.

                }
            }catch (FileNotFoundException e){
                e.printStackTrace();
            }
        }
    }

}

