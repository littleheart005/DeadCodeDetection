package Util;

import com.github.javaparser.ast.body.FieldDeclaration;
import com.github.javaparser.ast.visitor.VoidVisitorAdapter;

import java.util.List;


public class FieldNameCollector extends VoidVisitorAdapter<List<String>> {

    @Override
    public void visit(FieldDeclaration fd, List<String> collector) {
        super.visit(fd, collector);
        collector.add(fd.getVariable(0).toString()); //use index 0 refers to variable index
    }

}
