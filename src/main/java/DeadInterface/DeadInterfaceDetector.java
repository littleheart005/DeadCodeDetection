package DeadInterface;

import Files_Reader.File_Reader;
import Util.InterfaceNameCollector;
import com.github.javaparser.StaticJavaParser;
import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.body.ClassOrInterfaceDeclaration;
import com.github.javaparser.ast.visitor.VoidVisitor;
import com.github.javaparser.ast.visitor.VoidVisitorAdapter;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DeadInterfaceDetector {

    private static Map<String, Integer> interfaceCount = new HashMap<>();
    private static List<CompilationUnit> cu = new ArrayList<>();

    // Create visitor object
    private static VoidVisitor<List<String>> interfaceVisitor = new InterfaceNameCollector();

    private static List<String> interfaceName = new ArrayList<>();

    public DeadInterfaceDetector(List<CompilationUnit> cu){
        this.cu = cu;
        parseAST();
    }

    // Parsing java files to AST.
    private static void parseAST(){

        try {
            for (CompilationUnit cuTmp : cu){
                interfaceVisitor.visit(cuTmp,interfaceName);
                interfaceName.forEach(name->interfaceCount.put(name,1));
            }
        }catch (Exception e){
            System.out.println("Error in DeadInterface parseAST()");
            e.printStackTrace();
        }
    }


    public void printMap(){
        interfaceCount.forEach((k, v)->System.out.println("Interface: "+k+"  count: "+v));
    }


}
