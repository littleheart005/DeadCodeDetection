package DeadClassInterface;

import java.util.ArrayList;
import java.util.List;

public class ClassDB {
    private final List<String> className = new ArrayList<>();
    private final List<String> classPath = new ArrayList<>();
    private final List<Integer> lineNumber = new ArrayList<>();
    private final List<String> declareLine = new ArrayList<>();
    private final List<Integer> counts = new ArrayList<>();



    public void addClassName(String name){
        this.className.add(name);
    }

    public void addClassPath(String path){
        this.classPath.add(path);
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

    public String getClassName(int index){
        return this.className.get(index);
    }

    public String getClassPath(int index){
        return this.classPath.get(index);
    }

    public int getLine(int index){
        return this.lineNumber.get(index);
    }

    public int getCount(int index){
        return this.counts.get(index);
    }

    public List<String> getClassName() {
        return className;
    }

    /* public String getDeclare(int index){
        return this.declareLine.get(index);
    }

    public List<String> getClassPath() {
        return classPath;
    }

    public List<Integer> getLineNumber() {
        return lineNumber;
    }

    public List<String> getDeclareLine() {
        return declareLine;
    }

    public List<Integer> getCounts() {
        return counts;
    }

    public void print(){
        for (int i=0;i<className.size();i++){
                System.out.print("Class : "+className.get(i)+
                        " path : "+classPath.get(i)+
                        " : line  "+lineNumber.get(i)+
                        " : "+declareLine.get(i)+
                        " : count = "+counts.get(i)+"\n");

        }
    }*/

}
