package Util;

import com.github.javaparser.ast.NodeList;
import com.github.javaparser.ast.body.ClassOrInterfaceDeclaration;
import com.github.javaparser.ast.visitor.VoidVisitorAdapter;

import java.util.List;

public class ClassImplementationCollector extends VoidVisitorAdapter<List<String>> {
    @Override
    public void visit(ClassOrInterfaceDeclaration n, List<String> arg) {
        super.visit(n, arg);

        NodeList implement;
        implement = n.getImplementedTypes();

        for(Object node : implement){
            String tmpStr = node.toString();
            arg.add(tmpStr);
        }
    }
}
