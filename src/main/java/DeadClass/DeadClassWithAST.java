package DeadClass;

import Util.*;
import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.visitor.VoidVisitor;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.Buffer;
import java.util.*;

public class DeadClassWithAST {

    private final Map<String, Integer> classCount = new HashMap<>();
    private final List<CompilationUnit> cu;

    // Visitors
    private final VoidVisitor<List<String>> classVisitor = new ClassNameCollector();
    private final VoidVisitor<List<String>> classExtension = new ClassExtensionCollector();
    private final VoidVisitor<List<String>> variableTypeCollector = new VariableTypeCollector();
    private final VoidVisitor<List<String>> parameterCollector = new ParameterCollector();
    private final VoidVisitor<List<String>> methodTypeCollector = new MethodTypeCollector();
    private final VoidVisitor<List<String>> objectAssignCollector = new ObjectDeclarationVisitor();

    // List of all class name in source files.
    private final List<String> className = new ArrayList<>();
    // List of all extended name.
    private final List<String> extendedList = new ArrayList<>();
    // List of all variable type and method type.
    private final List<String> variableType = new ArrayList<>();
    private final List<String> methodType = new ArrayList<>();
    // List of all parameter type.
    private final List<String> parameterType = new ArrayList<>();
    // List of all object assignment.
    private final List<String> objectAssignmentType = new ArrayList<>();
    // List of all Dead Class.
    private final List<String> deadClass = new ArrayList<>();

    public DeadClassWithAST(List<CompilationUnit> cu){
        this.cu = cu;
        prepareData();
        detect();
        getDeadClass();
    }

    // Get all necessary data.
    public void prepareData(){
        try {
            for (CompilationUnit cuTmp : cu) {

                // visit node in AST for getting classes name
                classVisitor.visit(cuTmp,className);
                // Map class name and count
                className.forEach(name->classCount.put(name,0));

                //Case 1 : Extends, Get all extended class name.
                classExtension.visit(cuTmp, extendedList);

                //Case 2 : Variable Type and Method Type, Get all variable (include array type) and method type.
                variableTypeCollector.visit(cuTmp, variableType);
                methodTypeCollector.visit(cuTmp, methodType);

                // Case 3 : Object, Get all object assignment type.
                // Ex. PizzaStore nyStore = new NYPizzaStore(); ->  Type: NYPizzaStore
                objectAssignCollector.visit(cuTmp, objectAssignmentType);

                // Case 4 : Parameter, Get all parameter type.
                parameterCollector.visit(cuTmp, parameterType);

            }
        }catch (Exception e){
            System.out.println("Error in Dead class prepareData.");
            e.printStackTrace();
        }
    }

    public void detect(){
        for(String name : className){
            if(extendedList.contains(name) || variableType.contains(name) ||
                    methodType.contains(name) || objectAssignmentType.contains(name) ||
                    parameterType.contains(name)){

                    classCount.put(name,1);
            }
        }
    }

    private void getDeadClass(){
        for(Map.Entry<String, Integer> map : classCount.entrySet()){
            if(map.getValue()==0){
                deadClass.add(map.getKey());
            }
        }
    }

    // Print class count map
    public void printMap(){
        System.out.println("Total File read: "+cu.size()+" Total Class: "+classCount.size());
        classCount.forEach((k, v)->System.out.println("Class: "+k+"  count: "+v));
    }

    public void printDeadClass(){
        System.out.println("\n ======== Dead Class ========== \n Total Found: "+deadClass.size());
        deadClass.forEach(dc -> System.out.println(dc));
    }

    private FileWriter f;
    private BufferedWriter bw;

    public void createReport(String name){
        String fileName = "src/main/resources/"+name+"_Deadclass.txt";
        try{
            f = new FileWriter(fileName);
            bw = new BufferedWriter(f);
            bw.write("============ Dead Class ===========\n Total Dead class: "+deadClass.size()+"\n");
            for (String dead : deadClass){
                String tmp = "Class "+dead+" has 0 reference.\n";
                bw.write(tmp);
            }
            System.out.println("\n\nReport Created");
            bw.close();
        }catch (IOException e){
            System.out.println("Error in Deadclass createReport");
            e.printStackTrace();
        }
    }


}

