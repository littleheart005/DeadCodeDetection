package Util;

import com.github.javaparser.ast.stmt.WhileStmt;
import com.github.javaparser.ast.visitor.VoidVisitorAdapter;

import java.util.List;

public class WhileStmtCollector extends VoidVisitorAdapter<List<String>> {

    @Override
    public void visit(WhileStmt is, List<String> collector) {
        super.visit(is, collector);
        collector.add(is.getCondition().toString());
    }
}