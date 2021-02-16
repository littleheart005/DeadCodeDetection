package Util;

import com.github.javaparser.ast.stmt.ReturnStmt;
import com.github.javaparser.ast.visitor.VoidVisitorAdapter;

import java.util.ArrayList;
import java.util.List;

public class ReturnStmtCollector extends VoidVisitorAdapter<List<String>> {
    private List<String> returnStmt = new ArrayList<>();
    private Integer beginLine;
    private Integer endLine;

    public ReturnStmtCollector() { }

    public ReturnStmtCollector(Integer beginLine, Integer endLine) {
        this.beginLine = beginLine;
        this.endLine = endLine;
    }

    @Override
    public void visit(ReturnStmt vd, List<String> arg) {
        super.visit(vd, arg);
        String stmt = vd.getChildNodes().toString();
        stmt = removeExtensionForStmt(stmt);
        stmt = removeExtensionForStmt2(stmt);

        if (this.beginLine != null && this.endLine != null) {
            if (vd.getRange().get().begin.line >= beginLine && vd.getRange().get().begin.line <= endLine) {
                this.returnStmt.add(stmt);
            }
        }

        arg.add(stmt);
    }

    public List<String> getReturnStmt() {
        return returnStmt;
    }

    private static String removeExtensionForStmt(String stmt) {
        return stmt.replaceFirst("^(\\[)", "");
    }

    private static String removeExtensionForStmt2(String stmt) {
        return stmt.replaceFirst("(\\])$", "");
    }
}