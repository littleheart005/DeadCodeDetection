package TokenGenerator;

import DeadVariable.Variable;
import Util.*;
import com.github.javaparser.ast.CompilationUnit;

import java.util.*;

public class ConstructFileToken {
    List<CompilationUnit> cu;
    List<FileToken> fileTokenList = new ArrayList<>();

    public ConstructFileToken(List<CompilationUnit> cu) {
        this.cu = new ArrayList<>(cu);

        //loop to construct Token
        for (int i=0; i<this.cu.size(); i++) {
            //First step : construct Method Token
            List<MethodToken> methodTokenTemp = new ArrayList<>();
            MethodDeclarationCollector methodDeclarationCollector = new MethodDeclarationCollector();
            setMethodTokenTemp(this.cu.get(i), methodTokenTemp, methodDeclarationCollector);

            //Second step : construct File Token -> put Method Token to File Token
            FileToken fileToken = new FileToken();
            fileToken.setFileName(replaceExtention(this.cu.get(i).getStorage().get().getFileName()));
            fileToken.setLocation(this.cu.get(i).getStorage().get().getPath().toString());
            fileToken.setParentClass(getParentClass(this.cu.get(i)));
            fileToken.setMethodTokenList(methodTokenTemp);
            fileToken.setStaticField(getStaticFieldForFileTokenTemp(this.cu.get(i)));
            fileToken.setField(getFieldNameForFileTokenTemp(this.cu.get(i), methodTokenTemp, fileToken.getStaticField()));
            fileToken.setMethodCalls(getMethodCallForFileTokenTemp(this.cu.get(i), methodTokenTemp));
            fileToken.setAssignExpr(getAssignExprForFileTokenTemp(this.cu.get(i), methodTokenTemp));
            fileToken.setObjectCreationExpr(getObjectCreationExprForFileTokenTemp(this.cu.get(i), methodTokenTemp));
            fileToken.setIfStmt(getIfStmtForFileTokenTemp(this.cu.get(i), methodTokenTemp));
            fileToken.setForStmt(getForStmtForFileTokenTemp(this.cu.get(i), methodTokenTemp));
            fileToken.setForeachStmt(getForeachStmtForFileTokenTemp(this.cu.get(i), methodTokenTemp));
            fileToken.setReturnStmt(getReturnStmtForFileTokenTemp(this.cu.get(i), methodTokenTemp));
            fileToken.setWhileStmt(getWhileStmtForFileTokenTemp(this.cu.get(i), methodTokenTemp));
            fileToken.setDoStmt(getDoStmtForFileTokenTemp(this.cu.get(i), methodTokenTemp));
            fileToken.setSwitchStmt(getSwitchStmtForFileTokenTemp(this.cu.get(i), methodTokenTemp));
            fileToken.setVariableDeclarator(getVariableDeclaratorForFileTokenTemp(this.cu.get(i), methodTokenTemp));

            this.fileTokenList.add(fileToken);
        }

//        printInfo();
    }

    //method to set MethodTokenTemp
    private void setMethodTokenTemp(CompilationUnit cu, List<MethodToken> methodToken, MethodDeclarationCollector methodDeclarationCollector) {
        methodDeclarationCollector.visit(cu, null);
        List<String> methodName = methodDeclarationCollector.getMethodName();
        List<Integer> beginLine = methodDeclarationCollector.getBeginLine();
        List<Integer> endLine = methodDeclarationCollector.getEndLine();

        for (int i=0; i<methodName.size(); i++) {
            MethodToken methodTokenTemp = new MethodToken();
            methodTokenTemp.setMethodName(methodName.get(i));
            methodTokenTemp.setBeginLine(beginLine.get(i));
            methodTokenTemp.setEndLine(endLine.get(i));

            VariableNameCollector variableNameCollector = new VariableNameCollector(beginLine.get(i), endLine.get(i));
            methodTokenTemp.setVariable(getVariableForMethodTokenTemp(cu, variableNameCollector));

            MethodCallCollector methodCallCollector = new MethodCallCollector(beginLine.get(i), endLine.get(i));
            methodTokenTemp.setMethodCalls(getMethodCallForMethodTokenTemp(cu, methodCallCollector));

            AssignExprCollector assignExprCollector = new AssignExprCollector(beginLine.get(i), endLine.get(i));
            methodTokenTemp.setAssignExpr(getAssignExprForMethodTokenTemp(cu, assignExprCollector));

            ObjectCreationExprCollector objectCreationExprCollector = new ObjectCreationExprCollector(beginLine.get(i), endLine.get(i));
            methodTokenTemp.setObjectCreationExpr(getObjectCreationExprForMethodTokenTemp(cu, objectCreationExprCollector));

            IfStmtCollector ifStmtCollector = new IfStmtCollector(beginLine.get(i), endLine.get(i));
            methodTokenTemp.setIfStmt(getIfStmtForMethodTokenTemp(cu, ifStmtCollector));

            ForStmtCollector forStmtCollector = new ForStmtCollector(beginLine.get(i), endLine.get(i));
            methodTokenTemp.setForStmt(getForStmtForMethodTokenTemp(cu, forStmtCollector));

            ForeachStmtCollector foreachStmtCollector = new ForeachStmtCollector(beginLine.get(i), endLine.get(i));
            methodTokenTemp.setForeachStmt(getForeachStmtForMethodTokenTemp(cu, foreachStmtCollector));

            ReturnStmtCollector returnStmtCollector = new ReturnStmtCollector(beginLine.get(i), endLine.get(i));
            methodTokenTemp.setReturnStmt(getReturnStmtForMethodTokenTemp(cu, returnStmtCollector));

            WhileStmtCollector whileStmtCollector = new WhileStmtCollector(beginLine.get(i), endLine.get(i));
            methodTokenTemp.setWhileStmt(getWhileStmtForMethodTokenTemp(cu, whileStmtCollector));

            DoStmtCollector doStmtCollector = new DoStmtCollector(beginLine.get(i), endLine.get(i));
            methodTokenTemp.setDoStmt(getDoStmtForMethodTokenTemp(cu, doStmtCollector));

            SwitchStmtCollector switchStmtCollector = new SwitchStmtCollector(beginLine.get(i), endLine.get(i));
            methodTokenTemp.setSwitchStmt(getSwitchStmtForMethodTokenTemp(cu, switchStmtCollector));

            VariableDeclaratorCollector variableDeclaratorCollector = new VariableDeclaratorCollector(beginLine.get(i), endLine.get(i));
            methodTokenTemp.setVariableDeclarator(getVariableDeclaratorForMethodTokenTemp(cu, variableDeclaratorCollector));

            methodToken.add(methodTokenTemp);
        }
    }

    //method to get Variable within method (range begin-end)
    private List<Variable> getVariableForMethodTokenTemp(CompilationUnit cu, VariableNameCollector variableNameCollector) {
        variableNameCollector.visit(cu, null);
        List<Variable> variableList = variableNameCollector.getVariableList();
        return variableList;
    }

    //method to get MethodCall within method (range begin-end)
    private List<String> getMethodCallForMethodTokenTemp(CompilationUnit cu, MethodCallCollector methodCallCollector) {
        methodCallCollector.visit(cu, new ArrayList<>());
        List<String> methodcall = methodCallCollector.getMethodCall();
        return methodcall;
    }

    //method to get AssignExpr within method (range begin-end)
    private List<String> getAssignExprForMethodTokenTemp(CompilationUnit cu, AssignExprCollector assignExprCollector) {
        assignExprCollector.visit(cu, new ArrayList<>());
        List<String> assignExpr = assignExprCollector.getAssignExpr();
        return assignExpr;
    }

    //method to get ObjectCreationExpr within method (range begin-end)
    private List<String> getObjectCreationExprForMethodTokenTemp(CompilationUnit cu, ObjectCreationExprCollector objectCreationExprCollector) {
        objectCreationExprCollector.visit(cu, null);
        List<String> objectCreationExpr = objectCreationExprCollector.getObjectCreationExpr();
        return objectCreationExpr;
    }

    //method to get IfStmt within method (range begin-end)
    private List<String> getIfStmtForMethodTokenTemp(CompilationUnit cu, IfStmtCollector ifStmtCollector) {
        ifStmtCollector.visit(cu, new ArrayList<>());
        List<String> ifStmt = ifStmtCollector.getIfStmt();
        return ifStmt;
    }

    //method to get ForStmt within method (range begin-end)
    private List<String> getForStmtForMethodTokenTemp(CompilationUnit cu, ForStmtCollector forStmtCollector) {
        forStmtCollector.visit(cu, new ArrayList<>());
        List<String> forStmt = forStmtCollector.getForStmt();
        return forStmt;
    }

    //method to get ForeachStmt within method (range begin-end)
    private List<String> getForeachStmtForMethodTokenTemp(CompilationUnit cu, ForeachStmtCollector foreachStmtCollector) {
        foreachStmtCollector.visit(cu, new ArrayList<>());
        List<String> foreachStmt = foreachStmtCollector.getForeachStmt();
        return foreachStmt;
    }

    //method to get ReturnStmt within method (range begin-end)
    private List<String> getReturnStmtForMethodTokenTemp(CompilationUnit cu, ReturnStmtCollector returnStmtCollector) {
        returnStmtCollector.visit(cu, new ArrayList<>());
        List<String> returnStmt = returnStmtCollector.getReturnStmt();
        return returnStmt;
    }

    //method to get WhileStmt within method (range begin-end)
    private List<String> getWhileStmtForMethodTokenTemp(CompilationUnit cu, WhileStmtCollector whileStmtCollector) {
        whileStmtCollector.visit(cu, null);
        List<String> whileStmt = whileStmtCollector.getWhileStmt();
        return whileStmt;
    }

    //method to get DoStmt within method (range begin-end)
    private List<String> getDoStmtForMethodTokenTemp(CompilationUnit cu, DoStmtCollector doStmtCollector) {
        doStmtCollector.visit(cu, null);
        List<String> doStmt = doStmtCollector.getDoStmt();
        return doStmt;
    }

    //method to get SwitchStmt within method (range begin-end)
    private List<String> getSwitchStmtForMethodTokenTemp(CompilationUnit cu, SwitchStmtCollector switchStmtCollector) {
        switchStmtCollector.visit(cu, new ArrayList<>());
        List<String> switchStmt = switchStmtCollector.getSwitchStmt();
        return switchStmt;
    }

    //method to get VariableDeclarator within method (range begin-end)
    private List<String> getVariableDeclaratorForMethodTokenTemp(CompilationUnit cu, VariableDeclaratorCollector variableDeclaratorCollector) {
        variableDeclaratorCollector.visit(cu, null);
        List<String> variableDeclarator = variableDeclaratorCollector.getVariableDeclarator();
        return variableDeclarator;
    }

    //method to replace fileNameExtention
    private String replaceExtention(String fileName) {
        return fileName.replaceAll("[.].*", "");
    }


    //method to get parent class
    private List<String> getParentClass(CompilationUnit cu) {
        List<String> parentClass = new ArrayList<>();
        ClassExtensionCollector classExtensionCollector = new ClassExtensionCollector();
        classExtensionCollector.visit(cu, parentClass);
        return parentClass;
    }

    //method to get static field (not in method)
    private List<Variable> getStaticFieldForFileTokenTemp(CompilationUnit cu) {
        StaticFieldCollector staticFieldCollector = new StaticFieldCollector();
        staticFieldCollector.visit(cu, null);

        List<Variable> variable = staticFieldCollector.getVariableList();

        return variable;
    }

    //method to get Field name (not include Variable in method) (not include static field)
    private List<Variable> getFieldNameForFileTokenTemp(CompilationUnit cu, List<MethodToken> methodTokenTemp, List<Variable> staticField) {
        FieldNameCollector fieldNameCollector = new FieldNameCollector(methodTokenTemp);
        fieldNameCollector.visit(cu, null);
        List<Variable> commonFieldName = fieldNameCollector.getVariableList();

        List<Variable> toRemove = new ArrayList<>();

        for (Variable FieldName : commonFieldName) {
            for (Variable staticFieldName : staticField) {
                if (FieldName.getVariableName().equals(staticFieldName.getVariableName())) {
                    if (FieldName.getBeginLine().equals(staticFieldName.getBeginLine()) && staticFieldName.getModifier().equals("static")) {
                        toRemove.add(FieldName);
                        break;
                    }
                }
            }
        }

        commonFieldName.removeAll(toRemove);

        return commonFieldName;
    }

    //method to get MethodCall (not include in method)
    private List<String> getMethodCallForFileTokenTemp(CompilationUnit cu, List<MethodToken> methodTokenTemp) {
        MethodCallForFileTokenCollector methodCallForFileTokenCollector = new MethodCallForFileTokenCollector(methodTokenTemp);
        methodCallForFileTokenCollector.visit(cu, null);
        List<String> methodCall = methodCallForFileTokenCollector.getMethodCall();
        return methodCall;
    }

    //method to get AssignExpr (not include in method)
    private List<String> getAssignExprForFileTokenTemp(CompilationUnit cu, List<MethodToken> methodTokenTemp) {
        AssignExprForFileTokenCollector assignExprForFileTokenCollector = new AssignExprForFileTokenCollector(methodTokenTemp);
        assignExprForFileTokenCollector.visit(cu, null);
        List<String> assignExpr = assignExprForFileTokenCollector.getAssignExpr();
        return assignExpr;
    }

    //method to get ObjectCreationExpr (not include in method)
    private List<String> getObjectCreationExprForFileTokenTemp(CompilationUnit cu, List<MethodToken> methodTokenTemp) {
        ObjectCreationExprForFileTokenCollector objectCreationExprForFileTokenCollector = new ObjectCreationExprForFileTokenCollector(methodTokenTemp);
        objectCreationExprForFileTokenCollector.visit(cu, null);
        List<String> objectCreationExpr = objectCreationExprForFileTokenCollector.getObjectCreationExpr();
        return objectCreationExpr;
    }

    //method to get IfStmt (not include in method)
    private List<String> getIfStmtForFileTokenTemp(CompilationUnit cu, List<MethodToken> methodTokenTemp) {
        IfStmtForFileTokenCollector ifStmtForFileTokenCollector = new IfStmtForFileTokenCollector(methodTokenTemp);
        ifStmtForFileTokenCollector.visit(cu, null);
        List<String> ifStmt = ifStmtForFileTokenCollector.getIfStmt();
        return ifStmt;
    }

    //method to get ForStmt (not include in method)
    private List<String> getForStmtForFileTokenTemp(CompilationUnit cu, List<MethodToken> methodTokenTemp) {
        ForStmtForFileTokenCollector forStmtForFileTokenCollector = new ForStmtForFileTokenCollector(methodTokenTemp);
        forStmtForFileTokenCollector.visit(cu, null);
        List<String> forStmt = forStmtForFileTokenCollector.getForStmt();
        return forStmt;
    }

    //method to get ForeachStmt (not include in method)
    private List<String> getForeachStmtForFileTokenTemp(CompilationUnit cu, List<MethodToken> methodTokenTemp) {
        ForeachStmtForFileTokenCollector foreachStmtForFileTokenCollector = new ForeachStmtForFileTokenCollector(methodTokenTemp);
        foreachStmtForFileTokenCollector.visit(cu, null);
        List<String> foreachStmt = foreachStmtForFileTokenCollector.getForeachStmt();
        return foreachStmt;
    }

    //method to get ReturnStmt (not include in method)
    private List<String> getReturnStmtForFileTokenTemp(CompilationUnit cu, List<MethodToken> methodTokenTemp) {
        ReturnStmtForFileTokenCollector returnStmtForFileTokenCollector = new ReturnStmtForFileTokenCollector(methodTokenTemp);
        returnStmtForFileTokenCollector.visit(cu, null);
        List<String> returnStmt = returnStmtForFileTokenCollector.getReturnStmt();
        return returnStmt;
    }

    //method to get WhileStmt (not include in method)
    private List<String> getWhileStmtForFileTokenTemp(CompilationUnit cu, List<MethodToken> methodTokenTemp) {
        WhileStmtForFileTokenCollector whileStmtForFileTokenCollector = new WhileStmtForFileTokenCollector(methodTokenTemp);
        whileStmtForFileTokenCollector.visit(cu, null);
        List<String> whileStmt = whileStmtForFileTokenCollector.getWhileStmt();
        return whileStmt;
    }

    //method to get DoStmt (not include in method)
    private List<String> getDoStmtForFileTokenTemp(CompilationUnit cu, List<MethodToken> methodTokenTemp) {
        DoStmtForFileTokenCollector doStmtForFileTokenCollector = new DoStmtForFileTokenCollector(methodTokenTemp);
        doStmtForFileTokenCollector.visit(cu, null);
        List<String> doStmt = doStmtForFileTokenCollector.getDoStmt();
        return doStmt;
    }

    //method to get SwitchStmt (not include in method)
    private List<String> getSwitchStmtForFileTokenTemp(CompilationUnit cu, List<MethodToken> methodTokenTemp) {
        SwitchStmtForFileTokenCollector switchStmtForFileTokenCollector = new SwitchStmtForFileTokenCollector(methodTokenTemp);
        switchStmtForFileTokenCollector.visit(cu, null);
        List<String> switchStmt = switchStmtForFileTokenCollector.getSwitchStmt();
        return switchStmt;
    }

    //method to get VariableDeclarator (not include in method)
    private List<String> getVariableDeclaratorForFileTokenTemp(CompilationUnit cu, List<MethodToken> methodTokenTemp) {
        VariableDeclaratorForFileTokenCollector variableDeclaratorForFileTokenCollector = new VariableDeclaratorForFileTokenCollector(methodTokenTemp);
        variableDeclaratorForFileTokenCollector.visit(cu, null);
        List<String> variableDeclarator = variableDeclaratorForFileTokenCollector.getVariableDeclarator();
        return variableDeclarator;
    }

    private void printInfo() {
        System.out.println("file count : " + this.fileTokenList.size());
        for (int i=0; i<this.fileTokenList.size(); i++) {
            System.out.println("Filename : " + this.fileTokenList.get(i).getFileName());
            System.out.println("Location : " + this.fileTokenList.get(i).getLocation());
            System.out.println("parentClass : " + this.fileTokenList.get(i).getParentClass());
            System.out.println();
            System.out.println("========== File Token ==========");

            System.out.print("static field : [");
            if (this.fileTokenList.get(i).getStaticField().size() != 0) {
                for (Variable var : fileTokenList.get(i).getStaticField()) {
                    System.out.print(var.getParent() + "." + var.getVariableName() + ", ");
                }
            }
            System.out.println("]");

            System.out.print("field : [");
            if (this.fileTokenList.get(i).getField().size() != 0) {
                for (Variable var : fileTokenList.get(i).getField()) {
                    System.out.print(var.getVariableName() + ", ");
                }
            }
            System.out.println("]");

            System.out.println("methodCall : " + this.fileTokenList.get(i).getMethodCalls());
            System.out.println("assignExpr : " + this.fileTokenList.get(i).getAssignExpr());
            System.out.println("objectCreationExpr : " + this.fileTokenList.get(i).getObjectCreationExpr());
            System.out.println("ifStmt : " + this.fileTokenList.get(i).getIfStmt());
            System.out.println("forStmt : " + this.fileTokenList.get(i).getForStmt());
            System.out.println("foreachStmt : " + this.fileTokenList.get(i).getForeachStmt());
            System.out.println("returnStmt : " + this.fileTokenList.get(i).getReturnStmt());
            System.out.println("whileStmt : " + this.fileTokenList.get(i).getWhileStmt());
            System.out.println("doStmt : " + this.fileTokenList.get(i).getDoStmt());
            System.out.println("switchStmt : " + this.fileTokenList.get(i).getSwitchStmt());
            System.out.println("variableDeclarator : " + this.fileTokenList.get(i).getVariableDeclarator());
            System.out.println();

            System.out.println("========== Method Token ==========");
            for (int j = 0; j<this.fileTokenList.get(i).getMethodTokenList().size(); j++) {
                System.out.println("method name : " + this.fileTokenList.get(i).getMethodTokenList().get(j).getMethodName());
                System.out.println("begin line : " + this.fileTokenList.get(i).getMethodTokenList().get(j).getBeginLine());
                System.out.println("end line : " + this.fileTokenList.get(i).getMethodTokenList().get(j).getEndLine());

                System.out.print("variable : [");
                if (this.fileTokenList.get(i).getMethodTokenList().get(j).getVariable().size() != 0) {
                    for (Variable var : fileTokenList.get(i).getMethodTokenList().get(j).getVariable()) {
                        System.out.print(var.getVariableName() + ", ");
                    }
                }
                System.out.println("]");

                System.out.println("methodCall : " + this.fileTokenList.get(i).getMethodTokenList().get(j).getMethodCalls());
                System.out.println("assignExpr : " + this.fileTokenList.get(i).getMethodTokenList().get(j).getAssignExpr());
                System.out.println("objectCreationExpr : " + this.fileTokenList.get(i).getMethodTokenList().get(j).getObjectCreationExpr());
                System.out.println("ifStmt : " + this.fileTokenList.get(i).getMethodTokenList().get(j).getIfStmt());
                System.out.println("forStmt : " + this.fileTokenList.get(i).getMethodTokenList().get(j).getForStmt());
                System.out.println("foreachStmt : " + this.fileTokenList.get(i).getMethodTokenList().get(j).getForeachStmt());
                System.out.println("returnStmt : " + this.fileTokenList.get(i).getMethodTokenList().get(j).getReturnStmt());
                System.out.println("whileStmt : " + this.fileTokenList.get(i).getMethodTokenList().get(j).getWhileStmt());
                System.out.println("doStmt : " + this.fileTokenList.get(i).getMethodTokenList().get(j).getDoStmt());
                System.out.println("switchStmt : " + this.fileTokenList.get(i).getMethodTokenList().get(j).getSwitchStmt());
                System.out.println("variableDeclarator : " + this.fileTokenList.get(i).getMethodTokenList().get(j).getVariableDeclarator());
                System.out.println();
            }
        }
    }

    public List<FileToken> getFileTokenList() {
        return fileTokenList;
    }
}
