package Util;

import com.github.javaparser.ast.body.Parameter;
import com.github.javaparser.ast.visitor.VoidVisitorAdapter;

import java.util.List;

public  class ParameterCollector extends VoidVisitorAdapter<List<String>> {
    @Override
    public void visit(Parameter n, List<String> arg) {
        super.visit(n, arg);
        //System.out.println("Parameter: "+n.toString());
        System.out.println("Parameter: "+n.getParentNodeForChildren());
    }
}
