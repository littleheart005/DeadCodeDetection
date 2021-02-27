package Parser;

import Files_Reader.File_Reader;
import com.github.javaparser.StaticJavaParser;
import com.github.javaparser.ast.CompilationUnit;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

//class to read source as a project and parse to AST -> store at List<CompilationUnit> cu
public class ASTParser {
    protected static List<String> FILES_PATH = new ArrayList<>();
    public List<CompilationUnit> cu = new ArrayList<>();
    public static List<String> location = new ArrayList<>();

    public ASTParser(String source) {
        File_Reader file_reader = new File_Reader();
        FILES_PATH = file_reader.readPath(source); //read all file in source project
        parseAST();
    }

    //parse file to AST
    protected void parseAST() {
        try {
            for (String path : FILES_PATH){
                CompilationUnit cuTmp = StaticJavaParser.parse(new File(path));
                cu.add(cuTmp);
                location.add(path);
            }
        }catch (Exception e) {
            System.out.println("Error in Util.ASTParser");
            e.printStackTrace();
        }
    }


}
