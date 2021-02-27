package Util;

import DeadVariable.MethodToken;
import com.github.javaparser.ast.body.VariableDeclarator;
import com.github.javaparser.ast.visitor.VoidVisitorAdapter;

import java.util.List;

public class CheckLineOfVarDeclarationForField extends VoidVisitorAdapter<Void> {
    public String variableName;
    private List<MethodToken> methodTokenList;
    private Integer lineOfDeclaration = null;
    private boolean accept;

    public CheckLineOfVarDeclarationForField(List<MethodToken> methodTokenList, String variableName) {
        this.variableName= variableName;
        this.methodTokenList = methodTokenList;
    }

    @Override
    public void visit(VariableDeclarator vd, Void arg) {
        super.visit(vd, arg);
        if (vd.getNameAsString().equals(variableName)) {
            //        System.out.println("Check for : " + vd.getParentNodeForChildren().toString() + " at line : " + vd.getRange().get().begin.line);
            if (this.methodTokenList.size() > 0) {
                for (int i=0; i<this.methodTokenList.size(); i++) {
                    if (vd.getRange().get().begin.line >= this.methodTokenList.get(i).getBeginLine() && vd.getRange().get().begin.line <= this.methodTokenList.get(i).getEndLine()) {
                        this.accept = false;
//                System.out.println("String is in method line between " + this.methodTokenList.get(i).getBeginLine() + " - " + this.methodTokenList.get(i).getEndLine());
                        break;
                    }
                    else if (vd.getRange().get().begin.line <= this.methodTokenList.get(i).getBeginLine() || vd.getRange().get().begin.line >= this.methodTokenList.get(i).getEndLine()){
                        this.accept = true;
//                System.out.println("String is not in method line between " + this.methodTokenList.get(i).getBeginLine() + " - " + this.methodTokenList.get(i).getEndLine());
                    }
                }
            }
            else if (this.methodTokenList.size() == 0) {
                this.lineOfDeclaration = vd.getRange().get().begin.line;
            }

//        System.out.println("accept = " + this.accept);
//        System.out.println();
            if (this.accept == true) {
                this.lineOfDeclaration = vd.getRange().get().begin.line;
            }
        }
    }

    public Integer getLineOfDeclaration() {
        return lineOfDeclaration;
    }
}
