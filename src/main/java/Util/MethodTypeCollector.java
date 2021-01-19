package Util;

import com.github.javaparser.ast.body.MethodDeclaration;
import com.github.javaparser.ast.visitor.VoidVisitorAdapter;

import java.util.List;

public class MethodTypeCollector extends VoidVisitorAdapter<List<String>> {
    @Override
    public void visit(MethodDeclaration n, List<String> arg) {
        super.visit(n, arg);
//       System.out.println(n.getTypeAsString()+" "+n.getNameAsString());

        // Check method type that aren't void, string, int, double
        if(!n.getTypeAsString().equals("void") && !n.getTypeAsString().contains("String")
            && !n.getTypeAsString().contains("double") && !n.getTypeAsString().contains("int")){
            arg.add(n.getTypeAsString());
        }
    }
}
