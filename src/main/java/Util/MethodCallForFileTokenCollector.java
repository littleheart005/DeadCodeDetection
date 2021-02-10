package Util;

import TokenGenerator.MethodToken;
import com.github.javaparser.ast.expr.MethodCallExpr;
import com.github.javaparser.ast.visitor.VoidVisitorAdapter;

import java.util.ArrayList;
import java.util.List;

public class MethodCallForFileTokenCollector extends VoidVisitorAdapter<Void> {
    private List<String> MethodCall = new ArrayList<>();
    private List<MethodToken> methodTokenList;
    private boolean accept;

    public MethodCallForFileTokenCollector(List<MethodToken> methodTokenList) {
        this.methodTokenList = methodTokenList;
    }

    @Override
    public void visit(MethodCallExpr vd, Void arg) {
        super.visit(vd, arg);
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
            this.MethodCall.add(vd.getParentNodeForChildren().toString());
        }
//        System.out.println("accept = " + this.accept);
//        System.out.println();
        if (this.accept == true) {
            this.MethodCall.add(vd.getParentNodeForChildren().toString());
        }
    }

    public List<String> getMethodCall() {
        return MethodCall;
    }
}