package DeadClass;

import Files_Reader.File_Reader;
import com.github.javaparser.StaticJavaParser;
import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.NodeList;
import com.github.javaparser.ast.body.ClassOrInterfaceDeclaration;
import com.github.javaparser.ast.body.FieldDeclaration;
import com.github.javaparser.ast.visitor.VoidVisitor;
import com.github.javaparser.ast.visitor.VoidVisitorAdapter;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DeadClassWithAST {
    private static List<String> FILES_PATH = new ArrayList<>();
    private static Map<String, Integer> classCount = new HashMap<>();
    private static List<CompilationUnit> cu = new ArrayList<>();

    public DeadClassWithAST(String source){
        /*File_Reader reader = new File_Reader();
        FILES_PATH = reader.readPath(source);
        parseAST();*/
    }

    // Parsing java files to AST.
    private static void parseAST(){
        VoidVisitor<?> classVisitor; // Create visitor object
        try {
            for (String path : FILES_PATH){
                // parse
                CompilationUnit cuTmp = StaticJavaParser.parse(new File(path));

                // storing AST parsed object
                cu.add(cuTmp);

                // visit node in AST for getting classes name
                classVisitor = new ClassNameCollector();
                classVisitor.visit(cuTmp,null);
            }
        }catch (Exception e){
            System.out.println("Error in DeadClass parseAST()");
            e.printStackTrace();
        }
    }

    // VisitorAdapter for class name collecting
    public static class ClassNameCollector extends VoidVisitorAdapter<Void>{
        // use Class/Interface Declaration object
        @Override
        public void visit(ClassOrInterfaceDeclaration n, Void arg) {
            super.visit(n, arg);
            // check if a class
            if(n.isClassOrInterfaceDeclaration()&& !n.isInterface()){
                // Store to map
                classCount.put(n.getNameAsString(),1);
            }
        }
    }


    public void detect(){

        VoidVisitor<?> fieldDeclare = new FieldDeclarePrinter();
        VoidVisitor<?> classDeclare = new ClassDeclare();

        for(CompilationUnit tmp : cu){

            // Case 1 : Extends, Implement.

            classDeclare.visit(tmp,null);

            // fieldDeclare.visit(tmp,null);

        }

    }

    private static class ClassDeclare extends VoidVisitorAdapter<Void>{
        @Override
        public void visit(ClassOrInterfaceDeclaration n, Void arg) {
            super.visit(n, arg);

            NodeList implement;
            implement = n.getImplementedTypes();
            NodeList extend;
            extend = n.getExtendedTypes();
            for(Object node : implement){
                String tmpStr = node.toString();

                System.out.println(tmpStr);
            }

        }
    }

    private static class FieldDeclarePrinter extends VoidVisitorAdapter<Void>{
        @Override
        public void visit(FieldDeclaration n, Void arg) {
            super.visit(n, arg);
            System.out.println("Field: "+n.getVariables().getParentNodeForChildren());
        }
    }

    // Print class count map
    public void printMap(){
        classCount.forEach((k, v)->System.out.println("Class: "+k+"  count: "+v));
    }


}

