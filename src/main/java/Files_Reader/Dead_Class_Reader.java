package Files_Reader;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Dead_Class_Reader extends File_Reader{

    @Override
    public List<String> readPath(String source) {
        return super.readPath(source);
    }

    public List<String> readFile_Name(List<String> paths) {
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

    public String getFileName(File file){
        return removeExtension(file.getName());
    }

    public static String removeExtension(String file){
        return file.replaceFirst("[.][^.]+$", "");
    }


    // Search the names by convert the file to txt and then search for name after the word
    // "Class" or "Interface" in the file.
    public void searchClassName(List<String> words){
        String name = search(words);
        System.out.println("Class/Interface name is : " + name);
    }

    private String search(List<String> words) {
        String Name = " ";
        for (int i = 0; i < words.size(); i++) {
            if (words.get(i).contentEquals("class") || words.get(i).contentEquals("interface")) {
                Name = words.get(i + 1);
            }
        }
        return Name;
    }
}
