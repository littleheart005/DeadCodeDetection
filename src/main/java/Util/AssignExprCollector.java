package Util;

import com.github.javaparser.ast.expr.AssignExpr;
import com.github.javaparser.ast.visitor.VoidVisitorAdapter;

import java.util.ArrayList;
import java.util.List;

public class AssignExprCollector extends VoidVisitorAdapter<List<String>> {
    private List<String> AssignExpr = new ArrayList<>();
    private Integer beginLine;
    private Integer endLine;

    public AssignExprCollector() { }

    public AssignExprCollector(Integer beginLine, Integer endLine) {
        this.beginLine = beginLine;
        this.endLine = endLine;
    }

    @Override
    public void visit(AssignExpr vd, List<String> arg) {
        super.visit(vd, arg);

        if (this.beginLine != null && this.endLine != null) {
            if (vd.getRange().get().begin.line >= beginLine && vd.getRange().get().begin.line <= endLine) {
                this.AssignExpr.add(vd.getParentNodeForChildren().toString());
            }
        }

        arg.add(vd.getParentNodeForChildren().toString());
    }

    public List<String> getAssignExpr() {
        return AssignExpr;
    }
}