package Util;

import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.ImportDeclaration;
import com.github.javaparser.ast.NodeList;
import com.github.javaparser.ast.visitor.VoidVisitorAdapter;
import org.checkerframework.checker.units.qual.A;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class ImportCollector extends VoidVisitorAdapter<Void> {

    private List<String> importStm = new ArrayList<>();
    private List<String> fullImportStmt = new ArrayList<>();

    @Override
    public void visit(ImportDeclaration n, Void arg) {
        super.visit(n, arg);
//        System.out.println("Import--> "+n.getName());
        this.importStm.add(n.getNameAsString());
        this.fullImportStmt.add(removeRegexForImportStmt(n.getParentNodeForChildren().toString()));
    }

    public List<String> getImportStm() {
        return importStm;
    }

    public List<String> getFullImportStmt() {
        return fullImportStmt;
    }

    private String removeRegexForImportStmt(String importStmt) {
        String importStmtTemp = importStmt.replaceAll("import |;", "");
        return importStmtTemp;
    }
}
