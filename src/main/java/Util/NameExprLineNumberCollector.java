package Util;

import com.github.javaparser.ast.expr.NameExpr;
import com.github.javaparser.ast.visitor.VoidVisitorAdapter;

import java.util.List;

public class NameExprLineNumberCollector extends VoidVisitorAdapter<List<Integer>> {
    @Override
    public void visit(NameExpr nameExpr, List<Integer> collector) {
        super.visit(nameExpr, collector);
        collector.add(nameExpr.getName().getBegin().get().line);
        System.out.println("line : " + nameExpr.getName().getBegin().get().line + " column : " + nameExpr.getName().getBegin().get().column +
                "-" + nameExpr.getName().getEnd().get().column);
    }
}
