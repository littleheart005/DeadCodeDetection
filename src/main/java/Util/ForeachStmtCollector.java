package Util;

import com.github.javaparser.ast.stmt.ForEachStmt;
import com.github.javaparser.ast.visitor.VoidVisitorAdapter;

import java.util.List;

public class ForeachStmtCollector extends VoidVisitorAdapter<List<String>> {

    @Override
    public void visit(ForEachStmt is, List<String> collector) {
        super.visit(is, collector);
        collector.add(is.getIterable().toString());
    }
}