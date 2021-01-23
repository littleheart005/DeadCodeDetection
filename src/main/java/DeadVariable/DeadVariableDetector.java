package DeadVariable;

import java.util.ArrayList;
import java.util.List;

public class DeadVariableDetector {
    List<Component> componentList = new ArrayList<>();

    public DeadVariableDetector(List<Component> componentList) {
        this.componentList = componentList;

        printComponentInfo();
    }

    //method to print component info
    private void printComponentInfo() {
        for (int i = 0; i<this.componentList.size(); i++) {
            System.out.println("filename : " + this.componentList.get(i).getFileName());
            System.out.println("variables : " + this.componentList.get(i).getVariableNames());
            System.out.println("methodCalls : " + this.componentList.get(i).getMethodCalls());
            System.out.println("assignExpr : " + this.componentList.get(i).getAssignExpr());
            System.out.println("objectCreationExpr : " + this.componentList.get(i).getObjectCreationExpr());
            System.out.println("ifStmt : " + this.componentList.get(i).getIfStmt());
            System.out.println("forStmt : " + this.componentList.get(i).getForStmt());
            System.out.println("foreachStmt : " + this.componentList.get(i).getForeachStmt());
            System.out.println("returnStmt : " + this.componentList.get(i).getReturnStmt());
            System.out.println("whileStmt : " + this.componentList.get(i).getWhileStmt());
            System.out.println("doStmt : " + this.componentList.get(i).getDoStmt());
            System.out.println("switchStmt : " + this.componentList.get(i).getSwitchStmt());
            System.out.println("variableDeclarator : " + this.componentList.get(i).getVariableDeclarator());
            System.out.println();
        }
    }
}
