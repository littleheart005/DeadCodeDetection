package Util;

import com.github.javaparser.ast.stmt.ForStmt;
import com.github.javaparser.ast.visitor.VoidVisitorAdapter;

import java.util.ArrayList;
import java.util.List;

public class ForStmtCollector extends VoidVisitorAdapter<List<String>> {

    @Override
    public void visit(ForStmt is, List<String> collector) {
        super.visit(is, collector);
        if(is.getCompare().isPresent()){
            String stmt = is.getCompare().get().toString();
            collector.add(stmt);
        }
    }
}