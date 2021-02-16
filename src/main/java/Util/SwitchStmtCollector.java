package Util;

import com.github.javaparser.ast.Node;
import com.github.javaparser.ast.NodeList;
import com.github.javaparser.ast.stmt.SwitchStmt;
import com.github.javaparser.ast.visitor.VoidVisitorAdapter;

import java.util.ArrayList;
import java.util.List;

public class SwitchStmtCollector extends VoidVisitorAdapter<List<String>> {
    private List<String> switchStmt = new ArrayList<>();
    private Integer beginLine;
    private Integer endLine;

    private List<String> body = new ArrayList<>();

    public SwitchStmtCollector() { }

    public SwitchStmtCollector(Integer beginLine, Integer endLine) {
        this.beginLine = beginLine;
        this.endLine = endLine;
    }

    @Override
    public void visit(SwitchStmt vd, List<String> arg) {
        super.visit(vd, arg);

        if (this.beginLine != null && this.endLine != null) {
            if (vd.getRange().get().begin.line >= beginLine && vd.getRange().get().begin.line <= endLine) {
                this.switchStmt.add(vd.getSelector().toString());
            }
        }

        arg.add(vd.getSelector().toString());

        // Getting cases in switch statement.
        NodeList nodeList = vd.getEntries();
        for(Object node : nodeList){
            body.add(node.toString());
        }
    }

    public List<String> getSwitchStmt() {
        return switchStmt;
    }

    public List<String> getBody() {
        return body;
    }
}