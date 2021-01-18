package DeadInterface;

import Files_Reader.File_Reader;
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
    private static List<String> FILES_PATH = new ArrayList<>();
    private static Map<String, Integer> interfaceCount = new HashMap<>();
    private static List<CompilationUnit> cu = new ArrayList<>();

    public DeadInterfaceDetector(String source){
        File_Reader reader = new File_Reader();
        FILES_PATH = reader.readPath(source);
        parseAST();
    }

    // Parsing java files to AST.
    private static void parseAST(){
        VoidVisitor<?> interfaceVisitor; // Create visitor object
        try {
            for (String path : FILES_PATH){
                // parse
                CompilationUnit cuTmp = StaticJavaParser.parse(new File(path));

                // storing AST parsed object
                cu.add(cuTmp);

                // visit node in AST for getting interfaces name
                interfaceVisitor = new InterfaceNameCollector();
                interfaceVisitor.visit(cuTmp,null);
            }
        }catch (Exception e){
            System.out.println("Error in DeadInterface parseAST()");
            e.printStackTrace();
        }
    }

    // VisitorAdapter for class name collecting
    public static class InterfaceNameCollector extends VoidVisitorAdapter<Void> {
        @Override
        public void visit(ClassOrInterfaceDeclaration n, Void arg) {
            super.visit(n, arg);
            // Check if a interface
            if(n.isClassOrInterfaceDeclaration()&& n.isInterface()){
                // Store to map
                interfaceCount.put(n.getNameAsString(),1);
            }
        }
    }


    public void printMap(){
        interfaceCount.forEach((k, v)->System.out.println("Interface: "+k+"  count: "+v));
    }


}
