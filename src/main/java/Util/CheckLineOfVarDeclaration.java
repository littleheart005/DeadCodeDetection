package Util;

import com.github.javaparser.ast.body.VariableDeclarator;
import com.github.javaparser.ast.visitor.VoidVisitorAdapter;

public class CheckLineOfVarDeclaration extends VoidVisitorAdapter<Void> {
    public String variableName;
    private Integer lineOfDeclaration = null;
    private Integer beginLineOfMethod;
    private Integer endLineOfMethod;

    public CheckLineOfVarDeclaration(String variableName, Integer beginLineOfMethod, Integer endLineOfMethod) {
        this.variableName= variableName;
        this.beginLineOfMethod = beginLineOfMethod;
        this.endLineOfMethod = endLineOfMethod;
    }

    @Override
    public void visit(VariableDeclarator vd, Void arg) {
        super.visit(vd, arg);
        if (vd.getRange().get().begin.line >= beginLineOfMethod && vd.getRange().get().begin.line <= endLineOfMethod) {
            if (vd.getNameAsString().equals(variableName)) {
                this.lineOfDeclaration = vd.getRange().get().begin.line;
            }
        }
    }

    public Integer getLineOfDeclaration() {
        return lineOfDeclaration;
    }
}
