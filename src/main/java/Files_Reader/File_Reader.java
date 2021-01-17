package Files_Reader;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class File_Reader {
    private List<String> paths = new ArrayList<>();

    public List<String> readPath(String source){

        File folder = new File(source);

        if(!folder.isDirectory()){
            this.paths.add(source);
            return this.getPaths();
        }

        File[] files = folder.listFiles();

        for (File file : files) {
            if (file.isDirectory()) { // Checking if the current file is directory or not.
                String path = file.getAbsolutePath();
                readPath(path);
            } else {
                paths.add(file.getAbsolutePath());
            }
        }
        return this.getPaths();
    }

    public List<File> readFile(List<String> paths) throws FileNotFoundException {
        List<File> files = new ArrayList<>();
        for(String p : paths){
            File file = new File(p);
            files.add(file);
        }
        return files;
    }

    public List<String> getPaths() {
        return paths;
    }
}

