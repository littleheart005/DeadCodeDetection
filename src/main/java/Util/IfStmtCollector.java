package Util;

import com.github.javaparser.ast.stmt.IfStmt;
import com.github.javaparser.ast.visitor.VoidVisitorAdapter;

import java.util.ArrayList;
import java.util.List;

public class IfStmtCollector extends VoidVisitorAdapter<Void> {
    private List<String> ifStmt = new ArrayList<>();
    private Integer beginLine;
    private Integer endLine;

    public IfStmtCollector(Integer beginLine, Integer endLine) {
        this.beginLine = beginLine;
        this.endLine = endLine;
    }

    @Override
    public void visit(IfStmt vd, Void arg) {
        super.visit(vd, arg);
        if (vd.getRange().get().begin.line >= beginLine && vd.getRange().get().begin.line <= endLine) {
            this.ifStmt.add(vd.getCondition().toString());
        }
    }

    public List<String> getIfStmt() {
        return ifStmt;
    }
}