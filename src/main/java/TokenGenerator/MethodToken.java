package TokenGenerator;

import DeadVariable.Variable;

import java.util.ArrayList;
import java.util.List;

public class MethodToken {
    private String methodName;
    private Integer beginLine;
    private Integer endLine;
    private List<Variable> variable = new ArrayList<>();
    private List<String> methodCalls = new ArrayList<>();
    private List<String> assignExpr = new ArrayList<>();
    private List<String> objectCreationExpr = new ArrayList<>();
    private List<String> IfStmt = new ArrayList<>();
    private List<String> ForStmt = new ArrayList<>();
    private List<String> ForeachStmt = new ArrayList<>();
    private List<String> ReturnStmt = new ArrayList<>();
    private List<String> WhileStmt = new ArrayList<>();
    private List<String> DoStmt = new ArrayList<>();
    private List<String> SwitchStmt = new ArrayList<>();
    private List<String> VariableDeclarator = new ArrayList<>();

    public String getMethodName() {
        return methodName;
    }

    public void setMethodName(String methodName) {
        this.methodName = methodName;
    }

    public Integer getBeginLine() {
        return beginLine;
    }

    public void setBeginLine(Integer beginLine) {
        this.beginLine = beginLine;
    }

    public Integer getEndLine() {
        return endLine;
    }

    public void setEndLine(Integer endLine) {
        this.endLine = endLine;
    }

    public List<Variable> getVariable() {
        return variable;
    }

    public void setVariable(List<Variable> variableList) {
        this.variable = variableList;
    }

    public List<String> getMethodCalls() {
        return methodCalls;
    }

    public void setMethodCalls(List<String> methodCalls) {
        this.methodCalls = methodCalls;
    }

    public List<String> getAssignExpr() {
        return assignExpr;
    }

    public void setAssignExpr(List<String> assignExpr) {
        this.assignExpr = assignExpr;
    }

    public List<String> getObjectCreationExpr() {
        return objectCreationExpr;
    }

    public void setObjectCreationExpr(List<String> objectCreationExpr) {
        this.objectCreationExpr = objectCreationExpr;
    }

    public List<String> getIfStmt() {
        return IfStmt;
    }

    public void setIfStmt(List<String> ifStmt) {
        IfStmt = ifStmt;
    }

    public List<String> getForStmt() {
        return ForStmt;
    }

    public void setForStmt(List<String> forStmt) {
        ForStmt = forStmt;
    }

    public List<String> getForeachStmt() {
        return ForeachStmt;
    }

    public void setForeachStmt(List<String> foreachStmt) {
        ForeachStmt = foreachStmt;
    }

    public List<String> getReturnStmt() {
        return ReturnStmt;
    }

    public void setReturnStmt(List<String> returnStmt) {
        ReturnStmt = returnStmt;
    }

    public List<String> getWhileStmt() {
        return WhileStmt;
    }

    public void setWhileStmt(List<String> whileStmt) {
        WhileStmt = whileStmt;
    }

    public List<String> getDoStmt() {
        return DoStmt;
    }

    public void setDoStmt(List<String> doStmt) {
        DoStmt = doStmt;
    }

    public List<String> getSwitchStmt() {
        return SwitchStmt;
    }

    public void setSwitchStmt(List<String> switchStmt) {
        SwitchStmt = switchStmt;
    }

    public List<String> getVariableDeclarator() {
        return VariableDeclarator;
    }

    public void setVariableDeclarator(List<String> variableDeclarator) {
        VariableDeclarator = variableDeclarator;
    }
}
