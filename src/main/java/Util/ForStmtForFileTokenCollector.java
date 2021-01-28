package Util;

import TokenGenerator.MethodToken;
import com.github.javaparser.ast.stmt.ForStmt;
import com.github.javaparser.ast.visitor.VoidVisitorAdapter;

import java.util.ArrayList;
import java.util.List;

public class ForStmtForFileTokenCollector extends VoidVisitorAdapter<Void> {
    private List<String> forStmt = new ArrayList<>();
    private List<MethodToken> methodTokenList;
    private boolean accept;

    public ForStmtForFileTokenCollector(List<MethodToken> methodTokenList) {
        this.methodTokenList = methodTokenList;
    }

    @Override
    public void visit(ForStmt vd, Void arg) {
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
            String stmt = vd.getCompare().toString();
            stmt = removeExtensionForStmt(stmt);
            stmt = removeExtensionForStmt2(stmt);
            this.forStmt.add(stmt);
        }
    }

    public List<String> getForStmt() {
        return forStmt;
    }

    private static String removeExtensionForStmt(String stmt) {
        return stmt.replaceFirst("(^Optional\\[)", "");
    }

    private static String removeExtensionForStmt2(String stmt) {
        return stmt.replaceFirst("(\\])$", "");
    }
}