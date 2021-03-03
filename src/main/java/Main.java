import DeadVariable.ConstructFileToken;
import DeadVariable.DeadVariableDetector;
import DeadVariable.Printer;
import Parser.ASTParser;
import java.io.IOException;

public class Main {

    public static void main(String[] args) throws IOException {

        //Arduino
        String openSource = "D:\\Project\\openSource\\Arduino-1.8.13\\arduino-core\\src";

        //Bitcoin
//        String openSource = "D:\\Project\\openSource\\bitcoin-wallet-5.45\\wallet\\src";

        //Iceberg
//        String openSource = "D:\\Project\\openSource\\iceberg-apache-iceberg-0.11.0\\core\\src\\main\\java\\org\\apache\\iceberg";

        //OpenApi
//        String openSource = "D:\\Project\\openSource\\openapi-generator-5.0.1\\modules\\openapi-generator-core\\src\\main\\java";

        //OpenRefine
//        String openSource = "D:\\Project\\openSource\\OpenRefine-3.4.1\\main\\src";

        //portfolio
//        String openSource = "D:\\Project\\openSource\\portfolio-master\\name.abuchen.portfolio\\src\\name\\abuchen\\portfolio";

        //Signal-android
//        String openSource = "D:\\Project\\openSource\\Signal-Android-5.3.10\\libsignal\\service\\src\\main";

        //spring-framework
//        String openSource = "D:\\Project\\openSource\\spring-framework-5.3.3\\spring-core\\src\\main\\java";

        //Test Project
//        String openSource = "D:\\Project\\Test Project\\MementoPattern\\src";
        
        // Dead variable
        long start = System.currentTimeMillis();

        ASTParser astParser = new ASTParser(openSource);
        ConstructFileToken constructFileToken = new ConstructFileToken(astParser.cu);
        Printer printer = new Printer();
        DeadVariableDetector deadVariableDetector = new DeadVariableDetector(constructFileToken.getFileTokenList());
        printer.printDetectInFo(constructFileToken.getFileTokenList());

        long end = System.currentTimeMillis();
        float AstTime = (end - start)/1000F;

//        deadVariableDetector.createReport("");

        System.out.println("Dead Variable with AST. Total elapse time: " + AstTime + " seconds");


    }
}