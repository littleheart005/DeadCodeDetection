package DeadClass;

import Util.*;
import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.visitor.VoidVisitor;

import java.util.*;

public class DeadClassWithAST {
    private final Map<String, Integer> classCount = new HashMap<>();
    private List<CompilationUnit> cu;

    // Visitors
    private final VoidVisitor<List<String>> classVisitor = new ClassNameCollector();
    private final VoidVisitor<List<String>> classDeclare = new ClassDeclarationVisitor();
    private final VoidVisitor<List<String>> variableTypeCollector = new VariableTypeCollector();
    private final VoidVisitor<List<String>> parameterCollector = new ParameterCollector();
    private final VoidVisitor<List<String>> methodTypeCollector = new MethodTypeCollector();

    // List of all class name in source files.
    private final List<String> className = new ArrayList<>();

    // List of all extended name.
    private final List<String> extendedList = new ArrayList<>();

    // List of all variable type and method type.
    private final List<String> variableType = new ArrayList<>();
    private final List<String> methodType = new ArrayList<>();

    // List of all parameter type.
    private final List<String> parameterType = new ArrayList<>();

    public DeadClassWithAST(List<CompilationUnit> cu){
       this.cu = cu;
       setClassCount();

    }

    // Parsing java files to AST.
    private void setClassCount(){
        try {
            for (CompilationUnit cuTmp : cu){
                // visit node in AST for getting classes name
                classVisitor.visit(cuTmp,className);
                // Map class name and count
                className.forEach(name->classCount.put(name,1));
            }
        }catch (Exception e){
            System.out.println("Error in DeadClass parseAST()");
            e.printStackTrace();
        }
    }

    public void prepareData(){

        for(CompilationUnit tmp : cu){

//             Case 1 : Extends.
//             Get all extended class name.
            //classDeclare.visit(tmp,extendedList);

//             Case 2 : Variable Type and Method Type
//             Get all variable (also array) and method type.
             //variableTypeCollector.visit(tmp,variableType);

             //methodTypeCollector.visit(tmp,methodType);


//             Case 3 : Object

//             Case 4 : Parameter
//             Get all parameter type.
            // parameterCollector.visit(tmp,parameterType);

//

        }

        //variableType.forEach(ex->System.out.println("Variable type: "+ex));

    }

    // Print class count map
    public void printMap(){
        classCount.forEach((k, v)->System.out.println("Class: "+k+"  count: "+v));
    }

}

