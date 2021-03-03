package DeadVariable;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class DetectorUtil {

    //method to operate algorithm for checking regex for static field
    public void checkRegexForStaticField(FileToken currentFileToken, List<Variable> aliveStaticField, FileToken fileTokenToDetect) {
        //if location of fileToCheck equals to location of fileToDetect -> send (fieldName) to check
        //else if not equals -> send (className.fieldName) to check
        for (int i=0; i<currentFileToken.getStaticField().size(); i++) {
            if (!currentFileToken.getLocation().equals(fileTokenToDetect.getLocation())) {
                String stringToCheck = currentFileToken.getStaticField().get(i).getParent() + "." + currentFileToken.getStaticField().get(i).getVariableName();
                currentFileToken.getStaticField().get(i).setStringToCheck(stringToCheck);
            }
            else if (currentFileToken.getLocation().equals(fileTokenToDetect.getLocation())) {
                currentFileToken.getStaticField().get(i).setStringToCheck(currentFileToken.getStaticField().get(i).getVariableName());
            }
        }

        if (currentFileToken.getStaticField().size() > 0) {
            if (fileTokenToDetect.getMethodCalls().size() > 0) {
                aliveStaticField.addAll(checkDeadVariableInMethodCallExp(currentFileToken.getStaticField(), fileTokenToDetect.getMethodCalls()));
            }
        }
        if (currentFileToken.getStaticField().size() > 0) {
            if (fileTokenToDetect.getAssignExpr().size() > 0) {
                aliveStaticField.addAll(checkDeadVariableInAssignExp(currentFileToken.getStaticField(), fileTokenToDetect.getAssignExpr()));
            }
        }
        if (currentFileToken.getStaticField().size() > 0) {
            if (fileTokenToDetect.getObjectCreationExpr().size() > 0) {
                aliveStaticField.addAll(checkDeadVariableInObjectCreationExpr(currentFileToken.getStaticField(), fileTokenToDetect.getObjectCreationExpr()));
            }
        }
        if (currentFileToken.getStaticField().size() > 0) {
            if (fileTokenToDetect.getIfStmt().size() > 0) {
                aliveStaticField.addAll(checkDeadVariableInIfStmt(currentFileToken.getStaticField(), fileTokenToDetect.getIfStmt()));
            }
        }
        if (currentFileToken.getStaticField().size() > 0) {
            if (fileTokenToDetect.getForStmt().size() > 0) {
                aliveStaticField.addAll(checkDeadVariableInForStmt(currentFileToken.getStaticField(), fileTokenToDetect.getForStmt()));
            }
        }
        if (currentFileToken.getStaticField().size() > 0) {
            if (fileTokenToDetect.getForeachStmt().size() > 0) {
                aliveStaticField.addAll(checkDeadVariableInForeachStmt(currentFileToken.getStaticField(), fileTokenToDetect.getForeachStmt()));
            }
        }
        if (currentFileToken.getStaticField().size() > 0) {
            if (fileTokenToDetect.getReturnStmt().size() > 0) {
                aliveStaticField.addAll(checkDeadVariableInReturnStmt(currentFileToken.getStaticField(), fileTokenToDetect.getReturnStmt()));
            }
        }
        if (currentFileToken.getStaticField().size() > 0) {
            if (fileTokenToDetect.getWhileStmt().size() > 0) {
                aliveStaticField.addAll(checkDeadVariableInWhileStmt(currentFileToken.getStaticField(), fileTokenToDetect.getWhileStmt()));
            }
        }
        if (currentFileToken.getStaticField().size() > 0) {
            if (fileTokenToDetect.getDoStmt().size() > 0) {
                aliveStaticField.addAll(checkDeadVariableInDoStmt(currentFileToken.getStaticField(), fileTokenToDetect.getDoStmt()));
            }
        }
        if (currentFileToken.getStaticField().size() > 0) {
            if (fileTokenToDetect.getSwitchStmt().size() > 0) {
                aliveStaticField.addAll(checkDeadVariableInSwitchStmt(currentFileToken.getStaticField(), fileTokenToDetect.getSwitchStmt()));
            }
        }
        if (currentFileToken.getStaticField().size() > 0) {
            if (fileTokenToDetect.getVariableDeclarator().size() > 0) {
                aliveStaticField.addAll(checkDeadVariableInVariableDeclarator(currentFileToken.getStaticField(), fileTokenToDetect.getVariableDeclarator()));
            }
        }

        for (int i = 0; i < fileTokenToDetect.getMethodTokenList().size(); i++) {
            if (currentFileToken.getStaticField().size() > 0) {
                if (fileTokenToDetect.getMethodTokenList().get(i).getMethodCalls().size() > 0) {
                    aliveStaticField.addAll(checkDeadVariableInMethodCallExp(currentFileToken.getStaticField(), fileTokenToDetect.getMethodTokenList().get(i).getMethodCalls()));
                }
            }
            if (currentFileToken.getStaticField().size() > 0) {
                if (fileTokenToDetect.getMethodTokenList().get(i).getAssignExpr().size() > 0) {
                    aliveStaticField.addAll(checkDeadVariableInAssignExp(currentFileToken.getStaticField(), fileTokenToDetect.getMethodTokenList().get(i).getAssignExpr()));
                }
            }
            if (currentFileToken.getStaticField().size() > 0) {
                if (fileTokenToDetect.getMethodTokenList().get(i).getObjectCreationExpr().size() > 0) {
                    aliveStaticField.addAll(checkDeadVariableInObjectCreationExpr(currentFileToken.getStaticField(), fileTokenToDetect.getMethodTokenList().get(i).getObjectCreationExpr()));
                }
            }
            if (currentFileToken.getStaticField().size() > 0) {
                if (fileTokenToDetect.getMethodTokenList().get(i).getIfStmt().size() > 0) {
                    aliveStaticField.addAll(checkDeadVariableInIfStmt(currentFileToken.getStaticField(), fileTokenToDetect.getMethodTokenList().get(i).getIfStmt()));
                }
            }
            if (currentFileToken.getStaticField().size() > 0) {
                if (fileTokenToDetect.getMethodTokenList().get(i).getForStmt().size() > 0) {
                    aliveStaticField.addAll(checkDeadVariableInForStmt(currentFileToken.getStaticField(), fileTokenToDetect.getMethodTokenList().get(i).getForStmt()));
                }
            }
            if (currentFileToken.getStaticField().size() > 0) {
                if (fileTokenToDetect.getMethodTokenList().get(i).getForeachStmt().size() > 0) {
                    aliveStaticField.addAll(checkDeadVariableInForeachStmt(currentFileToken.getStaticField(), fileTokenToDetect.getMethodTokenList().get(i).getForeachStmt()));
                }
            }
            if (currentFileToken.getStaticField().size() > 0) {
                if (fileTokenToDetect.getMethodTokenList().get(i).getReturnStmt().size() > 0) {
                    aliveStaticField.addAll(checkDeadVariableInReturnStmt(currentFileToken.getStaticField(), fileTokenToDetect.getMethodTokenList().get(i).getReturnStmt()));
                }
            }
            if (currentFileToken.getStaticField().size() > 0) {
                if (fileTokenToDetect.getMethodTokenList().get(i).getWhileStmt().size() > 0) {
                    aliveStaticField.addAll(checkDeadVariableInWhileStmt(currentFileToken.getStaticField(), fileTokenToDetect.getMethodTokenList().get(i).getWhileStmt()));
                }
            }
            if (currentFileToken.getStaticField().size() > 0) {
                if (fileTokenToDetect.getMethodTokenList().get(i).getDoStmt().size() > 0) {
                    aliveStaticField.addAll(checkDeadVariableInDoStmt(currentFileToken.getStaticField(), fileTokenToDetect.getMethodTokenList().get(i).getDoStmt()));
                }
            }
            if (currentFileToken.getStaticField().size() > 0) {
                if (fileTokenToDetect.getMethodTokenList().get(i).getSwitchStmt().size() > 0) {
                    aliveStaticField.addAll(checkDeadVariableInSwitchStmt(currentFileToken.getStaticField(), fileTokenToDetect.getMethodTokenList().get(i).getSwitchStmt()));
                }
            }
            if (currentFileToken.getStaticField().size() > 0) {
                if (fileTokenToDetect.getMethodTokenList().get(i).getVariableDeclarator().size() > 0) {
                    aliveStaticField.addAll(checkDeadVariableInVariableDeclarator(currentFileToken.getStaticField(), fileTokenToDetect.getMethodTokenList().get(i).getVariableDeclarator()));
                }
            }
        }
    }

    //method to operate algorithm for checking regex for field
    public void checkRegexForField(FileToken currentFileToken, List<Variable> aliveField, FileToken fileTokenToDetect, String detectType) {
        List<Variable> variableToDetect = new ArrayList<>();

        if (detectType.equals("childCase")) {
            for (int i=0; i<currentFileToken.getStaticField().size(); i++) {
                currentFileToken.getStaticField().get(i).setStringToCheck(currentFileToken.getStaticField().get(i).getVariableName());
            }
            variableToDetect = currentFileToken.getStaticField();
        }
        else if (detectType.equals("general")) {
            for (int i=0; i<currentFileToken.getField().size(); i++) {
                currentFileToken.getField().get(i).setStringToCheck(currentFileToken.getField().get(i).getVariableName());
            }
            variableToDetect = currentFileToken.getField();
        }

        //check for -> field in file accept any cases (not include within method) if found -> add to alive variable
        if (variableToDetect.size() > 0) {
            aliveField.addAll(checkDeadVariableInMethodCallExp(variableToDetect, fileTokenToDetect.getMethodCalls()));
        }
        if (variableToDetect.size() > 0) {
            aliveField.addAll(checkDeadVariableInMethodCallExp(variableToDetect, fileTokenToDetect.getAssignExpr()));
        }
        if (variableToDetect.size() > 0) {
            aliveField.addAll(checkDeadVariableInObjectCreationExpr(variableToDetect, fileTokenToDetect.getObjectCreationExpr()));
        }
        if (variableToDetect.size() > 0) {
            aliveField.addAll(checkDeadVariableInIfStmt(variableToDetect, fileTokenToDetect.getIfStmt()));
        }
        if (variableToDetect.size() > 0) {
            aliveField.addAll(checkDeadVariableInForStmt(variableToDetect, fileTokenToDetect.getForStmt()));
        }
        if (variableToDetect.size() > 0) {
            aliveField.addAll(checkDeadVariableInForeachStmt(variableToDetect, fileTokenToDetect.getForeachStmt()));
        }
        if (variableToDetect.size() > 0) {
            aliveField.addAll(checkDeadVariableInReturnStmt(variableToDetect, fileTokenToDetect.getReturnStmt()));
        }
        if (variableToDetect.size() > 0) {
            aliveField.addAll(checkDeadVariableInWhileStmt(variableToDetect, fileTokenToDetect.getWhileStmt()));
        }
        if (variableToDetect.size() > 0) {
            aliveField.addAll(checkDeadVariableInDoStmt(variableToDetect, fileTokenToDetect.getDoStmt()));
        }
        if (variableToDetect.size() > 0) {
            aliveField.addAll(checkDeadVariableInSwitchStmt(variableToDetect, fileTokenToDetect.getSwitchStmt()));
        }
        if (variableToDetect.size() > 0) {
            aliveField.addAll(checkDeadVariableInVariableDeclarator(variableToDetect, fileTokenToDetect.getVariableDeclarator()));
        }

        //check for -> field in file accept any cases (within method) if found -> add to alive variable
        for (int i = 0; i < fileTokenToDetect.getMethodTokenList().size(); i++) {
            if (variableToDetect.size() > 0) {
                aliveField.addAll(checkDeadVariableInMethodCallExp(variableToDetect, fileTokenToDetect.getMethodTokenList().get(i).getMethodCalls()));
            }
            if (variableToDetect.size() > 0) {
                aliveField.addAll(checkDeadVariableInAssignExp(variableToDetect, fileTokenToDetect.getMethodTokenList().get(i).getAssignExpr()));
            }
            if (variableToDetect.size() > 0) {
                aliveField.addAll(checkDeadVariableInObjectCreationExpr(variableToDetect, fileTokenToDetect.getMethodTokenList().get(i).getObjectCreationExpr()));
            }
            if (variableToDetect.size() > 0) {
                aliveField.addAll(checkDeadVariableInIfStmt(variableToDetect, fileTokenToDetect.getMethodTokenList().get(i).getIfStmt()));
            }
            if (variableToDetect.size() > 0) {
                aliveField.addAll(checkDeadVariableInForStmt(variableToDetect, fileTokenToDetect.getMethodTokenList().get(i).getForStmt()));
            }
            if (variableToDetect.size() > 0) {
                aliveField.addAll(checkDeadVariableInForeachStmt(variableToDetect, fileTokenToDetect.getMethodTokenList().get(i).getForeachStmt()));
            }
            if (variableToDetect.size() > 0) {
                aliveField.addAll(checkDeadVariableInReturnStmt(variableToDetect, fileTokenToDetect.getMethodTokenList().get(i).getReturnStmt()));
            }
            if (variableToDetect.size() > 0) {
                aliveField.addAll(checkDeadVariableInWhileStmt(variableToDetect, fileTokenToDetect.getMethodTokenList().get(i).getWhileStmt()));
            }
            if (variableToDetect.size() > 0) {
                aliveField.addAll(checkDeadVariableInDoStmt(variableToDetect, fileTokenToDetect.getMethodTokenList().get(i).getDoStmt()));
            }
            if (variableToDetect.size() > 0) {
                aliveField.addAll(checkDeadVariableInSwitchStmt(variableToDetect, fileTokenToDetect.getMethodTokenList().get(i).getSwitchStmt()));
            }
            if (variableToDetect.size() > 0) {
                aliveField.addAll(checkDeadVariableInVariableDeclarator(variableToDetect, fileTokenToDetect.getMethodTokenList().get(i).getVariableDeclarator()));
            }
        }
    }

    //method to operate algorithm for checking regex for variable
    public void checkRegexForVariable(FileToken currentFileToken, List<Variable> aliveVariable, FileToken fileTokenToDetect) {
        for (int i=0; i<currentFileToken.getMethodTokenList().size(); i++) {
            for (int j=0; j<currentFileToken.getMethodTokenList().get(i).getVariable().size(); j++) {
                currentFileToken.getMethodTokenList().get(i).getVariable().get(j).setStringToCheck(currentFileToken.getMethodTokenList().get(i).getVariable().get(j).getVariableName());
            }
        }

        for (int i = 0; i<currentFileToken.getMethodTokenList().size(); i++) {
            if (currentFileToken.getMethodTokenList().get(i).getVariable().size() > 0) {
                if (currentFileToken.getMethodTokenList().get(i).getVariable().size() > 0) {
                    aliveVariable.addAll(checkDeadVariableInMethodCallExp(currentFileToken.getMethodTokenList().get(i).getVariable(), fileTokenToDetect.getMethodTokenList().get(i).getMethodCalls()));
                }
                if (currentFileToken.getMethodTokenList().get(i).getVariable().size() > 0) {
                    aliveVariable.addAll(checkDeadVariableInAssignExp(currentFileToken.getMethodTokenList().get(i).getVariable(), fileTokenToDetect.getMethodTokenList().get(i).getAssignExpr()));
                }
                if (currentFileToken.getMethodTokenList().get(i).getVariable().size() > 0) {
                    aliveVariable.addAll(checkDeadVariableInObjectCreationExpr(currentFileToken.getMethodTokenList().get(i).getVariable(), fileTokenToDetect.getMethodTokenList().get(i).getObjectCreationExpr()));
                }
                if (currentFileToken.getMethodTokenList().get(i).getVariable().size() > 0) {
                    aliveVariable.addAll(checkDeadVariableInIfStmt(currentFileToken.getMethodTokenList().get(i).getVariable(), fileTokenToDetect.getMethodTokenList().get(i).getIfStmt()));
                }
                if (currentFileToken.getMethodTokenList().get(i).getVariable().size() > 0) {
                    aliveVariable.addAll(checkDeadVariableInForStmt(currentFileToken.getMethodTokenList().get(i).getVariable(), fileTokenToDetect.getMethodTokenList().get(i).getForStmt()));
                }
                if (currentFileToken.getMethodTokenList().get(i).getVariable().size() > 0) {
                    aliveVariable.addAll(checkDeadVariableInForeachStmt(currentFileToken.getMethodTokenList().get(i).getVariable(), fileTokenToDetect.getMethodTokenList().get(i).getForeachStmt()));
                }
                if (currentFileToken.getMethodTokenList().get(i).getVariable().size() > 0) {
                    aliveVariable.addAll(checkDeadVariableInReturnStmt(currentFileToken.getMethodTokenList().get(i).getVariable(), fileTokenToDetect.getMethodTokenList().get(i).getReturnStmt()));
                }
                if (currentFileToken.getMethodTokenList().get(i).getVariable().size() > 0) {
                    aliveVariable.addAll(checkDeadVariableInWhileStmt(currentFileToken.getMethodTokenList().get(i).getVariable(), fileTokenToDetect.getMethodTokenList().get(i).getWhileStmt()));
                }
                if (currentFileToken.getMethodTokenList().get(i).getVariable().size() > 0) {
                    aliveVariable.addAll(checkDeadVariableInDoStmt(currentFileToken.getMethodTokenList().get(i).getVariable(), fileTokenToDetect.getMethodTokenList().get(i).getDoStmt()));
                }
                if (currentFileToken.getMethodTokenList().get(i).getVariable().size() > 0) {
                    aliveVariable.addAll(checkDeadVariableInSwitchStmt(currentFileToken.getMethodTokenList().get(i).getVariable(), fileTokenToDetect.getMethodTokenList().get(i).getSwitchStmt()));
                }
                if (currentFileToken.getMethodTokenList().get(i).getVariable().size() > 0) {
                    aliveVariable.addAll(checkDeadVariableInVariableDeclarator(currentFileToken.getMethodTokenList().get(i).getVariable(), fileTokenToDetect.getMethodTokenList().get(i).getVariableDeclarator()));
                }
            }
        }
    }

    //method to check regex for method call expr -> done
    private List<Variable> checkDeadVariableInMethodCallExp(List<Variable> variable, List<String> methodCallExpr) {
        List<Variable> aliveVariable = new ArrayList<>();
        List<Variable> toRemove = new ArrayList<>();

        for (int i = 0; i < variable.size(); i++) {
            String methodCallExprPattern = ".*[\\({1}]" + variable.get(i).getStringToCheck() + "[\\W]";
            String methodCallExprPattern2 = ".*[\\({1}].*(\\,)+(\\s)*" + variable.get(i).getStringToCheck() + "[\\W]";
            String methodCallExprPattern3 = ".*(\\+{1})(\\s)*" + variable.get(i).getStringToCheck() + ".*";
            String methodCallExprPattern4 = "^" + variable.get(i).getStringToCheck() + "[\\W{1}][\\w+]";
            String methodCallExprPattern5 = "(\\[{1}).*[\\W]*" + variable.get(i).getStringToCheck() + "[\\W]*.*(\\]{1})";
            String methodCallExprPattern6 = variable.get(i).getStringToCheck() + "(\\){1})";
            String methodCallExprPattern7 = "(\\({1})" + variable.get(i).getStringToCheck() + "[\\W]+";
            Pattern pattern = Pattern.compile(methodCallExprPattern);
            Pattern pattern2 = Pattern.compile(methodCallExprPattern2);
            Pattern pattern3 = Pattern.compile(methodCallExprPattern3);
            Pattern pattern4 = Pattern.compile(methodCallExprPattern4);
            Pattern pattern5 = Pattern.compile(methodCallExprPattern5);
            Pattern pattern6 = Pattern.compile(methodCallExprPattern6);
            Pattern pattern7 = Pattern.compile(methodCallExprPattern7);

            for (int j = 0; j < methodCallExpr.size(); j++) {
                Matcher matcher = pattern.matcher(methodCallExpr.get(j));
                Matcher matcher2 = pattern2.matcher(methodCallExpr.get(j));
                Matcher matcher3 = pattern3.matcher(methodCallExpr.get(j));
                Matcher matcher4 = pattern4.matcher(methodCallExpr.get(j));
                Matcher matcher5 = pattern5.matcher(methodCallExpr.get(j));
                Matcher matcher6 = pattern6.matcher(methodCallExpr.get(j));
                Matcher matcher7 = pattern7.matcher(methodCallExpr.get(j));
                if (matcher.find()) {
                    aliveVariable.add(variable.get(i));
                    toRemove.add(variable.get(i));
                    break;
                } else if (matcher2.find()) {
                    aliveVariable.add(variable.get(i));
                    toRemove.add(variable.get(i));
                    break;
                } else if (matcher3.find()) {
                    aliveVariable.add(variable.get(i));
                    toRemove.add(variable.get(i));
                    break;
                } else if (matcher4.find()) {
                    aliveVariable.add(variable.get(i));
                    toRemove.add(variable.get(i));
                    break;
                } else if (matcher5.find()) {
                    aliveVariable.add(variable.get(i));
                    toRemove.add(variable.get(i));
                    break;
                } else if (matcher6.find()) {
                    aliveVariable.add(variable.get(i));
                    toRemove.add(variable.get(i));
                    break;
                } else if (matcher7.find()) {
                    aliveVariable.add(variable.get(i));
                    toRemove.add(variable.get(i));
                    break;
                }
            }
        }

        variable.removeAll(toRemove);

        return aliveVariable;
    }

    //method to check regex for assign expr -> done
    private List<Variable> checkDeadVariableInAssignExp(List<Variable> variable, List<String>  assignExpr) {
        List<Variable> aliveVariable = new ArrayList<>();
        List<Variable> toRemove = new ArrayList<>();

        for (int i = 0; i < variable.size(); i++) {
            String assignExprPattern = "^.+(\\s)*(\\={1})(\\s)*[\\W]*" + variable.get(i).getStringToCheck() + "[.]";
            String assignExprPattern2 = "^.+(\\s)*(\\={1})(\\s)*.*(\\+{1})(\\s)*" + variable.get(i).getStringToCheck();
            String assignExprPattern3 = "^.+(\\s)*(\\={1})(\\s)*[\\W]*" + variable.get(i).getStringToCheck() + "$";
            String assignExprPattern4 = "^.+(\\s)*(\\={1})(\\s)*" + variable.get(i).getStringToCheck() + "[\\W{1}]";
            String assignExprPattern5 = "(\\={1})(\\s)*(\\({1}).*[\\W+]" + variable.get(i).getStringToCheck() + "[\\W+]";
            String assignExprPattern6 = "(\\={1})(\\s)*(\\({1})" + variable.get(i).getStringToCheck() + "[\\W+]";
            String assignExprPattern7 = "(\\[{1}).*[\\W]*" + variable.get(i).getStringToCheck() + "[\\W]*.*(\\]{1})";
            String assignExprPattern8 = "(\\{{1}).*[\\W]*" + variable.get(i).getStringToCheck() + "[\\W]*.*(\\}{1})";
            Pattern pattern = Pattern.compile(assignExprPattern);
            Pattern pattern2 = Pattern.compile(assignExprPattern2);
            Pattern pattern3 = Pattern.compile(assignExprPattern3);
            Pattern pattern4 = Pattern.compile(assignExprPattern4);
            Pattern pattern5 = Pattern.compile(assignExprPattern5);
            Pattern pattern6 = Pattern.compile(assignExprPattern6);
            Pattern pattern7 = Pattern.compile(assignExprPattern7);
            Pattern pattern8 = Pattern.compile(assignExprPattern8);

            for (int j = 0; j < assignExpr.size(); j++) {
                Matcher matcher = pattern.matcher(assignExpr.get(j));
                Matcher matcher2 = pattern2.matcher(assignExpr.get(j));
                Matcher matcher3 = pattern3.matcher(assignExpr.get(j));
                Matcher matcher4 = pattern4.matcher(assignExpr.get(j));
                Matcher matcher5 = pattern5.matcher(assignExpr.get(j));
                Matcher matcher6 = pattern6.matcher(assignExpr.get(j));
                Matcher matcher7 = pattern7.matcher(assignExpr.get(j));
                Matcher matcher8 = pattern8.matcher(assignExpr.get(j));
                if (matcher.find()) {
                    aliveVariable.add(variable.get(i));
                    toRemove.add(variable.get(i));
                    break;
                }
                else if (matcher2.find()) {
                    aliveVariable.add(variable.get(i));
                    toRemove.add(variable.get(i));
                    break;
                }
                else if (matcher3.find()) {
                    aliveVariable.add(variable.get(i));
                    toRemove.add(variable.get(i));
                    break;
                }
                else if (matcher4.find()) {
                    aliveVariable.add(variable.get(i));
                    toRemove.add(variable.get(i));
                    break;
                }
                else if (matcher5.find()) {
                    aliveVariable.add(variable.get(i));
                    toRemove.add(variable.get(i));
                    break;
                }
                else if (matcher6.find()) {
                    aliveVariable.add(variable.get(i));
                    toRemove.add(variable.get(i));
                    break;
                }
                else if (matcher7.find()) {
                    aliveVariable.add(variable.get(i));
                    toRemove.add(variable.get(i));
                    break;
                }
                else if (matcher8.find()) {
                    aliveVariable.add(variable.get(i));
                    toRemove.add(variable.get(i));
                    break;
                }
            }
        }

        variable.removeAll(toRemove);

        return aliveVariable;
    }

    //method to check regex for object creation expr -> done
    private List<Variable> checkDeadVariableInObjectCreationExpr(List<Variable> variable, List<String> objectCreationExpr){
        List<Variable> aliveVariable = new ArrayList<>();
        List<Variable> toRemove = new ArrayList<>();

        for (int i = 0; i < variable.size(); i++) {
            String objectCreationPattern = "^new[\\s]+.+\\(.*" + variable.get(i).getStringToCheck() + ".*\\)$";
            String objectCreationPattern2 = "[\\W]+"+ variable.get(i).getStringToCheck() + "[\\W]+";
            Pattern pattern = Pattern.compile(objectCreationPattern);
            Pattern pattern2 = Pattern.compile(objectCreationPattern2);

            for (int j = 0; j < objectCreationExpr.size(); j++) {
                Matcher matcher = pattern.matcher(objectCreationExpr.get(j));
                Matcher matcher2 = pattern2.matcher(objectCreationExpr.get(j));
                if (matcher.find()) {
                    aliveVariable.add(variable.get(i));
                    toRemove.add(variable.get(i));
                    break;
                }else if (matcher2.find()) {
                    aliveVariable.add(variable.get(i));
                    toRemove.add(variable.get(i));
                    break;
                }
            }
        }

        variable.removeAll(toRemove);

        return aliveVariable;
    }

    //method to check regex for if stmt -> done
    private List<Variable> checkDeadVariableInIfStmt(List<Variable> variable, List<String> ifStmt){
        List<Variable> aliveVariable = new ArrayList<>();
        List<Variable> toRemove = new ArrayList<>();

        for (int i = 0; i < variable.size(); i++) {
            String ifStmtPattern = "^.*" + variable.get(i).getStringToCheck() + "[\\W]";
            String ifStmtPattern2 = "(\\w)+(\\W{1})(\\w)+[\\({1}].*" + variable.get(i).getStringToCheck() + ".*";
            String ifStmtPattern3 = "(\\w)+(\\s)*(\\W{1,2})(\\s)*" + variable.get(i).getStringToCheck() + ".*";
            String ifStmtPattern4 = "(\\w)+(\\s)*(\\W{1,2})(\\s)*.*(\\+{1})(\\s)*" + variable.get(i).getStringToCheck() + ".*";
            String ifStmtPattern5 = variable.get(i).getStringToCheck();
            Pattern pattern = Pattern.compile(ifStmtPattern);
            Pattern pattern2 = Pattern.compile(ifStmtPattern2);
            Pattern pattern3 = Pattern.compile(ifStmtPattern3);
            Pattern pattern4 = Pattern.compile(ifStmtPattern4);
            Pattern pattern5 = Pattern.compile(ifStmtPattern5);

            for (int j = 0; j < ifStmt.size(); j++) {
                Matcher matcher = pattern.matcher(ifStmt.get(j));
                Matcher matcher2 = pattern2.matcher(ifStmt.get(j));
                Matcher matcher3 = pattern3.matcher(ifStmt.get(j));
                Matcher matcher4 = pattern4.matcher(ifStmt.get(j));
                Matcher matcher5 = pattern5.matcher(ifStmt.get(j));
                if (matcher.find()) {
                    aliveVariable.add(variable.get(i));
                    toRemove.add(variable.get(i));
                    break;
                } else if (matcher2.find()) {
                    aliveVariable.add(variable.get(i));
                    toRemove.add(variable.get(i));
                    break;
                } else if (matcher3.find()) {
                    aliveVariable.add(variable.get(i));
                    toRemove.add(variable.get(i));
                    break;
                } else if (matcher4.find()) {
                    aliveVariable.add(variable.get(i));
                    toRemove.add(variable.get(i));
                    break;
                } else if (matcher5.find()) {
                    aliveVariable.add(variable.get(i));
                    toRemove.add(variable.get(i));
                    break;
                }
            }
        }

        variable.removeAll(toRemove);

        return aliveVariable;
    }

    //method to check regex for for stmt -> done
    private List<Variable> checkDeadVariableInForStmt(List<Variable> variable, List<String> forStmt){
        List<Variable> aliveVariable = new ArrayList<>();
        List<Variable> toRemove = new ArrayList<>();

        for (int i = 0; i < variable.size(); i++) {
            String forStmtPattern = "^" + variable.get(i).getStringToCheck() + ".*";
            String forStmtPattern2 = "^.*(\\W)+(\\s)*" + variable.get(i).getStringToCheck() + ".*$";
            Pattern pattern = Pattern.compile(forStmtPattern);
            Pattern pattern2 = Pattern.compile(forStmtPattern2);

            for (int j = 0; j < forStmt.size(); j++) {
                Matcher matcher = pattern.matcher(forStmt.get(j));
                Matcher matcher2 = pattern2.matcher(forStmt.get(j));
                if (matcher.find()) {
                    aliveVariable.add(variable.get(i));
                    toRemove.add(variable.get(i));
                    break;
                } else if (matcher2.find()) {
                    aliveVariable.add(variable.get(i));
                    toRemove.add(variable.get(i));
                    break;
                }
            }
        }

        variable.removeAll(toRemove);

        return aliveVariable;
    }

    //method to check regex for foreach stmt -> done
    private List<Variable> checkDeadVariableInForeachStmt(List<Variable> variable, List<String> ForeachStmt){
        List<Variable> aliveVariable = new ArrayList<>();
        List<Variable> toRemove = new ArrayList<>();

        for (int i = 0; i < variable.size(); i++) {
            String foreachStmtPattern = "" + variable.get(i).getStringToCheck() + "$";
            Pattern pattern = Pattern.compile(foreachStmtPattern);

            for (int j = 0; j < ForeachStmt.size(); j++) {
                Matcher matcher = pattern.matcher(ForeachStmt.get(j));
                if (matcher.find()) {
                    aliveVariable.add(variable.get(i));
                    toRemove.add(variable.get(i));
                    break;
                }
            }
        }

        variable.removeAll(toRemove);

        return aliveVariable;
    }

    //method to check regex for return stmt -> done
    private List<Variable> checkDeadVariableInReturnStmt(List<Variable> variable, List<String> ReturnStmt){
        List<Variable> aliveVariable = new ArrayList<>();
        List<Variable> toRemove = new ArrayList<>();

        for (int i = 0; i < variable.size(); i++) {
            String returnStmtPattern = ".+(\\W){1}[\\s]*[\\+]{1}[\\s]*" + variable.get(i).getStringToCheck();
            String returnStmtPattern2 = "" + variable.get(i).getStringToCheck() + "(\\W*).*$";
            Pattern pattern = Pattern.compile(returnStmtPattern);
            Pattern pattern2 = Pattern.compile(returnStmtPattern2);

            for (int j = 0; j < ReturnStmt.size(); j++) {
                Matcher matcher = pattern.matcher(ReturnStmt.get(j));
                Matcher matcher2 = pattern2.matcher(ReturnStmt.get(j));
                if (matcher.find()) {
                    aliveVariable.add(variable.get(i));
                    toRemove.add(variable.get(i));
                    break;
                } else if (matcher2.find()) {
                    aliveVariable.add(variable.get(i));
                    toRemove.add(variable.get(i));
                    break;
                }
            }
        }

        variable.removeAll(toRemove);

        return aliveVariable;
    }

    //method to check regex for while stmt -> done
    private List<Variable> checkDeadVariableInWhileStmt(List<Variable> variable, List<String> whileStmt){
        List<Variable> aliveVariable = new ArrayList<>();
        List<Variable> toRemove = new ArrayList<>();

        for (int i = 0; i < variable.size(); i++) {
            String whileStmtPattern = "^" + variable.get(i).getStringToCheck();
            String whileStmtPattern2 = "(\\w)+(\\W{1})(\\w)+[\\({1}].*" + variable.get(i).getStringToCheck() + ".*";
            String whileStmtPattern3 = "(\\w)+(\\s)*(\\W{1,2})(\\s)*" + variable.get(i).getStringToCheck() + ".*";
            String whileStmtPattern4 = "(\\w)+(\\s)*(\\W{1,2})(\\s)*.*(\\+{1})(\\s)*" + variable.get(i).getStringToCheck() + ".*";
            Pattern pattern = Pattern.compile(whileStmtPattern);
            Pattern pattern2 = Pattern.compile(whileStmtPattern2);
            Pattern pattern3 = Pattern.compile(whileStmtPattern3);
            Pattern pattern4 = Pattern.compile(whileStmtPattern4);

            for (int j = 0; j < whileStmt.size(); j++) {
                Matcher matcher = pattern.matcher(whileStmt.get(j));
                Matcher matcher2 = pattern2.matcher(whileStmt.get(j));
                Matcher matcher3 = pattern3.matcher(whileStmt.get(j));
                Matcher matcher4 = pattern4.matcher(whileStmt.get(j));
                if (matcher.find()) {
                    aliveVariable.add(variable.get(i));
                    toRemove.add(variable.get(i));
                    break;
                } else if (matcher2.find()) {
                    aliveVariable.add(variable.get(i));
                    toRemove.add(variable.get(i));
                    break;
                } else if (matcher3.find()) {
                    aliveVariable.add(variable.get(i));
                    toRemove.add(variable.get(i));
                    break;
                } else if (matcher4.find()) {
                    aliveVariable.add(variable.get(i));
                    toRemove.add(variable.get(i));
                    break;
                }
            }
        }

        variable.removeAll(toRemove);

        return aliveVariable;
    }

    //method to check regex for do stmt -> done
    private List<Variable> checkDeadVariableInDoStmt(List<Variable> variable, List<String> doStmt){
        List<Variable> aliveVariable = new ArrayList<>();
        List<Variable> toRemove = new ArrayList<>();

        for (int i = 0; i < variable.size(); i++) {
            String doStmtPattern = "^" + variable.get(i).getStringToCheck();
            String doStmtPattern2 = "(\\w)+(\\W{1})(\\w)+[\\({1}].*" + variable.get(i).getStringToCheck() + ".*";
            String doStmtPattern3 = "(\\w)+(\\s)*(\\W{1,2})(\\s)*" + variable.get(i).getStringToCheck() + ".*";
            String doStmtPattern4 = "(\\w)+(\\s)*(\\W{1,2})(\\s)*.*(\\+{1})(\\s)*" + variable.get(i).getStringToCheck() + ".*";
            Pattern pattern = Pattern.compile(doStmtPattern);
            Pattern pattern2 = Pattern.compile(doStmtPattern2);
            Pattern pattern3 = Pattern.compile(doStmtPattern3);
            Pattern pattern4 = Pattern.compile(doStmtPattern4);

            for (int j = 0; j < doStmt.size(); j++) {
                Matcher matcher = pattern.matcher(doStmt.get(j));
                Matcher matcher2 = pattern2.matcher(doStmt.get(j));
                Matcher matcher3 = pattern3.matcher(doStmt.get(j));
                Matcher matcher4 = pattern4.matcher(doStmt.get(j));
                if (matcher.find()) {
                    aliveVariable.add(variable.get(i));
                    toRemove.add(variable.get(i));
                    break;
                } else if (matcher2.find()) {
                    aliveVariable.add(variable.get(i));
                    toRemove.add(variable.get(i));
                    break;
                } else if (matcher3.find()) {
                    aliveVariable.add(variable.get(i));
                    toRemove.add(variable.get(i));
                    break;
                } else if (matcher4.find()) {
                    aliveVariable.add(variable.get(i));
                    toRemove.add(variable.get(i));
                    break;
                }
            }
        }

        variable.removeAll(toRemove);

        return aliveVariable;
    }

    //method to check regex for switch stmt -> done
    private List<Variable> checkDeadVariableInSwitchStmt(List<Variable> variable, List<String> SwitchStmt){
        List<Variable> aliveVariable = new ArrayList<>();
        List<Variable> toRemove = new ArrayList<>();

        for (int i = 0; i < variable.size(); i++) {
            String switchStmtPattern = "" + variable.get(i) + "$";
            Pattern pattern = Pattern.compile(switchStmtPattern);

            for (int j = 0; j < SwitchStmt.size(); j++) {
                Matcher matcher = pattern.matcher(SwitchStmt.get(j));
                if (matcher.find()) {
                    aliveVariable.add(variable.get(i));
                    toRemove.add(variable.get(i));
                    break;
                }
            }
        }

        variable.removeAll(toRemove);

        return aliveVariable;
    }

    //method to check regex for variable declarator -> done
    private List<Variable> checkDeadVariableInVariableDeclarator(List<Variable> variable, List<String> variableDeclarator){
        List<Variable> aliveVariable = new ArrayList<>();
        List<Variable> toRemove = new ArrayList<>();


        for (int i = 0; i < variable.size(); i++) {
            String variableDeclaratorPattern = "^.+(\\s)*(\\={1})(\\s)*" + variable.get(i).getStringToCheck() + "[\\W]";
            String variableDeclaratorPattern2 = "^.+(\\s)*(\\={1})(\\s)*.*(\\+{1})(\\s)*" + variable.get(i).getStringToCheck();
            String variableDeclaratorPattern3 = "(\\={1})(\\s)*(\\({1}).*[\\W+]" + variable.get(i).getStringToCheck() + "[\\W+]";
            String variableDeclaratorPattern4 = "(\\={1})(\\s)*(\\({1})" + variable.get(i).getStringToCheck() + "[\\W+]";
            String variableDeclaratorPattern5 = "(\\{{1}).*[\\W]*" + variable.get(i).getStringToCheck() + "[\\W]*.*(\\}{1})";
            Pattern pattern = Pattern.compile(variableDeclaratorPattern);
            Pattern pattern2 = Pattern.compile(variableDeclaratorPattern2);
            Pattern pattern3 = Pattern.compile(variableDeclaratorPattern3);
            Pattern pattern4 = Pattern.compile(variableDeclaratorPattern4);
            Pattern pattern5 = Pattern.compile(variableDeclaratorPattern5);

            for (int j = 0; j < variableDeclarator.size(); j++) {
                Matcher matcher = pattern.matcher(variableDeclarator.get(j));
                Matcher matcher2 = pattern2.matcher(variableDeclarator.get(j));
                Matcher matcher3 = pattern3.matcher(variableDeclarator.get(j));
                Matcher matcher4 = pattern4.matcher(variableDeclarator.get(j));
                Matcher matcher5 = pattern5.matcher(variableDeclarator.get(j));
                if (matcher.find()) {
                    aliveVariable.add(variable.get(i));
                    toRemove.add(variable.get(i));
                    break;
                } else if (matcher2.find()) {
                    aliveVariable.add(variable.get(i));
                    toRemove.add(variable.get(i));
                    break;
                } else if (matcher3.find()) {
                    aliveVariable.add(variable.get(i));
                    toRemove.add(variable.get(i));
                    break;
                } else if (matcher4.find()) {
                    aliveVariable.add(variable.get(i));
                    toRemove.add(variable.get(i));
                    break;
                } else if (matcher5.find()) {
                    aliveVariable.add(variable.get(i));
                    toRemove.add(variable.get(i));
                    break;
                }
            }
        }

        variable.removeAll(toRemove);

        return aliveVariable;
    }
}
