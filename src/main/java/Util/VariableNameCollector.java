package Util;

import DeadVariable.Variable;
import com.github.javaparser.ast.body.VariableDeclarator;
import com.github.javaparser.ast.visitor.VoidVisitorAdapter;

import java.util.ArrayList;
import java.util.List;

public class VariableNameCollector extends VoidVisitorAdapter<Void> {
    private List<Variable> variableList = new ArrayList<>();
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
            Variable variable = new Variable();
            variable.setVariableName(vd.getNameAsString());
            variable.setBeginLine(vd.getRange().get().begin.line);
            variable.setModifier("local");
            this.variableList.add(variable);
        }
    }

    public List<Variable> getVariableList() {
        return variableList;
    }
}