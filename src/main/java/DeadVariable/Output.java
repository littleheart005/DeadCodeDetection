package DeadVariable;

import java.io.*;
import java.util.*;


public class Output {
    private String fileLocation;
    private FileWriter fileWriter;
    private BufferedWriter bufferedWriter;
    private List<Component> componentList;

    public void createFile(String name) {
        this.fileLocation = "src/main/resources/deadvariable/" + name + ".txt";

        try {
            this.fileWriter = new FileWriter(fileLocation);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void sendInfo(List<Component> componentList) {
        this.componentList = componentList;
    }

    public void write() throws IOException {
        this.bufferedWriter = new BufferedWriter(this.fileWriter);

        for (int i=0; i<this.componentList.size(); i++) {
            if (this.componentList.get(i).getDeadVariable().size() > 0) {
                try {
                    Set<Integer> line = this.componentList.get(i).getDeadVariable().keySet();
                    Iterator<Integer> iterator = line.iterator();

                    while (iterator.hasNext()) {
                        Integer lineNumber = iterator.next();
                        String tmp = "Variable ; Variable " + this.componentList.get(i).getDeadVariable().get(lineNumber) + " has 0 references"
                                + " ; " + this.componentList.get(i).getLocation() + " line " + lineNumber + " ; dead variable\n";
                        bufferedWriter.write(tmp);
                    }

                    for (int j = 0; j<this.componentList.get(i).getDeadVariable().size(); j++) {

                    }
                }catch (IOException e){
                    System.out.print("Print Error!");
                    e.printStackTrace();
                }
            }
        }
        this.bufferedWriter.close();
    }
}
