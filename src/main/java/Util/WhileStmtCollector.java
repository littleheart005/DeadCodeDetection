package Util;

import com.github.javaparser.ast.stmt.WhileStmt;
import com.github.javaparser.ast.visitor.VoidVisitorAdapter;

import java.util.ArrayList;
import java.util.List;

public class WhileStmtCollector extends VoidVisitorAdapter<Void> {
    private List<String> whileStmt = new ArrayList<>();
    private Integer beginLine;
    private Integer endLine;

    public WhileStmtCollector(Integer beginLine, Integer endLine) {
        this.beginLine = beginLine;
        this.endLine = endLine;
    }

    @Override
    public void visit(WhileStmt vd, Void arg) {
        super.visit(vd, arg);
        if (vd.getRange().get().begin.line >= beginLine && vd.getRange().get().begin.line <= endLine) {
            this.whileStmt.add(vd.getCondition().toString());
        }
    }

    public List<String> getWhileStmt() {
        return whileStmt;
    }
}