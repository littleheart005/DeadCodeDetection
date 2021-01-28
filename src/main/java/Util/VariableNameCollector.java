package Util;

import com.github.javaparser.ast.body.VariableDeclarator;
import com.github.javaparser.ast.visitor.VoidVisitorAdapter;

import java.util.ArrayList;
import java.util.List;

public class VariableNameCollector extends VoidVisitorAdapter<Void> {
    private List<String> variableNames = new ArrayList<>();
    private Integer beginLine;
    private Integer endLine;

    public VariableNameCollector(Integer beginLine, Integer endLine) {
        this.beginLine = beginLine;
        this.endLine = endLine;
    }

    @Override
    public void visit(VariableDeclarator vd, Void arg) {
        super.visit(vd, arg);
        if (vd.getRange().get().begin.line >= beginLine && vd.getRange().get().begin.line <= endLine) {
            this.variableNames.add(vd.getNameAsString());
        }
    }

    public List<String> getVariableNames() {
        return variableNames;
    }
}