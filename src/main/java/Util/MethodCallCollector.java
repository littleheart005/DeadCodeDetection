package Util;

import com.github.javaparser.ast.expr.MethodCallExpr;
import com.github.javaparser.ast.visitor.VoidVisitorAdapter;

import java.util.List;

public class MethodCallCollector extends VoidVisitorAdapter<List<String>> {
    @Override
    public void visit(MethodCallExpr mc, List<String> collector) {
        super.visit(mc, collector);
        collector.add(mc.getParentNodeForChildren().toString());
    }
}
