package Util;

import com.github.javaparser.ast.stmt.ReturnStmt;
import com.github.javaparser.ast.visitor.VoidVisitorAdapter;

import java.util.List;

public class ReturnStmtCollector extends VoidVisitorAdapter<List<String>> {

    @Override
    public void visit(ReturnStmt is, List<String> collector) {
        super.visit(is, collector);
        collector.add(is.getChildNodes().toString());
    }
}