package DeadVariable;

import java.util.List;

public class Printer {

    public void printFileTokenInfo(List<FileToken> fileTokenList) {
        System.out.println("file count : " + fileTokenList.size() + "\n");

        for (int i=0; i<fileTokenList.size(); i++) {
            System.out.println("Filename : " + fileTokenList.get(i).getFileName());
            System.out.println("Location : " + fileTokenList.get(i).getLocation());
            System.out.println("Package : " + fileTokenList.get(i).getPackageName());

            /*System.out.print("extendsClass : [");
            if (fileTokenList.get(i).getExtendsClass().size() != 0) {
                for (int j=0; j<fileTokenList.get(i).getExtendsClass().size(); j++) {
                    System.out.print(fileTokenList.get(i).getExtendsClass().get(j).getFileName() + ", ");
                }
            }
            System.out.println("]");

            System.out.print("importClass : [");
            if (fileTokenList.get(i).getImportClass().size() != 0) {
                for (int j=0; j<fileTokenList.get(i).getImportClass().size(); j++) {
                    System.out.print(fileTokenList.get(i).getImportClass().get(j).getFileName() + ", ");
                }
            }
            System.out.println("]");*/

            System.out.print("ChildClassToDetect : [");
            if (fileTokenList.get(i).getChildClassToDetect().size() != 0) {
                for (int j=0; j<fileTokenList.get(i).getChildClassToDetect().size(); j++) {
                    System.out.print(fileTokenList.get(i).getChildClassToDetect().get(j).getFileName() + ", ");
                }
            }
            System.out.println("]");

            System.out.print("ClassThatImportToDetect : [");
            if (fileTokenList.get(i).getClassThatImportToDetect().size() != 0) {
                for (int j=0; j<fileTokenList.get(i).getClassThatImportToDetect().size(); j++) {
                    System.out.print(fileTokenList.get(i).getClassThatImportToDetect().get(j).getFileName() + ", ");
                }
            }
            System.out.println("]");

            System.out.print("AllClassInSamePackageToDetect : [");
            if (fileTokenList.get(i).getFileInSamePackageToDetect().size() != 0) {
                for (int j=0; j<fileTokenList.get(i).getFileInSamePackageToDetect().size(); j++) {
                    System.out.print(fileTokenList.get(i).getFileInSamePackageToDetect().get(j).getFileName() + ", ");
                }
            }
            System.out.println("]");

            System.out.println();
            System.out.println("========== File Token ==========");

            System.out.print("static field : [");
            if (fileTokenList.get(i).getStaticField().size() != 0) {
                for (Variable var : fileTokenList.get(i).getStaticField()) {
                    System.out.print(var.getParent() + "." + var.getVariableName() + ", ");
                }
            }
            System.out.println("]");

            System.out.print("field : [");
            if (fileTokenList.get(i).getField().size() != 0) {
                for (Variable var : fileTokenList.get(i).getField()) {
                    System.out.print(var.getVariableName() + ", ");
                }
            }
            System.out.println("]");

            System.out.println("methodCall : " + fileTokenList.get(i).getMethodCalls());
            System.out.println("assignExpr : " + fileTokenList.get(i).getAssignExpr());
            System.out.println("objectCreationExpr : " + fileTokenList.get(i).getObjectCreationExpr());
            System.out.println("ifStmt : " + fileTokenList.get(i).getIfStmt());
            System.out.println("forStmt : " + fileTokenList.get(i).getForStmt());
            System.out.println("foreachStmt : " + fileTokenList.get(i).getForeachStmt());
            System.out.println("returnStmt : " + fileTokenList.get(i).getReturnStmt());
            System.out.println("whileStmt : " + fileTokenList.get(i).getWhileStmt());
            System.out.println("doStmt : " + fileTokenList.get(i).getDoStmt());
            System.out.println("switchStmt : " + fileTokenList.get(i).getSwitchStmt());
            System.out.println("variableDeclarator : " + fileTokenList.get(i).getVariableDeclarator());
            System.out.println();

            System.out.println("========== Method Token ==========");
            for (int j = 0; j<fileTokenList.get(i).getMethodTokenList().size(); j++) {
                System.out.println("method name : " + fileTokenList.get(i).getMethodTokenList().get(j).getMethodName());
                System.out.println("begin line : " + fileTokenList.get(i).getMethodTokenList().get(j).getBeginLine());
                System.out.println("end line : " + fileTokenList.get(i).getMethodTokenList().get(j).getEndLine());

                System.out.print("variable : [");
                if (fileTokenList.get(i).getMethodTokenList().get(j).getVariable().size() != 0) {
                    for (Variable var : fileTokenList.get(i).getMethodTokenList().get(j).getVariable()) {
                        System.out.print(var.getVariableName() + ", ");
                    }
                }
                System.out.println("]");

                System.out.println("methodCall : " + fileTokenList.get(i).getMethodTokenList().get(j).getMethodCalls());
                System.out.println("assignExpr : " + fileTokenList.get(i).getMethodTokenList().get(j).getAssignExpr());
                System.out.println("objectCreationExpr : " + fileTokenList.get(i).getMethodTokenList().get(j).getObjectCreationExpr());
                System.out.println("ifStmt : " + fileTokenList.get(i).getMethodTokenList().get(j).getIfStmt());
                System.out.println("forStmt : " + fileTokenList.get(i).getMethodTokenList().get(j).getForStmt());
                System.out.println("foreachStmt : " + fileTokenList.get(i).getMethodTokenList().get(j).getForeachStmt());
                System.out.println("returnStmt : " + fileTokenList.get(i).getMethodTokenList().get(j).getReturnStmt());
                System.out.println("whileStmt : " + fileTokenList.get(i).getMethodTokenList().get(j).getWhileStmt());
                System.out.println("doStmt : " + fileTokenList.get(i).getMethodTokenList().get(j).getDoStmt());
                System.out.println("switchStmt : " + fileTokenList.get(i).getMethodTokenList().get(j).getSwitchStmt());
                System.out.println("variableDeclarator : " + fileTokenList.get(i).getMethodTokenList().get(j).getVariableDeclarator());
                System.out.println();
            }
        }
    }

    public void printDetectInFo(List<FileToken> fileTokenList) {
        System.out.println("=============================== Detection Info ===============================");
        for (int i=0; i<fileTokenList.size(); i++) {
            if (!fileTokenList.get(i).getDeadStaticField().isEmpty()
                    || !fileTokenList.get(i).getDeadField().isEmpty()
                    || !fileTokenList.get(i).getDeadVariable().isEmpty())
            {
                System.out.println("Filename : " + fileTokenList.get(i).getFileName());
                System.out.println("Location : " + fileTokenList.get(i).getLocation());

                if (!fileTokenList.get(i).getDeadStaticField().isEmpty()) {
                    System.out.print("DeadStaticField : [");
                    for (int j=0; j<fileTokenList.get(i).getDeadStaticField().size(); j++) {
                        System.out.print(fileTokenList.get(i).getDeadStaticField().get(j).getVariableName() + ", ");
                    }
                    System.out.println("]");
                    System.out.println();
                }

                if (!fileTokenList.get(i).getDeadField().isEmpty()) {
                    System.out.print("DeadField : [");
                    for (int j=0; j<fileTokenList.get(i).getDeadField().size(); j++) {
                        System.out.print(fileTokenList.get(i).getDeadField().get(j).getVariableName() + ", ");
                    }
                    System.out.println("]");
                    System.out.println();
                }

                if (!fileTokenList.get(i).getDeadVariable().isEmpty()) {
                    System.out.print("DeadVariable : [");
                    for (int j=0; j<fileTokenList.get(i).getDeadVariable().size(); j++) {
                        System.out.print(fileTokenList.get(i).getDeadVariable().get(j).getVariableName() + ", ");
                    }
                    System.out.println("]");
                    System.out.println();
                }
            }
        }
    }

}
