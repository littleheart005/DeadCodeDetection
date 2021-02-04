import DeadClass.FileToken;
import Util.SwitchStmtCollector;
import com.github.javaparser.JavaParser;
import com.github.javaparser.StaticJavaParser;
import com.github.javaparser.ast.CompilationUnit;
import org.checkerframework.checker.units.qual.A;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class RunTest {
    public static void main(String[] args) throws FileNotFoundException {
        String path = "/Users/Peeradon/Documents/OpenSourceProject/portfolio-master/name.abuchen.portfolio/src/name/abuchen/portfolio/" +
                "snapshot/security/SecurityPerformanceRecord.java";
        CompilationUnit cu = StaticJavaParser.parse(new File(path));

        FileToken fileToken = new FileToken(cu);
        List<String> switchStm = new ArrayList<>();
        switchStm = fileToken.getSwitchStm();

        switchStm.forEach(sw->System.out.println("->"+sw));
    }
}
