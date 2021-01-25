package DeadInterface;

import Util.ClassImplementationCollector;
import Util.InterfaceNameCollector;
import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.visitor.VoidVisitor;
import org.checkerframework.checker.units.qual.A;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DeadInterfaceDetector {

    private final Map<String, Integer> interfaceCount = new HashMap<>();
    private final List<CompilationUnit> cu;

    // Create visitor object
    private final VoidVisitor<List<String>> interfaceVisitor = new InterfaceNameCollector();
    private final VoidVisitor<List<String>> classImplementation = new ClassImplementationCollector();

    // List of all interface name in source files.
    private final List<String> interfaceName = new ArrayList<>();

    // List of all implement name.
    private final List<String> implementedList = new ArrayList<>();

    // List of all dead interfaces.
    private final List<String> deadInterface = new ArrayList<>();


    public DeadInterfaceDetector(List<CompilationUnit> cu){
        this.cu = cu;
        prepareData();
        detect();
        getDeadInterface();
    }

    // Parsing java files to AST.
    private void prepareData(){

        try {
            for (CompilationUnit cuTmp : cu){
                // Visit node in AST for getting interface name.
                interfaceVisitor.visit(cuTmp,interfaceName);
                // Map interface name and count.
                interfaceName.forEach(name->interfaceCount.put(name,0));

                // Check of implementation.
                classImplementation.visit(cuTmp,implementedList);

            }
        }catch (Exception e){
            System.out.println("Error in DeadInterface prepareData");
            e.printStackTrace();
        }
    }

    // Detect dead interface.
    private void detect(){
        for (String name : interfaceName){
            if(implementedList.contains(name)){
                interfaceCount.put(name,1);
            }
        }
    }

    public void printMap(){
        System.out.println("Total File read: "+cu.size()+" Interfaces: "+interfaceCount.size());
        interfaceCount.forEach((k, v)->System.out.println("Interface: "+k+"  count: "+v));
    }

    private void getDeadInterface(){

        for(Map.Entry<String, Integer> map : interfaceCount.entrySet()){
            if(map.getValue()==0){
                deadInterface.add(map.getKey());
            }
        }
    }

    public void printDeadInterface(){
        System.out.println("\n ======== Dead Interface ========== \n Total Found: "+deadInterface.size());
        deadInterface.forEach(di -> System.out.println(di));
    }


    private FileWriter f;
    private BufferedWriter bw;

    public void createReport(String name){
        String fileName = "src/main/resources/DeadInterface/DeadInterface.txt";
        try{
            f = new FileWriter(fileName);
            bw = new BufferedWriter(f);
            bw.write("============ Dead Class ===========\n Total Dead class: "+deadInterface.size()+"\n");
            for (String dead : deadInterface){
                String tmp = "Interface "+dead+" has 0 reference.\n";
                bw.write(tmp);
            }
            System.out.println("\nReport Created");
            bw.close();
        }catch (IOException e){
            System.out.println("Error in Dead interface createReport");
            e.printStackTrace();
        }
    }

}
