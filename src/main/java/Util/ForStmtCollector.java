package Util;

import com.github.javaparser.ast.stmt.ForStmt;
import com.github.javaparser.ast.visitor.VoidVisitorAdapter;

import java.util.ArrayList;
import java.util.List;

public class ForStmtCollector extends VoidVisitorAdapter<Void> {
    private List<String> forStmt = new ArrayList<>();
    private Integer beginLine;
    private Integer endLine;

    public ForStmtCollector(Integer beginLine, Integer endLine) {
        this.beginLine = beginLine;
        this.endLine = endLine;
    }

    @Override
    public void visit(ForStmt vd, Void arg) {
        super.visit(vd, arg);
        String stmt = vd.getCompare().toString();
        stmt = removeExtensionForStmt(stmt);
        stmt = removeExtensionForStmt2(stmt);
        if (vd.getRange().get().begin.line >= beginLine && vd.getRange().get().begin.line <= endLine) {
            this.forStmt.add(stmt);
        }
    }

    public List<String> getForStmt() {
        return forStmt;
    }

    private static String removeExtensionForStmt(String stmt) {
        return stmt.replaceFirst("(^Optional\\[)", "");
    }

    private static String removeExtensionForStmt2(String stmt) {
        return stmt.replaceFirst("(\\])$", "");
    }
}