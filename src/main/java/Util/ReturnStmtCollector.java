package Util;

import com.github.javaparser.ast.stmt.ReturnStmt;
import com.github.javaparser.ast.visitor.VoidVisitorAdapter;

import java.util.List;

public class ReturnStmtCollector extends VoidVisitorAdapter<List<String>> {

    @Override
    public void visit(ReturnStmt is, List<String> collector) {
        super.visit(is, collector);
        String stmt = is.getChildNodes().toString();
        stmt = removeExtensionForStmt(stmt);
        stmt = removeExtensionForStmt2(stmt);
        collector.add(stmt);
    }

    private static String removeExtensionForStmt(String stmt) {
        return stmt.replaceFirst("^(\\[)", "");
    }

    private static String removeExtensionForStmt2(String stmt) {
        return stmt.replaceFirst("(\\])$", "");
    }
}