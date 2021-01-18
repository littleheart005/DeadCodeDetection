package Util;

import com.github.javaparser.ast.body.VariableDeclarator;
import com.github.javaparser.ast.visitor.VoidVisitorAdapter;

import java.util.List;

public class VariableCollector extends VoidVisitorAdapter<List<String>> {

    @Override
    public void visit(VariableDeclarator n, List<String> arg) {
        super.visit(n, arg);
        System.out.println("Variable: "+n.getName()+" type: "+n.getTypeAsString()+
                " at line: "+n.getRange().get().begin.line);
    }
}
