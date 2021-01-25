package Util;

import com.github.javaparser.ast.expr.AssignExpr;
import com.github.javaparser.ast.visitor.VoidVisitorAdapter;

import java.util.List;

public class AssignExprCollector extends VoidVisitorAdapter<List<String>> {

    @Override
    public void visit(AssignExpr n, List<String> collector) {
        super.visit(n, collector);
        collector.add(n.getParentNodeForChildren().toString());
    }
}