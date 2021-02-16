package Util;

import com.github.javaparser.ast.body.ClassOrInterfaceDeclaration;
import com.github.javaparser.ast.visitor.VoidVisitorAdapter;
import org.checkerframework.checker.units.qual.A;

import java.util.ArrayList;
import java.util.List;

public class CNameCollector extends VoidVisitorAdapter<Void> {

    private List<String> className = new ArrayList<>();
    private List<Integer> declarationLine = new ArrayList<>();

    @Override
    public void visit(ClassOrInterfaceDeclaration n, Void arg) {
        super.visit(n, arg);
        if(n.isClassOrInterfaceDeclaration()&& !n.isInterface()){
            className.add(n.getNameAsString());
            declarationLine.add(n.getRange().get().begin.line);
        }
    }

    public List<String> getClassName() {
        return className;
    }

    public List<Integer> getDeclarationLine() {
        return declarationLine;
    }

}
