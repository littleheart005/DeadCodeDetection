package DeadClass;

import javax.imageio.IIOException;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;


public class Output {

    private String fileName;

    public void createFile(String name) {
        this.fileName = "C:\\Users\\Punch\\Desktop\\DeadCode_workspace\\output" + name + ".txt";
        try {
            File file = new File(fileName);
            if (file.createNewFile()) {
                System.out.println("File created : " + file.getName());
            }else {
                System.out.println("File already exist.");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void writeFile(String text){
        try {
            FileWriter writer = new FileWriter(fileName);
            writer.write(text);
            writer.close();
        }catch (IOException e){
            System.out.println("Error occurred.");
            e.printStackTrace();
        }
    }


}
