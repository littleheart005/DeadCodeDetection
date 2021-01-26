package DeadVariable;

import TokenGenerator.Token;

import java.io.*;
import java.util.*;


public class Output {
    private String fileLocation;
    private FileWriter fileWriter;
    private BufferedWriter bufferedWriter;
    private List<Token> tokenList;

    public void createFile(String name) {
        this.fileLocation = "src/main/resources/deadvariable/" + name + ".txt";

        try {
            this.fileWriter = new FileWriter(fileLocation);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void sendInfo(List<Token> tokenList) {
        this.tokenList = tokenList;
    }

    public void write() throws IOException {
        this.bufferedWriter = new BufferedWriter(this.fileWriter);

        for (int i = 0; i<this.tokenList.size(); i++) {
            if (this.tokenList.get(i).getDeadVariable().size() > 0) {
                try {
                    Set<Integer> line = this.tokenList.get(i).getDeadVariable().keySet();
                    Iterator<Integer> iterator = line.iterator();

                    while (iterator.hasNext()) {
                        Integer lineNumber = iterator.next();
                        String tmp = "Variable ; Variable " + this.tokenList.get(i).getDeadVariable().get(lineNumber) + " has 0 references"
                                + " ; " + this.tokenList.get(i).getLocation() + " line " + lineNumber + " ; dead variable\n";
                        bufferedWriter.write(tmp);
                    }

                    for (int j = 0; j<this.tokenList.get(i).getDeadVariable().size(); j++) {

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
