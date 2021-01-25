package Util;

import com.github.javaparser.ast.body.VariableDeclarator;
import com.github.javaparser.ast.visitor.VoidVisitorAdapter;

import java.util.List;

public class VariableDeclaratorCollector extends VoidVisitorAdapter<List<String>> {

    @Override
    public void visit(VariableDeclarator is, List<String> collector) {
        super.visit(is, collector);
        collector.add(is.getParentNodeForChildren().toString());
    }
}