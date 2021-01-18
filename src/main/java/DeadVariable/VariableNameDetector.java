package DeadVariable;

import Util.MethodCallCollector;
import Util.VariableNameCollector;
import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.visitor.VoidVisitor;

import java.util.ArrayList;
import java.util.List;

public class VariableNameDetector {
    List<CompilationUnit> cu;

    List<String> variableNames = new ArrayList<>();
    List<String> methodCalls = new ArrayList<>();
    VoidVisitor<List<String>> variableNameCollector = new VariableNameCollector();
    VoidVisitor<List<String>> methodCallCollector = new MethodCallCollector();

    public VariableNameDetector(List<CompilationUnit> cu) {
        this.cu = new ArrayList<>(cu);

        for (int i=0; i<this.cu.size(); i++){
            this.variableNameCollector.visit(this.cu.get(i), this.variableNames);
            this.methodCallCollector.visit(this.cu.get(i), this.methodCalls);
        }

        for (String s : this.variableNames) {
            System.out.println(s);
        }

        for (String s : this.methodCalls) {
            System.out.println(s);
        }
    }
}
