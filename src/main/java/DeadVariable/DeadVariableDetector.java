package DeadVariable;

import Util.ASTParser;
import Util.CheckLineOfVarDeclaration;
import TokenGenerator.Token;

import java.io.IOException;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class DeadVariableDetector {
    List<Token> tokenList;

    public DeadVariableDetector(List<Token> tokenList) {
        this.tokenList = tokenList;

        printComponentInfo();
//        printAllVariableCount();

        for (int i = 0; i<this.tokenList.size(); i++) {
            if (this.tokenList.get(i).getVariableNames().size() > 0) {
                operate(this.tokenList.get(i));
            }
        }

//        printAliveVariableCount();
//        printDeadVariableCount();
    }

    //method to operate algorithm
    private void operate(Token token) {
        List<String> aliveVariable = new ArrayList<>();
        HashMap<Integer, String> deadVariable = new HashMap<>();

        System.out.println(token.getFileName());

        //check for any cases if found -> add to alive variable
        if (token.getVariableNames().size() > 0) {
            aliveVariable.addAll(checkDeadVariableInMethodCallExpr(token.getFileName(), token.getVariableNames(), token.getMethodCalls()));
        }
        if (token.getVariableNames().size() > 0) {
            aliveVariable.addAll(checkDeadVariableInAssignExpr(token.getFileName(), token.getVariableNames(), token.getAssignExpr()));
        }
        if (token.getVariableNames().size() > 0) {
            aliveVariable.addAll(checkDeadVariableInObjectCreationExpr(token.getFileName(), token.getVariableNames(), token.getObjectCreationExpr()));
        }
        if (token.getVariableNames().size() > 0) {
            aliveVariable.addAll(checkDeadVariableInIfStmt(token.getFileName(), token.getVariableNames(), token.getIfStmt()));
        }
        if (token.getVariableNames().size() > 0) {
            aliveVariable.addAll(checkDeadVariableInForStmt(token.getFileName(), token.getVariableNames(), token.getForStmt()));
        }
        if (token.getVariableNames().size() > 0) {
            aliveVariable.addAll(checkDeadVariableInForeachStmt(token.getFileName(), token.getVariableNames(), token.getForeachStmt()));
        }
        if (token.getVariableNames().size() > 0) {
            aliveVariable.addAll(checkDeadVariableInReturnStmt(token.getFileName(), token.getVariableNames(), token.getReturnStmt()));
        }
        if (token.getVariableNames().size() > 0) {
            aliveVariable.addAll(checkDeadVariableInWhileStmt(token.getFileName(), token.getVariableNames(), token.getWhileStmt()));
        }
        if (token.getVariableNames().size() > 0) {
            aliveVariable.addAll(checkDeadVariableInDoStmt(token.getFileName(), token.getVariableNames(), token.getDoStmt()));
        }
        if (token.getVariableNames().size() > 0) {
            aliveVariable.addAll(checkDeadVariableInSwitchStmt(token.getFileName(), token.getVariableNames(), token.getSwitchStmt()));
        }
        if (token.getVariableNames().size() > 0) {
            aliveVariable.addAll(checkDeadVariableInVariableDeclarator(token.getFileName(), token.getVariableNames(), token.getVariableDeclarator()));
        }

        //check if variable Name remains or not -> if yes visit cu and get lineOfDeclaration -> add to dead variable (line, variableName)
        Integer lineOfDeclaration;
        if (token.getVariableNames().size() > 0) {
            String source = token.getLocation();
            ASTParser astParserTemp = new ASTParser(source);

            for (int i = 0; i< token.getVariableNames().size(); i++) {
                for (int j=0; j<astParserTemp.cu.size(); j++) {
                    CheckLineOfVarDeclaration checkLineOfVarDeclaration = new CheckLineOfVarDeclaration(token.getVariableNames().get(i));
                    checkLineOfVarDeclaration.visit(astParserTemp.cu.get(j), null);
                    lineOfDeclaration = checkLineOfVarDeclaration.getLineOfDeclaration();
                    deadVariable.put(lineOfDeclaration, token.getVariableNames().get(i));
                }
            }
        }

        token.setAliveVariable(aliveVariable);
        token.setDeadVariable(deadVariable);

        System.out.println();
    }

    //method to check regex for method call expr -> done
    private List<String> checkDeadVariableInMethodCallExpr(String fileName, List<String> variableNames, List<String> methodCallExpr) {
        List<String> aliveVariable = new ArrayList<>();
        List<String> toRemove = new ArrayList<>();

        for (int i=0; i<variableNames.size(); i++) {
            String methodCallExprPattern = ".*[\\({1}][^\\W]*" + variableNames.get(i) + ".*";
            String methodCallExprPattern2 = ".*[\\({1}].*(\\,)+(\\s)*" + variableNames.get(i) + ".*";
            String methodCallExprPattern3 = ".*(\\+{1})(\\s)*" + variableNames.get(i) + ".*";
            String methodCallExprPattern4 = "^" + variableNames.get(i) + "[\\W{1}][\\({1}].*[\\){1}]";
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
                    System.out.println("methodCallExpr find pattern 1 : " + variableNames.get(i) + " \\\\ " + methodCallExpr.get(j));
                    aliveVariable.add(variableNames.get(i));
                    toRemove.add(variableNames.get(i));
                    break;
                }
                else if (matcher2.find()) {
                    System.out.println("methodCallExpr find pattern 2 : " + variableNames.get(i) + " \\\\ " + methodCallExpr.get(j));
                    aliveVariable.add(variableNames.get(i));
                    toRemove.add(variableNames.get(i));
                    break;
                }
                else if (matcher3.find()) {
                    System.out.println("methodCallExpr find pattern 3 : " + variableNames.get(i) + " \\\\ " + methodCallExpr.get(j));
                    aliveVariable.add(variableNames.get(i));
                    toRemove.add(variableNames.get(i));
                    break;
                }
                else if (matcher4.find()) {
                    System.out.println("methodCallExpr find pattern 4 : " + variableNames.get(i) + " \\\\ " + methodCallExpr.get(j));
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
            String assignExprPattern = "^.+(\\s)*(\\={1})(\\s)*" + variableNames.get(i) + "[\\W]*[^\\(].*[^\\)]$";
            String assignExprPattern2 = "^.+(\\s)*(\\={1})(\\s)*.*(\\+{1})(\\s)*" + variableNames.get(i);
            Pattern pattern = Pattern.compile(assignExprPattern);
            Pattern pattern2 = Pattern.compile(assignExprPattern2);

            for (int j=0; j<assignExpr.size(); j++) {
                Matcher matcher = pattern.matcher(assignExpr.get(j));
                Matcher matcher2 = pattern2.matcher(assignExpr.get(j));
                if (matcher.find()) {
                    System.out.println("assignExpr find pattern 1 : " + variableNames.get(i) + " \\\\ " + assignExpr.get(j));
                    aliveVariable.add(variableNames.get(i));
                    toRemove.add(variableNames.get(i));
                    break;
                }
                else if (matcher2.find()) {
                    System.out.println("assignExpr find pattern 2 : " + variableNames.get(i) + " \\\\ " + assignExpr.get(j));
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
                    System.out.println("objectCreationExpr find pattern 1 : " + variableNames.get(i) + " \\\\ " + objectCreationExpr.get(j));
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
            String ifStmtPattern = "^.*" + variableNames.get(i);
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
                    System.out.println("IfStmt find pattern 1 : " + variableNames.get(i) + " \\\\ " + ifStmt.get(j));
                    aliveVariable.add(variableNames.get(i));
                    toRemove.add(variableNames.get(i));
                    break;
                }
                else if (matcher2.find()) {
                    System.out.println("IfStmt find pattern 2 : " + variableNames.get(i) + " \\\\ " + ifStmt.get(j));
                    aliveVariable.add(variableNames.get(i));
                    toRemove.add(variableNames.get(i));
                    break;
                }
                else if (matcher3.find()) {
                    System.out.println("IfStmt find pattern 3 : " + variableNames.get(i) + " \\\\ " + ifStmt.get(j));
                    aliveVariable.add(variableNames.get(i));
                    toRemove.add(variableNames.get(i));
                    break;
                }
                else if (matcher4.find()) {
                    System.out.println("IfStmt find pattern 4 : " + variableNames.get(i) + " \\\\ " + ifStmt.get(j));
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
                    System.out.println("forStmt find pattern 1 : " + variableNames.get(i) + " \\\\ " + forStmt.get(j));
                    aliveVariable.add(variableNames.get(i));
                    toRemove.add(variableNames.get(i));
                    break;
                }
                else if (matcher2.find()) {
                    System.out.println("forStmt find pattern 2 : " + variableNames.get(i) + " \\\\ " + forStmt.get(j));
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
                    System.out.println("ForeachStmt find pattern 1 : " + variableNames.get(i) + " \\\\ " + ForeachStmt.get(j));
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
                    System.out.println("returnStmt find pattern 1 : " + variableNames.get(i) + " \\\\ " + ReturnStmt.get(j));
                    aliveVariable.add(variableNames.get(i));
                    toRemove.add(variableNames.get(i));
                    break;
                }
                else if (matcher2.find()) {
                    System.out.println("returnStmt find pattern 2 : " + variableNames.get(i) + " \\\\ " + ReturnStmt.get(j));
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
                    System.out.println("whileStmt find pattern 1 : " + variableNames.get(i) + " \\\\ " + whileStmt.get(j));
                    aliveVariable.add(variableNames.get(i));
                    toRemove.add(variableNames.get(i));
                    break;
                }
                else if (matcher2.find()) {
                    System.out.println("whileStmt find pattern 2 : " + variableNames.get(i) + " \\\\ " + whileStmt.get(j));
                    aliveVariable.add(variableNames.get(i));
                    toRemove.add(variableNames.get(i));
                    break;
                }
                else if (matcher3.find()) {
                    System.out.println("whileStmt find pattern 3 : " + variableNames.get(i) + " \\\\ " + whileStmt.get(j));
                    aliveVariable.add(variableNames.get(i));
                    toRemove.add(variableNames.get(i));
                    break;
                }
                else if (matcher4.find()) {
                    System.out.println("whileStmt find pattern 4 : " + variableNames.get(i) + " \\\\ " + whileStmt.get(j));
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
                    System.out.println("doStmt find pattern 1 : " + variableNames.get(i) + " \\\\ " + doStmt.get(j));
                    aliveVariable.add(variableNames.get(i));
                    toRemove.add(variableNames.get(i));
                    break;
                }
                else if (matcher2.find()) {
                    System.out.println("doStmt find pattern 2 : " + variableNames.get(i) + " \\\\ " + doStmt.get(j));
                    aliveVariable.add(variableNames.get(i));
                    toRemove.add(variableNames.get(i));
                    break;
                }
                else if (matcher3.find()) {
                    System.out.println("doStmt find pattern 3 : " + variableNames.get(i) + " \\\\ " + doStmt.get(j));
                    aliveVariable.add(variableNames.get(i));
                    toRemove.add(variableNames.get(i));
                    break;
                }
                else if (matcher4.find()) {
                    System.out.println("doStmt find pattern 4 : " + variableNames.get(i) + " \\\\ " + doStmt.get(j));
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
                    System.out.println("switchStmt find pattern 1 : " + variableNames.get(i) + " \\\\ " + SwitchStmt.get(j));
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
            String variableDeclaratorPattern = "^.+(\\s)*(\\={1})(\\s)*" + variableNames.get(i) + ".*$";
            String variableDeclaratorPattern2 = "^.+(\\s)*(\\={1})(\\s)*.*(\\+{1})(\\s)*" + variableNames.get(i);
            Pattern pattern = Pattern.compile(variableDeclaratorPattern);
            Pattern pattern2 = Pattern.compile(variableDeclaratorPattern2);

            for (int j=0; j<variableDeclarator.size(); j++) {
                Matcher matcher = pattern.matcher(variableDeclarator.get(j));
                Matcher matcher2 = pattern2.matcher(variableDeclarator.get(j));
                if (matcher.find()) {
                    System.out.println("variableDeclarator find pattern 1 : " + variableNames.get(i) + " \\\\ " + variableDeclarator.get(j));
                    aliveVariable.add(variableNames.get(i));
                    toRemove.add(variableNames.get(i));
                    break;
                }
                else if (matcher2.find()) {
                    System.out.println("variableDeclarator find pattern 2 : " + variableNames.get(i) + " \\\\ " + variableDeclarator.get(j));
                    aliveVariable.add(variableNames.get(i));
                    toRemove.add(variableNames.get(i));
                    break;
                }
            }
        }

        variableNames.removeAll(toRemove);

        return aliveVariable;
    }

    //method to print component info
    private void printComponentInfo() {
        for (int i = 0; i<this.tokenList.size(); i++) {
            System.out.println("filename : " + this.tokenList.get(i).getFileName());
            System.out.println("variables : " + this.tokenList.get(i).getVariableNames());
            System.out.println("methodCalls : " + this.tokenList.get(i).getMethodCalls());
            System.out.println("assignExpr : " + this.tokenList.get(i).getAssignExpr());
            System.out.println("objectCreationExpr : " + this.tokenList.get(i).getObjectCreationExpr());
            System.out.println("ifStmt : " + this.tokenList.get(i).getIfStmt());
            System.out.println("forStmt : " + this.tokenList.get(i).getForStmt());
            System.out.println("foreachStmt : " + this.tokenList.get(i).getForeachStmt());
            System.out.println("returnStmt : " + this.tokenList.get(i).getReturnStmt());
            System.out.println("whileStmt : " + this.tokenList.get(i).getWhileStmt());
            System.out.println("doStmt : " + this.tokenList.get(i).getDoStmt());
            System.out.println("switchStmt : " + this.tokenList.get(i).getSwitchStmt());
            System.out.println("variableDeclarator : " + this.tokenList.get(i).getVariableDeclarator());
            System.out.println();
        }
    }

    //method to print all variable count (include alive and dead variable)
    private void printAllVariableCount() {
        int VarCount = 0;
        for (int i = 0; i<this.tokenList.size(); i++) {
            VarCount = VarCount + this.tokenList.get(i).getVariableNames().size();
        }
        System.out.println("All variable number in project : " + VarCount);
    }

    //method to print alive variable count
    private void printAliveVariableCount() {
        int AliveCount = 0;
        for (int i = 0; i<this.tokenList.size(); i++) {
            AliveCount = AliveCount + this.tokenList.get(i).getAliveVariable().size();
        }
        System.out.println("All alive variable number in project : " + AliveCount);
    }

    //method to print dead variable count
    private void printDeadVariableCount() {
        int deadCount = 0;
        for (int i = 0; i<this.tokenList.size(); i++) {
            deadCount = deadCount + this.tokenList.get(i).getDeadVariable().size();
        }
        System.out.println("All dead variable number in project : " + deadCount);
        System.out.println();
        System.out.println("========== Dead Variable ==========");
        for (int i = 0; i<this.tokenList.size(); i++) {
            if (this.tokenList.get(i).getDeadVariable().size() > 0) {
                Set<Integer> line = this.tokenList.get(i).getDeadVariable().keySet();
                Iterator<Integer> iterator = line.iterator();
                while (iterator.hasNext()) {
                    Integer lineNumber = iterator.next();
                    System.out.println("Variable : " + this.tokenList.get(i).getDeadVariable().get(lineNumber) + " \\\\ Line : " + lineNumber
                                        + " \\\\ location : " + this.tokenList.get(i).getLocation() + " \\\\ Type : Dead Variable");
                }
            }
        }

    }

    //method to create report specific file name
    public void createReport(String fileName) throws IOException {
        String filename = "DeadVariableDetector" + fileName + "ProjectOutput";
        Output output = new Output();
        output.createFile(filename);
        output.sendInfo(this.tokenList);
        output.write();
    }
}