package Util;

import com.github.javaparser.ast.body.VariableDeclarator;
import com.github.javaparser.ast.visitor.VoidVisitorAdapter;

public class CheckLineOfVarDeclaration extends VoidVisitorAdapter<Void> {
    public String variableName;
    private Integer lineOfDeclaration = null;

    public CheckLineOfVarDeclaration(String variableName) {
        this.variableName= variableName;
    }

    @Override
    public void visit(VariableDeclarator vd, Void arg) {
        super.visit(vd, arg);
        if (vd.getNameAsString().equals(variableName)) {
            this.lineOfDeclaration = vd.getRange().get().begin.line;
        }
    }

    public Integer getLineOfDeclaration() {
        return lineOfDeclaration;
    }
}
