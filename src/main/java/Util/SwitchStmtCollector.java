package Util;

import com.github.javaparser.ast.stmt.SwitchStmt;
import com.github.javaparser.ast.visitor.VoidVisitorAdapter;

import java.util.ArrayList;
import java.util.List;

public class SwitchStmtCollector extends VoidVisitorAdapter<Void> {
    private List<String> switchStmt = new ArrayList<>();
    private Integer beginLine;
    private Integer endLine;

    public SwitchStmtCollector(Integer beginLine, Integer endLine) {
        this.beginLine = beginLine;
        this.endLine = endLine;
    }

    @Override
    public void visit(SwitchStmt vd, Void arg) {
        super.visit(vd, arg);
        if (vd.getRange().get().begin.line >= beginLine && vd.getRange().get().begin.line <= endLine) {
            this.switchStmt.add(vd.getSelector().toString());
        }
    }

    public List<String> getSwitchStmt() {
        return switchStmt;
    }
}