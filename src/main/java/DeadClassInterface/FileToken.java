package DeadClassInterface;

import Util.*;
import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.visitor.VoidVisitor;

import java.util.*;

// Token for store each class data
public class FileToken {

    private String fileName;
    private String location;
    private String packageName;

    private final List<String> importStm = new ArrayList<>();
    private final List<String> className = new ArrayList<>();
    private final List<String> interfaceName = new ArrayList<>();

    // Map of class/interface with declaration line
    private final Map<String,Integer> classLine = new HashMap<>();
    private final Map<String,Integer> interfaceLine = new HashMap<>();

    // List of all necessary item, which to look up for the name usage.
    private final List<String> extendedList = new ArrayList<>();
    private final List<String> implementList = new ArrayList<>();
    private final List<String> variableType = new ArrayList<>();
    private final List<String> methodType = new ArrayList<>();
    private final List<String> parameterType = new ArrayList<>();
    private final List<String> objectAssignmentType = new ArrayList<>();
    private final List<String> methodScope = new ArrayList<>();
    private final List<String> methodArgument = new ArrayList<>();
    private final List<String> returnStm = new ArrayList<>();
    private final List<String> ifStm = new ArrayList<>();
    private final List<String> forStm = new ArrayList<>();
    private final List<String> forEachStm = new ArrayList<>();
    private final List<String> switchStm = new ArrayList<>();
    private final List<String> valueAssign = new ArrayList<>();



    public FileToken(CompilationUnit cu) {

        if(cu.getStorage().isPresent()){
            // Get file name.
            this.fileName = cu.getStorage().get().getFileName();
            // Get package name.
            if(cu.getPackageDeclaration().isPresent()){
                this.packageName = cu.getPackageDeclaration().get().getNameAsString();
            }
            // Get path.
            this.location = cu.getStorage().get().getPath().toString();
        }
        // Getting imports
        ImportCollector importCollector = new ImportCollector();
        importCollector.visit(cu,null);
        this.importStm.addAll(importCollector.getImportStm());

        // visit node in AST for getting classes and interfaces name and line.
        CNameCollector classVisitor = new CNameCollector();
        InterfaceNameCollector interfaceVisitor = new InterfaceNameCollector();
        classVisitor.visit(cu,null);
        interfaceVisitor.visit(cu,interfaceName);
        this.className.addAll(classVisitor.getClassName());

        List<Integer> classLine = new ArrayList<>(classVisitor.getDeclarationLine());
        for(int i=0;i<className.size();i++){
            this.classLine.put(className.get(i),classLine.get(i));
        }

        List<Integer> interfaceLine = new ArrayList<>(interfaceVisitor.getDeclarationLine());
        for(int i=0;i<interfaceName.size();i++){
            this.interfaceLine.put(interfaceName.get(i),interfaceLine.get(i));
        }

        //Case 1 : Extends, Get all extended and implement name.
        VoidVisitor<List<String>> classExtension = new ClassExtensionCollector();
        ClassImplementationCollector classImplementation = new ClassImplementationCollector();
        classExtension.visit(cu, extendedList);
        classImplementation.visit(cu,implementList);

        //Case 2 : Variable Type and Method Type, Get all variable (include array type) and method type.
        VoidVisitor<List<String>> variableTypeCollector = new VariableTypeCollector();
        variableTypeCollector.visit(cu, variableType);
        VoidVisitor<List<String>> methodTypeCollector = new MethodTypeCollector();
        methodTypeCollector.visit(cu, methodType);

        // Case 3 : Object, Get all object assignment type.
        // Ex. PizzaStore nyStore = new NYPizzaStore(); ->  Type: NYPizzaStore
        VoidVisitor<List<String>> objectAssignCollector = new ObjectDeclarationVisitor();
        objectAssignCollector.visit(cu, objectAssignmentType);

        // Case 4 : Parameter, Get all parameter type.
        VoidVisitor<List<String>> parameterCollector = new ParameterCollector();
        parameterCollector.visit(cu, parameterType);

        // Case 5 : Method Call and Method Argument.
        MethodCallCollector methodCallCollector = new MethodCallCollector();
        List<String> methodCallExpr = new ArrayList<>();
        methodCallCollector.visit(cu, methodCallExpr);
        methodScope.addAll(methodCallCollector.getMethodScope());
        methodArgument.addAll(methodCallCollector.getMethodArgument());

        // Case 6 : Return Type.
        ReturnStmtCollector returnStmtCollector = new ReturnStmtCollector();
        returnStmtCollector.visit(cu,returnStm);

        // Case 7 : For, If, While Loop.
        IfStmtCollector ifStmtCollector = new IfStmtCollector();
        ifStmtCollector.visit(cu,ifStm);

        ForStmtCollector forStmtCollector = new ForStmtCollector();
        forStmtCollector.visit(cu,forStm);

        ForeachStmtCollector foreachStmtCollector = new ForeachStmtCollector();
        foreachStmtCollector.visit(cu,forEachStm);

        SwitchStmtCollector switchStmtCollector = new SwitchStmtCollector();
        switchStmtCollector.visit(cu,new ArrayList<>());
        switchStm.addAll(switchStmtCollector.getBody());

        // Case 8 : Value Assignment
        AssignExprCollector assignExprCollector = new AssignExprCollector();
        assignExprCollector.visit(cu,valueAssign);

    }

    public String getFileName() {
        return fileName;
    }

    public String getLocation() {
        return location;
    }

    public String getPackageName() {
        return packageName;
    }

    public List<String> getClassName() {
        return className;
    }

    public List<String> getExtendedList() {
        return extendedList;
    }

    public List<String> getImplementList() {
        return implementList;
    }

    public List<String> getVariableType() {
        return variableType;
    }

    public List<String> getMethodType() {
        return methodType;
    }

    public List<String> getParameterType() {
        return parameterType;
    }

    public List<String> getObjectAssignmentType() {
        return objectAssignmentType;
    }


    public List<String> getMethodScope() {
        return methodScope;
    }

    public List<String> getMethodArgument() {
        return methodArgument;
    }

    public List<String> getReturnStm() {
        return returnStm;
    }

    public List<String> getInterfaceName() {
        return interfaceName;
    }

    public int getClassLine(String className){
        return classLine.get(className);
    }

    public int getInterfaceLine(String interfaceName){
        return interfaceLine.get(interfaceName);
    }

    public List<String> getImportStm() {
        return importStm;
    }

    public List<String> getIfStm() {
        return ifStm;
    }

    public List<String> getForStm() {
        return forStm;
    }

    public List<String> getForEachStm() {
        return forEachStm;
    }

    public List<String> getSwitchStm() {
        return switchStm;
    }

    public List<String> getValueAssign() {
        return valueAssign;
    }
}
