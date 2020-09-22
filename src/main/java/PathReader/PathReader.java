package PathReader;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class PathReader {
    private List<String> paths = new ArrayList<>();

    public List<String> readPath(String source){

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
        return this.getPaths();
    }


    public List<String> getPaths() {
        return paths;
    }
}

