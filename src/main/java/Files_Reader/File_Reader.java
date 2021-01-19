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

        if(!folder.isDirectory() && source.matches(".+[.]java")){
            this.paths.add(source);
        }else {
            File[] files = folder.listFiles();

            for (File file : files) {
                if (file.isDirectory()) { // Checking if the current file is directory or not.
                    String path = file.getAbsolutePath();
                    readPath(path);
                } else {
                    if (file.getAbsolutePath().matches(".+[.]java")) {
                        paths.add(file.getAbsolutePath());
                    }
                }
            }
        }
        return this.getPaths();
    }

    public List<String> getPaths() {
        return paths;
    }
}

