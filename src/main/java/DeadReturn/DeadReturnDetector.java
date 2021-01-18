package DeadReturn;

import Files_Reader.File_Reader;
import PathReader.PathReader;
import com.github.javaparser.StaticJavaParser;
import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.body.MethodDeclaration;
import com.github.javaparser.ast.body.VariableDeclarator;
import com.github.javaparser.ast.visitor.VoidVisitor;
import com.github.javaparser.ast.visitor.VoidVisitorAdapter;
import org.checkerframework.checker.units.qual.C;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;

public class DeadReturnDetector {

    private static List<String> FILES_PATH = new ArrayList<>();
    private static List<String> methodNames = new ArrayList<>();
    private static List<CompilationUnit> cu = new ArrayList<>();
    private static List<String> callers = new ArrayList<>();

    public DeadReturnDetector(String source){
        File_Reader reader = new File_Reader();
        FILES_PATH = reader.readPath(source);
        //FILES_PATH.forEach(n -> System.out.println("Path: "+n));
        parseAST();
    }

    // Parse file to AST to get method name.
    private void parseAST() {

        VoidVisitor<List<String>> methodNameVisitor;
        try {
            for (String path : FILES_PATH){
                CompilationUnit cuTmp = StaticJavaParser.parse(new File(path));
                cu.add(cuTmp);
                methodNameVisitor = new MethodNameCollector();
                methodNameVisitor.visit(cuTmp,methodNames);
            }
        }catch (Exception e) {
            System.out.println("Error in DeadReturn.parseAST");
            e.printStackTrace();
        }
    }


    // Get all not void method's name
    public static class MethodNameCollector extends VoidVisitorAdapter<List<String>>{
        @Override
        public void visit(MethodDeclaration n, List<String> collector) {
            super.visit(n,collector);
//            if (!n.getType().toString().equals("void")) {
                collector.add(n.getNameAsString());
//            }
        }
    }

    public void printMethodName(){
        methodNames.forEach(n->System.out.println("Method: "+n.toString()));
    }


}
