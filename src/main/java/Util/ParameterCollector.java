package Util;

import com.github.javaparser.ast.body.Parameter;
import com.github.javaparser.ast.visitor.VoidVisitorAdapter;

import java.util.ArrayList;
import java.util.List;

public  class ParameterCollector extends VoidVisitorAdapter<List<String>> {
    private List<String> fullParameterList = new ArrayList<>();
    @Override
    public void visit(Parameter n, List<String> arg) {
        super.visit(n, arg);
        if(!n.getTypeAsString().contains("String") && !n.getTypeAsString().contains("double")
                && !n.getTypeAsString().contains("int")){
            arg.add(n.getTypeAsString());
        }
        fullParameterList.add(n.getTypeAsString());
    }

    public List<String> getFullParameterList() {
        return fullParameterList;
    }
}
