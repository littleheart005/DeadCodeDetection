package Util;

import com.github.javaparser.ast.stmt.ForStmt;
import com.github.javaparser.ast.visitor.VoidVisitorAdapter;

import java.util.List;

public class ForStmtCollector extends VoidVisitorAdapter<List<String>> {

    @Override
    public void visit(ForStmt is, List<String> collector) {
        super.visit(is, collector);
        collector.add(is.getCompare().toString());
    }
}