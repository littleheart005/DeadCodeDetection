
import DeadClass.DeadClassWithAST;
import DeadInterface.DeadInterfaceDetector;
import Util.*;


public class Main {

    public static void main(String[] args) {

        String source = "/Users/Peeradon/Documents/DesignPatternCode/HF_DP/src/headfirst/strategy";
        ASTParser astParser = new ASTParser(source);

        System.out.println("============== Dead Class =============");
        DeadClassWithAST deadClassWithAST= new DeadClassWithAST(astParser.cu);
        deadClassWithAST.printMap();
        //deadClassWithAST.detect();

        System.out.println("============== Dead Interface =============");
        DeadInterfaceDetector deadInterfaceDetector = new DeadInterfaceDetector(astParser.cu);
        deadInterfaceDetector.printMap();



    }

}