package DeadVariable;

import java.util.*;

public class FileToken {
    private String fileName;
    private String location;
    private String packageName;

    private List<String> extendsClassStmt = new ArrayList<>();
    private List<FileToken> extendsClass = new ArrayList<>();
    private List<String> importClassStmt = new ArrayList<>();
    private List<FileToken> importClass = new ArrayList<>();
    private List<FileToken> FileInSamePackageToDetect = new ArrayList<>();

    private List<FileToken> childClassToDetect = new ArrayList<>();
    private List<FileToken> classThatImportToDetect = new ArrayList<>();

    private List<Variable> staticField = new ArrayList<>();
    private List<Variable> field = new ArrayList<>();

    private List<MethodToken> methodTokenList = new ArrayList<>();

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

    private List<Variable> AliveStaticField = new ArrayList<>();
    private List<Variable> DeadStaticField = new ArrayList<>();
    private List<Variable> AliveField = new ArrayList<>();
    private List<Variable> DeadField = new ArrayList<>();
    private List<Variable> AliveVariable = new ArrayList<>();
    private List<Variable> DeadVariable = new ArrayList<>();
    private List<Variable> AllAliveVariable = new ArrayList<>();
    private List<Variable> AllDeadVariable = new ArrayList<>();

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

    public String getPackageName() {
        return packageName;
    }

    public void setPackageName(String packageName) {
        this.packageName = packageName;
    }

    public List<FileToken> getFileInSamePackageToDetect() {
        return FileInSamePackageToDetect;
    }

    public void setFileInSamePackageToDetect(List<FileToken> fileInSamePackageToDetect) {
        FileInSamePackageToDetect = fileInSamePackageToDetect;
    }



    public List<String> getExtendsClassStmt() {
        return extendsClassStmt;
    }

    public void setExtendsClassStmt(List<String> extendsClassStmt) {
        this.extendsClassStmt = extendsClassStmt;
    }

    public List<String> getImportClassStmt() {
        return importClassStmt;
    }

    public void setImportClassStmt(List<String> importClassStmt) {
        this.importClassStmt = importClassStmt;
    }



    public List<FileToken> getExtendsClass() {
        return extendsClass;
    }

    public void setExtendsClass(List<FileToken> extendsClass) {
        this.extendsClass = extendsClass;
    }

    public List<FileToken> getImportClass() {
        return importClass;
    }

    public void setImportClass(List<FileToken> importClass) {
        this.importClass = importClass;
    }



    public List<FileToken> getChildClassToDetect() {
        return childClassToDetect;
    }

    public void addChildClassToDetect(FileToken childClassToDetect) {
        this.childClassToDetect.add(childClassToDetect);
    }

    public List<FileToken> getClassThatImportToDetect() {
        return classThatImportToDetect;
    }

    public void addClassThatImportToDetect(FileToken classThatImportToDetect) {
        this.classThatImportToDetect.add(classThatImportToDetect);
    }



    public List<Variable> getStaticField() {
        return staticField;
    }

    public List<Variable> getField() {
        return field;
    }

    public void setStaticField(List<Variable> staticFieldList) {
        this.staticField = staticFieldList;
    }

    public void setField(List<Variable> field) {
        this.field = field;
    }



    public List<MethodToken> getMethodTokenList() {
        return methodTokenList;
    }

    public void setMethodTokenList(List<MethodToken> methodTokenList) {
        this.methodTokenList = methodTokenList;
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



    public List<Variable> getDeadStaticField() {
        return DeadStaticField;
    }

    public List<Variable> getDeadField() {
        return DeadField;
    }

    public List<Variable> getDeadVariable() {
        return DeadVariable;
    }

    public void setDeadStaticField(List<Variable> deadStaticField) {
        DeadStaticField = deadStaticField;
    }

    public void setDeadField(List<Variable> deadField) {
        DeadField = deadField;
    }

    public void addDeadVariable(List<Variable> deadVariable) {
        this.DeadVariable.addAll(deadVariable);
    }



    public List<Variable> getAliveStaticField() {
        return AliveStaticField;
    }

    public List<Variable> getAliveField() {
        return AliveField;
    }

    public List<Variable> getAliveVariable() {
        return AliveVariable;
    }

    public void setAliveStaticField(List<Variable> aliveStaticField) {
        AliveStaticField = aliveStaticField;
    }

    public void setAliveField(List<Variable> aliveField) {
        AliveField = aliveField;
    }

    public void setAliveVariable(List<Variable> aliveVariable) {
        AliveVariable = aliveVariable;
    }



    public List<Variable> getAllAliveVariable() {
        return AllAliveVariable;
    }

    public List<Variable> getAllDeadVariable() {
        return AllDeadVariable;
    }

    public void setAllAliveVariable(List<Variable> allAliveVariable) {
        AllAliveVariable = allAliveVariable;
    }

    public void setAllDeadVariable(List<Variable> allDeadVariable) {
        AllDeadVariable = allDeadVariable;
    }
}
