import DeadClassInterface.ClassToken;
import DeadClassInterface.DeadClassInterfaceDetector;
import DeadClassInterface.FileToken;
import Util.ASTParser;
import com.github.javaparser.StaticJavaParser;
import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.body.ConstructorDeclaration;
import com.github.javaparser.ast.expr.InstanceOfExpr;
import com.github.javaparser.ast.expr.Name;
import com.github.javaparser.ast.expr.SuperExpr;
import com.github.javaparser.ast.stmt.AssertStmt;
import com.github.javaparser.ast.stmt.ExpressionStmt;
import com.github.javaparser.ast.visitor.VoidVisitor;
import com.github.javaparser.ast.visitor.VoidVisitorAdapter;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;

public class Demo {
    public static void main(String[] args) throws FileNotFoundException {
        // Cases -> inner static class and super method.

        String path = "/Users/Peeradon/Documents/OpenSourceProject/spring-framework-5.3.3/spring-core/src/main/" +
                "java/org/springframework/core/OrderComparator.java";
        CompilationUnit cu = StaticJavaParser.parse(new File(path));

        List<String> conBody = new ArrayList<String>();
        ConstructorVisitor constructorVisitor = new ConstructorVisitor();
        constructorVisitor.visit(cu,conBody);


//        FileToken fileToken = new FileToken(cu);
//        List<String> tmp = fileToken.getMethodArgument();
//        tmp.forEach(vt->System.out.println(vt));

//        List<String> classes = fileToken.getClassName();
//        for(String name : classes) {
//            ClassToken classToken = new ClassToken(name,
//                    fileToken.getFileName(),
//                    fileToken.getPackageName(),
//                    fileToken.getLocation(),
//                    fileToken.getClassLine(name));
//            classTokens.add(classToken);
//        }
//        List<String> tmp = fileToken.getMethodCall();
//        tmp.forEach(mc->System.out.println(mc));


//        int i = 1;
//        for(ClassToken classToken : classTokens){
//            System.out.print("no."+i);
//            System.out.print(" Class Name: "+classToken.getName());
//            System.out.println("\t\tpackage: "+classToken.getPackageName());
//            i++;
//        }

//        ASTParser astParser = new ASTParser(path);
//        DeadClassInterfaceDetector detector = new DeadClassInterfaceDetector(astParser.cu);
//        for(ClassToken classToken : detector.getClassTokens()){
//            System.out.println(classToken.getName());
//        }

//        detector.detect();
//        System.out.println("\n========== Dead class ===========");
//        detector.printResult();

    }

    private static class ConstructorVisitor extends VoidVisitorAdapter<List<String>>{
        @Override
        public void visit(InstanceOfExpr n, List<String> arg) {
            super.visit(n, arg);
            System.out.println(n.getType());
        }
    }
}

