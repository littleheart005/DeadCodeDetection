package Util;

import DeadVariable.MethodToken;
import DeadVariable.Variable;
import com.github.javaparser.ast.body.VariableDeclarator;
import com.github.javaparser.ast.visitor.VoidVisitorAdapter;

import java.util.*;

public class FieldNameCollector extends VoidVisitorAdapter<Void> {
    private List<Variable> variableList = new ArrayList<>();
    private List<MethodToken> methodTokenList;
    private boolean accept;

    public FieldNameCollector(List<MethodToken> methodTokenList) {
        this.methodTokenList = methodTokenList;
    }

    @Override
    public void visit(VariableDeclarator vd, Void arg) {
        super.visit(vd, arg);

        Variable variable = new Variable();

        if (this.methodTokenList.size() > 0) {
            for (int j = 0; j < this.methodTokenList.size(); j++) {
                if (vd.getRange().get().begin.line >= this.methodTokenList.get(j).getBeginLine() && vd.getRange().get().begin.line <= this.methodTokenList.get(j).getEndLine()) {
                    this.accept = false;
                    break;
                } else if (vd.getRange().get().begin.line <= this.methodTokenList.get(j).getBeginLine() || vd.getRange().get().begin.line >= this.methodTokenList.get(j).getEndLine()) {
                    this.accept = true;
                }
            }
        } else if (this.methodTokenList.size() == 0) {
            variable.setVariableName(vd.getNameAsString());
            variable.setBeginLine(vd.getRange().get().begin.line);
            variable.setModifier("common");
            this.variableList.add(variable);
        }

        if (this.accept == true) {
            variable.setVariableName(vd.getNameAsString());
            variable.setBeginLine(vd.getRange().get().begin.line);
            variable.setModifier("common");
            this.variableList.add(variable);
        }
    }

    public List<Variable> getVariableList() {
        return variableList;
    }
}
