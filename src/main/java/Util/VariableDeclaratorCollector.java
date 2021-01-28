package Util;

import com.github.javaparser.ast.body.VariableDeclarator;
import com.github.javaparser.ast.visitor.VoidVisitorAdapter;

import java.util.ArrayList;
import java.util.List;

public class VariableDeclaratorCollector extends VoidVisitorAdapter<Void> {
    private List<String> variableDeclarator = new ArrayList<>();
    private Integer beginLine;
    private Integer endLine;

    public VariableDeclaratorCollector(Integer beginLine, Integer endLine) {
        this.beginLine = beginLine;
        this.endLine = endLine;
    }

    @Override
    public void visit(VariableDeclarator vd, Void arg) {
        super.visit(vd, arg);
        if (vd.getRange().get().begin.line >= beginLine && vd.getRange().get().begin.line <= endLine) {
            this.variableDeclarator.add(vd.getParentNodeForChildren().toString());
        }
    }

    public List<String> getVariableDeclarator() {
        return variableDeclarator;
    }
}