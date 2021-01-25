package Util;

import com.github.javaparser.ast.stmt.ForStmt;
import com.github.javaparser.ast.visitor.VoidVisitorAdapter;

import java.util.List;

public class ForStmtCollector extends VoidVisitorAdapter<List<String>> {

    @Override
    public void visit(ForStmt is, List<String> collector) {
        super.visit(is, collector);
        String stmt = is.getCompare().toString();
        stmt = removeExtensionForStmt(stmt);
        stmt = removeExtensionForStmt2(stmt);
        collector.add(stmt);
    }

    private static String removeExtensionForStmt(String stmt) {
        return stmt.replaceFirst("(^Optional\\[)", "");
    }

    private static String removeExtensionForStmt2(String stmt) {
        return stmt.replaceFirst("(\\])$", "");
    }

}