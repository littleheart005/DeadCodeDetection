package Util;

import com.github.javaparser.ast.expr.ObjectCreationExpr;
import com.github.javaparser.ast.visitor.VoidVisitorAdapter;

import java.util.ArrayList;
import java.util.List;

public class ObjectCreationExprCollector extends VoidVisitorAdapter<Void> {
    private List<String> ObjectCreationExpr = new ArrayList<>();
    private Integer beginLine;
    private Integer endLine;

    public ObjectCreationExprCollector(Integer beginLine, Integer endLine) {
        this.beginLine = beginLine;
        this.endLine = endLine;
    }

    @Override
    public void visit(ObjectCreationExpr vd, Void arg) {
        super.visit(vd, arg);
        if (vd.getRange().get().begin.line >= beginLine && vd.getRange().get().begin.line <= endLine) {
            this.ObjectCreationExpr.add(vd.getParentNodeForChildren().toString());
        }
    }

    public List<String> getObjectCreationExpr() {
        return ObjectCreationExpr;
    }
}