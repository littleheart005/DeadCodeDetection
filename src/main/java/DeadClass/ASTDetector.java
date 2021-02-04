package DeadClass;


import com.github.javaparser.ast.CompilationUnit;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ASTDetector {

    List<FileToken> fileTokenList = new ArrayList<>();
    List<ClassToken> classTokens = new ArrayList<>();
    List<InterfaceToken> interfaceTokens = new ArrayList<>();
    private String reportLocation = "/Users/Peeradon/Desktop/Detecting Result/Dead_class_interface.txt";

    public ASTDetector(List<CompilationUnit> cu){
        setToken(cu);
    }

    private void setToken(List<CompilationUnit> cu){
        for(CompilationUnit cuTmp : cu){
            FileToken fileToken = new FileToken(cuTmp);
            // Setting classes token
            List<String> classes = fileToken.getClassName();
            for(String name : classes){
                ClassToken classToken = new ClassToken(name,
                        fileToken.getPackageName(),
                        fileToken.getLocation(),
                        fileToken.getClassLine(name));
                classTokens.add(classToken);
            }
            // Setting interfaces token
            List<String> interfaces = fileToken.getInterfaceName();
            for (String name : interfaces){
                InterfaceToken interfaceToken = new InterfaceToken(name,
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
//                if(isNotOwnFile(fileToken.getFileName(),classToken.getName())){
                    // Check if class is already used.
                    if(classToken.getDead().equals(true)){
                        if(checkContain(fileToken.getExtendedList(),classToken.getName())
//                                || fileToken.getExtendedList().contains(classToken.getName())
//                                || fileToken.getVariableType().contains(classToken.getName())
//                                || fileToken.getMethodType().contains(classToken.getName())
//                                || fileToken.getObjectAssignmentType().contains(classToken.getName())
//                                || fileToken.getParameterType().contains(classToken.getName())
//                                || fileToken.getMethodScope().contains(classToken.getName())
                                || checkContain(fileToken.getVariableType(),classToken.getName())
                                || checkContain(fileToken.getMethodType(),classToken.getName())
                                || checkContain(fileToken.getObjectAssignmentType(),classToken.getName())
                                || checkContain(fileToken.getParameterType(),classToken.getName())
                                || checkContain(fileToken.getMethodScope(),classToken.getName())
                                || checkReturn(fileToken.getReturnStm(),classToken.getName())
                                || checkMethodArg(fileToken.getMethodArgument(),classToken.getName())
                                || checkIfStatement(fileToken.getIfStm(),classToken.getName())
                                || checkForStatement(fileToken.getForStm(),classToken.getName())
                                || checkForEachStatement(fileToken.getForEachStm(),classToken.getName())
                                || checkContain(fileToken.getSwitchStm(),classToken.getName())){
                            // If class is in the same package and if not.
                            if(fileToken.getPackageName().equals(classToken.getPackageName())){
                                classToken.setDead(false);
                            }
                            if(!fileToken.getPackageName().equals(classToken.getPackageName())){
                                if(checkImport(fileToken.getImportStm(), classToken.getPackageName())){
                                    classToken.setDead(false);
                                }
                            }
                        }
//                    }
                }
            }
            // Interfaces.
            for(InterfaceToken interfaceToken : interfaceTokens){
//                if(isNotOwnFile(fileToken.getFileName(),interfaceToken.getName())){
                    // check if interface is already used.
                    if(interfaceToken.getDead().equals(true)){
                        if(checkContain(fileToken.getImplementList(),interfaceToken.getName())
//                                || fileToken.getImplementList().contains(interfaceToken.getName())
//                                || fileToken.getVariableType().contains(interfaceToken.getName())
//                                || fileToken.getMethodType().contains(interfaceToken.getName())
//                                || fileToken.getObjectAssignmentType().contains(interfaceToken.getName())
//                                || fileToken.getParameterType().contains(interfaceToken.getName())
//                                || fileToken.getMethodScope().contains(interfaceToken.getName())
                                || checkContain(fileToken.getVariableType(),interfaceToken.getName())
                                || checkContain(fileToken.getMethodType(),interfaceToken.getName())
                                || checkContain(fileToken.getObjectAssignmentType(),interfaceToken.getName())
                                || checkContain(fileToken.getParameterType(),interfaceToken.getName())
                                || checkContain(fileToken.getMethodScope(),interfaceToken.getName())
                                || checkReturn(fileToken.getReturnStm(),interfaceToken.getName())
                                || checkMethodArg(fileToken.getMethodArgument(),interfaceToken.getName())
                                || checkIfStatement(fileToken.getIfStm(),interfaceToken.getName())
                                || checkForStatement(fileToken.getForStm(),interfaceToken.getName())
                                || checkForEachStatement(fileToken.getForEachStm(),interfaceToken.getName())
                                || checkContain(fileToken.getSwitchStm(),interfaceToken.getName())) {
                            // If interface is in the same package and if not.
                            if(fileToken.getPackageName().equals(interfaceToken.getPackageName())){
                                interfaceToken.setDead(false);
                            }
                            if(!fileToken.getPackageName().equals(interfaceToken.getPackageName())){
                                if (checkImport(fileToken.getImportStm(), interfaceToken.getPackageName())){
                                    interfaceToken.setDead(false);
                                }
                            }
                        }
                    }
             //   }
            }
        }
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

    private boolean checkImport(List<String> importStm, String classPackage){
        Pattern pattern = Pattern.compile(classPackage);
        for (String stm : importStm){
            Matcher matcher = pattern.matcher(stm);
            if(matcher.find()) {
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

    public void printClassAndInterface(){
        System.out.println("Total class: "+classTokens.size());
        System.out.println("Total interface: "+interfaceTokens.size());
        System.out.println("Total File: "+fileTokenList.size()+"\n============= Dead classes ==========");
        int count = 0;
        for (ClassToken classToken : classTokens){
            if(classToken.getDead().equals(true)){
                System.out.println("Class: "+classToken.getName()+" at: "
                        +classToken.getPath()+" line: "+classToken.getLine());
                count++;
            }
        }
        System.out.println("Total DeadClass: "+count);
        count=0;
        System.out.println("================== Dead interface ==================");
        for (InterfaceToken interfaceToken : interfaceTokens){
            if(interfaceToken.getDead().equals(true)){
                System.out.println("Interface: "+interfaceToken.getName()+" at: "
                        +interfaceToken.getPath()+" line: "+interfaceToken.getLine());
                count++;
            }
        }
        System.out.println("Total DeadInterface: "+count);
    }

    public void createReport(float elapseTime){
        FileWriter f;
        BufferedWriter bw;
        try{
            f = new FileWriter(this.reportLocation);
            bw = new BufferedWriter(f);
            System.out.println("Total detect time: "+elapseTime+" seconds.");
            for (ClassToken classToken : classTokens){
                if(classToken.getDead().equals(true)) {
                    String tmp = "Class\t"+classToken.getName()+"\tat line: "+classToken.getLine()+
                            "\tin: "+classToken.getPackageName()+"\n";
                    bw.write(tmp);
                }
            }
            for (InterfaceToken interfaceToken : interfaceTokens){
                if(interfaceToken.getDead().equals(true)) {
                    String tmp = "Interface\t"+interfaceToken.getName()+"\tat line: "+interfaceToken.getLine()+
                            "\tin: "+interfaceToken.getPackageName()+"\n";
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

    public void setReportLocation(String reportLocation) {
        this.reportLocation = reportLocation;
    }
}
