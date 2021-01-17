import com.github.javaparser.StaticJavaParser;
import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.body.MethodDeclaration;
import com.github.javaparser.ast.visitor.VoidVisitor;
import com.github.javaparser.ast.visitor.VoidVisitorAdapter;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class VoidVisitorDemo {

    private static final  String FILE_PATH = "";


    public static void main(String[] args) throws Exception{

        // Create AST of input file.
        CompilationUnit cu = StaticJavaParser.parse(new File(FILE_PATH));
        // Create Visitor object.
        VoidVisitor<?> methodNameVisitor = new MethodNamePrinter();
        // Run Visitor's visit command.
        methodNameVisitor.visit(cu,null);

        // Sending list to Visitor for collecting method's names.
        List<String> methodNames = new ArrayList<>();

        // Create Visitor object with type of List<String>.
        VoidVisitor<List<String>> methodNameCollector = new MethodNameCollector();

        // Visit.
        methodNameCollector.visit(cu,methodNames);

        // Print name in List.
        methodNames.forEach(n -> System.out.println("Method Name Collected: "+n));

    }

    // Create Void Visitor class to visit specific node in AST.
    public static class MethodNamePrinter extends VoidVisitorAdapter<Void>{
        @Override
        public void visit(MethodDeclaration md, Void arg){
            super.visit(md, arg);
            System.out.println("Method Name Printed : "+md.getName());
        }
    }

    // Create Visitor that take other type as parameter.
    public static class MethodNameCollector extends VoidVisitorAdapter<List<String>>{

        @Override
        public void visit(MethodDeclaration n, List<String> collector) {
            super.visit(n, collector);
            collector.add(n.getNameAsString());
        }
    }


}

