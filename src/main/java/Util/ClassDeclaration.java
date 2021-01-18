package Util;

import com.github.javaparser.ast.NodeList;
import com.github.javaparser.ast.body.ClassOrInterfaceDeclaration;
import com.github.javaparser.ast.visitor.VoidVisitorAdapter;

import java.util.List;

public  class ClassDeclaration extends VoidVisitorAdapter<List<String>> {
    @Override
    public void visit(ClassOrInterfaceDeclaration n, List<String> arg) {
        super.visit(n, arg);

        NodeList implement;
        implement = n.getImplementedTypes();
        NodeList extend;
        extend = n.getExtendedTypes();
        for(Object node : implement){
            String tmpStr = node.toString();
            System.out.println("Implement: "+tmpStr);
        }
        for(Object node : extend){
            String tmpStr = node.toString();
            System.out.println("Extend: "+tmpStr);
        }

    }
}
