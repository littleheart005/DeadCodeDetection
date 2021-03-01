package DeadClassInterface;


import com.github.javaparser.ast.CompilationUnit;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class DeadClassInterfaceDetector {

    private List<FileToken> fileTokenList = new ArrayList<>();
    private List<ClassToken> classTokens = new ArrayList<>();
    private List<InterfaceToken> interfaceTokens = new ArrayList<>();
    private String reportName = "DeadClassInterface";

    public DeadClassInterfaceDetector(List<CompilationUnit> cu){
        setToken(cu);
    }

    private void setToken(List<CompilationUnit> cu){
        for(CompilationUnit cuTmp : cu){
            FileToken fileToken = new FileToken(cuTmp);
            // Setting classes token
            List<String> classes = fileToken.getClassName();
            for(String name : classes){
                ClassToken classToken = new ClassToken(name,
                        fileToken.getFileName(),
                        fileToken.getPackageName(),
                        fileToken.getLocation(),
                        fileToken.getClassLine(name));
                classTokens.add(classToken);
            }
            // Setting interfaces token
            List<String> interfaces = fileToken.getInterfaceName();
            for (String name : interfaces){
                InterfaceToken interfaceToken = new InterfaceToken(name,
                        fileToken.getFileName(),
                        fileToken.getPackageName(),
                        fileToken.getLocation(),
                        fileToken.getInterfaceLine(name));
                interfaceTokens.add(interfaceToken);
            }
            fileTokenList.add(fileToken);
        }
    }

    public void detect(){
        for(FileToken fileToken : fileTokenList){
            // Classes
            for(ClassToken classToken : classTokens){
               if(isNotOwnFile(fileToken.getFileName(),classToken.getName())){
                    // Check if class is already used.
                    if(classToken.getDead().equals(true)){
                        if(checkMatch(fileToken.getExtendedList(),classToken.getName())
                                || checkMatch(fileToken.getVariableType(),classToken.getName())
                                || checkContain(fileToken.getMethodCall(),classToken.getName())
                                || checkContain(fileToken.getMethodType(),classToken.getName())
                                || checkContain(fileToken.getObjectAssignmentType(),classToken.getName())
                                || checkContain(fileToken.getParameterType(),classToken.getName())
                                || checkContain(fileToken.getMethodScope(),classToken.getName())
                                || checkReturn(fileToken.getReturnStm(),classToken.getName())
                                || checkMatch(fileToken.getInstanceOf(),classToken.getName())
                                || checkMethodArg(fileToken.getMethodArgument(),classToken.getName())
                                || checkIfStatement(fileToken.getIfStm(),classToken.getName())
                                || checkForStatement(fileToken.getForStm(),classToken.getName())
                                || checkForEachStatement(fileToken.getForEachStm(),classToken.getName())
                                || checkContain(fileToken.getSwitchStm(),classToken.getName())
                                || checkContain(fileToken.getValueAssign(),classToken.getName())){
                            // If class is in the same package and if not.
                            if(classToken.getPackageName().matches(fileToken.getPackageName())){
                                classToken.setDead(false);
                            }
                            if(!classToken.getPackageName().matches(fileToken.getPackageName())){
                                if(checkImport(fileToken.getImportStm()
                                        ,classToken.getPackageName()
                                        ,classToken.getName()
                                        ,classToken.getFileName())){
                                    classToken.setDead(false);
                                }
                            }
                        }
                   }
                }
            }
            // Interfaces.
            for(InterfaceToken interfaceToken : interfaceTokens){
               if(isNotOwnFile(fileToken.getFileName(),interfaceToken.getName())){
                    // check if interface is already used.
                    if(interfaceToken.getDead().equals(true)){
                        if(checkMatch(fileToken.getImplementList(),interfaceToken.getName())
                                || checkMatch(fileToken.getExtendedList(),interfaceToken.getName())
                                || checkMatch(fileToken.getVariableType(),interfaceToken.getName())
                                || checkContain(fileToken.getMethodCall(),interfaceToken.getName())
                                || checkContain(fileToken.getMethodType(),interfaceToken.getName())
                                || checkContain(fileToken.getObjectAssignmentType(),interfaceToken.getName())
                                || checkContain(fileToken.getParameterType(),interfaceToken.getName())
                                || checkContain(fileToken.getMethodScope(),interfaceToken.getName())
                                || checkMatch(fileToken.getInstanceOf(),interfaceToken.getName())
                                || checkReturn(fileToken.getReturnStm(),interfaceToken.getName())
                                || checkMethodArg(fileToken.getMethodArgument(),interfaceToken.getName())
                                || checkIfStatement(fileToken.getIfStm(),interfaceToken.getName())
                                || checkForStatement(fileToken.getForStm(),interfaceToken.getName())
                                || checkForEachStatement(fileToken.getForEachStm(),interfaceToken.getName())
                                || checkContain(fileToken.getSwitchStm(),interfaceToken.getName())
                                || checkContain(fileToken.getValueAssign(),interfaceToken.getName())) {
                            // If interface is in the same package and if not.
                            if(fileToken.getPackageName().equals(interfaceToken.getPackageName())){
                                interfaceToken.setDead(false);
                            }
                            if(!fileToken.getPackageName().equals(interfaceToken.getPackageName())){
                                if (checkImport(fileToken.getImportStm()
                                        ,interfaceToken.getPackageName()
                                        ,interfaceToken.getName()
                                        ,interfaceToken.getFileName())){
                                    interfaceToken.setDead(false);
                                }
                            }
                        }
                    }
               }
            }
        }
    }

    private boolean checkMatch(List<String> list, String name){
        // Pattern1 : starting with class/interface Name
        String pat1 = "^"+name;
        Pattern pattern1 = Pattern.compile(pat1);

        for (String stm : list){
            Matcher matcher1 = pattern1.matcher(stm);
            if(matcher1.find()){
                return true;
            }
        }
        return false;
    }

    private boolean checkContain(List<String> list, String name){
            Pattern pattern = Pattern.compile(name);
            for (String tmp : list){
                Matcher matcher = pattern.matcher(tmp);
                if (matcher.find()){
                    return true;
                }
            }
        return false;
    }

    private boolean checkReturn(List<String> returnList, String name){
        String returnPattern = ".*"+name+"[/(].*";
        Pattern pattern = Pattern.compile(returnPattern);
        for (String stm : returnList){
            Matcher matcher = pattern.matcher(stm);
            if (matcher.find()){
                return true;
            }
        }
        return false;
    }

    private boolean checkMethodArg(List<String> methodArg, String name){
        String methodArgPattgern = name+"[.]";
        Pattern pattern = Pattern.compile(methodArgPattgern);
        for (String stm : methodArg){
            Matcher matcher = pattern.matcher(stm);
            if (matcher.find()){
                return true;
            }
        }
        return false;
    }

    private boolean checkIfStatement(List<String> ifStm, String name){
        Pattern pattern = Pattern.compile(name);
        for(String stm : ifStm){
            Matcher matcher = pattern.matcher(stm);
            if (matcher.find()){
                return true;
            }
        }
        return false;
    }

    private boolean checkForStatement(List<String> forStm, String name){
        Pattern pattern = Pattern.compile(name);
        for(String stm : forStm){
            Matcher matcher = pattern.matcher(stm);
            if (matcher.find()){
                return true;
            }
        }
        return false;
    }

    private boolean checkForEachStatement(List<String> forEachStm, String name){
        Pattern pattern = Pattern.compile(name);
        for(String stm : forEachStm){
            Matcher matcher = pattern.matcher(stm);
            if (matcher.find()){
                return true;
            }
        }
        return false;
    }

    private boolean checkImport(List<String> importStm, String classPackage,
                                String className, String fileName){

        // remove .java from file name.
        String tmpFileName = fileName.replace(".java","");

        String packageWithName = classPackage+"\\."+className+"$";
        String packageAll = classPackage+"$";
        String packageWithFileName = classPackage+"\\."+tmpFileName+"$";

        Pattern pattern = Pattern.compile(packageWithName);
        Pattern pattern1 = Pattern.compile(packageAll);
        Pattern pattern2 = Pattern.compile(packageWithFileName);

        for (String stm : importStm){
            Matcher matcher = pattern.matcher(stm);
            Matcher matcher1 = pattern1.matcher(stm);
            Matcher matcher2 = pattern2.matcher(stm);
            if(matcher.find() || matcher1.find() || matcher2.find()) {
                return true;
            }
        }
        return false;
    }

    private boolean isNotOwnFile(String fileName, String className){
        String classFileName = className+".java";
        if(fileName.matches(classFileName)){
            return false;
        }
        return true;
    }

    public void createReport(float elapseTime){
        FileWriter f;
        BufferedWriter bw;
        try{
            f = new FileWriter("src/main/resources/DeadClassInterface/"+reportName+".txt");
            bw = new BufferedWriter(f);
            bw.write("Total detect time: "+elapseTime+" seconds.\n");
            for (ClassToken classToken : classTokens){
                if(classToken.getDead().equals(true)) {
                    String tmp = "Class\t"+classToken.getName()+"\tat line: "+classToken.getLine()+
                            "\tin: "+classToken.getPackageName()+"."+classToken.getFileName()+"\n";
                    bw.write(tmp);
                }
            }
            for (InterfaceToken interfaceToken : interfaceTokens){
                if(interfaceToken.getDead().equals(true)) {
                    String tmp = "Interface\t"+interfaceToken.getName()+"\tat line: "+interfaceToken.getLine()+
                            "\tin: "+interfaceToken.getPackageName()+"."+interfaceToken.getFileName()+"\n";
                    bw.write(tmp);
                }
            }
            System.out.println("Report Created");
            bw.close();
        }catch (IOException e){
            System.out.println("Error in ASTDetector createReport");
            e.printStackTrace();
        }
    }

    public void setReportName(String reportName) {
        this.reportName = reportName;
    }

    public List<ClassToken> getClassTokens() {
        return classTokens;
    }

    public List<InterfaceToken> getInterfaceTokens() {
        return interfaceTokens;
    }
}
