package Util;

import com.github.javaparser.ast.expr.MethodCallExpr;
import com.github.javaparser.ast.visitor.VoidVisitorAdapter;

import java.util.ArrayList;
import java.util.List;

public class MethodCallCollector extends VoidVisitorAdapter<Void> {
    private List<String> MethodCall = new ArrayList<>();
    private Integer beginLine;
    private Integer endLine;

    public MethodCallCollector(Integer beginLine, Integer endLine) {
        this.beginLine = beginLine;
        this.endLine = endLine;
    }

    @Override
    public void visit(MethodCallExpr vd, Void arg) {
        super.visit(vd, arg);
        if (vd.getRange().get().begin.line >= beginLine && vd.getRange().get().begin.line <= endLine) {
            this.MethodCall.add(vd.getParentNodeForChildren().toString());
        }

    }

    public List<String> getMethodCall() {
        return MethodCall;
    }
}