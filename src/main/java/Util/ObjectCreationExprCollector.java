package Util;

import com.github.javaparser.ast.expr.ObjectCreationExpr;
import com.github.javaparser.ast.visitor.VoidVisitorAdapter;

import java.util.List;

public class ObjectCreationExprCollector extends VoidVisitorAdapter<List<String>> {

    @Override
    public void visit(ObjectCreationExpr n, List<String> collector) {
        super.visit(n, collector);
        collector.add(n.getParentNodeForChildren().toString());
    }
}