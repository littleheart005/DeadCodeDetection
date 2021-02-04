package Util;

import com.github.javaparser.ast.Node;
import com.github.javaparser.ast.NodeList;
import com.github.javaparser.ast.stmt.SwitchStmt;
import com.github.javaparser.ast.visitor.VoidVisitorAdapter;

import java.util.ArrayList;
import java.util.List;

public class SwitchStmtCollector extends VoidVisitorAdapter<List<String>> {
    private List<String> body = new ArrayList<>();

    @Override
    public void visit(SwitchStmt is, List<String> collector) {
        super.visit(is, collector);
        collector.add(is.getSelector().toString());

        // Getting cases in switch statement.
        NodeList nodeList = is.getEntries();
        for(Object node : nodeList){
            body.add(node.toString());
        }
    }

    public List<String> getBody() {
        return body;
    }
}