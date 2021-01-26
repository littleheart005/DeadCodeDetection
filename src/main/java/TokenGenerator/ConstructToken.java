package TokenGenerator;

import Util.*;
import com.github.javaparser.ast.CompilationUnit;

import java.util.ArrayList;
import java.util.List;

public class ConstructToken {
    List<CompilationUnit> cu;
    List<Token> tokenList = new ArrayList<>();

    public ConstructToken(List<CompilationUnit> cu, List<String> location) {
        this.cu = new ArrayList<>(cu);

        FileNameCollector fileNameCollector = new FileNameCollector();
        VariableNameCollector variableNameCollector = new VariableNameCollector();
        MethodCallCollector methodCallCollector = new MethodCallCollector();
        AssignExprCollector assignExprCollector = new AssignExprCollector();
        ObjectCreationExprCollector objectCreationExprCollector = new ObjectCreationExprCollector();
        IfStmtCollector ifStmtCollector = new IfStmtCollector();
        ForStmtCollector forStmtCollector = new ForStmtCollector();
        ForeachStmtCollector foreachStmtCollector = new ForeachStmtCollector();
        ReturnStmtCollector returnStmtCollector = new ReturnStmtCollector();
        WhileStmtCollector whileStmtCollector = new WhileStmtCollector();
        DoStmtCollector doStmtCollector = new DoStmtCollector();
        SwitchStmtCollector switchStmtCollector = new SwitchStmtCollector();
        VariableDeclaratorCollector variableDeclaratorCollector = new VariableDeclaratorCollector();

        //loop to construct component
        for (int i=0; i<this.cu.size(); i++) {
            Token tokenTemp = new Token();
            setFileNameToComponentTemp(this.cu.get(i), tokenTemp, fileNameCollector);
            setLocation(tokenTemp, location.get(i));
            setVariableNamesToComponentTemp(this.cu.get(i), tokenTemp, variableNameCollector);
            setMethodCallsToComponentTemp(this.cu.get(i), tokenTemp, methodCallCollector);//1
            setAssignExprToComponentTemp(this.cu.get(i), tokenTemp, assignExprCollector); //2
            setObjectCreationExprToComponentTemp(this.cu.get(i), tokenTemp, objectCreationExprCollector); //3
            setIfStmtToComponentTemp(this.cu.get(i), tokenTemp, ifStmtCollector); //4
            setForStmtToComponentTemp(this.cu.get(i), tokenTemp, forStmtCollector); //5
            setForeachStmtToComponentTemp(this.cu.get(i), tokenTemp, foreachStmtCollector); //6
            setReturnStmtToComponentTemp(this.cu.get(i), tokenTemp, returnStmtCollector); //7
            setWhileStmtToComponentTemp(this.cu.get(i), tokenTemp, whileStmtCollector); //8
            setDoStmtToComponentTemp(this.cu.get(i), tokenTemp, doStmtCollector); //9
            setSwitchStmtToComponentTemp(this.cu.get(i), tokenTemp, switchStmtCollector); //10
            setVariableDeclaratorToComponentTemp(this.cu.get(i), tokenTemp, variableDeclaratorCollector); //11
            this.tokenList.add(tokenTemp);
        }
    }

    //to set location of file to componentTemp
    private void setLocation(Token tokenTemp, String location) {
        if (location.contains(tokenTemp.getFileName()+".java")) {
            tokenTemp.setLocation(location);
        }
    }

    //to set fileName in file to componentTemp
    private void setFileNameToComponentTemp(CompilationUnit cu, Token tokenTemp, FileNameCollector fileNameCollector) {
        fileNameCollector.visit(cu, null);
        tokenTemp.setFileName(fileNameCollector.getFileName());
    }

    //to set all variables (with number of line declaration) in file to componentTemp
    private void setVariableNamesToComponentTemp(CompilationUnit cu, Token tokenTemp, VariableNameCollector variableNameCollector) {
        List<String> variableNamesForCollector = new ArrayList<>();
        List<Integer> varDeclarationLine = new ArrayList<>();
        variableNameCollector.visit(cu, variableNamesForCollector);
        tokenTemp.setVariableNames(variableNamesForCollector);
        tokenTemp.setVarDeclarationLine(varDeclarationLine);
    }

    //to set method call in file to componentTemp
    private void setMethodCallsToComponentTemp(CompilationUnit cu, Token tokenTemp, MethodCallCollector methodCallCollector) {
        List<String> methodCallsForCollector = new ArrayList<>();
        methodCallCollector.visit(cu, methodCallsForCollector);
        tokenTemp.setMethodCalls(methodCallsForCollector);
    }

    //to set if statement in file to componentTemp
    private void setAssignExprToComponentTemp(CompilationUnit cu, Token tokenTemp, AssignExprCollector assignExprCollector) {
        List<String> assignExprForCollector = new ArrayList<>();
        assignExprCollector.visit(cu, assignExprForCollector);
        tokenTemp.setAssignExpr(assignExprForCollector);
    }

    //to set if statement in file to componentTemp
    private void setObjectCreationExprToComponentTemp(CompilationUnit cu, Token tokenTemp, ObjectCreationExprCollector objectCreationExprCollector) {
        List<String> objectCreationExprForCollector = new ArrayList<>();
        objectCreationExprCollector.visit(cu, objectCreationExprForCollector);
        tokenTemp.setObjectCreationExpr(objectCreationExprForCollector);
    }

    //to set if statement in file to componentTemp
    private void setIfStmtToComponentTemp(CompilationUnit cu, Token tokenTemp, IfStmtCollector ifStmtCollector) {
        List<String> ifStmtForCollector = new ArrayList<>();
        ifStmtCollector.visit(cu, ifStmtForCollector);
        tokenTemp.setIfStmt(ifStmtForCollector);
    }

    //to set for statement in file to componentTemp
    private void setForStmtToComponentTemp(CompilationUnit cu, Token tokenTemp, ForStmtCollector forStmtCollector) {
        List<String> forStmtForCollector = new ArrayList<>();
        forStmtCollector.visit(cu, forStmtForCollector);
        tokenTemp.setForStmt(forStmtForCollector);
    }

    //to set foreach statement in file to componentTemp
    private void setForeachStmtToComponentTemp(CompilationUnit cu, Token tokenTemp, ForeachStmtCollector foreachStmtCollector) {
        List<String> foreachStmtForCollector = new ArrayList<>();
        foreachStmtCollector.visit(cu, foreachStmtForCollector);
        tokenTemp.setForeachStmt(foreachStmtForCollector);
    }

    //to set return statement in file to componentTemp
    private void setReturnStmtToComponentTemp(CompilationUnit cu, Token tokenTemp, ReturnStmtCollector returnStmtCollector) {
        List<String> returnStmtForCollector = new ArrayList<>();
        returnStmtCollector.visit(cu, returnStmtForCollector);
        tokenTemp.setReturnStmt(returnStmtForCollector);
    }

    //to set while statement in file to componentTemp
    private void setWhileStmtToComponentTemp(CompilationUnit cu, Token tokenTemp, WhileStmtCollector whileStmtCollector) {
        List<String> whileStmtForCollector = new ArrayList<>();
        whileStmtCollector.visit(cu, whileStmtForCollector);
        tokenTemp.setWhileStmt(whileStmtForCollector);
    }

    //to set do statement in file to componentTemp
    private void setDoStmtToComponentTemp(CompilationUnit cu, Token tokenTemp, DoStmtCollector doStmtCollector) {
        List<String> doStmtForCollector = new ArrayList<>();
        doStmtCollector.visit(cu, doStmtForCollector);
        tokenTemp.setDoStmt(doStmtForCollector);
    }

    //to set switch statement in file to componentTemp
    private void setSwitchStmtToComponentTemp(CompilationUnit cu, Token tokenTemp, SwitchStmtCollector switchStmtCollector) {
        List<String> switchStmtForCollector = new ArrayList<>();
        switchStmtCollector.visit(cu, switchStmtForCollector);
        tokenTemp.setSwitchStmt(switchStmtForCollector);
    }

    //to set variable declarator in file to componentTemp
    private void setVariableDeclaratorToComponentTemp(CompilationUnit cu, Token tokenTemp, VariableDeclaratorCollector variableDeclaratorCollector) {
        List<String> variableDeclaratorForCollector = new ArrayList<>();
        variableDeclaratorCollector.visit(cu, variableDeclaratorForCollector);
        tokenTemp.setVariableDeclarator(variableDeclaratorForCollector);
    }

    public List<Token> getComponentList() {
        return tokenList;
    }
}