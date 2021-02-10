package Util;

import TokenGenerator.MethodToken;
import com.github.javaparser.ast.stmt.SwitchStmt;
import com.github.javaparser.ast.visitor.VoidVisitorAdapter;

import java.util.ArrayList;
import java.util.List;

public class SwitchStmtForFileTokenCollector extends VoidVisitorAdapter<Void> {
    private List<String> switchStmt = new ArrayList<>();
    private List<MethodToken> methodTokenList;
    private boolean accept;

    public SwitchStmtForFileTokenCollector(List<MethodToken> methodTokenList) {
        this.methodTokenList = methodTokenList;
    }

    @Override
    public void visit(SwitchStmt vd, Void arg) {
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
            this.switchStmt.add(vd.getSelector().toString());
        }
//        System.out.println("accept = " + this.accept);
//        System.out.println();
        if (this.accept == true) {
            this.switchStmt.add(vd.getSelector().toString());
        }
    }

    public List<String> getSwitchStmt() {
        return switchStmt;
    }
}