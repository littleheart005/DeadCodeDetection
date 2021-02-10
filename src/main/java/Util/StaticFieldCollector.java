package Util;

import DeadVariable.Variable;
import com.github.javaparser.ast.Modifier;
import com.github.javaparser.ast.body.ClassOrInterfaceDeclaration;
import com.github.javaparser.ast.body.FieldDeclaration;
import com.github.javaparser.ast.visitor.VoidVisitorAdapter;

import java.util.ArrayList;
import java.util.List;

public class StaticFieldCollector extends VoidVisitorAdapter<Void> {
    private List<Variable> variableList = new ArrayList<>();

    @Override
    public void visit(ClassOrInterfaceDeclaration s, Void arg) {
        super.visit(s, arg);
        for (FieldDeclaration ff : s.getFields()) {
            if (ff.getModifiers().contains(Modifier.staticModifier())) {
                Variable variable = new Variable();
                variable.setVariableName(ff.getVariables().stream().iterator().next().getNameAsString());
                variable.setParent(s.getName().toString());
                variable.setModifier("static");
                variable.setBeginLine(ff.getVariables().stream().iterator().next().getRange().get().begin.line);
                this.variableList.add(variable);
            }
        }
    }

    public List<Variable> getVariableList() {
        return variableList;
    }
}
