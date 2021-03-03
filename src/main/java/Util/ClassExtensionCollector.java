package Util;

import com.github.javaparser.ast.NodeList;
import com.github.javaparser.ast.PackageDeclaration;
import com.github.javaparser.ast.body.ClassOrInterfaceDeclaration;
import com.github.javaparser.ast.visitor.VoidVisitorAdapter;

import java.util.List;

public  class ClassExtensionCollector extends VoidVisitorAdapter<List<String>> {


    @Override
    public void visit(ClassOrInterfaceDeclaration n, List<String> arg) {
        super.visit(n, arg);

        NodeList extend;
        extend = n.getExtendedTypes();

        for(Object node : extend){
            String tmpStr = node.toString();
            arg.add(tmpStr);
        }
    }
}
