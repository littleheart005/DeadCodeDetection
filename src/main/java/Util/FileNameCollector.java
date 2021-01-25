package Util;

import com.github.javaparser.ast.body.ClassOrInterfaceDeclaration;
import com.github.javaparser.ast.visitor.VoidVisitorAdapter;

public class FileNameCollector extends VoidVisitorAdapter<Void> {
    private String fileName = null;

    //get class name of FILE
    @Override
    public void visit(ClassOrInterfaceDeclaration declaration, Void arg) {
        super.visit(declaration, arg);
        if (declaration.isClassOrInterfaceDeclaration()) {
            this.fileName = declaration.getNameAsString();
        }
    }

    public String getFileName() {
        return fileName;
    }
}
