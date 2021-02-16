package Util;

import com.github.javaparser.ast.expr.MethodCallExpr;
import com.github.javaparser.ast.visitor.VoidVisitorAdapter;

import java.util.ArrayList;
import java.util.List;

public class MethodCallCollector extends VoidVisitorAdapter<List<String>> {
    private List<String> MethodCall = new ArrayList<>();
    private Integer beginLine;
    private Integer endLine;

    // List for store class name of static method -> ex. Main.Create() -> store Main as String.
    private List<String> methodScope = new ArrayList<>();

    // Method argument ex. model.registerObserver((BeatObserver)this);
    // argument is (BeatObserver)this
    private List<String> methodArgument = new ArrayList<>();

    public MethodCallCollector() { }

    public MethodCallCollector(Integer beginLine, Integer endLine) {
        this.beginLine = beginLine;
        this.endLine = endLine;
    }

    @Override
    public void visit(MethodCallExpr vd, List<String> arg) {
        super.visit(vd, arg);
        arg.add(vd.getParentNodeForChildren().toString());

        // Get Scope and parse to string
        if (!vd.getScope().isEmpty()) {
            this.methodScope.add(vd.getScope().get().toString());
        }

        methodArgument.add(vd.getArguments().toString());


        if (this.beginLine != null && this.endLine != null) {
            if (vd.getRange().get().begin.line >= beginLine && vd.getRange().get().begin.line <= endLine) {
                this.MethodCall.add(vd.getParentNodeForChildren().toString());
            }
        }
    }

    public List<String> getMethodScope() {
        return methodScope;
    }

    public List<String> getMethodArgument() {
        return methodArgument;
    }

    public List<String> getMethodCall() {
        return MethodCall;
    }
}