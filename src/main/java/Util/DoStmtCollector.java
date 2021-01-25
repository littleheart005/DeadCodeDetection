package Util;

import com.github.javaparser.ast.stmt.DoStmt;
import com.github.javaparser.ast.visitor.VoidVisitorAdapter;

import java.util.List;

public class DoStmtCollector extends VoidVisitorAdapter<List<String>> {

    @Override
    public void visit(DoStmt is, List<String> collector) {
        super.visit(is, collector);
        collector.add(is.getCondition().toString());
    }
}