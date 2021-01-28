package DeadVariable;

import TokenGenerator.FileToken;
import Util.ASTParser;
import Util.CheckLineOfVarDeclaration;
import Util.CheckLineOfVarDeclarationForField;

import java.io.IOException;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class DeadVariableDetector {
    List<FileToken> fileTokenList;

    public DeadVariableDetector(List<FileToken> fileTokenList) {
        this.fileTokenList = fileTokenList;

//        printFileTokenInfo();

        for (int i=0; i<this.fileTokenList.size(); i++) {
            List<String> aliveField = new ArrayList<>();
            HashMap<Integer, String> deadField = new HashMap<>();
            List<String> aliveVariable = new ArrayList<>();
            HashMap<Integer, String> deadVariable = new HashMap<>();

//            System.out.println(this.fileTokenList.get(i).getFileName());
//            System.out.println();

            if (this.fileTokenList.get(i).getVariableNames().size() > 0) {
//                System.out.println("==================================== checkRegexForFileToken ====================================");
                checkRegexForFileToken(aliveField, deadField, this.fileTokenList.get(i));
            }

            Integer count = 0;
            for (int j=0; j<this.fileTokenList.get(i).getMethodTokenList().size(); j++) {
                count = count +  this.fileTokenList.get(i).getMethodTokenList().get(j).getVariableNames().size();
            }

            if (count > 0) {
//                System.out.println("==================================== checkRegexForMethodToken ====================================");
                checkRegexForMethodToken(aliveVariable, deadVariable, this.fileTokenList.get(i));
            }

            List<String> AllAliveVariable = new ArrayList<>();
            AllAliveVariable.addAll(aliveField);
            AllAliveVariable.addAll(aliveVariable);

            HashMap<Integer, String> AllDeadVariable = new HashMap<>();
            AllDeadVariable.putAll(deadField);
            AllDeadVariable.putAll(deadVariable);

            this.fileTokenList.get(i).setAliveVariable(AllAliveVariable);
            this.fileTokenList.get(i).setDeadVariable(AllDeadVariable);
        }

        printAllDeadVariableInfo();
    }

    //method to operate algorithm for checking regex for file token
    private void checkRegexForFileToken(List<String> aliveField, HashMap<Integer, String> deadField, FileToken fileToken) {
//        System.out.println("check for : " + fileToken.getVariableNames());

        //check for -> field in file accept any cases (not include within method) if found -> add to alive variable
        if (fileToken.getVariableNames().size() > 0) {
            aliveField.addAll(checkDeadVariableInMethodCallExpr(fileToken.getFileName(), fileToken.getVariableNames(), fileToken.getMethodCalls()));
        }
        if (fileToken.getVariableNames().size() > 0) {
            aliveField.addAll(checkDeadVariableInAssignExpr(fileToken.getFileName(), fileToken.getVariableNames(), fileToken.getAssignExpr()));
        }
        if (fileToken.getVariableNames().size() > 0) {
            aliveField.addAll(checkDeadVariableInObjectCreationExpr(fileToken.getFileName(), fileToken.getVariableNames(), fileToken.getObjectCreationExpr()));
        }
        if (fileToken.getVariableNames().size() > 0) {
            aliveField.addAll(checkDeadVariableInIfStmt(fileToken.getFileName(), fileToken.getVariableNames(), fileToken.getIfStmt()));
        }
        if (fileToken.getVariableNames().size() > 0) {
            aliveField.addAll(checkDeadVariableInForStmt(fileToken.getFileName(), fileToken.getVariableNames(), fileToken.getForStmt()));
        }
        if (fileToken.getVariableNames().size() > 0) {
            aliveField.addAll(checkDeadVariableInForeachStmt(fileToken.getFileName(), fileToken.getVariableNames(), fileToken.getForeachStmt()));
        }
        if (fileToken.getVariableNames().size() > 0) {
            aliveField.addAll(checkDeadVariableInReturnStmt(fileToken.getFileName(), fileToken.getVariableNames(), fileToken.getReturnStmt()));
        }
        if (fileToken.getVariableNames().size() > 0) {
            aliveField.addAll(checkDeadVariableInWhileStmt(fileToken.getFileName(), fileToken.getVariableNames(), fileToken.getWhileStmt()));
        }
        if (fileToken.getVariableNames().size() > 0) {
            aliveField.addAll(checkDeadVariableInDoStmt(fileToken.getFileName(), fileToken.getVariableNames(), fileToken.getDoStmt()));
        }
        if (fileToken.getVariableNames().size() > 0) {
            aliveField.addAll(checkDeadVariableInSwitchStmt(fileToken.getFileName(), fileToken.getVariableNames(), fileToken.getSwitchStmt()));
        }
        if (fileToken.getVariableNames().size() > 0) {
            aliveField.addAll(checkDeadVariableInVariableDeclarator(fileToken.getFileName(), fileToken.getVariableNames(), fileToken.getVariableDeclarator()));
        }

        //check for -> field in file accept any cases (within method) if found -> add to alive variable
        for (int i=0; i<fileToken.getMethodTokenList().size(); i++) {
            if (fileToken.getVariableNames().size() > 0) {
                aliveField.addAll(checkDeadVariableInMethodCallExpr(fileToken.getFileName(), fileToken.getVariableNames(), fileToken.getMethodTokenList().get(i).getMethodCalls()));
            }
            if (fileToken.getVariableNames().size() > 0) {
                aliveField.addAll(checkDeadVariableInAssignExpr(fileToken.getFileName(), fileToken.getVariableNames(), fileToken.getMethodTokenList().get(i).getAssignExpr()));
            }
            if (fileToken.getVariableNames().size() > 0) {
                aliveField.addAll(checkDeadVariableInObjectCreationExpr(fileToken.getFileName(), fileToken.getVariableNames(), fileToken.getMethodTokenList().get(i).getObjectCreationExpr()));
            }
            if (fileToken.getVariableNames().size() > 0) {
                aliveField.addAll(checkDeadVariableInIfStmt(fileToken.getFileName(), fileToken.getVariableNames(), fileToken.getMethodTokenList().get(i).getIfStmt()));
            }
            if (fileToken.getVariableNames().size() > 0) {
                aliveField.addAll(checkDeadVariableInForStmt(fileToken.getFileName(), fileToken.getVariableNames(), fileToken.getMethodTokenList().get(i).getForStmt()));
            }
            if (fileToken.getVariableNames().size() > 0) {
                aliveField.addAll(checkDeadVariableInForeachStmt(fileToken.getFileName(), fileToken.getVariableNames(), fileToken.getMethodTokenList().get(i).getForeachStmt()));
            }
            if (fileToken.getVariableNames().size() > 0) {
                aliveField.addAll(checkDeadVariableInReturnStmt(fileToken.getFileName(), fileToken.getVariableNames(), fileToken.getMethodTokenList().get(i).getReturnStmt()));
            }
            if (fileToken.getVariableNames().size() > 0) {
                aliveField.addAll(checkDeadVariableInWhileStmt(fileToken.getFileName(), fileToken.getVariableNames(), fileToken.getMethodTokenList().get(i).getWhileStmt()));
            }
            if (fileToken.getVariableNames().size() > 0) {
                aliveField.addAll(checkDeadVariableInDoStmt(fileToken.getFileName(), fileToken.getVariableNames(), fileToken.getMethodTokenList().get(i).getDoStmt()));
            }
            if (fileToken.getVariableNames().size() > 0) {
                aliveField.addAll(checkDeadVariableInSwitchStmt(fileToken.getFileName(), fileToken.getVariableNames(), fileToken.getMethodTokenList().get(i).getSwitchStmt()));
            }
            if (fileToken.getVariableNames().size() > 0) {
                aliveField.addAll(checkDeadVariableInVariableDeclarator(fileToken.getFileName(), fileToken.getVariableNames(), fileToken.getMethodTokenList().get(i).getVariableDeclarator()));
            }
        }
//        System.out.println();

        //check if variable Name remains or not -> if yes visit cu and get lineOfDeclaration -> add to dead variable (line, variableName)
        Integer lineOfDeclaration;
        if (fileToken.getVariableNames().size() > 0) {
//            System.out.println("field after detect");
            String source = fileToken.getLocation();
            ASTParser astParserTemp = new ASTParser(source);

            for (int i = 0; i< fileToken.getVariableNames().size(); i++) {
                for (int j=0; j<astParserTemp.cu.size(); j++) {
                    CheckLineOfVarDeclarationForField checkLineOfVarDeclaration = new CheckLineOfVarDeclarationForField(fileToken.getMethodTokenList(), fileToken.getVariableNames().get(i));
                    checkLineOfVarDeclaration.visit(astParserTemp.cu.get(j), null);
                    lineOfDeclaration = checkLineOfVarDeclaration.getLineOfDeclaration();
                    deadField.put(lineOfDeclaration, fileToken.getVariableNames().get(i));
//                    System.out.println("deadField put : " + fileToken.getVariableNames().get(i) + " line : " + lineOfDeclaration);
                }
            }
        }

//        System.out.println();
    }

    //method to operate algorithm for checking regex for method token
    private void checkRegexForMethodToken( List<String> aliveVariable, HashMap<Integer, String> deadVariable, FileToken fileToken) {
        for (int i=0; i<fileToken.getMethodTokenList().size(); i++) {
            if (fileToken.getMethodTokenList().get(i).getVariableNames().size() > 0) {
//                System.out.println("method : " + fileToken.getMethodTokenList().get(i).getMethodName() + " check for : " + fileToken.getMethodTokenList().get(i).getVariableNames());

                if (fileToken.getMethodTokenList().get(i).getVariableNames().size() > 0)  {
                    aliveVariable.addAll(checkDeadVariableInMethodCallExpr(fileToken.getFileName(), fileToken.getMethodTokenList().get(i).getVariableNames(), fileToken.getMethodTokenList().get(i).getMethodCalls()));
                }
                if (fileToken.getMethodTokenList().get(i).getVariableNames().size() > 0)  {
                    aliveVariable.addAll(checkDeadVariableInAssignExpr(fileToken.getFileName(), fileToken.getMethodTokenList().get(i).getVariableNames(), fileToken.getMethodTokenList().get(i).getAssignExpr()));
                }
                if (fileToken.getMethodTokenList().get(i).getVariableNames().size() > 0)  {
                    aliveVariable.addAll(checkDeadVariableInObjectCreationExpr(fileToken.getFileName(), fileToken.getMethodTokenList().get(i).getVariableNames(), fileToken.getMethodTokenList().get(i).getObjectCreationExpr()));
                }
                if (fileToken.getMethodTokenList().get(i).getVariableNames().size() > 0)  {
                    aliveVariable.addAll(checkDeadVariableInIfStmt(fileToken.getFileName(), fileToken.getMethodTokenList().get(i).getVariableNames(), fileToken.getMethodTokenList().get(i).getIfStmt()));
                }
                if (fileToken.getMethodTokenList().get(i).getVariableNames().size() > 0)  {
                    aliveVariable.addAll(checkDeadVariableInForStmt(fileToken.getFileName(), fileToken.getMethodTokenList().get(i).getVariableNames(), fileToken.getMethodTokenList().get(i).getForStmt()));
                }
                if (fileToken.getMethodTokenList().get(i).getVariableNames().size() > 0)  {
                    aliveVariable.addAll(checkDeadVariableInForeachStmt(fileToken.getFileName(), fileToken.getMethodTokenList().get(i).getVariableNames(), fileToken.getMethodTokenList().get(i).getForeachStmt()));
                }
                if (fileToken.getMethodTokenList().get(i).getVariableNames().size() > 0)  {
                    aliveVariable.addAll(checkDeadVariableInReturnStmt(fileToken.getFileName(), fileToken.getMethodTokenList().get(i).getVariableNames(), fileToken.getMethodTokenList().get(i).getReturnStmt()));
                }
                if (fileToken.getMethodTokenList().get(i).getVariableNames().size() > 0)  {
                    aliveVariable.addAll(checkDeadVariableInWhileStmt(fileToken.getFileName(), fileToken.getMethodTokenList().get(i).getVariableNames(), fileToken.getMethodTokenList().get(i).getWhileStmt()));
                }
                if (fileToken.getMethodTokenList().get(i).getVariableNames().size() > 0)  {
                    aliveVariable.addAll(checkDeadVariableInDoStmt(fileToken.getFileName(), fileToken.getMethodTokenList().get(i).getVariableNames(), fileToken.getMethodTokenList().get(i).getDoStmt()));
                }
                if (fileToken.getMethodTokenList().get(i).getVariableNames().size() > 0)  {
                    aliveVariable.addAll(checkDeadVariableInSwitchStmt(fileToken.getFileName(), fileToken.getMethodTokenList().get(i).getVariableNames(), fileToken.getMethodTokenList().get(i).getSwitchStmt()));
                }
                if (fileToken.getMethodTokenList().get(i).getVariableNames().size() > 0)  {
                    aliveVariable.addAll(checkDeadVariableInVariableDeclarator(fileToken.getFileName(), fileToken.getMethodTokenList().get(i).getVariableNames(), fileToken.getMethodTokenList().get(i).getVariableDeclarator()));
                }
//                System.out.println();
            }
        }

        //check if variable Name remains or not -> if yes visit cu and get lineOfDeclaration -> add to dead variable (line, variableName)
        Integer lineOfDeclaration;
        Integer count = 0;
        for (int i=0; i<fileToken.getMethodTokenList().size(); i++) {
            count = count + fileToken.getMethodTokenList().get(i).getVariableNames().size();
        }

        if (count > 0) {
//            System.out.println("variable after detect method token");
            String source = fileToken.getLocation();
            ASTParser astParserTemp = new ASTParser(source);

            for (int i=0; i<fileToken.getMethodTokenList().size(); i++) {
                if (fileToken.getMethodTokenList().get(i).getVariableNames().size() > 0) {
                    for (int j=0; j<fileToken.getMethodTokenList().get(i).getVariableNames().size(); j++) {
                        CheckLineOfVarDeclaration checkLineOfVarDeclaration = new CheckLineOfVarDeclaration(fileToken.getMethodTokenList().get(i).getVariableNames().get(j), fileToken.getMethodTokenList().get(i).getBeginLine(), fileToken.getMethodTokenList().get(i).getEndLine());
                        checkLineOfVarDeclaration.visit(astParserTemp.cu.get(0), null);
                        lineOfDeclaration = checkLineOfVarDeclaration.getLineOfDeclaration();
                        deadVariable.put(lineOfDeclaration, fileToken.getMethodTokenList().get(i).getVariableNames().get(j));
//                        System.out.println("put : " + fileToken.getMethodTokenList().get(i).getVariableNames().get(j) + " line : " + lineOfDeclaration);
                    }
                }
            }
        }

//        System.out.println();
    }

    //method to check regex for method call expr -> done
    private List<String> checkDeadVariableInMethodCallExpr(String fileName, List<String> variableNames, List<String> methodCallExpr) {
        List<String> aliveVariable = new ArrayList<>();
        List<String> toRemove = new ArrayList<>();

        for (int i=0; i<variableNames.size(); i++) {
            String methodCallExprPattern = ".*[\\({1}]" + variableNames.get(i) + "[\\W]";
            String methodCallExprPattern2 = ".*[\\({1}].*(\\,)+(\\s)*" + variableNames.get(i) + "[\\W]";
            String methodCallExprPattern3 = ".*(\\+{1})(\\s)*" + variableNames.get(i) + ".*";
            String methodCallExprPattern4 = "^" + variableNames.get(i) + "[\\W{1}][\\w+]";
            Pattern pattern = Pattern.compile(methodCallExprPattern);
            Pattern pattern2 = Pattern.compile(methodCallExprPattern2);
            Pattern pattern3 = Pattern.compile(methodCallExprPattern3);
            Pattern pattern4 = Pattern.compile(methodCallExprPattern4);

            for (int j=0; j<methodCallExpr.size(); j++) {
                Matcher matcher = pattern.matcher(methodCallExpr.get(j));
                Matcher matcher2 = pattern2.matcher(methodCallExpr.get(j));
                Matcher matcher3 = pattern3.matcher(methodCallExpr.get(j));
                Matcher matcher4 = pattern4.matcher(methodCallExpr.get(j));
                if (matcher.find()) {
//                    System.out.println("methodCallExpr find pattern 1 : " + variableNames.get(i) + " \\\\ " + methodCallExpr.get(j));
                    aliveVariable.add(variableNames.get(i));
                    toRemove.add(variableNames.get(i));
                    break;
                }
                else if (matcher2.find()) {
//                    System.out.println("methodCallExpr find pattern 2 : " + variableNames.get(i) + " \\\\ " + methodCallExpr.get(j));
                    aliveVariable.add(variableNames.get(i));
                    toRemove.add(variableNames.get(i));
                    break;
                }
                else if (matcher3.find()) {
//                    System.out.println("methodCallExpr find pattern 3 : " + variableNames.get(i) + " \\\\ " + methodCallExpr.get(j));
                    aliveVariable.add(variableNames.get(i));
                    toRemove.add(variableNames.get(i));
                    break;
                }
                else if (matcher4.find()) {
//                    System.out.println("methodCallExpr find pattern 4 : " + variableNames.get(i) + " \\\\ " + methodCallExpr.get(j));
                    aliveVariable.add(variableNames.get(i));
                    toRemove.add(variableNames.get(i));
                    break;
                }
            }
        }

        variableNames.removeAll(toRemove);

        return aliveVariable;
    }

    //method to check regex for assign expr -> done
    private List<String> checkDeadVariableInAssignExpr(String fileName, List<String> variableNames, List<String> assignExpr) {
        List<String> aliveVariable = new ArrayList<>();
        List<String> toRemove = new ArrayList<>();

        for (int i=0; i<variableNames.size(); i++) {
            String assignExprPattern = "^.+(\\s)*(\\={1})(\\s)*[\\W]*" + variableNames.get(i) + "[.]";
            String assignExprPattern2 = "^.+(\\s)*(\\={1})(\\s)*.*(\\+{1})(\\s)*" + variableNames.get(i);
            String assignExprPattern3 = "^.+(\\s)*(\\={1})(\\s)*[\\W]*" + variableNames.get(i) + "$";
            Pattern pattern = Pattern.compile(assignExprPattern);
            Pattern pattern2 = Pattern.compile(assignExprPattern2);
            Pattern pattern3 = Pattern.compile(assignExprPattern3);

            for (int j=0; j<assignExpr.size(); j++) {
                Matcher matcher = pattern.matcher(assignExpr.get(j));
                Matcher matcher2 = pattern2.matcher(assignExpr.get(j));
                Matcher matcher3 = pattern3.matcher(assignExpr.get(j));
                if (matcher.find()) {
//                    System.out.println("assignExpr find pattern 1 : " + variableNames.get(i) + " \\\\ " + assignExpr.get(j));
                    aliveVariable.add(variableNames.get(i));
                    toRemove.add(variableNames.get(i));
                    break;
                }
                else if (matcher2.find()) {
//                    System.out.println("assignExpr find pattern 2 : " + variableNames.get(i) + " \\\\ " + assignExpr.get(j));
                    aliveVariable.add(variableNames.get(i));
                    toRemove.add(variableNames.get(i));
                    break;
                }else if (matcher3.find()) {
//                    System.out.println("assignExpr find pattern 3 : " + variableNames.get(i) + " \\\\ " + assignExpr.get(j));
                    aliveVariable.add(variableNames.get(i));
                    toRemove.add(variableNames.get(i));
                    break;
                }
            }
        }

        variableNames.removeAll(toRemove);

        return aliveVariable;
    }

    //method to check regex for object creation expr -> done
    private List<String> checkDeadVariableInObjectCreationExpr(String fileName, List<String> variableNames, List<String> objectCreationExpr) {
        List<String> aliveVariable = new ArrayList<>();
        List<String> toRemove = new ArrayList<>();

        for (int i=0; i<variableNames.size(); i++) {
            String objectCreationPattern = "^new[\\s]+.+\\(.*" + variableNames.get(i) + ".*\\)$";
            Pattern pattern = Pattern.compile(objectCreationPattern);

            for (int j=0; j<objectCreationExpr.size(); j++) {
                Matcher matcher = pattern.matcher(objectCreationExpr.get(j));
                if (matcher.find()) {
//                    System.out.println("objectCreationExpr find pattern 1 : " + variableNames.get(i) + " \\\\ " + objectCreationExpr.get(j));
                    aliveVariable.add(variableNames.get(i));
                    toRemove.add(variableNames.get(i));
                    break;
                }
            }
        }

        variableNames.removeAll(toRemove);

        return aliveVariable;
    }

    //method to check regex for if stmt -> done
    private List<String> checkDeadVariableInIfStmt(String fileName, List<String> variableNames, List<String> ifStmt) {
        List<String> aliveVariable = new ArrayList<>();
        List<String> toRemove = new ArrayList<>();

        for (int i=0; i<variableNames.size(); i++) {
            String ifStmtPattern = "^.*" + variableNames.get(i) + "[\\W]";
            String ifStmtPattern2 = "(\\w)+(\\W{1})(\\w)+[\\({1}].*" + variableNames.get(i) + ".*";
            String ifStmtPattern3 = "(\\w)+(\\s)*(\\W{1,2})(\\s)*" + variableNames.get(i) + ".*";
            String ifStmtPattern4 = "(\\w)+(\\s)*(\\W{1,2})(\\s)*.*(\\+{1})(\\s)*" + variableNames.get(i) + ".*";
            Pattern pattern = Pattern.compile(ifStmtPattern);
            Pattern pattern2 = Pattern.compile(ifStmtPattern2);
            Pattern pattern3 = Pattern.compile(ifStmtPattern3);
            Pattern pattern4 = Pattern.compile(ifStmtPattern4);

            for (int j=0; j<ifStmt.size(); j++) {
                Matcher matcher = pattern.matcher(ifStmt.get(j));
                Matcher matcher2 = pattern2.matcher(ifStmt.get(j));
                Matcher matcher3 = pattern3.matcher(ifStmt.get(j));
                Matcher matcher4 = pattern4.matcher(ifStmt.get(j));
                if (matcher.find()) {
//                    System.out.println("IfStmt find pattern 1 : " + variableNames.get(i) + " \\\\ " + ifStmt.get(j));
                    aliveVariable.add(variableNames.get(i));
                    toRemove.add(variableNames.get(i));
                    break;
                }
                else if (matcher2.find()) {
//                    System.out.println("IfStmt find pattern 2 : " + variableNames.get(i) + " \\\\ " + ifStmt.get(j));
                    aliveVariable.add(variableNames.get(i));
                    toRemove.add(variableNames.get(i));
                    break;
                }
                else if (matcher3.find()) {
//                    System.out.println("IfStmt find pattern 3 : " + variableNames.get(i) + " \\\\ " + ifStmt.get(j));
                    aliveVariable.add(variableNames.get(i));
                    toRemove.add(variableNames.get(i));
                    break;
                }
                else if (matcher4.find()) {
//                    System.out.println("IfStmt find pattern 4 : " + variableNames.get(i) + " \\\\ " + ifStmt.get(j));
                    aliveVariable.add(variableNames.get(i));
                    toRemove.add(variableNames.get(i));
                    break;
                }
            }
        }

        variableNames.removeAll(toRemove);

        return aliveVariable;
    }

    //method to check regex for for stmt -> done
    private List<String> checkDeadVariableInForStmt(String fileName, List<String> variableNames, List<String> forStmt) {
        List<String> aliveVariable = new ArrayList<>();
        List<String> toRemove = new ArrayList<>();

        for (int i=0; i<variableNames.size(); i++) {
            String forStmtPattern = "^" + variableNames.get(i) + ".*";
            String forStmtPattern2 = "^.*(\\W)+(\\s)*" + variableNames.get(i) + ".*$";
            Pattern pattern = Pattern.compile(forStmtPattern);
            Pattern pattern2 = Pattern.compile(forStmtPattern2);

            for (int j=0; j<forStmt.size(); j++) {
                Matcher matcher = pattern.matcher(forStmt.get(j));
                Matcher matcher2 = pattern2.matcher(forStmt.get(j));
                if (matcher.find()) {
//                    System.out.println("forStmt find pattern 1 : " + variableNames.get(i) + " \\\\ " + forStmt.get(j));
                    aliveVariable.add(variableNames.get(i));
                    toRemove.add(variableNames.get(i));
                    break;
                }
                else if (matcher2.find()) {
//                    System.out.println("forStmt find pattern 2 : " + variableNames.get(i) + " \\\\ " + forStmt.get(j));
                    aliveVariable.add(variableNames.get(i));
                    toRemove.add(variableNames.get(i));
                    break;
                }
            }
        }

        variableNames.removeAll(toRemove);

        return aliveVariable;
    }

    //method to check regex for foreach stmt -> done
    private List<String> checkDeadVariableInForeachStmt(String fileName, List<String> variableNames, List<String> ForeachStmt) {
        List<String> aliveVariable = new ArrayList<>();
        List<String> toRemove = new ArrayList<>();

        for (int i=0; i<variableNames.size(); i++) {
            String foreachStmtPattern = "" + variableNames.get(i) + "$";
            Pattern pattern = Pattern.compile(foreachStmtPattern);

            for (int j=0; j<ForeachStmt.size(); j++) {
                Matcher matcher = pattern.matcher(ForeachStmt.get(j));
                if (matcher.find()) {
//                    System.out.println("ForeachStmt find pattern 1 : " + variableNames.get(i) + " \\\\ " + ForeachStmt.get(j));
                    aliveVariable.add(variableNames.get(i));
                    toRemove.add(variableNames.get(i));
                    break;
                }
            }
        }

        variableNames.removeAll(toRemove);

        return aliveVariable;
    }

    //method to check regex for return stmt -> done
    private List<String> checkDeadVariableInReturnStmt(String fileName, List<String> variableNames, List<String> ReturnStmt) {
        List<String> aliveVariable = new ArrayList<>();
        List<String> toRemove = new ArrayList<>();

        for (int i=0; i<variableNames.size(); i++) {
            String returnStmtPattern = ".+(\\W){1}[\\s]*[\\+]{1}[\\s]*" + variableNames.get(i);
            String returnStmtPattern2 = "" + variableNames.get(i) + "(\\W*).*$";
            Pattern pattern = Pattern.compile(returnStmtPattern);
            Pattern pattern2 = Pattern.compile(returnStmtPattern2);

            for (int j=0; j<ReturnStmt.size(); j++) {
                Matcher matcher = pattern.matcher(ReturnStmt.get(j));
                Matcher matcher2 = pattern2.matcher(ReturnStmt.get(j));
                if (matcher.find()) {
//                    System.out.println("returnStmt find pattern 1 : " + variableNames.get(i) + " \\\\ " + ReturnStmt.get(j));
                    aliveVariable.add(variableNames.get(i));
                    toRemove.add(variableNames.get(i));
                    break;
                }
                else if (matcher2.find()) {
//                    System.out.println("returnStmt find pattern 2 : " + variableNames.get(i) + " \\\\ " + ReturnStmt.get(j));
                    aliveVariable.add(variableNames.get(i));
                    toRemove.add(variableNames.get(i));
                    break;
                }
            }
        }

        variableNames.removeAll(toRemove);

        return aliveVariable;
    }

    //method to check regex for while stmt -> done
    private List<String> checkDeadVariableInWhileStmt(String fileName, List<String> variableNames, List<String> whileStmt) {
        List<String> aliveVariable = new ArrayList<>();
        List<String> toRemove = new ArrayList<>();

        for (int i=0; i<variableNames.size(); i++) {
            String whileStmtPattern = "^" + variableNames.get(i);
            String whileStmtPattern2 = "(\\w)+(\\W{1})(\\w)+[\\({1}].*" + variableNames.get(i) + ".*";
            String whileStmtPattern3 = "(\\w)+(\\s)*(\\W{1,2})(\\s)*" + variableNames.get(i) + ".*";
            String whileStmtPattern4 = "(\\w)+(\\s)*(\\W{1,2})(\\s)*.*(\\+{1})(\\s)*" + variableNames.get(i) + ".*";
            Pattern pattern = Pattern.compile(whileStmtPattern);
            Pattern pattern2 = Pattern.compile(whileStmtPattern2);
            Pattern pattern3 = Pattern.compile(whileStmtPattern3);
            Pattern pattern4 = Pattern.compile(whileStmtPattern4);

            for (int j=0; j<whileStmt.size(); j++) {
                Matcher matcher = pattern.matcher(whileStmt.get(j));
                Matcher matcher2 = pattern2.matcher(whileStmt.get(j));
                Matcher matcher3 = pattern3.matcher(whileStmt.get(j));
                Matcher matcher4 = pattern4.matcher(whileStmt.get(j));
                if (matcher.find()) {
//                    System.out.println("whileStmt find pattern 1 : " + variableNames.get(i) + " \\\\ " + whileStmt.get(j));
                    aliveVariable.add(variableNames.get(i));
                    toRemove.add(variableNames.get(i));
                    break;
                }
                else if (matcher2.find()) {
//                    System.out.println("whileStmt find pattern 2 : " + variableNames.get(i) + " \\\\ " + whileStmt.get(j));
                    aliveVariable.add(variableNames.get(i));
                    toRemove.add(variableNames.get(i));
                    break;
                }
                else if (matcher3.find()) {
//                    System.out.println("whileStmt find pattern 3 : " + variableNames.get(i) + " \\\\ " + whileStmt.get(j));
                    aliveVariable.add(variableNames.get(i));
                    toRemove.add(variableNames.get(i));
                    break;
                }
                else if (matcher4.find()) {
//                    System.out.println("whileStmt find pattern 4 : " + variableNames.get(i) + " \\\\ " + whileStmt.get(j));
                    aliveVariable.add(variableNames.get(i));
                    toRemove.add(variableNames.get(i));
                    break;
                }
            }
        }

        variableNames.removeAll(toRemove);

        return aliveVariable;
    }

    //method to check regex for do stmt -> done
    private List<String> checkDeadVariableInDoStmt(String fileName, List<String> variableNames, List<String> doStmt) {
        List<String> aliveVariable = new ArrayList<>();
        List<String> toRemove = new ArrayList<>();

        for (int i=0; i<variableNames.size(); i++) {
            String doStmtPattern = "^" + variableNames.get(i);
            String doStmtPattern2 = "(\\w)+(\\W{1})(\\w)+[\\({1}].*" + variableNames.get(i) + ".*";
            String doStmtPattern3 = "(\\w)+(\\s)*(\\W{1,2})(\\s)*" + variableNames.get(i) + ".*";
            String doStmtPattern4 = "(\\w)+(\\s)*(\\W{1,2})(\\s)*.*(\\+{1})(\\s)*" + variableNames.get(i) + ".*";
            Pattern pattern = Pattern.compile(doStmtPattern);
            Pattern pattern2 = Pattern.compile(doStmtPattern2);
            Pattern pattern3 = Pattern.compile(doStmtPattern3);
            Pattern pattern4 = Pattern.compile(doStmtPattern4);

            for (int j=0; j<doStmt.size(); j++) {
                Matcher matcher = pattern.matcher(doStmt.get(j));
                Matcher matcher2 = pattern2.matcher(doStmt.get(j));
                Matcher matcher3 = pattern3.matcher(doStmt.get(j));
                Matcher matcher4 = pattern4.matcher(doStmt.get(j));
                if (matcher.find()) {
//                    System.out.println("doStmt find pattern 1 : " + variableNames.get(i) + " \\\\ " + doStmt.get(j));
                    aliveVariable.add(variableNames.get(i));
                    toRemove.add(variableNames.get(i));
                    break;
                }
                else if (matcher2.find()) {
//                    System.out.println("doStmt find pattern 2 : " + variableNames.get(i) + " \\\\ " + doStmt.get(j));
                    aliveVariable.add(variableNames.get(i));
                    toRemove.add(variableNames.get(i));
                    break;
                }
                else if (matcher3.find()) {
//                    System.out.println("doStmt find pattern 3 : " + variableNames.get(i) + " \\\\ " + doStmt.get(j));
                    aliveVariable.add(variableNames.get(i));
                    toRemove.add(variableNames.get(i));
                    break;
                }
                else if (matcher4.find()) {
//                    System.out.println("doStmt find pattern 4 : " + variableNames.get(i) + " \\\\ " + doStmt.get(j));
                    aliveVariable.add(variableNames.get(i));
                    toRemove.add(variableNames.get(i));
                    break;
                }
            }
        }

        variableNames.removeAll(toRemove);

        return aliveVariable;
    }

    //method to check regex for switch stmt -> done
    private List<String> checkDeadVariableInSwitchStmt(String fileName, List<String> variableNames, List<String> SwitchStmt) {
        List<String> aliveVariable = new ArrayList<>();
        List<String> toRemove = new ArrayList<>();

        for (int i=0; i<variableNames.size(); i++) {
            String switchStmtPattern = "" + variableNames.get(i) + "$";
            Pattern pattern = Pattern.compile(switchStmtPattern);

            for (int j=0; j<SwitchStmt.size(); j++) {
                Matcher matcher = pattern.matcher(SwitchStmt.get(j));
                if (matcher.find()) {
//                    System.out.println("switchStmt find pattern 1 : " + variableNames.get(i) + " \\\\ " + SwitchStmt.get(j));
                    aliveVariable.add(variableNames.get(i));
                    toRemove.add(variableNames.get(i));
                    break;
                }
            }
        }

        variableNames.removeAll(toRemove);

        return aliveVariable;
    }

    //method to check regex for variable declarator -> done
    private List<String> checkDeadVariableInVariableDeclarator(String fileName, List<String> variableNames, List<String> variableDeclarator) {
        List<String> aliveVariable = new ArrayList<>();
        List<String> toRemove = new ArrayList<>();

        for (int i=0; i<variableNames.size(); i++) {
            String variableDeclaratorPattern = "^.+(\\s)*(\\={1})(\\s)*" + variableNames.get(i) + "[\\W]";
            String variableDeclaratorPattern2 = "^.+(\\s)*(\\={1})(\\s)*.*(\\+{1})(\\s)*" + variableNames.get(i);
            Pattern pattern = Pattern.compile(variableDeclaratorPattern);
            Pattern pattern2 = Pattern.compile(variableDeclaratorPattern2);

            for (int j=0; j<variableDeclarator.size(); j++) {
                Matcher matcher = pattern.matcher(variableDeclarator.get(j));
                Matcher matcher2 = pattern2.matcher(variableDeclarator.get(j));
                if (matcher.find()) {
//                    System.out.println("variableDeclarator find pattern 1 : " + variableNames.get(i) + " \\\\ " + variableDeclarator.get(j));
                    aliveVariable.add(variableNames.get(i));
                    toRemove.add(variableNames.get(i));
                    break;
                }
                else if (matcher2.find()) {
//                    System.out.println("variableDeclarator find pattern 2 : " + variableNames.get(i) + " \\\\ " + variableDeclarator.get(j));
                    aliveVariable.add(variableNames.get(i));
                    toRemove.add(variableNames.get(i));
                    break;
                }
            }
        }

        variableNames.removeAll(toRemove);

        return aliveVariable;
    }

    //method to print FileToken info
    private void printFileTokenInfo() {
        for (int i = 0; i<this.fileTokenList.size(); i++) {
//            System.out.println();
//            System.out.println("filename : " + this.fileTokenList.get(i).getFileName());
//            System.out.println("location : " + this.fileTokenList.get(i).getLocation());
//            System.out.println("variables : " + this.fileTokenList.get(i).getVariableNames());
//            System.out.println("methodCalls : " + this.fileTokenList.get(i).getMethodCalls());
//            System.out.println("assignExpr : " + this.fileTokenList.get(i).getAssignExpr());
//            System.out.println("objectCreationExpr : " + this.fileTokenList.get(i).getObjectCreationExpr());
//            System.out.println("ifStmt : " + this.fileTokenList.get(i).getIfStmt());
//            System.out.println("forStmt : " + this.fileTokenList.get(i).getForStmt());
//            System.out.println("foreachStmt : " + this.fileTokenList.get(i).getForeachStmt());
//            System.out.println("returnStmt : " + this.fileTokenList.get(i).getReturnStmt());
//            System.out.println("whileStmt : " + this.fileTokenList.get(i).getWhileStmt());
//            System.out.println("doStmt : " + this.fileTokenList.get(i).getDoStmt());
//            System.out.println("switchStmt : " + this.fileTokenList.get(i).getSwitchStmt());
//            System.out.println("variableDeclarator : " + this.fileTokenList.get(i).getVariableDeclarator());
//            System.out.println();

//            System.out.println("================= Method Token Info =================");
            printMethodTokenInfo(this.fileTokenList.get(i));
//            System.out.println("==============================================================================================================");
        }
    }

    //method to print MethodToken info
    private void printMethodTokenInfo(FileToken fileToken) {
        for (int j=0; j<fileToken.getMethodTokenList().size(); j++) {
//            System.out.println("methodName : " + fileToken.getMethodTokenList().get(j).getMethodName());
//            System.out.println("beginLine : " + fileToken.getMethodTokenList().get(j).getBeginLine());
//            System.out.println("endLine : " + fileToken.getMethodTokenList().get(j).getEndLine());
//            System.out.println("variables : " + fileToken.getMethodTokenList().get(j).getVariableNames());
//            System.out.println("methodCalls : " + fileToken.getMethodTokenList().get(j).getMethodCalls());
//            System.out.println("assignExpr : " + fileToken.getMethodTokenList().get(j).getAssignExpr());
//            System.out.println("objectCreationExpr : " + fileToken.getMethodTokenList().get(j).getObjectCreationExpr());
//            System.out.println("ifStmt : " + fileToken.getMethodTokenList().get(j).getIfStmt());
//            System.out.println("forStmt : " + fileToken.getMethodTokenList().get(j).getForStmt());
//            System.out.println("foreachStmt : " + fileToken.getMethodTokenList().get(j).getForeachStmt());
//            System.out.println("returnStmt : " + fileToken.getMethodTokenList().get(j).getReturnStmt());
//            System.out.println("whileStmt : " + fileToken.getMethodTokenList().get(j).getWhileStmt());
//            System.out.println("doStmt : " + fileToken.getMethodTokenList().get(j).getDoStmt());
//            System.out.println("switchStmt : " + fileToken.getMethodTokenList().get(j).getSwitchStmt());
//            System.out.println("variableDeclarator : " + fileToken.getMethodTokenList().get(j).getVariableDeclarator());
//            System.out.println();
        }
    }

    //method to print All dead variable info
    private void printAllDeadVariableInfo() {
        for (int i=0; i<this.fileTokenList.size(); i++) {
            System.out.println("file : " + this.fileTokenList.get(i).getFileName() + " has " + this.fileTokenList.get(i).getDeadVariable().size() + " Dead variables");
            Set<Integer> line = this.fileTokenList.get(i).getDeadVariable().keySet();
            Iterator<Integer> iterator = line.iterator();
            while (iterator.hasNext()) {
                Integer lineNumber = iterator.next();
                System.out.println("Variable : " + this.fileTokenList.get(i).getDeadVariable().get(lineNumber) + " \\\\ Line : " + lineNumber
                        + " \\\\ location : " + this.fileTokenList.get(i).getLocation() + " \\\\ Type : Dead Variable");
            }
            System.out.println();
        }
    }

    //method to create report specific file name
    public void createReport(String fileName) throws IOException {
        String filename = "DeadVariableDetector" + fileName + "ProjectOutput";
        Output output = new Output();
        output.createFile(filename);
        output.sendInfo(this.fileTokenList);
        output.write();
    }
}