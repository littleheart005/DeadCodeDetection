package Util;

import com.github.javaparser.ast.body.VariableDeclarator;
import com.github.javaparser.ast.visitor.VoidVisitorAdapter;

import java.util.List;

public class VariableNameCollector extends VoidVisitorAdapter<List<String>> {

    @Override
    public void visit(VariableDeclarator vd, List<String> collector) {
        super.visit(vd, collector);
        collector.add(vd.getNameAsString()); //add variable name to collector
        /*System.out.println(vd.getInitializer());*/

    }
}
