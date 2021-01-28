package Util;

import TokenGenerator.MethodToken;
import com.github.javaparser.ast.expr.ObjectCreationExpr;
import com.github.javaparser.ast.visitor.VoidVisitorAdapter;

import java.util.ArrayList;
import java.util.List;

public class ObjectCreationExprForFileTokenCollector extends VoidVisitorAdapter<Void> {
    private List<String> ObjectCreationExpr = new ArrayList<>();
    private List<MethodToken> methodTokenList;
    private boolean accept;

    public ObjectCreationExprForFileTokenCollector(List<MethodToken> methodTokenList) {
        this.methodTokenList = methodTokenList;
    }

    @Override
    public void visit(ObjectCreationExpr vd, Void arg) {
        super.visit(vd, arg);
//        System.out.println("Check for : " + vd.getParentNodeForChildren().toString() + " at line : " + vd.getRange().get().begin.line);
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
//        System.out.println("accept = " + this.accept);
//        System.out.println();
        if (this.accept == true) {
            this.ObjectCreationExpr.add(vd.getParentNodeForChildren().toString());
        }
    }

    public List<String> getObjectCreationExpr() {
        return ObjectCreationExpr;
    }
}