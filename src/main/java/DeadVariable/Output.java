package DeadVariable;

import java.io.*;
import java.util.*;


public class Output {
    private String fileLocation;
    private FileWriter fileWriter;
    private BufferedWriter bufferedWriter;
    private List<FileToken> fileTokenList;

    public void createFile(String name) {
        this.fileLocation = "src/main/resources/DeadVariable/OpenSource/" + name + ".txt";

        try {
            this.fileWriter = new FileWriter(fileLocation);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void sendInfo(List<FileToken> fileTokenList) {
        this.fileTokenList = fileTokenList;
    }

    public void write() throws IOException {
        this.bufferedWriter = new BufferedWriter(this.fileWriter);

        for (int i = 0; i<this.fileTokenList.size(); i++) {
            if (this.fileTokenList.get(i).getAllDeadVariable().size() > 0) {
                try {
                    for (int j=0; j<this.fileTokenList.get(i).getAllDeadVariable().size(); j++) {
                        String tmp = "Variable ; " + this.fileTokenList.get(i).getAllDeadVariable().get(j).getVariableName() + " has 0 references"
                                + " ; " + this.fileTokenList.get(i).getLocation() + "(line:" + this.fileTokenList.get(i).getAllDeadVariable().get(j).getBeginLine() + ") ; dead variable\n";
                        bufferedWriter.write(tmp);
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
