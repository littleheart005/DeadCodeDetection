
import DeadClass.DeadClassWithAST;
import DeadInterface.DeadInterfaceDetector;
import Files_Reader.File_Reader;
import Util.ASTParser;
import DeadClass.Detector;
import Files_Reader.File_Reader;
import Util.*;
import org.checkerframework.checker.units.qual.A;

import java.util.ArrayList;
import java.util.List;


public class Main {

    public static void main(String[] args) {

        long start = System.currentTimeMillis();
        String source = "C:\\Users\\birdn\\Desktop\\Project\\headfirst\\HF_DP\\src\\headfirst\\combining\\observer";

        ASTParser astParser = new ASTParser(source);
        System.out.println("============== Dead Class =============");
        DeadClassWithAST deadClassWithAST= new DeadClassWithAST(astParser.cu);
        //deadClassWithAST.printMap();

        long end = System.currentTimeMillis();
        float elapseTimeInSecond = (end - start)/1000F;
        System.out.println("\nDeadClass AST Elapse Time: "+elapseTimeInSecond+" seconds");



         //Dead Class Detector with regular expression and line splitting. (Commended all dead interface portion)
        start = System.currentTimeMillis();
        Detector detector = new Detector(source);
        detector.detect();

        end = System.currentTimeMillis();
        elapseTimeInSecond = (end - start)/1000F;
        System.out.println("\nDeadClass split line Elapse Time: "+elapseTimeInSecond+" seconds");





//        System.out.println("============== Dead Interface =============");
//        DeadInterfaceDetector deadInterfaceDetector = new DeadInterfaceDetector(astParser.cu);
//        deadInterfaceDetector.printMap();



    }

}