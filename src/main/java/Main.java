import DeadVariable.DeadVariableDetector;
import TokenGenerator.ConstructFileToken;
import Util.ASTParser;

import java.io.IOException;

public class Main {

    public static void main(String[] args) throws IOException {

        /*String source = "/Users/Peeradon/Documents/DesignPatternCode/HF_DP/src/TestElapse/combined";

        // ===================== Dead Class & Dead Interface =======================
        long start = System.currentTimeMillis();

        ASTParser astParser = new ASTParser(source);

        DeadClassWithAST deadClassWithAST= new DeadClassWithAST(astParser.cu);
        deadClassWithAST.createReport();

        DeadInterfaceDetector deadInterfaceDetector = new DeadInterfaceDetector(astParser.cu);
        deadInterfaceDetector.createReport();

        long end = System.currentTimeMillis();

        float AstTime = (end - start)/1000F;

        // Dead Class Detector with regular expression and line splitting. (Commended all dead interface portion)
        start = System.currentTimeMillis();

        Detector detector = new Detector(source);
        detector.detect();

        end = System.currentTimeMillis();
        float SplitTime = (end - start)/1000F;

        detector.createReport("Sample");

        //System.out.println("\n\nDead class and interface with AST. Total elapse time: "+AstTime+" seconds");
        System.out.println("Dead Class and interface with regex line splitting: Total elapse time: "+SplitTime+" seconds");*/

        /* test project */
//        String source = "D:\\Project\\Test Project\\DecoratorPattern";
//        String source = "D:\\Project\\Test Project\\FactoryPattern";
//        String source = "D:\\Project\\Test Project\\MementoPattern\\src";
//        String source = "D:\\Project\\Test Project\\ObserverPattern";
//        String source = "D:\\Project\\Test Project\\StrategyPattern";

        /* Open Source */
        String source = "D:\\Project\\openSource\\Arduino-1.8.13\\arduino-core\\src";
//        String source = "D:\\Project\\openSource\\iceberg-apache-iceberg-0.11.0\\core\\src\\main";
//        String source = "D:\\Project\\openSource\\portfolio-master\\name.abuchen.portfolio\\src";
//        String source = "D:\\Project\\openSource\\Signal-Android-5.3.10\\libsignal\\service\\src\\main";
//        String source = "D:\\Project\\openSource\\bitcoin-wallet-5.45\\wallet\\src\\de\\schildbach\\wallet";
//        String source = "D:\\Project\\openSource\\openapi-generator-5.0.1\\modules\\openapi-generator-core\\src\\main";
//        String source = "D:\\Project\\openSource\\OpenRefine-3.4.1\\main\\src";
//        String source = "D:\\Project\\openSource\\spring-framework-5.3.3\\spring-core\\src\\main";

        long start = System.currentTimeMillis();

        ASTParser astParser = new ASTParser(source);
        ConstructFileToken constructFileToken = new ConstructFileToken(astParser.cu);
        DeadVariableDetector deadVariableDetector = new DeadVariableDetector(constructFileToken.getFileTokenList());

        long end = System.currentTimeMillis();
        float AstTime = (end - start)/1000F;

        deadVariableDetector.createReport("Arduino");

        System.out.println("Dead Variable with AST. Total elapse time: " + AstTime + " seconds");
    }
}