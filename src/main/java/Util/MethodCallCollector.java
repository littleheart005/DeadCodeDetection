package Util;


import com.github.javaparser.ast.expr.MethodCallExpr;
import com.github.javaparser.ast.visitor.VoidVisitorAdapter;

import java.util.ArrayList;
import java.util.List;

public class MethodCallCollector extends VoidVisitorAdapter<List<String>> {

    // List for store class name of static method -> ex. Main.Create() -> store Main as String.
    private static List<String> staticClassCall = new ArrayList<>();

    @Override
    public void visit(MethodCallExpr mc, List<String> collector) {
        super.visit(mc, collector);
        collector.add(mc.getParentNodeForChildren().toString());

        // Get Scope and parse to string
        if(!mc.getScope().isEmpty()){
            this.staticClassCall.add(mc.getScope().get().toString());
        }

    }
    public static List<String> getStaticClassCall() {
        return staticClassCall;
    }
}
