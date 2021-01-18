import DeadClass.DeadClassWithAST;
import DeadInterface.DeadInterfaceDetector;

public class Main {

    public static void main(String[] args){

        String source = "/Users/Peeradon/Documents/DesignPatternCode/HF_DP/src/headfirst/strategy";

        System.out.println("============== Dead Class =============");
        DeadClassWithAST deadClassWithAST= new DeadClassWithAST(source);
        //deadClassWithAST.printMap();
        deadClassWithAST.detect();

//        System.out.println("============== Dead Interface =============");
//        DeadInterfaceDetector deadInterfaceDetector = new DeadInterfaceDetector(source);
//        deadInterfaceDetector.printMap();



    }

}