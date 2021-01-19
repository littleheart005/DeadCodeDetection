package Util;

import com.github.javaparser.ast.body.ClassOrInterfaceDeclaration;
import com.github.javaparser.ast.visitor.VoidVisitorAdapter;

import java.util.List;

// VisitorAdapter for class name collecting

public class ClassNameCollector extends VoidVisitorAdapter<List<String>> {

    // use Class/Interface Declaration object
    @Override
    public void visit(ClassOrInterfaceDeclaration n, List<String> arg) {
        super.visit(n, arg);
        if(n.isClassOrInterfaceDeclaration()&& !n.isInterface()){
            arg.add(n.getNameAsString());
        }
    }

}
