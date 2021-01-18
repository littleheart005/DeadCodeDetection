package DeadClass;

import Util.*;
import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.body.FieldDeclaration;
import com.github.javaparser.ast.body.Parameter;
import com.github.javaparser.ast.body.VariableDeclarator;
import com.github.javaparser.ast.visitor.VoidVisitor;
import com.github.javaparser.ast.visitor.VoidVisitorAdapter;
import org.checkerframework.checker.units.qual.A;
import org.w3c.dom.ls.LSException;

import java.util.*;

public class DeadClassWithAST {
    private static Map<String, Integer> classCount = new HashMap<>();
    private static List<CompilationUnit> cu = new ArrayList<>();

    private static VoidVisitor<List<String>> classVisitor = new ClassNameCollector();
    private static VoidVisitor<List<String>> classDeclare = new ClassDeclaration();
    private static VoidVisitor<List<String>> variableCollector = new VariableCollector();
    private static VoidVisitor<List<String>> parameterCollector = new ParameterCollector();

    private static List<String> className = new ArrayList<>();
    private static List<String> variableType = new ArrayList<>();
    private static List<String> parameterType = new ArrayList<>();

    public DeadClassWithAST(List<CompilationUnit> cu){
       this.cu = cu;
       parseAST();
    }

    // Parsing java files to AST.
    private static void parseAST(){
        try {
            for (CompilationUnit cuTmp : cu){
                // visit node in AST for getting classes name
                classVisitor.visit(cuTmp,className);
                className.forEach(name->classCount.put(name,1));
            }
        }catch (Exception e){
            System.out.println("Error in DeadClass parseAST()");
            e.printStackTrace();
        }
    }

    public void detect(){

        for(CompilationUnit tmp : cu){

            // Case 1 : Extends, Implement.
            //classDeclare.visit(tmp,className);

            // Case ? : Variable & Field
            //variableCollector.visit(tmp,variableType);

            // Case ? : Parameter
            //parameterCollector.visit(tmp,parameterType);

        }

    }

    // Print class count map
    public void printMap(){
        classCount.forEach((k, v)->System.out.println("Class: "+k+"  count: "+v));
    }


}

