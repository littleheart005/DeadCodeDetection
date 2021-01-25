package Util;

import com.github.javaparser.ast.stmt.IfStmt;
import com.github.javaparser.ast.visitor.VoidVisitorAdapter;

import java.util.List;

public class IfStmtCollector extends VoidVisitorAdapter<List<String>> {

    @Override
    public void visit(IfStmt is, List<String> collector) {
        super.visit(is, collector);
        collector.add(is.getCondition().toString());
    }
}