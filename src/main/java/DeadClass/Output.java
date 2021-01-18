package DeadClass;

import java.io.*;


public class Output {
    private String fileName;
    private PrintWriter out;
    private FileWriter f;
    private BufferedWriter bw;

    public void createFile(String name) {
        fileName = "src/main/resources/deadclass_interface/" + name + ".txt";
        try {
            f = new FileWriter(fileName);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void write(ClassDB classes,InterfaceDB interfaces){
        bw = new BufferedWriter(f);
        try {

            for (int i = 0; i < classes.getClassName().size(); i++) {
                if(classes.getCount(i)==0){
                    String tmp = "Class ; Class "+classes.getClassName(i)+" has 0 references"
                            + " ; "+classes.getClassPath(i)+" line "+classes.getLine(i)+" ; dead class\n";
                    bw.write(tmp);
                    System.out.print(tmp);
                }
            }
            for (int i = 0; i < interfaces.getInterfaceName().size(); i++) {
                if(interfaces.getCount(i)==0){
                    String tmp = "Interface ; Interface "+interfaces.getInterfaceName(i)+" has 0 references"
                            + " ; "+interfaces.getInterfacePath(i)+" line "+interfaces.getLine(i)+"; dead interface\n";
                    bw.write(tmp);
                    System.out.print(tmp);
                }
            }
            bw.close();
        }catch (IOException e){
            System.out.print("Print Error!");
            e.printStackTrace();
        }
    }

    public void write(String text){
        bw = new BufferedWriter(f);
       try {
            bw.write(text+"\n");
            System.out.println(text);
       }catch (IOException e){
           System.out.println("Print find result error!");
       }
    }


}
