package Util;

import com.github.javaparser.ast.body.Parameter;
import com.github.javaparser.ast.visitor.VoidVisitorAdapter;

import java.util.List;

public  class ParameterCollector extends VoidVisitorAdapter<List<String>> {
    @Override
    public void visit(Parameter n, List<String> arg) {
        super.visit(n, arg);

        //System.out.println("Parameter: "+n.getTypeAsString()+" "+n.getName());

        if(!n.getTypeAsString().contains("String") && !n.getTypeAsString().contains("double")
                && !n.getTypeAsString().contains("int")){
            arg.add(n.getTypeAsString());
        }
    }
}
