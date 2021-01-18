package DeadVariable;

import Util.FieldNameCollector;
import Util.NameExprLineNumberCollector;
import Util.VariableNameCollector;
import com.github.javaparser.TokenRange;
import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.expr.Name;
import com.github.javaparser.ast.modules.ModuleDeclaration;
import com.github.javaparser.ast.visitor.VoidVisitor;
import org.checkerframework.checker.nullness.Opt;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class VariableNameDetector {
    List<CompilationUnit> cu;
    List<String> variableNames = new ArrayList<>();
    List<Integer> nameExprLineNumber = new ArrayList<>();
    VoidVisitor<List<String>> fieldNameCollector = new FieldNameCollector();
    VoidVisitor<List<String>> variableNameCollector = new VariableNameCollector();
    VoidVisitor<List<Integer>> nameExprLineNumberCollector = new NameExprLineNumberCollector();

    public VariableNameDetector(List<CompilationUnit> cu) {
        this.cu = new ArrayList<>(cu);

        for (int i=0; i<this.cu.size(); i++){
            this.fieldNameCollector.visit(this.cu.get(i), this.variableNames);
            this.variableNameCollector.visit(this.cu.get(i), this.variableNames);
            this.nameExprLineNumberCollector.visit(this.cu.get(i), this.nameExprLineNumber);
        }

        for (String s : this.variableNames) {
            System.out.println(s);
        }
    }
}
