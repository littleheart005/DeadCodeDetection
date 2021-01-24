package DeadVariable;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class Component {
    private String fileName;
    private String location;
    private List<String> variableNames = new ArrayList<>();
    private List<Integer> varDeclarationLine = new ArrayList<>();
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
    private List<String> AliveVariable = new ArrayList<>();
    private HashMap<Integer, String> DeadVariable = new HashMap<>();

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public List<String> getVariableNames() {
        return variableNames;
    }

    public void setVariableNames(List<String> variableNames) {
        this.variableNames = variableNames;
    }

    public List<Integer> getVarDeclarationLine() {
        return varDeclarationLine;
    }

    public void setVarDeclarationLine(List<Integer> varDeclarationLine) {
        this.varDeclarationLine = varDeclarationLine;
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

    public List<String> getAliveVariable() {
        return AliveVariable;
    }

    public void setAliveVariable(List<String> aliveVariable) {
        AliveVariable = aliveVariable;
    }

    public HashMap<Integer, String> getDeadVariable() {
        return DeadVariable;
    }

    public void setDeadVariable(HashMap<Integer, String> deadVariable) {
        DeadVariable = deadVariable;
    }
}
