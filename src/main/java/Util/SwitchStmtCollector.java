package Util;

import com.github.javaparser.ast.stmt.SwitchStmt;
import com.github.javaparser.ast.visitor.VoidVisitorAdapter;

import java.util.List;

public class SwitchStmtCollector extends VoidVisitorAdapter<List<String>> {

    @Override
    public void visit(SwitchStmt is, List<String> collector) {
        super.visit(is, collector);
        collector.add(is.getSelector().toString());
    }
}