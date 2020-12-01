package DeadClass_Interface;

import java.util.ArrayList;
import java.util.List;

public class InterfaceDB {
    private final List<String> interfaceName = new ArrayList<>();
    private final List<String> interfacePath = new ArrayList<>();
    private final List<Integer> lineNumber = new ArrayList<>();
    private final List<String> declareLine = new ArrayList<>();
    private final List<Integer> counts = new ArrayList<>();


    public void addInterfaceName(String name){
        this.interfaceName.add(name);
    }

    public void addInterfacePath(String path){
        this.interfacePath.add(path);
    }

    public void addLine(int line){
        this.lineNumber.add(line);
    }

    public void addDeclareLine(String declare){
        this.declareLine.add(declare);
    }

    public void setCount(){
        this.counts.add(0);
    }

    public void increaseCount(int index){
        this.counts.set(index,counts.get(index)+1);
    }

    public String getInterfaceName(int index){
        return this.interfaceName.get(index);
    }

    public String getInterfacePath(int index){
        return this.interfacePath.get(index);
    }

    public int getLine(int index){
        return this.lineNumber.get(index);
    }

    public int getCount(int index){
        return this.counts.get(index);
    }

    public List<String> getInterfaceName() {
        return interfaceName;
    }


   /* public String getDeclare(int index){
        return this.declareLine.get(index);
    }

    public void print(){
        for (int i=0;i<interfaceName.size();i++){
            System.out.print("Interface : "+interfaceName.get(i)+
                    " path : "+interfacePath.get(i)+
                    " : line  "+lineNumber.get(i)+
                    " : "+declareLine.get(i)+
                    " : count = "+counts.get(i)+"\n");
        }
    }

    public List<String> getInterfacePath() {
        return interfacePath;
    }

    public List<Integer> getLineNumber() {
        return lineNumber;
    }

    public List<String> getDeclareLine() {
        return declareLine;
    }

    public List<Integer> getCounts() {
        return counts;
    }*/
}
