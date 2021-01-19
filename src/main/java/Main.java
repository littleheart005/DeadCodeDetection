
import DeadClass.DeadClassWithAST;
import Util.*;


public class Main {

    public static void main(String[] args) {

        String source = "/Users/Peeradon/Documents/DesignPatternCode/HF_DP/src/headfirst/factory/pizzafm";
        ASTParser astParser = new ASTParser(source);

        System.out.println("============== Dead Class =============");
        DeadClassWithAST deadClassWithAST= new DeadClassWithAST(astParser.cu);
        //deadClassWithAST.printMap();
        deadClassWithAST.prepareData();

//        System.out.println("============== Dead Interface =============");
//        DeadInterfaceDetector deadInterfaceDetector = new DeadInterfaceDetector(astParser.cu);
//        deadInterfaceDetector.printMap();



    }

}