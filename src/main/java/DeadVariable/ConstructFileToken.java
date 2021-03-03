package DeadVariable;

import Util.*;
import com.github.javaparser.ast.CompilationUnit;

import java.util.*;

public class ConstructFileToken {
    List<CompilationUnit> cu;
    List<FileToken> fileTokenList = new ArrayList<>();
    List<String> packageNameList = new ArrayList<>();

    public ConstructFileToken(List<CompilationUnit> cu) {
        this.cu = new ArrayList<>(cu);

        for (int i=0; i<this.cu.size(); i++) {
            //if packageName is not in packageNameList -> add to it -> result show all packageName
            if (!this.cu.get(i).getPackageDeclaration().isEmpty()) {
                if (!this.packageNameList.contains(this.cu.get(i).getPackageDeclaration().get().getNameAsString())) {
                    this.packageNameList.add(this.cu.get(i).getPackageDeclaration().get().getNameAsString());
                }
            }
            else if (this.cu.get(i).getPackageDeclaration().isEmpty()) {
                if (!this.packageNameList.contains("default")) {
                    this.packageNameList.add("default");
                }
            }
        }

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

            if (this.cu.get(i).getPackageDeclaration().isEmpty()) {
                fileToken.setPackageName("default");
            }
            else if (!this.cu.get(i).getPackageDeclaration().isEmpty()) {
                fileToken.setPackageName(this.cu.get(i).getPackageDeclaration().get().getNameAsString());
            }

            fileToken.setExtendsClassStmt(getExtendsClassStmt(this.cu.get(i)));
            fileToken.setImportClassStmt(getImportClassStmt(this.cu.get(i)));

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

        //to set extendsClass
        for (int i=0; i<this.fileTokenList.size(); i++) {
            if (!this.fileTokenList.get(i).getExtendsClassStmt().isEmpty()) {
                this.fileTokenList.get(i).setExtendsClass(getExtendsClass(this.fileTokenList.get(i)));
            }
        }

        //to set ImportClass
        for (int i=0; i<this.fileTokenList.size(); i++) {
            if (!this.fileTokenList.get(i).getImportClassStmt().isEmpty()) {
                this.fileTokenList.get(i).setImportClass(getImportClass(this.fileTokenList.get(i)));
            }
        }

        //to add childClassThatExtendsToDetect to parent
        for (int i=0; i<this.fileTokenList.size(); i++) {
            if (!this.fileTokenList.get(i).getExtendsClass().isEmpty()) {
                for (int j = 0; j<this.fileTokenList.get(i).getExtendsClass().size(); j++) {
                    this.fileTokenList.get(i).getExtendsClass().get(j).addChildClassToDetect(this.fileTokenList.get(i));
                }
            }
        }

        //to add classThatImportToDetect to parent
        for (int i=0; i<this.fileTokenList.size(); i++) {
            if (!this.fileTokenList.get(i).getImportClass().isEmpty()) {
                for (int j = 0; j<this.fileTokenList.get(i).getImportClass().size(); j++) {
                    this.fileTokenList.get(i).getImportClass().get(j).addClassThatImportToDetect(this.fileTokenList.get(i));
                }
            }
        }

        //to set FileInSamePackageToDetect
        for (int i=0; i<this.fileTokenList.size(); i++) {
            String packageName = this.fileTokenList.get(i).getPackageName();
            List<FileToken> AllFileInPackage = new ArrayList<>();

            for (int j=0; j<this.fileTokenList.size(); j++) {
                if (this.fileTokenList.get(j).getPackageName().equals(packageName)) {
                    if (!this.fileTokenList.get(j).getFileName().equals(this.fileTokenList.get(i).getFileName())) {
                        AllFileInPackage.add(this.fileTokenList.get(j));
                    }
                }
            }

            this.fileTokenList.get(i).setFileInSamePackageToDetect(getFileInSamePackageToDetect(this.fileTokenList.get(i), AllFileInPackage));
        }
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
    private List<String> getExtendsClassStmt(CompilationUnit cu) {
        List<String> extendsClass = new ArrayList<>();
        ClassExtensionCollector classExtensionCollector = new ClassExtensionCollector();
        classExtensionCollector.visit(cu, extendsClass);
        return extendsClass;
    }

    //method to get extends file token (not duplicate with import file token)
    private List<FileToken> getExtendsClass(FileToken fileToken) {
        List<FileToken> extendsClass = new ArrayList<>();

        for (int i=0; i<fileToken.getExtendsClassStmt().size(); i++) {
            for (int j=0; j<this.fileTokenList.size(); j++) {
                if (fileToken.getExtendsClassStmt().get(i).equals(this.fileTokenList.get(j).getFileName())) {
                    if (!extendsClass.contains(this.fileTokenList.get(j))) {
                        extendsClass.add(this.fileTokenList.get(j));
                    }
                }
            }
        }

        return extendsClass;
    }

    //method to get import class
    private List<String> getImportClassStmt(CompilationUnit cu) {
        ImportCollector importCollector = new ImportCollector();
        importCollector.visit(cu, null);
        List<String> fullImportStmt = importCollector.getFullImportStmt();
        List<String> importStmt = new ArrayList<>();

        for (int i=0; i<fullImportStmt.size(); i++) {
            StringTokenizer tokenizer = new StringTokenizer(fullImportStmt.get(i), ".");
            List<String> tokens = new ArrayList<>();

            while (tokenizer.hasMoreTokens()) {
                tokens.add(tokenizer.nextToken());
            }

            String packageNameToken = ""; // import package (not include class)
            String lastToken = ""; // import class

            for (int j=0; j<tokens.size(); j++) {
                if (j != tokens.size()-1 && j != tokens.size()-2) {
                    packageNameToken = packageNameToken + tokens.get(j) + ".";
                }
                else if (j != tokens.size()-1 && j == tokens.size()-2) {
                    packageNameToken = packageNameToken + tokens.get(j);
                }
                else if (j == tokens.size()-1) {
                    lastToken = tokens.get(j).replaceAll("\\s", "");
                }
            }

            for (int j=0; j<packageNameList.size(); j++) {
                if (packageNameToken.equals(packageNameList.get(j))) {
                    importStmt.add(packageNameToken + "." + lastToken);
                }
            }
        }

        return importStmt;
    }

    //method to get import file token
    private List<FileToken> getImportClass(FileToken fileToken) {
        List<FileToken> importClass = new ArrayList<>();
        List<String> fullImportStmt = fileToken.getImportClassStmt();

        for (int i=0; i<fullImportStmt.size(); i++) {
            StringTokenizer tokenizer = new StringTokenizer(fullImportStmt.get(i), ".");
            List<String> tokens = new ArrayList<>();

            while (tokenizer.hasMoreTokens()) {
                tokens.add(tokenizer.nextToken());
            }

            String packageNameToken = ""; // import package (not include class)
            String lastToken = ""; // import class

            for (int j=0; j<tokens.size(); j++) {
                if (j != tokens.size()-1 && j != tokens.size()-2) {
                    packageNameToken = packageNameToken + tokens.get(j) + ".";
                }
                else if (j != tokens.size()-1 && j == tokens.size()-2) {
                    packageNameToken = packageNameToken + tokens.get(j);
                }
                else if (j == tokens.size()-1) {
                    lastToken = tokens.get(j).replaceAll("\\s", "");
                }
            }

            if (lastToken.equals("*")) {
                for (int j=0; j<this.fileTokenList.size(); j++) {
                    if (packageNameToken.equals(this.fileTokenList.get(j).getPackageName())) {
                        if (!fileToken.getExtendsClass().isEmpty()) {
                            for (int k=0; k<fileToken.getExtendsClass().size(); k++) {
                                if (!fileToken.getExtendsClass().get(k).equals(this.fileTokenList.get(j))) {
                                    if (!importClass.contains(this.fileTokenList.get(j))) {
                                        importClass.add(this.fileTokenList.get(j));
                                    }
                                }
                            }
                        }
                        else if (fileToken.getExtendsClass().isEmpty()) {
                            if (!importClass.contains(this.fileTokenList.get(j))) {
                                importClass.add(this.fileTokenList.get(j));
                            }
                        }
                    }
                }
            }
            else if (!lastToken.equals("*")) {
                for (int j=0; j<this.fileTokenList.size(); j++) {
                    if (lastToken.equals(this.fileTokenList.get(j).getFileName()) && packageNameToken.equals(this.fileTokenList.get(j).getPackageName())) {
                        if (!fileToken.getExtendsClass().isEmpty()) {
                            for (int k=0; k<fileToken.getExtendsClass().size(); k++) {
                                if (!fileToken.getExtendsClass().get(k).equals(this.fileTokenList.get(j))) {
                                    if (!importClass.contains(this.fileTokenList.get(j))) {
                                        importClass.add(this.fileTokenList.get(j));
                                    }
                                }
                            }
                        }
                        else if (fileToken.getExtendsClass().isEmpty()) {
                            if (!importClass.contains(this.fileTokenList.get(j))) {
                                importClass.add(this.fileTokenList.get(j));
                            }
                        }
                    }
                }
            }
        }
        return importClass;
    }

    //method to get fileInSamePackageToDetect (not include extendsClass in same package)
    private List<FileToken> getFileInSamePackageToDetect(FileToken fileToken, List<FileToken> AllFileInPackage) {
        List<FileToken> AllFileInPackageToDetect = new ArrayList<>();
        //check extendsClass not in AllFileInPackage
        if (!fileToken.getChildClassToDetect().isEmpty()) {
            for (int i = 0; i<fileToken.getChildClassToDetect().size(); i++) {
                for (int j=0; j<AllFileInPackage.size(); j++) {
                    if (AllFileInPackage.contains(fileToken.getChildClassToDetect().get(i))) {
                        AllFileInPackage.remove(fileToken.getChildClassToDetect().get(i));
                    }
                }
            }
        }

        AllFileInPackageToDetect.addAll(AllFileInPackage);
        return AllFileInPackageToDetect;
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

    public List<FileToken> getFileTokenList() {
        return fileTokenList;
    }
}
