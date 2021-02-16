package Util;

import com.github.javaparser.ast.stmt.ForEachStmt;
import com.github.javaparser.ast.visitor.VoidVisitorAdapter;

import java.util.ArrayList;
import java.util.List;

public class ForeachStmtCollector extends VoidVisitorAdapter<List<String>> {
    private List<String> foreachStmt = new ArrayList<>();
    private Integer beginLine;
    private Integer endLine;

    public ForeachStmtCollector() { }

    public ForeachStmtCollector(Integer beginLine, Integer endLine) {
        this.beginLine = beginLine;
        this.endLine = endLine;
    }

    @Override
    public void visit(ForEachStmt vd, List<String> arg) {
        super.visit(vd, arg);

        if (this.beginLine != null && this.endLine != null) {
            if (vd.getRange().get().begin.line >= beginLine && vd.getRange().get().begin.line <= endLine) {
                this.foreachStmt.add(vd.getIterable().toString());
            }
        }

        arg.add(vd.getIterable().toString());
    }

    public List<String> getForeachStmt() {
        return foreachStmt;
    }
}