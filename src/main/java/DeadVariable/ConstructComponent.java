package DeadVariable;

import Util.*;
import com.github.javaparser.ast.CompilationUnit;

import java.util.ArrayList;
import java.util.List;

public class ConstructComponent {
    List<CompilationUnit> cu;
    List<Component> componentList = new ArrayList<>();

    public ConstructComponent(List<CompilationUnit> cu) {
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


        //to construct component
        for (int i=0; i<this.cu.size(); i++) {
            Component componentTemp = new Component();
            setFileNameToComponentTemp(this.cu.get(i), componentTemp, fileNameCollector);
            setVariableNamesToComponentTemp(this.cu.get(i), componentTemp, variableNameCollector);
            setMethodCallsToComponentTemp(this.cu.get(i), componentTemp, methodCallCollector);//1
            setAssignExprToComponentTemp(this.cu.get(i), componentTemp, assignExprCollector); //2
            setObjectCreationExprToComponentTemp(this.cu.get(i), componentTemp, objectCreationExprCollector); //3
            setIfStmtToComponentTemp(this.cu.get(i), componentTemp, ifStmtCollector); //4
            setForStmtToComponentTemp(this.cu.get(i), componentTemp, forStmtCollector); //5
            setForeachStmtToComponentTemp(this.cu.get(i), componentTemp, foreachStmtCollector); //6
            setReturnStmtToComponentTemp(this.cu.get(i), componentTemp, returnStmtCollector); //7
            setWhileStmtToComponentTemp(this.cu.get(i), componentTemp, whileStmtCollector); //8
            setDoStmtToComponentTemp(this.cu.get(i), componentTemp, doStmtCollector); //9
            setSwitchStmtToComponentTemp(this.cu.get(i), componentTemp, switchStmtCollector); //10
            setVariableDeclaratorToComponentTemp(this.cu.get(i), componentTemp, variableDeclaratorCollector); //11
            this.componentList.add(componentTemp);
        }
    }

    //to set fileName in file to componentTemp
    private void setFileNameToComponentTemp(CompilationUnit cu, Component componentTemp, FileNameCollector fileNameCollector) {
        fileNameCollector.visit(cu, null);
        componentTemp.setFileName(fileNameCollector.getFileName());
    }

    //to set all variables in file to componentTemp
    private void setVariableNamesToComponentTemp(CompilationUnit cu, Component componentTemp, VariableNameCollector variableNameCollector) {
        List<String> variableNamesForCollector = new ArrayList<>();
        variableNameCollector.visit(cu, variableNamesForCollector);
        componentTemp.setVariableNames(variableNamesForCollector);
    }

    //to set method call in file to componentTemp
    private void setMethodCallsToComponentTemp(CompilationUnit cu, Component componentTemp, MethodCallCollector methodCallCollector) {
        List<String> methodCallsForCollector = new ArrayList<>();
        methodCallCollector.visit(cu, methodCallsForCollector);
        componentTemp.setMethodCalls(methodCallsForCollector);
    }

    //to set if statement in file to componentTemp
    private void setAssignExprToComponentTemp(CompilationUnit cu, Component componentTemp, AssignExprCollector assignExprCollector) {
        List<String> assignExprForCollector = new ArrayList<>();
        assignExprCollector.visit(cu, assignExprForCollector);
        componentTemp.setAssignExpr(assignExprForCollector);
    }

    //to set if statement in file to componentTemp
    private void setObjectCreationExprToComponentTemp(CompilationUnit cu, Component componentTemp, ObjectCreationExprCollector objectCreationExprCollector) {
        List<String> objectCreationExprForCollector = new ArrayList<>();
        objectCreationExprCollector.visit(cu, objectCreationExprForCollector);
        componentTemp.setObjectCreationExpr(objectCreationExprForCollector);
    }

    //to set if statement in file to componentTemp
    private void setIfStmtToComponentTemp(CompilationUnit cu, Component componentTemp, IfStmtCollector ifStmtCollector) {
        List<String> ifStmtForCollector = new ArrayList<>();
        ifStmtCollector.visit(cu, ifStmtForCollector);
        componentTemp.setIfStmt(ifStmtForCollector);
    }

    //to set for statement in file to componentTemp
    private void setForStmtToComponentTemp(CompilationUnit cu, Component componentTemp, ForStmtCollector forStmtCollector) {
        List<String> forStmtForCollector = new ArrayList<>();
        forStmtCollector.visit(cu, forStmtForCollector);
        componentTemp.setForStmt(forStmtForCollector);
    }

    //to set foreach statement in file to componentTemp
    private void setForeachStmtToComponentTemp(CompilationUnit cu, Component componentTemp, ForeachStmtCollector foreachStmtCollector) {
        List<String> foreachStmtForCollector = new ArrayList<>();
        foreachStmtCollector.visit(cu, foreachStmtForCollector);
        componentTemp.setForeachStmt(foreachStmtForCollector);
    }

    //to set return statement in file to componentTemp
    private void setReturnStmtToComponentTemp(CompilationUnit cu, Component componentTemp, ReturnStmtCollector returnStmtCollector) {
        List<String> returnStmtForCollector = new ArrayList<>();
        returnStmtCollector.visit(cu, returnStmtForCollector);
        componentTemp.setReturnStmt(returnStmtForCollector);
    }

    //to set while statement in file to componentTemp
    private void setWhileStmtToComponentTemp(CompilationUnit cu, Component componentTemp, WhileStmtCollector whileStmtCollector) {
        List<String> whileStmtForCollector = new ArrayList<>();
        whileStmtCollector.visit(cu, whileStmtForCollector);
        componentTemp.setWhileStmt(whileStmtForCollector);
    }

    //to set do statement in file to componentTemp
    private void setDoStmtToComponentTemp(CompilationUnit cu, Component componentTemp, DoStmtCollector doStmtCollector) {
        List<String> doStmtForCollector = new ArrayList<>();
        doStmtCollector.visit(cu, doStmtForCollector);
        componentTemp.setDoStmt(doStmtForCollector);
    }

    //to set switch statement in file to componentTemp
    private void setSwitchStmtToComponentTemp(CompilationUnit cu, Component componentTemp, SwitchStmtCollector switchStmtCollector) {
        List<String> switchStmtForCollector = new ArrayList<>();
        switchStmtCollector.visit(cu, switchStmtForCollector);
        componentTemp.setSwitchStmt(switchStmtForCollector);
    }

    //to set variable declarator in file to componentTemp
    private void setVariableDeclaratorToComponentTemp(CompilationUnit cu, Component componentTemp, VariableDeclaratorCollector variableDeclaratorCollector) {
        List<String> variableDeclaratorForCollector = new ArrayList<>();
        variableDeclaratorCollector.visit(cu, variableDeclaratorForCollector);
        componentTemp.setVariableDeclarator(variableDeclaratorForCollector);
    }

    public List<Component> getComponentList() {
        return componentList;
    }
}