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

    @Override
    public void readFile(List<String> paths) {
        /* Next step, this method should return a list 'word' to caller
         * for using in various method */
        for(String p : paths){
            File file = new File(p);
            List<String> words = new ArrayList<>(); // create array to store all words in file

            try {
                Scanner sc = new Scanner(file);
                while (sc.hasNext()){
                    String word = sc.next();
                    words.add(word); // add words to array
                }
                searchClassName(words);
            }catch (FileNotFoundException e){
                e.printStackTrace();
            }

        }
    }

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
