package Util;

import com.github.javaparser.ast.expr.InstanceOfExpr;
import com.github.javaparser.ast.visitor.VoidVisitorAdapter;

import java.util.List;

public class InstanceOfCollector extends VoidVisitorAdapter<List<String>> {
    @Override
    public void visit(InstanceOfExpr n, List<String> arg) {
        super.visit(n, arg);
        arg.add(n.getTypeAsString());
    }
}
