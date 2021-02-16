package DeadVariable;

import TokenGenerator.FileToken;

import java.io.IOException;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class DeadVariableDetector {
    List<FileToken> fileTokenList;

    public DeadVariableDetector(List<FileToken> fileTokenList) {
        this.fileTokenList = fileTokenList;

//        printFileTokenInfo();

        for (int i = 0; i < this.fileTokenList.size(); i++) {
//            System.out.println("=======================================================================================================");
//            System.out.println("loop file : " + this.fileTokenList.get(i).getFileName());
            System.out.println("location : " + this.fileTokenList.get(i).getLocation());
            List<Variable> aliveStaticField = new ArrayList<>(); // set aliveStaticField to FileToken
            List<Variable> aliveField = new ArrayList<>(); // add aliveField to aliveVariable
            List<Variable> aliveVariable = new ArrayList<>(); // contains aliveField and aliveVariable
            List<Variable> deadVariable = new ArrayList<>(); // contains deadVariable and deadField (not contains dead static field)

            //==================================================== check regex for static field =====================================================================
            // check static field
            if (this.fileTokenList.get(i).getStaticField().size() > 0) {
                for (int j = 0; j < this.fileTokenList.size(); j++) {
                    if (this.fileTokenList.get(i).getStaticField().size() > 0) {
                        System.out.println("check static field with : " + this.fileTokenList.get(j).getLocation());
                        checkRegexForStaticField(this.fileTokenList.get(i), aliveStaticField, this.fileTokenList.get(j), this.fileTokenList.get(i).getStaticField());
                    }
                }
            }

            //set alive/dead static field to fileToken
            if (aliveStaticField.size() > 0) {
                this.fileTokenList.get(i).setAliveStaticField(aliveStaticField);
            }
            if (this.fileTokenList.get(i).getStaticField().size() > 0) {
                this.fileTokenList.get(i).setDeadStaticField(this.fileTokenList.get(i).getStaticField()); // dead static field
                this.fileTokenList.get(i).addDeadField(this.fileTokenList.get(i).getStaticField()); // add DeadStaticField to DeadField
            }
            //========================================================================================================================================================


            //====================================================== check regex for field ===========================================================================
            // check field for file token
            if (this.fileTokenList.get(i).getField().size() > 0) {
                checkRegexForFileToken(aliveField, this.fileTokenList.get(i));
            }

            //set alive/dead field to fileToken
            if (aliveField.size() > 0) {
                aliveVariable.addAll(aliveField);
            }
            if (this.fileTokenList.get(i).getField().size() > 0) {
                this.fileTokenList.get(i).addDeadField(this.fileTokenList.get(i).getField());
                deadVariable.addAll(this.fileTokenList.get(i).getField()); // dead field
            }
            //========================================================================================================================================================


            //==================================================== check regex method token section ==================================================================
            // check variable for method token
            Integer count = 0;
            for (int j = 0; j < this.fileTokenList.get(i).getMethodTokenList().size(); j++) {
                count = count + this.fileTokenList.get(i).getMethodTokenList().get(j).getVariable().size();
            }

            if (count > 0) {
                checkRegexForMethodToken(aliveVariable, this.fileTokenList.get(i));
            }

            if (aliveVariable.size() > 0) {
                aliveVariable.addAll(aliveVariable);
            }

            if (this.fileTokenList.get(i).getMethodTokenList().size() > 0) {
                for (int k=0; k<this.fileTokenList.get(i).getMethodTokenList().size(); k++) {
                    if (this.fileTokenList.get(i).getMethodTokenList().get(k).getVariable().size() > 0) {
                        deadVariable.addAll(this.fileTokenList.get(i).getMethodTokenList().get(k).getVariable()); // dead variable
                    }
                }
            }
            //================================================= set Alive/Dead variable section =============================================================

            this.fileTokenList.get(i).setAliveVariable(aliveVariable);
            this.fileTokenList.get(i).setDeadVariable(deadVariable);

            List<Variable> AllAliveVariable = new ArrayList<>();
            AllAliveVariable.addAll(aliveVariable);
            AllAliveVariable.addAll(aliveStaticField);
            List<Variable> AllDeadVariable = new ArrayList<>();
            AllDeadVariable.addAll(deadVariable);
            AllDeadVariable.addAll(this.fileTokenList.get(i).getDeadStaticField());
            this.fileTokenList.get(i).setAllAliveVariable(AllAliveVariable);
            this.fileTokenList.get(i).setAllDeadVariable(AllDeadVariable);
        }

        //============================================== check FieldFromParentClass section ================================================================
        //นำ class ที่เป็น parent ของทุก class ทีมี dead field เหลืออยู่
        //add ไป fileThatHasDeadFieldFromParentClass -> จะได้ 1 group

        //หาว่าไฟล์ไหนมี dead field บ้าง
        List<FileToken> fileThatHasDeadField = new ArrayList<>();
        for (int y=0; y<this.fileTokenList.size(); y++) {
            if (this.fileTokenList.get(y).getDeadField().size() > 0) {
                fileThatHasDeadField.add(this.fileTokenList.get(y));
            }
        }

        //หาว่าไฟล์ไหนมี parent class บ้าง
        List<FileToken> fileThatHasParentClass = new ArrayList<>();
        for (int i=0; i<this.fileTokenList.size(); i++) {
            if (this.fileTokenList.get(i).getParentClass().size() > 0) {
                fileThatHasParentClass.add(this.fileTokenList.get(i));
            }
        }

        //ParentAndChild เก็บ (ชิ่อ Parent, List<Child>) -> List<Child> ยังไม่มี DeadFieldFromParentClass
        HashMap<String, List<FileToken>> ParentAndChild = new HashMap<>();
        for (int i=0; i<fileThatHasParentClass.size(); i++) {
            for (int j=0; j<fileThatHasDeadField.size(); j++) {
                for (int k=0; k<fileThatHasParentClass.get(i).getParentClass().size(); k++) {
                    if (fileThatHasParentClass.get(i).getParentClass().get(k).equals(fileThatHasDeadField.get(j).getFileName())) {
                        List<FileToken> temp = new ArrayList<>();
                        if (ParentAndChild.isEmpty()) {
                            fileThatHasParentClass.get(i).setFieldFromParentClass(fileThatHasDeadField.get(j).getDeadField());
                            temp.add(fileThatHasParentClass.get(i));
                            ParentAndChild.put(fileThatHasDeadField.get(j).getFileName(), temp);
                        }
                        else if (!ParentAndChild.isEmpty()) {
                            if (ParentAndChild.containsKey(fileThatHasDeadField.get(j).getFileName())) {
                                fileThatHasParentClass.get(i).setFieldFromParentClass(fileThatHasDeadField.get(j).getDeadField());
                                ParentAndChild.get(fileThatHasDeadField.get(j).getFileName()).add(fileThatHasParentClass.get(i));
                            }
                            else if (!ParentAndChild.containsKey(fileThatHasDeadField.get(j).getFileName())) {
                                fileThatHasParentClass.get(i).setFieldFromParentClass(fileThatHasDeadField.get(j).getDeadField());
                                temp.add(fileThatHasParentClass.get(i));
                                ParentAndChild.put(fileThatHasDeadField.get(j).getFileName(), temp);
                            }
                        }
                    }
                }
            }
        }

        //สร้าง fileHasDeadFieldFromParentList
        List<FileHasDeadFieldFromParent> fileHasDeadFieldFromParentList = new ArrayList<>();

        Set<String> set = ParentAndChild.keySet();
        Iterator it = set.iterator();
        while (it.hasNext()) {
            String filename = (String) it.next();

            List<FileToken> temp = new ArrayList<>();
            for (int i=0; i<ParentAndChild.get(filename).size(); i++) {
                temp.add(ParentAndChild.get(filename).get(i));
            }

            FileHasDeadFieldFromParent fileHasDeadFieldFromParent = new FileHasDeadFieldFromParent();
            fileHasDeadFieldFromParent.setParentName(filename);
            fileHasDeadFieldFromParent.setChildList(temp);
            fileHasDeadFieldFromParentList.add(fileHasDeadFieldFromParent);
        }

        //สร้าง checkRegexForFieldFromParentClass เพื่อตรวจเฉพาะ fieldFromParentClass
        HashMap<String, List<Variable>> mapToDeleteDeadVariable = new HashMap<>();
        for (int i=0; i<fileHasDeadFieldFromParentList.size(); i++) {
            List<Variable> aliveFieldFromParentClass = new ArrayList<>();

            for (int j=0; j<fileHasDeadFieldFromParentList.get(i).getChildList().size(); j++) {
                checkRegexForFieldFromParentClass(aliveFieldFromParentClass, fileHasDeadFieldFromParentList.get(i).getChildList().get(j));
            }

            if (aliveFieldFromParentClass.size() > 0) {
                mapToDeleteDeadVariable.put(fileHasDeadFieldFromParentList.get(i).getParentName(), aliveFieldFromParentClass); // put to HashMap
            }
        }

        //ลบ dead field
        Iterator iterator1 = mapToDeleteDeadVariable.keySet().iterator();
        while (iterator1.hasNext()) {
            String fileName = (String) iterator1.next();
            for (int k=0; k<this.fileTokenList.size(); k++) {
                if (this.fileTokenList.get(k).getFileName().equals(fileName)) {
                    this.fileTokenList.get(k).getAllDeadVariable().removeAll(mapToDeleteDeadVariable.get(fileName));
                }
            }

        }
        //========================================================================================================================================================

//        printAllDeadVariableInfo();
    }

    private void checkRegexForStaticField(FileToken currentFileToken, List<Variable> aliveStaticField, FileToken fileTokenToDetect, List<Variable> staticField) {
        List<Variable> staticFieldName = staticField;

        if (staticField.size() > 0) {
            if (fileTokenToDetect.getMethodCalls().size() > 0) {
                aliveStaticField.addAll(checkDeadVariableInMethodCallExpr(currentFileToken, fileTokenToDetect, staticFieldName, fileTokenToDetect.getMethodCalls()));
            }
        }
        if (staticField.size() > 0) {
            if (fileTokenToDetect.getAssignExpr().size() > 0) {
                aliveStaticField.addAll(checkDeadVariableInAssignExpr(currentFileToken, fileTokenToDetect, staticFieldName, fileTokenToDetect.getAssignExpr()));
            }
        }
        if (staticField.size() > 0) {
            if (fileTokenToDetect.getObjectCreationExpr().size() > 0) {
                aliveStaticField.addAll(checkDeadVariableInObjectCreationExpr(currentFileToken, fileTokenToDetect, staticFieldName, fileTokenToDetect.getObjectCreationExpr()));
            }
        }
        if (staticField.size() > 0) {
            if (fileTokenToDetect.getIfStmt().size() > 0) {
                aliveStaticField.addAll(checkDeadVariableInIfStmt(currentFileToken, fileTokenToDetect, staticFieldName, fileTokenToDetect.getIfStmt()));
            }
        }
        if (staticField.size() > 0) {
            if (fileTokenToDetect.getForStmt().size() > 0) {
                aliveStaticField.addAll(checkDeadVariableInForStmt(currentFileToken, fileTokenToDetect, staticFieldName, fileTokenToDetect.getForStmt()));
            }
        }
        if (staticField.size() > 0) {
            if (fileTokenToDetect.getForeachStmt().size() > 0) {
                aliveStaticField.addAll(checkDeadVariableInForeachStmt(currentFileToken, fileTokenToDetect, staticFieldName, fileTokenToDetect.getForeachStmt()));
            }
        }
        if (staticField.size() > 0) {
            if (fileTokenToDetect.getReturnStmt().size() > 0) {
                aliveStaticField.addAll(checkDeadVariableInReturnStmt(currentFileToken, fileTokenToDetect, staticFieldName, fileTokenToDetect.getReturnStmt()));
            }
        }
        if (staticField.size() > 0) {
            if (fileTokenToDetect.getWhileStmt().size() > 0) {
                aliveStaticField.addAll(checkDeadVariableInWhileStmt(currentFileToken, fileTokenToDetect, staticFieldName, fileTokenToDetect.getWhileStmt()));
            }
        }
        if (staticField.size() > 0) {
            if (fileTokenToDetect.getDoStmt().size() > 0) {
                aliveStaticField.addAll(checkDeadVariableInDoStmt(currentFileToken, fileTokenToDetect, staticFieldName, fileTokenToDetect.getDoStmt()));
            }
        }
        if (staticField.size() > 0) {
            if (fileTokenToDetect.getSwitchStmt().size() > 0) {
                aliveStaticField.addAll(checkDeadVariableInSwitchStmt(currentFileToken, fileTokenToDetect, staticFieldName, fileTokenToDetect.getSwitchStmt()));
            }
        }
        if (staticField.size() > 0) {
            if (fileTokenToDetect.getVariableDeclarator().size() > 0) {
                aliveStaticField.addAll(checkDeadVariableInVariableDeclarator(currentFileToken, fileTokenToDetect, staticFieldName, fileTokenToDetect.getVariableDeclarator()));
            }
        }

        for (int i = 0; i < fileTokenToDetect.getMethodTokenList().size(); i++) {
            if (staticField.size() > 0) {
                if (fileTokenToDetect.getMethodTokenList().get(i).getMethodCalls().size() > 0) {
                    aliveStaticField.addAll(checkDeadVariableInMethodCallExpr(currentFileToken, fileTokenToDetect, staticFieldName, fileTokenToDetect.getMethodTokenList().get(i).getMethodCalls()));
                }
            }
            if (staticField.size() > 0) {
                if (fileTokenToDetect.getMethodTokenList().get(i).getAssignExpr().size() > 0) {
                    aliveStaticField.addAll(checkDeadVariableInAssignExpr(currentFileToken, fileTokenToDetect, staticFieldName, fileTokenToDetect.getMethodTokenList().get(i).getAssignExpr()));
                }
            }
            if (staticField.size() > 0) {
                if (fileTokenToDetect.getMethodTokenList().get(i).getObjectCreationExpr().size() > 0) {
                    aliveStaticField.addAll(checkDeadVariableInObjectCreationExpr(currentFileToken, fileTokenToDetect, staticFieldName, fileTokenToDetect.getMethodTokenList().get(i).getObjectCreationExpr()));
                }
            }
            if (staticField.size() > 0) {
                if (fileTokenToDetect.getMethodTokenList().get(i).getIfStmt().size() > 0) {
                    aliveStaticField.addAll(checkDeadVariableInIfStmt(currentFileToken, fileTokenToDetect, staticFieldName, fileTokenToDetect.getMethodTokenList().get(i).getIfStmt()));
                }
            }
            if (staticField.size() > 0) {
                if (fileTokenToDetect.getMethodTokenList().get(i).getForStmt().size() > 0) {
                    aliveStaticField.addAll(checkDeadVariableInForStmt(currentFileToken, fileTokenToDetect, staticFieldName, fileTokenToDetect.getMethodTokenList().get(i).getForStmt()));
                }
            }
            if (staticField.size() > 0) {
                if (fileTokenToDetect.getMethodTokenList().get(i).getForeachStmt().size() > 0) {
                    aliveStaticField.addAll(checkDeadVariableInForeachStmt(currentFileToken, fileTokenToDetect, staticFieldName, fileTokenToDetect.getMethodTokenList().get(i).getForeachStmt()));
                }
            }
            if (staticField.size() > 0) {
                if (fileTokenToDetect.getMethodTokenList().get(i).getReturnStmt().size() > 0) {
                    aliveStaticField.addAll(checkDeadVariableInReturnStmt(currentFileToken, fileTokenToDetect, staticFieldName, fileTokenToDetect.getMethodTokenList().get(i).getReturnStmt()));
                }
            }
            if (staticField.size() > 0) {
                if (fileTokenToDetect.getMethodTokenList().get(i).getWhileStmt().size() > 0) {
                    aliveStaticField.addAll(checkDeadVariableInWhileStmt(currentFileToken, fileTokenToDetect, staticFieldName, fileTokenToDetect.getMethodTokenList().get(i).getWhileStmt()));
                }
            }
            if (staticField.size() > 0) {
                if (fileTokenToDetect.getMethodTokenList().get(i).getDoStmt().size() > 0) {
                    aliveStaticField.addAll(checkDeadVariableInDoStmt(currentFileToken, fileTokenToDetect, staticFieldName, fileTokenToDetect.getMethodTokenList().get(i).getDoStmt()));
                }
            }
            if (staticField.size() > 0) {
                if (fileTokenToDetect.getMethodTokenList().get(i).getSwitchStmt().size() > 0) {
                    aliveStaticField.addAll(checkDeadVariableInSwitchStmt(currentFileToken, fileTokenToDetect, staticFieldName, fileTokenToDetect.getMethodTokenList().get(i).getSwitchStmt()));
                }
            }
            if (staticField.size() > 0) {
                if (fileTokenToDetect.getMethodTokenList().get(i).getVariableDeclarator().size() > 0) {
                    aliveStaticField.addAll(checkDeadVariableInVariableDeclarator(currentFileToken, fileTokenToDetect, staticFieldName, fileTokenToDetect.getMethodTokenList().get(i).getVariableDeclarator()));
                }
            }
        }
    }

    //method to operate algorithm for checking regex for file token
    private void checkRegexForFileToken(List<Variable> aliveField, FileToken fileTokenToDetect) {
        FileToken currentFileToken = fileTokenToDetect;
        List<Variable> variableToDetect = fileTokenToDetect.getField();

        //check for -> field in file accept any cases (not include within method) if found -> add to alive variable
        if (currentFileToken.getField().size() > 0) {
            aliveField.addAll(checkDeadVariableInMethodCallExpr(currentFileToken, fileTokenToDetect, variableToDetect, fileTokenToDetect.getMethodCalls()));
        }
        if (currentFileToken.getField().size() > 0) {
            aliveField.addAll(checkDeadVariableInAssignExpr(currentFileToken, fileTokenToDetect, variableToDetect, fileTokenToDetect.getAssignExpr()));
        }
        if (currentFileToken.getField().size() > 0) {
            aliveField.addAll(checkDeadVariableInObjectCreationExpr(currentFileToken, fileTokenToDetect, variableToDetect, fileTokenToDetect.getObjectCreationExpr()));
        }
        if (currentFileToken.getField().size() > 0) {
            aliveField.addAll(checkDeadVariableInIfStmt(currentFileToken, fileTokenToDetect, variableToDetect, fileTokenToDetect.getIfStmt()));
        }
        if (currentFileToken.getField().size() > 0) {
            aliveField.addAll(checkDeadVariableInForStmt(currentFileToken, fileTokenToDetect, variableToDetect, fileTokenToDetect.getForStmt()));
        }
        if (currentFileToken.getField().size() > 0) {
            aliveField.addAll(checkDeadVariableInForeachStmt(currentFileToken, fileTokenToDetect, variableToDetect, fileTokenToDetect.getForeachStmt()));
        }
        if (currentFileToken.getField().size() > 0) {
            aliveField.addAll(checkDeadVariableInReturnStmt(currentFileToken, fileTokenToDetect, variableToDetect, fileTokenToDetect.getReturnStmt()));
        }
        if (currentFileToken.getField().size() > 0) {
            aliveField.addAll(checkDeadVariableInWhileStmt(currentFileToken, fileTokenToDetect, variableToDetect, fileTokenToDetect.getWhileStmt()));
        }
        if (currentFileToken.getField().size() > 0) {
            aliveField.addAll(checkDeadVariableInDoStmt(currentFileToken, fileTokenToDetect, variableToDetect, fileTokenToDetect.getDoStmt()));
        }
        if (currentFileToken.getField().size() > 0) {
            aliveField.addAll(checkDeadVariableInSwitchStmt(currentFileToken, fileTokenToDetect, variableToDetect, fileTokenToDetect.getSwitchStmt()));
        }
        if (currentFileToken.getField().size() > 0) {
            aliveField.addAll(checkDeadVariableInVariableDeclarator(currentFileToken, fileTokenToDetect, variableToDetect, fileTokenToDetect.getVariableDeclarator()));
        }

        //check for -> field in file accept any cases (within method) if found -> add to alive variable
        for (int i = 0; i < fileTokenToDetect.getMethodTokenList().size(); i++) {
            if (currentFileToken.getField().size() > 0) {
                aliveField.addAll(checkDeadVariableInMethodCallExpr(currentFileToken, fileTokenToDetect, variableToDetect, fileTokenToDetect.getMethodTokenList().get(i).getMethodCalls()));
            }
            if (currentFileToken.getField().size() > 0) {
                aliveField.addAll(checkDeadVariableInAssignExpr(currentFileToken, fileTokenToDetect, variableToDetect, fileTokenToDetect.getMethodTokenList().get(i).getAssignExpr()));
            }
            if (currentFileToken.getField().size() > 0) {
                aliveField.addAll(checkDeadVariableInObjectCreationExpr(currentFileToken, fileTokenToDetect, variableToDetect, fileTokenToDetect.getMethodTokenList().get(i).getObjectCreationExpr()));
            }
            if (currentFileToken.getField().size() > 0) {
                aliveField.addAll(checkDeadVariableInIfStmt(currentFileToken, fileTokenToDetect, variableToDetect, fileTokenToDetect.getMethodTokenList().get(i).getIfStmt()));
            }
            if (currentFileToken.getField().size() > 0) {
                aliveField.addAll(checkDeadVariableInForStmt(currentFileToken, fileTokenToDetect, variableToDetect, fileTokenToDetect.getMethodTokenList().get(i).getForStmt()));
            }
            if (currentFileToken.getField().size() > 0) {
                aliveField.addAll(checkDeadVariableInForeachStmt(currentFileToken, fileTokenToDetect, variableToDetect, fileTokenToDetect.getMethodTokenList().get(i).getForeachStmt()));
            }
            if (currentFileToken.getField().size() > 0) {
                aliveField.addAll(checkDeadVariableInReturnStmt(currentFileToken, fileTokenToDetect, variableToDetect, fileTokenToDetect.getMethodTokenList().get(i).getReturnStmt()));
            }
            if (currentFileToken.getField().size() > 0) {
                aliveField.addAll(checkDeadVariableInWhileStmt(currentFileToken, fileTokenToDetect, variableToDetect, fileTokenToDetect.getMethodTokenList().get(i).getWhileStmt()));
            }
            if (currentFileToken.getField().size() > 0) {
                aliveField.addAll(checkDeadVariableInDoStmt(currentFileToken, fileTokenToDetect, variableToDetect, fileTokenToDetect.getMethodTokenList().get(i).getDoStmt()));
            }
            if (currentFileToken.getField().size() > 0) {
                aliveField.addAll(checkDeadVariableInSwitchStmt(currentFileToken, fileTokenToDetect, variableToDetect, fileTokenToDetect.getMethodTokenList().get(i).getSwitchStmt()));
            }
            if (currentFileToken.getField().size() > 0) {
                aliveField.addAll(checkDeadVariableInVariableDeclarator(currentFileToken, fileTokenToDetect, variableToDetect, fileTokenToDetect.getMethodTokenList().get(i).getVariableDeclarator()));
            }
        }
    }

    //method to operate algorithm for checking regex for method token
    private void checkRegexForMethodToken(List<Variable> aliveVariable, FileToken fileTokenToDetect) {
        FileToken currentFileToken = fileTokenToDetect;

        for (int i = 0; i < currentFileToken.getMethodTokenList().size(); i++) {
            if (currentFileToken.getMethodTokenList().get(i).getVariable().size() > 0) {
                if (currentFileToken.getMethodTokenList().get(i).getVariable().size() > 0) {
                    aliveVariable.addAll(checkDeadVariableInMethodCallExpr(currentFileToken, fileTokenToDetect, currentFileToken.getMethodTokenList().get(i).getVariable(), fileTokenToDetect.getMethodTokenList().get(i).getMethodCalls()));
                }
                if (currentFileToken.getMethodTokenList().get(i).getVariable().size() > 0) {
                    aliveVariable.addAll(checkDeadVariableInAssignExpr(currentFileToken, fileTokenToDetect, currentFileToken.getMethodTokenList().get(i).getVariable(), fileTokenToDetect.getMethodTokenList().get(i).getAssignExpr()));
                }
                if (currentFileToken.getMethodTokenList().get(i).getVariable().size() > 0) {
                    aliveVariable.addAll(checkDeadVariableInObjectCreationExpr(currentFileToken, fileTokenToDetect, currentFileToken.getMethodTokenList().get(i).getVariable(), fileTokenToDetect.getMethodTokenList().get(i).getObjectCreationExpr()));
                }
                if (currentFileToken.getMethodTokenList().get(i).getVariable().size() > 0) {
                    aliveVariable.addAll(checkDeadVariableInIfStmt(currentFileToken, fileTokenToDetect, currentFileToken.getMethodTokenList().get(i).getVariable(), fileTokenToDetect.getMethodTokenList().get(i).getIfStmt()));
                }
                if (currentFileToken.getMethodTokenList().get(i).getVariable().size() > 0) {
                    aliveVariable.addAll(checkDeadVariableInForStmt(currentFileToken, fileTokenToDetect, currentFileToken.getMethodTokenList().get(i).getVariable(), fileTokenToDetect.getMethodTokenList().get(i).getForStmt()));
                }
                if (currentFileToken.getMethodTokenList().get(i).getVariable().size() > 0) {
                    aliveVariable.addAll(checkDeadVariableInForeachStmt(currentFileToken, fileTokenToDetect, currentFileToken.getMethodTokenList().get(i).getVariable(), fileTokenToDetect.getMethodTokenList().get(i).getForeachStmt()));
                }
                if (currentFileToken.getMethodTokenList().get(i).getVariable().size() > 0) {
                    aliveVariable.addAll(checkDeadVariableInReturnStmt(currentFileToken, fileTokenToDetect, currentFileToken.getMethodTokenList().get(i).getVariable(), fileTokenToDetect.getMethodTokenList().get(i).getReturnStmt()));
                }
                if (currentFileToken.getMethodTokenList().get(i).getVariable().size() > 0) {
                    aliveVariable.addAll(checkDeadVariableInWhileStmt(currentFileToken, fileTokenToDetect, currentFileToken.getMethodTokenList().get(i).getVariable(), fileTokenToDetect.getMethodTokenList().get(i).getWhileStmt()));
                }
                if (currentFileToken.getMethodTokenList().get(i).getVariable().size() > 0) {
                    aliveVariable.addAll(checkDeadVariableInDoStmt(currentFileToken, fileTokenToDetect, currentFileToken.getMethodTokenList().get(i).getVariable(), fileTokenToDetect.getMethodTokenList().get(i).getDoStmt()));
                }
                if (currentFileToken.getMethodTokenList().get(i).getVariable().size() > 0) {
                    aliveVariable.addAll(checkDeadVariableInSwitchStmt(currentFileToken, fileTokenToDetect, currentFileToken.getMethodTokenList().get(i).getVariable(), fileTokenToDetect.getMethodTokenList().get(i).getSwitchStmt()));
                }
                if (currentFileToken.getMethodTokenList().get(i).getVariable().size() > 0) {
                    aliveVariable.addAll(checkDeadVariableInVariableDeclarator(currentFileToken, fileTokenToDetect, currentFileToken.getMethodTokenList().get(i).getVariable(), fileTokenToDetect.getMethodTokenList().get(i).getVariableDeclarator()));
                }
            }
        }
    }

    //method to operate algorithm for checking fieldFromParentClass for file token
    private void checkRegexForFieldFromParentClass(List<Variable> aliveField, FileToken fileTokenToDetect) {
        FileToken currentFileToken = fileTokenToDetect;

        //check for -> field in file accept any cases (not include within method) if found -> add to alive variable
        if (currentFileToken.getFieldFromParentClass().size() > 0) {
            List<Variable> varTemp = checkDeadVariableInMethodCallExpr(currentFileToken, fileTokenToDetect, fileTokenToDetect.getFieldFromParentClass(), fileTokenToDetect.getMethodCalls());

            if (aliveField.isEmpty()) {
                aliveField.addAll(varTemp);
            }
            else if (!aliveField.isEmpty()) {
                for (int i=0; i<aliveField.size(); i++) {
                    for (int j=0; j<varTemp.size(); j++) {
                        if (!aliveField.contains(varTemp.get(j))) {
                            aliveField.add(varTemp.get(j));
                        }
                    }
                }
            }
        }
        if (currentFileToken.getFieldFromParentClass().size() > 0) {
            List<Variable> varTemp = checkDeadVariableInAssignExpr(currentFileToken, fileTokenToDetect, fileTokenToDetect.getFieldFromParentClass(), fileTokenToDetect.getAssignExpr());

            if (aliveField.isEmpty()) {
                aliveField.addAll(varTemp);
            }
            else if (!aliveField.isEmpty()) {
                for (int i=0; i<aliveField.size(); i++) {
                    for (int j=0; j<varTemp.size(); j++) {
                        if (!aliveField.contains(varTemp.get(j))) {
                            aliveField.add(varTemp.get(j));
                        }
                    }
                }
            }
        }
        if (currentFileToken.getFieldFromParentClass().size() > 0) {
            List<Variable> varTemp = checkDeadVariableInObjectCreationExpr(currentFileToken, fileTokenToDetect, fileTokenToDetect.getFieldFromParentClass(), fileTokenToDetect.getObjectCreationExpr());

            if (aliveField.isEmpty()) {
                aliveField.addAll(varTemp);
            }
            else if (!aliveField.isEmpty()) {
                for (int i=0; i<aliveField.size(); i++) {
                    for (int j=0; j<varTemp.size(); j++) {
                        if (!aliveField.contains(varTemp.get(j))) {
                            aliveField.add(varTemp.get(j));
                        }
                    }
                }
            }
        }
        if (currentFileToken.getFieldFromParentClass().size() > 0) {
            List<Variable> varTemp = checkDeadVariableInIfStmt(currentFileToken, fileTokenToDetect, fileTokenToDetect.getFieldFromParentClass(), fileTokenToDetect.getIfStmt());

            if (aliveField.isEmpty()) {
                aliveField.addAll(varTemp);
            }
            else if (!aliveField.isEmpty()) {
                for (int i=0; i<aliveField.size(); i++) {
                    for (int j=0; j<varTemp.size(); j++) {
                        if (!aliveField.contains(varTemp.get(j))) {
                            aliveField.add(varTemp.get(j));
                        }
                    }
                }
            }
        }
        if (currentFileToken.getFieldFromParentClass().size() > 0) {
            List<Variable> varTemp = checkDeadVariableInForStmt(currentFileToken, fileTokenToDetect, fileTokenToDetect.getFieldFromParentClass(), fileTokenToDetect.getForStmt());

            if (aliveField.isEmpty()) {
                aliveField.addAll(varTemp);
            }
            else if (!aliveField.isEmpty()) {
                for (int i=0; i<aliveField.size(); i++) {
                    for (int j=0; j<varTemp.size(); j++) {
                        if (!aliveField.contains(varTemp.get(j))) {
                            aliveField.add(varTemp.get(j));
                        }
                    }
                }
            }
        }
        if (currentFileToken.getFieldFromParentClass().size() > 0) {
            List<Variable> varTemp = checkDeadVariableInForeachStmt(currentFileToken, fileTokenToDetect, fileTokenToDetect.getFieldFromParentClass(), fileTokenToDetect.getForeachStmt());

            if (aliveField.isEmpty()) {
                aliveField.addAll(varTemp);
            }
            else if (!aliveField.isEmpty()) {
                for (int i=0; i<aliveField.size(); i++) {
                    for (int j=0; j<varTemp.size(); j++) {
                        if (!aliveField.contains(varTemp.get(j))) {
                            aliveField.add(varTemp.get(j));
                        }
                    }
                }
            }
        }
        if (currentFileToken.getFieldFromParentClass().size() > 0) {
            List<Variable> varTemp = checkDeadVariableInReturnStmt(currentFileToken, fileTokenToDetect, fileTokenToDetect.getFieldFromParentClass(), fileTokenToDetect.getReturnStmt());

            if (aliveField.isEmpty()) {
                aliveField.addAll(varTemp);
            }
            else if (!aliveField.isEmpty()) {
                for (int i=0; i<aliveField.size(); i++) {
                    for (int j=0; j<varTemp.size(); j++) {
                        if (!aliveField.contains(varTemp.get(j))) {
                            aliveField.add(varTemp.get(j));
                        }
                    }
                }
            }
        }
        if (currentFileToken.getFieldFromParentClass().size() > 0) {
            List<Variable> varTemp = checkDeadVariableInWhileStmt(currentFileToken, fileTokenToDetect, fileTokenToDetect.getFieldFromParentClass(), fileTokenToDetect.getWhileStmt());

            if (aliveField.isEmpty()) {
                aliveField.addAll(varTemp);
            }
            else if (!aliveField.isEmpty()) {
                for (int i=0; i<aliveField.size(); i++) {
                    for (int j=0; j<varTemp.size(); j++) {
                        if (!aliveField.contains(varTemp.get(j))) {
                            aliveField.add(varTemp.get(j));
                        }
                    }
                }
            }
        }
        if (currentFileToken.getFieldFromParentClass().size() > 0) {
            List<Variable> varTemp = checkDeadVariableInDoStmt(currentFileToken, fileTokenToDetect, fileTokenToDetect.getFieldFromParentClass(), fileTokenToDetect.getDoStmt());

            if (aliveField.isEmpty()) {
                aliveField.addAll(varTemp);
            }
            else if (!aliveField.isEmpty()) {
                for (int i=0; i<aliveField.size(); i++) {
                    for (int j=0; j<varTemp.size(); j++) {
                        if (!aliveField.contains(varTemp.get(j))) {
                            aliveField.add(varTemp.get(j));
                        }
                    }
                }
            }
        }
        if (currentFileToken.getFieldFromParentClass().size() > 0) {
            List<Variable> varTemp = checkDeadVariableInSwitchStmt(currentFileToken, fileTokenToDetect, fileTokenToDetect.getFieldFromParentClass(), fileTokenToDetect.getSwitchStmt());

            if (aliveField.isEmpty()) {
                aliveField.addAll(varTemp);
            }
            else if (!aliveField.isEmpty()) {
                for (int i=0; i<aliveField.size(); i++) {
                    for (int j=0; j<varTemp.size(); j++) {
                        if (!aliveField.contains(varTemp.get(j))) {
                            aliveField.add(varTemp.get(j));
                        }
                    }
                }
            }
        }
        if (currentFileToken.getFieldFromParentClass().size() > 0) {
            List<Variable> varTemp = checkDeadVariableInVariableDeclarator(currentFileToken, fileTokenToDetect, fileTokenToDetect.getFieldFromParentClass(), fileTokenToDetect.getVariableDeclarator());

            if (aliveField.isEmpty()) {
                aliveField.addAll(varTemp);
            }
            else if (!aliveField.isEmpty()) {
                for (int i=0; i<aliveField.size(); i++) {
                    for (int j=0; j<varTemp.size(); j++) {
                        if (!aliveField.contains(varTemp.get(j))) {
                            aliveField.add(varTemp.get(j));
                        }
                    }
                }
            }
        }

        //check for -> field in file accept any cases (within method) if found -> add to alive variable
        for (int i = 0; i < fileTokenToDetect.getMethodTokenList().size(); i++) {
            if (currentFileToken.getFieldFromParentClass().size() > 0) {
                List<Variable> varTemp = checkDeadVariableInMethodCallExpr(currentFileToken, fileTokenToDetect, fileTokenToDetect.getFieldFromParentClass(), fileTokenToDetect.getMethodTokenList().get(i).getMethodCalls());

                if (aliveField.isEmpty()) {
                    aliveField.addAll(varTemp);
                }
                else if (!aliveField.isEmpty()) {
                    for (int k=0; k<aliveField.size(); k++) {
                        for (int j=0; j<varTemp.size(); j++) {
                            if (!aliveField.contains(varTemp.get(j))) {
                                aliveField.add(varTemp.get(j));
                            }
                        }
                    }
                }
            }
            if (currentFileToken.getFieldFromParentClass().size() > 0) {
                List<Variable> varTemp = checkDeadVariableInAssignExpr(currentFileToken, fileTokenToDetect, fileTokenToDetect.getFieldFromParentClass(), fileTokenToDetect.getMethodTokenList().get(i).getAssignExpr());

                if (aliveField.isEmpty()) {
                    aliveField.addAll(varTemp);
                }
                else if (!aliveField.isEmpty()) {
                    for (int k=0; k<aliveField.size(); k++) {
                        for (int j=0; j<varTemp.size(); j++) {
                            if (!aliveField.contains(varTemp.get(j))) {
                                aliveField.add(varTemp.get(j));
                            }
                        }
                    }
                }
            }
            if (currentFileToken.getFieldFromParentClass().size() > 0) {
                List<Variable> varTemp = checkDeadVariableInObjectCreationExpr(currentFileToken, fileTokenToDetect, fileTokenToDetect.getFieldFromParentClass(), fileTokenToDetect.getMethodTokenList().get(i).getObjectCreationExpr());

                if (aliveField.isEmpty()) {
                    aliveField.addAll(varTemp);
                }
                else if (!aliveField.isEmpty()) {
                    for (int k=0; k<aliveField.size(); k++) {
                        for (int j=0; j<varTemp.size(); j++) {
                            if (!aliveField.contains(varTemp.get(j))) {
                                aliveField.add(varTemp.get(j));
                            }
                        }
                    }
                }
            }
            if (currentFileToken.getFieldFromParentClass().size() > 0) {
                List<Variable> varTemp = checkDeadVariableInIfStmt(currentFileToken, fileTokenToDetect, fileTokenToDetect.getFieldFromParentClass(), fileTokenToDetect.getMethodTokenList().get(i).getIfStmt());

                if (aliveField.isEmpty()) {
                    aliveField.addAll(varTemp);
                }
                else if (!aliveField.isEmpty()) {
                    for (int k=0; k<aliveField.size(); k++) {
                        for (int j=0; j<varTemp.size(); j++) {
                            if (!aliveField.contains(varTemp.get(j))) {
                                aliveField.add(varTemp.get(j));
                            }
                        }
                    }
                }
            }
            if (currentFileToken.getFieldFromParentClass().size() > 0) {
                List<Variable> varTemp = checkDeadVariableInForStmt(currentFileToken, fileTokenToDetect, fileTokenToDetect.getFieldFromParentClass(), fileTokenToDetect.getMethodTokenList().get(i).getForStmt());

                if (aliveField.isEmpty()) {
                    aliveField.addAll(varTemp);
                }
                else if (!aliveField.isEmpty()) {
                    for (int k=0; k<aliveField.size(); k++) {
                        for (int j=0; j<varTemp.size(); j++) {
                            if (!aliveField.contains(varTemp.get(j))) {
                                aliveField.add(varTemp.get(j));
                            }
                        }
                    }
                }
            }
            if (currentFileToken.getFieldFromParentClass().size() > 0) {
                List<Variable> varTemp = checkDeadVariableInForeachStmt(currentFileToken, fileTokenToDetect, fileTokenToDetect.getFieldFromParentClass(), fileTokenToDetect.getMethodTokenList().get(i).getForeachStmt());

                if (aliveField.isEmpty()) {
                    aliveField.addAll(varTemp);
                }
                else if (!aliveField.isEmpty()) {
                    for (int k=0; k<aliveField.size(); k++) {
                        for (int j=0; j<varTemp.size(); j++) {
                            if (!aliveField.contains(varTemp.get(j))) {
                                aliveField.add(varTemp.get(j));
                            }
                        }
                    }
                }
            }
            if (currentFileToken.getFieldFromParentClass().size() > 0) {
                List<Variable> varTemp = checkDeadVariableInReturnStmt(currentFileToken, fileTokenToDetect, fileTokenToDetect.getFieldFromParentClass(), fileTokenToDetect.getMethodTokenList().get(i).getReturnStmt());

                if (aliveField.isEmpty()) {
                    aliveField.addAll(varTemp);
                }
                else if (!aliveField.isEmpty()) {
                    for (int k=0; k<aliveField.size(); k++) {
                        for (int j=0; j<varTemp.size(); j++) {
                            if (!aliveField.contains(varTemp.get(j))) {
                                aliveField.add(varTemp.get(j));
                            }
                        }
                    }
                }
            }
            if (currentFileToken.getFieldFromParentClass().size() > 0) {
                List<Variable> varTemp = checkDeadVariableInWhileStmt(currentFileToken, fileTokenToDetect, fileTokenToDetect.getFieldFromParentClass(), fileTokenToDetect.getMethodTokenList().get(i).getWhileStmt());

                if (aliveField.isEmpty()) {
                    aliveField.addAll(varTemp);
                }
                else if (!aliveField.isEmpty()) {
                    for (int k=0; k<aliveField.size(); k++) {
                        for (int j=0; j<varTemp.size(); j++) {
                            if (!aliveField.contains(varTemp.get(j))) {
                                aliveField.add(varTemp.get(j));
                            }
                        }
                    }
                }
            }
            if (currentFileToken.getFieldFromParentClass().size() > 0) {
                List<Variable> varTemp = checkDeadVariableInDoStmt(currentFileToken, fileTokenToDetect, fileTokenToDetect.getFieldFromParentClass(), fileTokenToDetect.getMethodTokenList().get(i).getDoStmt());

                if (aliveField.isEmpty()) {
                    aliveField.addAll(varTemp);
                }
                else if (!aliveField.isEmpty()) {
                    for (int k=0; k<aliveField.size(); k++) {
                        for (int j=0; j<varTemp.size(); j++) {
                            if (!aliveField.contains(varTemp.get(j))) {
                                aliveField.add(varTemp.get(j));
                            }
                        }
                    }
                }
            }
            if (currentFileToken.getFieldFromParentClass().size() > 0) {
                List<Variable> varTemp = checkDeadVariableInSwitchStmt(currentFileToken, fileTokenToDetect, fileTokenToDetect.getFieldFromParentClass(), fileTokenToDetect.getMethodTokenList().get(i).getSwitchStmt());

                if (aliveField.isEmpty()) {
                    aliveField.addAll(varTemp);
                }
                else if (!aliveField.isEmpty()) {
                    for (int k=0; k<aliveField.size(); k++) {
                        for (int j=0; j<varTemp.size(); j++) {
                            if (!aliveField.contains(varTemp.get(j))) {
                                aliveField.add(varTemp.get(j));
                            }
                        }
                    }
                }
            }
            if (currentFileToken.getFieldFromParentClass().size() > 0) {
                List<Variable> varTemp = checkDeadVariableInVariableDeclarator(currentFileToken, fileTokenToDetect, fileTokenToDetect.getFieldFromParentClass(), fileTokenToDetect.getMethodTokenList().get(i).getVariableDeclarator());

                if (aliveField.isEmpty()) {
                    aliveField.addAll(varTemp);
                }
                else if (!aliveField.isEmpty()) {
                    for (int k=0; k<aliveField.size(); k++) {
                        for (int j=0; j<varTemp.size(); j++) {
                            if (!aliveField.contains(varTemp.get(j))) {
                                aliveField.add(varTemp.get(j));
                            }
                        }
                    }
                }
            }
        }
    }

    //method to check regex for method call expr -> done
    private List<Variable> checkDeadVariableInMethodCallExpr(FileToken currentFileToken, FileToken fileTokenToDetect, List<Variable> variable, List<String> methodCallExpr) {
        //if location of fileToCheck equals to location of fileToDetect -> send fieldName with Extension (Class.field) -> set fieldName not contains extension  -> (field)
        if (!fileTokenToDetect.getLocation().equals(currentFileToken.getLocation())) {
            for (int i=0; i<variable.size(); i++) {
                String fieldName = variable.get(i).getParent() + "." + variable.get(i).getVariableName();
                variable.get(i).setStringToCheck(fieldName);
            }
        }
        else if (fileTokenToDetect.getLocation().equals(currentFileToken.getLocation())) {
            for (int i=0; i<variable.size(); i++) {
                variable.get(i).setStringToCheck(variable.get(i).getVariableName());
            }
        }

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
    private List<Variable> checkDeadVariableInAssignExpr(FileToken currentFileToken, FileToken fileTokenToDetect, List<Variable> variable, List<String>  assignExpr) {
        //if location of fileToCheck equals to location of fileToDetect -> send fieldName with Extension (Class.field) -> set fieldName not contains extension  -> (field)
        if (!fileTokenToDetect.getLocation().equals(currentFileToken.getLocation())) {
            for (int i=0; i<variable.size(); i++) {
                String fieldName = variable.get(i).getParent() + "." + variable.get(i).getVariableName();
                variable.get(i).setStringToCheck(fieldName);
            }
        }
        else if (fileTokenToDetect.getLocation().equals(currentFileToken.getLocation())) {
            for (int i=0; i<variable.size(); i++) {
                variable.get(i).setStringToCheck(variable.get(i).getVariableName());
            }
        }

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
    private List<Variable> checkDeadVariableInObjectCreationExpr (FileToken currentFileToken, FileToken fileTokenToDetect, List<Variable> variable, List<String> objectCreationExpr){
        //if location of fileToCheck equals to location of fileToDetect -> send fieldName with Extension (Class.field) -> set fieldName not contains extension  -> (field)
        if (!fileTokenToDetect.getLocation().equals(currentFileToken.getLocation())) {
            for (int i=0; i<variable.size(); i++) {
                String fieldName = variable.get(i).getParent() + "." + variable.get(i).getVariableName();
                variable.get(i).setStringToCheck(fieldName);
            }
        }
        else if (fileTokenToDetect.getLocation().equals(currentFileToken.getLocation())) {
            for (int i=0; i<variable.size(); i++) {
                variable.get(i).setStringToCheck(variable.get(i).getVariableName());
            }
        }

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
    private List<Variable> checkDeadVariableInIfStmt (FileToken currentFileToken, FileToken fileTokenToDetect, List<Variable> variable, List<String> ifStmt){
        //if location of fileToCheck equals to location of fileToDetect -> send fieldName with Extension (Class.field) -> set fieldName not contains extension  -> (field)
        if (!fileTokenToDetect.getLocation().equals(currentFileToken.getLocation())) {
            for (int i=0; i<variable.size(); i++) {
                String fieldName = variable.get(i).getParent() + "." + variable.get(i).getVariableName();
                variable.get(i).setStringToCheck(fieldName);
            }
        }
        else if (fileTokenToDetect.getLocation().equals(currentFileToken.getLocation())) {
            for (int i=0; i<variable.size(); i++) {
                variable.get(i).setStringToCheck(variable.get(i).getVariableName());
            }
        }

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
    private List<Variable> checkDeadVariableInForStmt (FileToken currentFileToken, FileToken fileTokenToDetect, List<Variable> variable, List<String> forStmt){
        //if location of fileToCheck equals to location of fileToDetect -> send fieldName with Extension (Class.field) -> set fieldName not contains extension  -> (field)
        if (!fileTokenToDetect.getLocation().equals(currentFileToken.getLocation())) {
            for (int i=0; i<variable.size(); i++) {
                String fieldName = variable.get(i).getParent() + "." + variable.get(i).getVariableName();
                variable.get(i).setStringToCheck(fieldName);
            }
        }
        else if (fileTokenToDetect.getLocation().equals(currentFileToken.getLocation())) {
            for (int i=0; i<variable.size(); i++) {
                variable.get(i).setStringToCheck(variable.get(i).getVariableName());
            }
        }

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
    private List<Variable> checkDeadVariableInForeachStmt (FileToken currentFileToken, FileToken fileTokenToDetect, List<Variable> variable, List<String> ForeachStmt){
        //if location of fileToCheck equals to location of fileToDetect -> send fieldName with Extension (Class.field) -> set fieldName not contains extension  -> (field)
        if (!fileTokenToDetect.getLocation().equals(currentFileToken.getLocation())) {
            for (int i=0; i<variable.size(); i++) {
                String fieldName = variable.get(i).getParent() + "." + variable.get(i).getVariableName();
                variable.get(i).setStringToCheck(fieldName);
            }
        }
        else if (fileTokenToDetect.getLocation().equals(currentFileToken.getLocation())) {
            for (int i=0; i<variable.size(); i++) {
                variable.get(i).setStringToCheck(variable.get(i).getVariableName());
            }
        }

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
    private List<Variable> checkDeadVariableInReturnStmt (FileToken currentFileToken, FileToken fileTokenToDetect, List<Variable> variable, List<String> ReturnStmt){
        //if location of fileToCheck equals to location of fileToDetect -> send fieldName with Extension (Class.field) -> set fieldName not contains extension  -> (field)
        if (!fileTokenToDetect.getLocation().equals(currentFileToken.getLocation())) {
            for (int i=0; i<variable.size(); i++) {
                String fieldName = variable.get(i).getParent() + "." + variable.get(i).getVariableName();
                variable.get(i).setStringToCheck(fieldName);
            }
        }
        else if (fileTokenToDetect.getLocation().equals(currentFileToken.getLocation())) {
            for (int i=0; i<variable.size(); i++) {
                variable.get(i).setStringToCheck(variable.get(i).getVariableName());
            }
        }

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
    private List<Variable> checkDeadVariableInWhileStmt (FileToken currentFileToken, FileToken fileTokenToDetect, List<Variable> variable, List<String> whileStmt){
        //if location of fileToCheck equals to location of fileToDetect -> send fieldName with Extension (Class.field) -> set fieldName not contains extension  -> (field)
        if (!fileTokenToDetect.getLocation().equals(currentFileToken.getLocation())) {
            for (int i=0; i<variable.size(); i++) {
                String fieldName = variable.get(i).getParent() + "." + variable.get(i).getVariableName();
                variable.get(i).setStringToCheck(fieldName);
            }
        }
        else if (fileTokenToDetect.getLocation().equals(currentFileToken.getLocation())) {
            for (int i=0; i<variable.size(); i++) {
                variable.get(i).setStringToCheck(variable.get(i).getVariableName());
            }
        }

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
    private List<Variable> checkDeadVariableInDoStmt (FileToken currentFileToken, FileToken fileTokenToDetect, List<Variable> variable, List<String> doStmt){
        //if location of fileToCheck equals to location of fileToDetect -> send fieldName with Extension (Class.field) -> set fieldName not contains extension  -> (field)
        if (!fileTokenToDetect.getLocation().equals(currentFileToken.getLocation())) {
            for (int i=0; i<variable.size(); i++) {
                String fieldName = variable.get(i).getParent() + "." + variable.get(i).getVariableName();
                variable.get(i).setStringToCheck(fieldName);
            }
        }
        else if (fileTokenToDetect.getLocation().equals(currentFileToken.getLocation())) {
            for (int i=0; i<variable.size(); i++) {
                variable.get(i).setStringToCheck(variable.get(i).getVariableName());
            }
        }

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
    private List<Variable> checkDeadVariableInSwitchStmt (FileToken currentFileToken, FileToken fileTokenToDetect, List<Variable> variable, List<String> SwitchStmt){
        //if location of fileToCheck equals to location of fileToDetect -> send fieldName with Extension (Class.field) -> set fieldName not contains extension  -> (field)
        if (!fileTokenToDetect.getLocation().equals(currentFileToken.getLocation())) {
            for (int i=0; i<variable.size(); i++) {
                String fieldName = variable.get(i).getParent() + "." + variable.get(i).getVariableName();
                variable.get(i).setStringToCheck(fieldName);
            }
        }
        else if (fileTokenToDetect.getLocation().equals(currentFileToken.getLocation())) {
            for (int i=0; i<variable.size(); i++) {
                variable.get(i).setStringToCheck(variable.get(i).getVariableName());
            }
        }

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
    private List<Variable> checkDeadVariableInVariableDeclarator (FileToken currentFileToken, FileToken fileTokenToDetect, List<Variable> variable, List<String> variableDeclarator){
        //if location of fileToCheck equals to location of fileToDetect -> send fieldName with Extension (Class.field) -> set fieldName not contains extension  -> (field)
        if (!fileTokenToDetect.getLocation().equals(currentFileToken.getLocation())) {
            for (int i=0; i<variable.size(); i++) {
                String fieldName = variable.get(i).getParent() + "." + variable.get(i).getVariableName();
                variable.get(i).setStringToCheck(fieldName);
            }
        }
        else if (fileTokenToDetect.getLocation().equals(currentFileToken.getLocation())) {
            for (int i=0; i<variable.size(); i++) {
                variable.get(i).setStringToCheck(variable.get(i).getVariableName());
            }
        }

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

    /*//method to print FileToken info
    private void printFileTokenInfo() {
        for (int i = 0; i<this.fileTokenList.size(); i++) {
            System.out.println();
            System.out.println("filename : " + this.fileTokenList.get(i).getFileName());
            System.out.println("location : " + this.fileTokenList.get(i).getLocation());
            System.out.println("variables : " + this.fileTokenList.get(i).getVariableNames());
            System.out.println("methodCalls : " + this.fileTokenList.get(i).getMethodCalls());
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

            System.out.println("================= Method Token Info =================");
            printMethodTokenInfo(this.fileTokenList.get(i));
            System.out.println("==============================================================================================================");
        }
    }

    //method to print MethodToken info
    private void printMethodTokenInfo(FileToken fileToken) {
        for (int j=0; j<fileToken.getMethodTokenList().size(); j++) {
            System.out.println("methodName : " + fileToken.getMethodTokenList().get(j).getMethodName());
            System.out.println("beginLine : " + fileToken.getMethodTokenList().get(j).getBeginLine());
            System.out.println("endLine : " + fileToken.getMethodTokenList().get(j).getEndLine());
            System.out.println("variables : " + fileToken.getMethodTokenList().get(j).getVariableNames());
            System.out.println("methodCalls : " + fileToken.getMethodTokenList().get(j).getMethodCalls());
            System.out.println("assignExpr : " + fileToken.getMethodTokenList().get(j).getAssignExpr());
            System.out.println("objectCreationExpr : " + fileToken.getMethodTokenList().get(j).getObjectCreationExpr());
            System.out.println("ifStmt : " + fileToken.getMethodTokenList().get(j).getIfStmt());
            System.out.println("forStmt : " + fileToken.getMethodTokenList().get(j).getForStmt());
            System.out.println("foreachStmt : " + fileToken.getMethodTokenList().get(j).getForeachStmt());
            System.out.println("returnStmt : " + fileToken.getMethodTokenList().get(j).getReturnStmt());
            System.out.println("whileStmt : " + fileToken.getMethodTokenList().get(j).getWhileStmt());
            System.out.println("doStmt : " + fileToken.getMethodTokenList().get(j).getDoStmt());
            System.out.println("switchStmt : " + fileToken.getMethodTokenList().get(j).getSwitchStmt());
            System.out.println("variableDeclarator : " + fileToken.getMethodTokenList().get(j).getVariableDeclarator());
            System.out.println();
        }
    }*/

        //method to print All dead variable info
        private void printAllDeadVariableInfo () {
            for (int i = 0; i < this.fileTokenList.size(); i++) {
                System.out.println("====================================================================================================================================");

                System.out.println("file : " + this.fileTokenList.get(i).getFileName() + " has " + this.fileTokenList.get(i).getAllDeadVariable().size() + " Dead variables");
                for (int j=0; j<this.fileTokenList.get(i).getAllDeadVariable().size(); j++) {
                    System.out.println("Variable : " + this.fileTokenList.get(i).getAllDeadVariable().get(j).getVariableName() + " \\\\ Line : " + this.fileTokenList.get(i).getAllDeadVariable().get(j).getBeginLine()
                            + " \\\\ location : " + this.fileTokenList.get(i).getLocation() + " \\\\ Type : Dead Variable");
                }

                System.out.println("====================================================================================================================================");
                System.out.println();
            }
        }

        //method to create report specific file name
        public void createReport (String fileName) throws IOException {
            String filename = "DeadVariableDetector" + fileName + "ProjectOutput";
            Output output = new Output();
            output.createFile(filename);
            output.sendInfo(this.fileTokenList);
            output.write();
        }
    }