
import DeadClass.DeadClassWithAST;
import DeadInterface.DeadInterfaceDetector;
import Files_Reader.File_Reader;
import Util.ASTParser;


public class Main {

    public static void main(String[] args) {

        String source = "C:\\Users\\birdn\\Desktop\\Project\\headfirst\\HF_DP\\src\\headfirst\\combining";

        System.out.println("============== Dead Class =============");
        DeadClassWithAST deadClassWithAST= new DeadClassWithAST(source);
        //deadClassWithAST.printMap();
        deadClassWithAST.detect();



//        System.out.println("============== Dead Interface =============");
//        DeadInterfaceDetector deadInterfaceDetector = new DeadInterfaceDetector(source);
//        deadInterfaceDetector.printMap();



    }

}