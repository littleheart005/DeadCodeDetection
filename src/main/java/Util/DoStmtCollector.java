package Util;

import com.github.javaparser.ast.stmt.DoStmt;
import com.github.javaparser.ast.visitor.VoidVisitorAdapter;

import java.util.ArrayList;
import java.util.List;

public class DoStmtCollector extends VoidVisitorAdapter<Void> {
    private List<String> doStmt = new ArrayList<>();
    private Integer beginLine;
    private Integer endLine;

    public DoStmtCollector(Integer beginLine, Integer endLine) {
        this.beginLine = beginLine;
        this.endLine = endLine;
    }

    @Override
    public void visit(DoStmt vd, Void arg) {
        super.visit(vd, arg);
        if (vd.getRange().get().begin.line >= beginLine && vd.getRange().get().begin.line <= endLine) {
            this.doStmt.add(vd.getCondition().toString());
        }
    }

    public List<String> getDoStmt() {
        return doStmt;
    }
}