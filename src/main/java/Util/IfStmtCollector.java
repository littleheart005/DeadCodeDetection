package Util;

import com.github.javaparser.ast.stmt.IfStmt;
import com.github.javaparser.ast.visitor.VoidVisitorAdapter;

import java.util.ArrayList;
import java.util.List;

public class IfStmtCollector extends VoidVisitorAdapter<List<String>> {
    private List<String> ifStmt = new ArrayList<>();
    private Integer beginLine;
    private Integer endLine;

    public IfStmtCollector() { }

    public IfStmtCollector(Integer beginLine, Integer endLine) {
        this.beginLine = beginLine;
        this.endLine = endLine;
    }

    @Override
    public void visit(IfStmt vd, List<String> arg) {
        super.visit(vd, arg);

        if (this.beginLine != null && this.endLine != null) {
            if (vd.getRange().get().begin.line >= beginLine && vd.getRange().get().begin.line <= endLine) {
                this.ifStmt.add(vd.getCondition().toString());
            }
        }

        arg.add(vd.getCondition().toString());
    }

    public List<String> getIfStmt() {
        return ifStmt;
    }
}