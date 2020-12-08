package DeadVariable;

import Files_Reader.File_Reader;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.*;

public class DeadVariableDetector {
    private List<String> paths;
    private List<File> files;
    private List<String> className = new ArrayList<>();
    private Map<String, Map<Integer, String>> lineOfFile = new HashMap<>();
    private List<String> variableInMethod = new ArrayList<>();
    private List<String> aliveVariableInMethod = new ArrayList<>();
    private List<String> deadVariableInMethod = new ArrayList<>();
    private List<String> variableInFile = new ArrayList<>();
    private List<String> aliveVariableInFile = new ArrayList<>();
    private List<String> deadVariableInFile = new ArrayList<>();
    //All result to show
    private List<String> AllVariableInMethod = new ArrayList<>();
    private List<String> AllAliveVariableInMethod = new ArrayList<>();
    private List<String> AllDeadVariableInMethod = new ArrayList<>();
    private List<String> AllVariableInFile = new ArrayList<>();
    private List<String> AllAliveVariableInFile = new ArrayList<>();
    private List<String> AllDeadVariableInFile = new ArrayList<>();

    public DeadVariableDetector(String source) throws FileNotFoundException {
        //import File_Reader from Files_Reader File_Reader
        File_Reader reader = new File_Reader();
        paths = reader.readPath(source);
        files = reader.readFile(paths);

        //remove main.java from files (to remove file that has main function)
        for (int i=0 ; i<files.size(); i++){
            boolean fileThatHasMainFunc = scanFileToFindMainFunc(files.get(i));

            //if true -> remove that file
            if (fileThatHasMainFunc){
                System.out.println("remove " + files.get(i).getName());
                files.remove(i);
            }
        }

        //-------------------------------------------------(---String--, Map(Integer, String)----)
        //to scan each file save to lineOfFile variable -> ("className",       1,   public class ...)
        for (int i=0 ; i<files.size(); i++){
            className.add(removeExtension(files.get(i).getName()));
            Map<Integer, String> line = scanFile(files.get(i));
            lineOfFile.put(className.get(i), line);
        }


        File fileAdd = null;

        for (int numOfFile=0; numOfFile<className.size(); numOfFile++){
            fileAdd = files.get(numOfFile);

            System.out.println();
            System.out.println("========================================================");
            System.out.println();
            System.out.println("file : " + className.get(numOfFile));
            System.out.println();

            Map<Integer, String> line = lineOfFile.get(className.get(numOfFile));
            String className = this.className.get(numOfFile);

            List<Component> components = scanForConstructorAndMethodWithParameters(line, className);

            //section to cut component from lineOfFile -> constructor, method
            for (int i=0; i<components.size(); i++){
                lineOfFile = removeLineInlineOfFile(components.get(i), lineOfFile, numOfFile);
            }

            //section to search variable in component
            System.out.println();
            for (int i=0; i<components.size(); i++){
                components.get(i).setVariableInMethod(searchVariableInMethod(components.get(i)));
                System.out.println();
            }

            for (int i=0; i<components.size(); i++){
                for (int j=0; j<components.get(i).getVariableInMethod().size(); j++){
                    if (components.get(i).getVariableInMethod().size() != 0){
                        variableInMethod.add(components.get(i).getVariableInMethod().get(j));
                    }
                }
            }

            //section to search dead variable in method
            for (int i=0; i<components.size(); i++){
                searchDeadVariableInMethod(components.get(i));
            }

            //section to search variable in file (not include variable in method)
            searchVariableInfile(lineOfFile, this.className.get(numOfFile));

            //section to search dead variable in file -> send component loop to searchDeadVariable in file method
            for (int i=0; i<components.size(); i++){
                searchDeadVariableInFile(components.get(i));
            }

            List<String> toRemove = new ArrayList<>();
            for (int i=0; i<variableInFile.size(); i++){
                deadVariableInFile.add(variableInFile.get(i));
                toRemove.add(variableInFile.get(i));
            }
            variableInFile.removeAll(toRemove);

            //display section
            System.out.println();
            System.out.println("========= variables in constructor / method ==========");
            System.out.println("variables in constructor/method = " + variableInMethod);
            AllVariableInMethod.addAll(variableInMethod);
            System.out.println("aliveVariable in method = " + aliveVariableInMethod);
            AllAliveVariableInMethod.addAll(aliveVariableInMethod);
            System.out.println("deadVariable in method = " + deadVariableInMethod);
            AllDeadVariableInMethod.addAll(deadVariableInMethod);

            if (deadVariableInMethod.size() > 0) {
                System.out.println("Path = " + fileAdd.getAbsolutePath());
            }

            variableInFile.addAll(aliveVariableInFile);
            variableInFile.addAll(deadVariableInFile);
            System.out.println();
            System.out.println("========= variables in file ==========");
            System.out.println("variables in file = " + variableInFile);
            AllVariableInFile.addAll(variableInFile);
            System.out.println("aliveVariable in file = " + aliveVariableInFile);
            AllAliveVariableInFile.addAll(aliveVariableInFile);
            System.out.println("deadVariable in file = " + deadVariableInFile);
            AllDeadVariableInFile.addAll(deadVariableInFile);

            if (deadVariableInFile.size() > 0) {
                System.out.println("Path = " + fileAdd.getAbsolutePath());
            }

            variableInMethod.clear();
            aliveVariableInMethod.clear();
            deadVariableInMethod.clear();
            variableInFile.clear();
            aliveVariableInFile.clear();
            deadVariableInFile.clear();
        }

        System.out.println();
        System.out.println("=========== Final output ===========");
        System.out.println("All variable in method in project (" + AllVariableInMethod.size() + ") = " + AllVariableInMethod);
        System.out.println("All alive variable in method in project (" + AllAliveVariableInMethod.size() + ") = " + AllAliveVariableInMethod);
        System.out.println("All dead variable in method in project (" + AllDeadVariableInMethod.size() + ") = " + AllDeadVariableInMethod);

        System.out.println("All variable in file in project (" + AllVariableInFile.size() + ") = " + AllVariableInFile);
        System.out.println("All alive variable in file in project (" + AllAliveVariableInFile.size() + ") = " + AllAliveVariableInFile);
        System.out.println("All dead variable in file in project (" + AllDeadVariableInFile.size() + ") = " + AllDeadVariableInFile);
    }

    //Step 1 : scan for constructor + parameters in constructor -> call step 2,3
    public List<Component> scanForConstructorAndMethodWithParameters(Map<Integer, String> AllLine, String constructorName){
        System.out.println("=========== scanForConstructorAndMethodWithParameters Method ===========");
        List<Component> components = new ArrayList<>();

        Integer numberOfLine = 1;

        while(numberOfLine <= AllLine.size()){
            String lineTemp = AllLine.get(numberOfLine);

            if (lineTemp.isEmpty()){
                numberOfLine++;
            }
            else{
                StringTokenizer token = new StringTokenizer(lineTemp);
                List<String> str = new ArrayList<>();

                for (int i=0; token.hasMoreTokens(); i++){
                    str.add(token.nextToken());
                }

                List<String> toRemove = new ArrayList<>();
                for (int j=0; j<str.size(); j++) {
                    if (str.get(j).equals("private") || str.get(j).equals("public") || str.get(j).equals("protected") || str.get(j).equals("static") || str.get(j).equals("final")) {
                        toRemove.add(str.get(j));
                    }
                }
                str.removeAll(toRemove);

                //check if AllLine after private/public/protected is constructorName( -> public Employee( -> means its a constructor
                if (str.size() > 0){
                    if (str.get(0).contains(constructorName) && str.get(0).contains("(")){
                        Integer lineStart = cutConstructorAndMethod(AllLine, numberOfLine).get("lineStart");
                        Integer lineStop = cutConstructorAndMethod(AllLine, numberOfLine).get("lineStop");
                        List<String> parameters = searchParameters(AllLine, lineStart);

                        Map<Integer, String> line = new HashMap<>();
                        for (int i=lineStart; i<=lineStop; i++){
                            line.put(i, AllLine.get(i));
                        }

                        components.add(new Component(constructorName, "constructor" , lineStart, lineStop, parameters, line));
                        System.out.println("constructor = " + constructorName + ", lineStart = " + lineStart + ", lineStop = " + lineStop + ", parameters = " + parameters);
                    }
                    //check if line is method
                    else if (str.size() >= 2){
                        if (str.get(1).contains("(")){
                            List<String> listReturnType = new ArrayList<>();

                            String[] returnType = {"String", "char", "Integer", "int"
                                    , "byte", "short", "long", "float", "double"
                                    , "boolean", "void", "Object", "ArrayList"};

                            for (String s : returnType){
                                listReturnType.add(s);
                            }

                            for (int i=0; i<className.size(); i++){
                                listReturnType.add(className.get(i));
                            }

                            if (containsAny(str.get(0), listReturnType)){
                                Integer lineStart = cutConstructorAndMethod(AllLine, numberOfLine).get("lineStart");
                                Integer lineStop = cutConstructorAndMethod(AllLine, numberOfLine).get("lineStop");
                                List<String> parameters = searchParameters(AllLine, lineStart);

                                Map<Integer, String> line = new HashMap<>();
                                for (int i=lineStart; i<=lineStop; i++){
                                    line.put(i, AllLine.get(i));
                                }

                                String methodName = removeExtensionForMethodName(str.get(1));

                                components.add(new Component(methodName, "method", lineStart, lineStop, parameters, line));
                                System.out.println("method = " + methodName  + ", lineStart = " + lineStart + ", lineStop = " + lineStop + ", parameters = " + parameters);
                            }
                        }
                    }
                }
                numberOfLine++;
            }
        }

        return components;
    }

    //step 2 : cut Constructor return start and stop number
    public Map<String, Integer> cutConstructorAndMethod(Map<Integer, String> AllLine, Integer numberOfLine){
        String lineNow = AllLine.get(numberOfLine);
        Integer lineStart = numberOfLine;
        Integer lineStop = numberOfLine;
        Integer numOfOpen = 0;
        Integer numOfClose = 0;
        Map<String, Integer> map = new HashMap<>();

        if (lineNow.contains("{")){
            numOfOpen++;
            while(numOfOpen != numOfClose){
                numberOfLine++;
                lineStop = numberOfLine;
                lineNow = AllLine.get(numberOfLine);

                if (lineNow.contains("{") && !lineNow.contains("}")){
                    numOfOpen++;
                }
                else if (lineNow.contains("}") && !lineNow.contains("{")){
                    numOfClose++;
                }
                else if (lineNow.contains("{") && lineNow.contains("}")){
                    numOfOpen++;
                    numOfClose++;
                }
            }
        }
        else if (!lineNow.contains("{")){
            while (!lineNow.contains("{") && numberOfLine < AllLine.size()){
                numberOfLine++;
                lineNow = AllLine.get(numberOfLine);
            }
        }
        else if (lineNow.contains("{") && lineNow.contains("}")){

        }

        map.put("lineStart", lineStart);
        map.put("lineStop", lineStop);
        return map;
    }

    //step 3 : search and return List<String> parameters
    public List<String> searchParameters(Map<Integer, String> AllLine ,Integer lineStart){
        List<String> parameters = new ArrayList<>();
        Integer lineNow = lineStart;
        String line = AllLine.get(lineNow);

        StringTokenizer token = new StringTokenizer(line);
        List<String> str = new ArrayList<>();

        for (int j = 0; token.hasMoreTokens(); j++) {
            str.add(token.nextToken());
        }

        for (int j = 0; j < str.size(); j++) {
            if (str.get(j).equals("private") || str.get(j).equals("public") || str.get(j).equals("protected")) {
                str.remove(j);
            }
        }

        //save parameter for single line
        if (line.contains("(") && line.contains(")") && line.contains("{")) {
            for (int j = 0; j < str.size(); j++) {
                //for constructor
                if (str.get(0).contains("(") && !str.get(0).contains(")")){
                    if (str.get(j).contains(",") || str.get(j).contains(")")) {
                        String parameter = removeExtensionForParameter(str.get(j).substring(0, str.get(j).length() - 1));
                        parameters.add(parameter);
                    }
                }
                //for method
                else if (str.get(1).contains("(") && !str.get(1).contains(")")){
                    if (str.get(j).contains(",") || str.get(j).contains(")")) {
                        String parameter = removeExtensionForParameter(str.get(j).substring(0, str.get(j).length() - 1));
                        parameters.add(parameter);
                    }
                }
            }
        }

        //save parameter for multi line
        else if (line.contains("(") && !line.contains(")")) {
            //for constructor
            if (str.get(0).contains("(") && !str.get(0).contains(")")){
                for (int j = 0; j < str.size(); j++) {
                    if (str.get(j).contains(",")) {
                        String parameter = removeExtensionForParameter(str.get(j).substring(0, str.get(j).length() - 1));
                        parameters.add(parameter);
                    }
                }

                while (!line.contains(")")) {
                    lineNow++;
                    line = AllLine.get(lineNow);

                    token = new StringTokenizer(line);
                    str = new ArrayList<>();

                    for (int j = 0; token.hasMoreTokens(); j++) {
                        str.add(token.nextToken());
                    }

                    for (int j = 0; j < str.size(); j++) {
                        if (str.get(j).equals("private") || str.get(j).equals("public") || str.get(j).equals("protected")) {
                            str.remove(j);
                        }
                    }

                    if (line.contains(")")) {
                        for (int j = 0; j < str.size(); j++) {
                            if (str.get(j).contains(",") || str.get(j).contains(")")) {
                                String parameter = removeExtensionForParameter(str.get(j).substring(0, str.get(j).length() - 1));
                                parameters.add(parameter);
                            }
                        }
                    } else {
                        for (int j = 0; j < str.size(); j++) {
                            if (str.get(j).contains(",")) {
                                String parameter = removeExtensionForParameter(str.get(j).substring(0, str.get(j).length() - 1));
                                parameters.add(parameter);
                            }
                        }
                    }
                }
            }
            //for method
            else if (str.get(1).contains("(") && !str.get(1).contains(")")){
                for (int j = 0; j < str.size(); j++) {
                    if (str.get(j).contains(",")) {
                        String parameter = removeExtensionForParameter(str.get(j).substring(0, str.get(j).length() - 1));
                        parameters.add(parameter);
                    }
                }

                while (!line.contains(")")) {
                    lineNow++;
                    line = AllLine.get(lineNow);

                    token = new StringTokenizer(line);
                    str = new ArrayList<>();

                    for (int j = 0; token.hasMoreTokens(); j++) {
                        str.add(token.nextToken());
                    }

                    for (int j = 0; j < str.size(); j++) {
                        if (str.get(j).equals("private") || str.get(j).equals("public") || str.get(j).equals("protected")) {
                            str.remove(j);
                        }
                    }

                    if (line.contains(")")) {
                        for (int j = 0; j < str.size(); j++) {
                            if (str.get(j).contains(",") || str.get(j).contains(")")) {
                                String parameter = removeExtensionForParameter(str.get(j).substring(0, str.get(j).length() - 1));
                                parameters.add(parameter);
                            }
                        }
                    } else {
                        for (int j = 0; j < str.size(); j++) {
                            if (str.get(j).contains(",")) {
                                String parameter = removeExtensionForParameter(str.get(j).substring(0, str.get(j).length() - 1));
                                parameters.add(parameter);
                            }
                        }
                    }
                }
            }
        }

        return parameters;
    }

    //step 4 : to remove line that is constructor / method -> save to lineOfFile
    public Map<String, Map<Integer, String>> removeLineInlineOfFile(Component component, Map<String, Map<Integer, String>> lineOfFile, int numOfFile){
        //remove line at start to stop number -> remove constructor from file
        for (int i=component.getLineStart(); i<= component.getLineStop(); i++){
            Integer lineStart = i;
            lineOfFile.get(className.get(numOfFile)).remove(lineStart);
        }

        return lineOfFile;
    }

    //step 5 : search variable in Constructor / Method
    public List<String> searchVariableInMethod(Component component){
        System.out.println("========== search variable ==========");
        Map<Integer, String> line = new HashMap<>();
        List<String> variable = new ArrayList<>();

        for (int i=component.getLineStart(); i<=component.getLineStop(); i++){
            System.out.println(component.getLine().get(i));
            line = component.getLine();
        }

        line = removeLineContainsOpenClose(line, component);

        List<String> temp = new ArrayList<>();

        for (int i=component.getToSearchStart(); i<=component.getToSearchStop(); i++){
            temp.add(line.get(i));
        }

        Integer commentOpen = 0;
        Integer commentClose = 0;

        //section to search variable
        for (int i=0; i<temp.size(); i++){
            //if line is comment -> do nothing
            if (temp.get(i).contains("/*")){
                commentOpen++;
                while (commentOpen != commentClose){
                    if (temp.get(i).contains("*/")){
                        commentClose++;
                    }
                    i++;
                }
            }
            // if line is comment -> do nothing
            else if (temp.get(i).contains("//")){
                continue;
            }
            else {
                if (temp.size() != 0 && temp.get(i) != null){
                    StringTokenizer token = new StringTokenizer(temp.get(i));
                    List<String> str = new ArrayList<>();

                    for (int j = 0; token.hasMoreTokens(); j++) {
                        str.add(token.nextToken());
                    }

                    if (temp.get(i).contains("String") || temp.get(i).contains("char") || temp.get(i).contains("Integer") || temp.get(i).contains("int") ||
                            temp.get(i).contains("byte") || temp.get(i).contains("short") || temp.get(i).contains("long") || temp.get(i).contains("float") ||
                            temp.get(i).contains("double") || temp.get(i).contains("boolean") || temp.get(i).contains("Object") || temp.get(i).contains("ArrayList")) {
                        if (str.size() >= 2 && !temp.get(i).contains(".") && !temp.get(i).contains("=") && !temp.get(i).contains("(") && !temp.get(i).contains("return")){
                            for (int j=0; j<str.size(); j++){
                                if (str.get(j).contains(";") || str.get(j).contains(",")){
                                    System.out.println(temp.get(i));
                                    System.out.println("1.1 test = " + str.get(j).substring(0, str.get(j).length() - 1));
                                    variable.add(str.get(j).substring(0, str.get(j).length() - 1));
                                }
                            }
                        }else if (str.size() > 2 && !temp.get(i).contains("System.out")){
                            if (str.get(2).equals("=")){
                                System.out.println(temp.get(i));
                                System.out.println("1.2 test = " + str.get(1));
                                variable.add(str.get(1));
                            }
                        }
                    }

                    if (temp.get(i).contains("(") && temp.get(i).contains("=") && !temp.get(i).contains("{") && !temp.get(i).contains("System.out") && !temp.get(i).contains("\"")){
                        if (str.size() > 2 && !str.get(0).equals("+") && !str.get(1).equals("=")){
                            if (!variable.contains(str.get(1))){
                                System.out.println(temp.get(i));
                                System.out.println("2 test = " + str.get(1));
                                variable.add(str.get(1));
                            }
                        }
                    }

                    for (int j=0; j<className.size(); j++){
                        if (str.size() >= 2 && str.get(0).equals(className.get(j))){
                            if (str.get(1).contains(";")){
                                if (!variable.contains(str.get(1).substring(0, str.get(1).length() - 1))){
                                    System.out.println(temp.get(i));
                                    System.out.println("3.1 test = " + str.get(1).substring(0, str.get(1).length() - 1));
                                    variable.add(str.get(1).substring(0, str.get(1).length() - 1));
                                }
                            } else {
                                if (!variable.contains(str.get(1))){
                                    System.out.println(temp.get(i));
                                    System.out.println("3.2 test = " + str.get(1));
                                    variable.add(str.get(1));
                                }
                            }
                        }
                    }
                }
            }
        }

        for (int i=0; i<variable.size(); i++){
            if (variable.get(i).contains("[") && variable.get(i).contains("]")){
                String varName = variable.get(i);
                String result = removeExtentionForVariableThatIsArray(varName);
                variable.remove(i);
                variable.add(result);
            }
        }

        return variable;
    }

    //step 6 : to remove line that contains { } in constructor / method
    public Map<Integer, String> removeLineContainsOpenClose(Map<Integer, String> line, Component component){
        //section to remove line contain { } -> result shows element in constructor / method
        Integer toRemoveStart = component.getLineStart();
        Integer toRemoveStop = component.getLineStop();
        Integer toSearchStart = toRemoveStart + 1;
        Integer toSearchStop = toRemoveStop - 1;

        line.remove(toRemoveStart);
        line.remove(toRemoveStop);

        component.setToSearchStart(toSearchStart);
        component.setToSearchStop(toSearchStop);

        return line;
    }

    //step 7 : to search Dead variable in constructor / method
    public void searchDeadVariableInMethod(Component component){
        Map<Integer, String> line = new HashMap<>();
        line = component.getLine();
        List<String> variable = component.getVariableInMethod();

        List<String> temp = new ArrayList<>();

        for (int i=component.getToSearchStart(); i<=component.getToSearchStop(); i++){
            temp.add(line.get(i));
        }

        Integer commentOpen = 0;
        Integer commentClose = 0;

        for (int i=0; i<temp.size(); i++){
            //if line is comment -> do nothing
            if (temp.get(i).contains("/*")){
                commentOpen++;
                while (commentOpen != commentClose){
                    if (temp.get(i).contains("*/")){
                        commentClose++;
                    }
                    i++;
                }
            }
            // if line is comment -> do nothing
            else if (temp.get(i).contains("//")){
                continue;
            }
            else {
                //if behide '=' contains variable name and '()'
                if (temp.get(i).contains("=") && temp.get(i).contains(";")){
                    String[] split =  temp.get(i).split("=");
                    List<String> listSplit = new ArrayList<>();

                    for (String a : split){
                        listSplit.add(a);
                    }

                    //check is string behind '='
                    String check = listSplit.get(1);

                    //if string check is not containing (double quote)
                    if (!check.contains("\"")){
                        List<String> toRemove = new ArrayList<>();
                        for (int j=0; j<variable.size(); j++){
                            //if string check contains variable name loops -> add to aliveVariableInMethod and remove from all variable
                            if (check.contains(variable.get(j))){
                                System.out.println("searchDeadInMethod 1.1 = " + temp.get(i));
                                System.out.println("add to aliveInMethod 1.1 " + variable.get(j));
                                aliveVariableInMethod.add(variable.get(j));
                                toRemove.add(variable.get(j));
                            }
                        }
                        variable.removeAll(toRemove);
                    }
                }
                // line contains -> something = someone.callmethod();
                else if (temp.get(i).contains("=") && temp.get(i).contains("(") && temp.get(i).contains(")") && temp.get(i).contains(";")){
                    String[] split =  temp.get(i).split("=");
                    List<String> listSplit = new ArrayList<>();

                    for (String a : split){
                        listSplit.add(a);
                    }

                    //check is string behind '='
                    String check = listSplit.get(1);

                    //if string check is not containing (double quote)
                    if (!check.contains("\"")){
                        List<String> toRemove = new ArrayList<>();
                        for (int j=0; j<variable.size(); j++){
                            //if string check contains variable name loops -> add to aliveVariableInMethod and remove from all variable
                            if (check.contains(variable.get(j))){
                                System.out.println("searchDeadInMethod 1.2 = " + temp.get(i));
                                System.out.println("add to aliveInMethod 1.2 " + variable.get(j));
                                aliveVariableInMethod.add(variable.get(j));
                                toRemove.add(variable.get(j));
                            }
                        }
                        variable.removeAll(toRemove);
                    }
                }
                //if line contains . and () -> means call method
                else if (temp.get(i).contains(".") && temp.get(i).contains("(") && temp.get(i).contains(")") && temp.get(i).contains(";") && !temp.get(i).contains("System")){
                    List<String> toRemove = new ArrayList<>();
                    for (int j=0; j<variable.size(); j++){
                        //if line contains variableName -> means variable call method
                        if (temp.get(i).contains(variable.get(j))){
                            System.out.println("searchDeadInMethod 1.3 = " + temp.get(i));
                            System.out.println("add to aliveInMethod 1.3 " + variable.get(j));
                            aliveVariableInMethod.add(variable.get(j));
                            toRemove.add(variable.get(j));
                        }
                    }
                    variable.removeAll(toRemove);
                }
                // line contains ( ) { -> example. if (varName == 1) {
                else if (temp.get(i).contains("(") && temp.get(i).contains(")") && temp.get(i).contains("{")){
                    List<String> toRemove = new ArrayList<>();
                    for (int j=0; j<variable.size(); j++){
                        //if line contains variableName -> means variable call method
                        if (temp.get(i).contains(variable.get(j))){
                            System.out.println("searchDeadInMethod 1.4 = " + temp.get(i));
                            System.out.println("add to aliveInMethod 1.4 " + variable.get(j));
                            aliveVariableInMethod.add(variable.get(j));
                            toRemove.add(variable.get(j));
                        }
                    }
                    variable.removeAll(toRemove);
                }
                // return varName;
                else if (temp.get(i).contains("return") && temp.get(i).contains(";")){
                    List<String> toRemove = new ArrayList<>();
                    for (int j=0; j<variable.size(); j++){
                        //if line contains variableName -> means variable call method
                        if (temp.get(i).contains(variable.get(j))){
                            System.out.println("searchDeadInMethod 1.5 = " + temp.get(i));
                            System.out.println("add to aliveInMethod 1.5 " + variable.get(j));
                            aliveVariableInMethod.add(variable.get(j));
                            toRemove.add(variable.get(j));
                        }
                    }
                    variable.removeAll(toRemove);
                }
                // insert variable to method -> test(variable);
                else if (temp.get(i).contains("(") && temp.get(i).contains(")") && temp.get(i).contains(";")){
                    List<String> toRemove = new ArrayList<>();
                    for (int j=0; j<variable.size(); j++){
                        //if line contains variableName -> means variable call method
                        if (temp.get(i).contains(variable.get(j))){
                            System.out.println("searchDeadInMethod 1.6 = " + temp.get(i));
                            System.out.println("add to aliveInMethod 1.6 " + variable.get(j));
                            aliveVariableInMethod.add(variable.get(j));
                            toRemove.add(variable.get(j));
                        }
                    }
                    variable.removeAll(toRemove);
                }

                if (temp.get(i).contains("+") && temp.get(i).contains("\"")){
                    List<String> toRemove = new ArrayList<>();
                    for (int j=0; j<variable.size(); j++){
                        //if line contains variableName -> means variable call method
                        if (temp.get(i).contains(variable.get(j))){
                            StringTokenizer token = new StringTokenizer(temp.get(i));
                            List<String> str = new ArrayList<>();

                            for (int k=0; token.hasMoreTokens(); k++){
                                str.add(token.nextToken());
                            }

                            for (int k=0; k<str.size(); k++){
                                if (str.get(k).equals(variable.get(j))){
                                    if (str.get(k-1).equals("+")){
                                        System.out.println("searchDeadInMethod 1.6 = " + temp.get(i));
                                        System.out.println("add to aliveInMethod 1.6 " + variable.get(j));
                                        aliveVariableInMethod.add(variable.get(j));
                                        toRemove.add(variable.get(j));
                                    }
                                }
                                // duplicate of variable (has 1 time but exist 2 times)
                            /*if (!aliveVariableInMethod.contains(variable.get(j))){
                                if (str.get(k).equals(variable.get(j))){
                                    if (str.get(k-1).equals("+")){
                                        System.out.println("test = " + temp.get(i));
                                        System.out.println("add = " + variable.get(j));
                                        aliveVariableInMethod.add(variable.get(j));
                                        toRemove.add(variable.get(j));
                                    }
                                }
                            }*/
                            }
                        }
                    }
                    variable.removeAll(toRemove);
                }
            }
        }

        //at the end the rest variable in method -> add to deadVariableInMethod
        List<String> toRemove = new ArrayList<>();
        for (int i=0; i<variable.size(); i++){
            deadVariableInMethod.add(variable.get(i));
            toRemove.add(variable.get(i));
        }
        variable.removeAll(toRemove);
    }

    //step 8 : to search variable in file
    public void searchVariableInfile(Map<String, Map<Integer, String>> lineOfFile, String className){
        List<String> dataType = Arrays.asList("String", "char", "Integer", "int"
                , "byte", "short", "long", "float", "double"
                , "boolean", "Object", "ArrayList");

        Iterator itr = lineOfFile.get(className).values().iterator();
        List<String> temp = new ArrayList<>();

        while (itr.hasNext()){
            String line = (String) itr.next();
            temp.add(line);
        }

        Integer commentOpen = 0;
        Integer commentClose = 0;

        for (int i=0; i<temp.size(); i++){
            //if line is comment -> do nothing
            if (temp.get(i).contains("/*")){
                commentOpen++;
                while (commentOpen != commentClose){
                    if (temp.get(i).contains("*/")){
                        commentClose++;
                    }
                    i++;
                }
            }
            // if line is comment -> do nothing
            else if (temp.get(i).contains("//")){
                continue;
            }
            else {
                StringTokenizer token = new StringTokenizer(temp.get(i));
                List<String> str = new ArrayList<>();

                for (int j=0; token.hasMoreTokens(); j++){
                    str.add(token.nextToken());
                }

                List<String> toRemove = new ArrayList<>();
                for (int j=0; j<str.size(); j++) {
                    if (str.get(j).equals("private") || str.get(j).equals("public") || str.get(j).equals("protected") || str.get(j).equals("static") || str.get(j).equals("final")) {
                        toRemove.add(str.get(j));
                    }
                }
                str.removeAll(toRemove);

                if (!temp.get(i).isEmpty()){
                    if (str.size() >= 2){
                        if (!temp.get(i).contains("interface") && !temp.get(i).contains("class") && !temp.get(i).contains("abstract")){
                            if (containsAny(str.get(0), dataType)){
                                if (temp.get(i).contains("=")){
                                    System.out.println(temp.get(i));
                                    System.out.println("1.1 test = " + str.get(1));
                                    variableInFile.add(str.get(1));
                                }else if (temp.get(i).contains(",")){
                                    for (int j=0; j<str.size(); j++){
                                        if(str.get(j).contains(",") || str.get(j).contains(";")){
                                            String varName = removeExtensionForVariable(str.get(j));
                                            variableInFile.add(varName);
                                            System.out.println(temp.get(i));
                                            System.out.println("1.2 test = " + varName);
                                        }
                                    }
                                } else {
                                    System.out.println(temp.get(i));
                                    System.out.println(str.get(0));
                                    System.out.println("1.3 test = " + str.get(1).substring(0, str.get(1).length() - 1));
                                    variableInFile.add(str.get(1).substring(0, str.get(1).length() - 1));
                                }
                            }
                            else if (temp.get(i).contains("(") && temp.get(i).contains("=")){
                                if (!variableInFile.contains(str.get(1))){
                                    System.out.println(temp.get(i));
                                    System.out.println("1.4 test = " + str.get(1));
                                    variableInFile.add(str.get(1));
                                }
                            }
                        }
                    }

                    for (int j=0; j<this.className.size(); j++){
                        if (str.size() >= 2 && str.get(0).equals(this.className.get(j))){
                            if (str.get(1).contains(";")){
                                if (!variableInFile.contains(str.get(1).substring(0, str.get(1).length() - 1))){
                                    System.out.println(temp.get(i));
                                    System.out.println("2.1 test = " + str.get(1).substring(0, str.get(1).length() - 1));
                                    variableInFile.add(str.get(1).substring(0, str.get(1).length() - 1));
                                }
                            } else {
                                if (!variableInFile.contains(str.get(1))){
                                    System.out.println(temp.get(i));
                                    System.out.println("2.2 test = " + str.get(1));
                                    variableInFile.add(str.get(1));
                                }
                            }
                        }
                    }
                }
            }
        }

        for (int i=0; i<variableInFile.size(); i++){
            if (variableInFile.get(i).contains("[") && variableInFile.get(i).contains("]")){
                String varName = variableInFile.get(i);
                String result = removeExtentionForVariableThatIsArray(varName);
                variableInFile.remove(i);
                variableInFile.add(result);
            }
        }
    }

    //step 9 : to search Dead variable in file -> to all components
    public void searchDeadVariableInFile(Component component){
        Map<Integer, String> line = new HashMap<>();
        line = component.getLine();

        List<String> temp = new ArrayList<>();

        for (int i=component.getToSearchStart(); i<=component.getToSearchStop(); i++){
            temp.add(line.get(i));
        }

        Integer commentOpen = 0;
        Integer commentClose = 0;

        for (int i=0; i<temp.size(); i++){
            //if line is comment -> do nothing
            if (temp.get(i).contains("/*")){
                commentOpen++;
                while (commentOpen != commentClose){
                    if (temp.get(i).contains("*/")){
                        commentClose++;
                    }
                    i++;
                }
            }
            // if line is comment -> do nothing
            else if (temp.get(i).contains("//")){
                continue;
            }
            else {
                //if behide '=' contains variable name and '()'
                if (temp.get(i).contains("=") && temp.get(i).contains(";")){
                    String[] split =  temp.get(i).split("=");
                    List<String> listSplit = new ArrayList<>();

                    for (String a : split){
                        listSplit.add(a);
                    }

                    //check is string behind '='
                    String check = listSplit.get(1);

                    //if string check is not containing (double quote)
                    if (!check.contains("\"")){
                        List<String> toRemove = new ArrayList<>();
                        for (int j=0; j<variableInFile.size(); j++){
                            //if string check contains variable name loops -> add to aliveVariableInMethod and remove from all variable
                            if (check.contains(variableInFile.get(j))){
                                System.out.println("searchDeadInFile 1.1 = " + temp.get(i));
                                System.out.println("add to aliveInFile 1.1 = " + variableInFile.get(j));
                                aliveVariableInFile.add(variableInFile.get(j));
                                toRemove.add(variableInFile.get(j));
                            }
                        }
                        variableInFile.removeAll(toRemove);
                    }
                }
                // line contains -> something = someone.callmethod();
                else if (temp.get(i).contains("=") && temp.get(i).contains("(") && temp.get(i).contains(")") && temp.get(i).contains(";")){
                    String[] split =  temp.get(i).split("=");
                    List<String> listSplit = new ArrayList<>();

                    for (String a : split){
                        listSplit.add(a);
                    }

                    //check is string behind '='
                    String check = listSplit.get(1);

                    //if string check is not containing (double quote)
                    if (!check.contains("\"")){
                        List<String> toRemove = new ArrayList<>();
                        for (int j=0; j<variableInFile.size(); j++){
                            //if string check contains variable name loops -> add to aliveVariableInMethod and remove from all variable
                            if (check.contains(variableInFile.get(j))){
                                System.out.println("searchDeadInFile 1.2 = " + temp.get(i));
                                System.out.println("add to aliveInFile 1.2 = " + variableInFile.get(j));
                                aliveVariableInFile.add(variableInFile.get(j));
                                toRemove.add(variableInFile.get(j));
                            }
                        }
                        variableInFile.removeAll(toRemove);
                    }
                }
                //if line contains . and () -> means call method
                else if (temp.get(i).contains(".") && temp.get(i).contains("(") && temp.get(i).contains(")") && temp.get(i).contains(";") && !temp.get(i).contains("System")){
                    List<String> toRemove = new ArrayList<>();
                    for (int j=0; j<variableInFile.size(); j++){
                        //if line contains variableName -> means variable call method
                        if (temp.get(i).contains(variableInFile.get(j))){
                            System.out.println("searchDeadInFile 1.3 = " + temp.get(i));
                            System.out.println("add to aliveInFile 1.3 = " + variableInFile.get(j));
                            aliveVariableInFile.add(variableInFile.get(j));
                            toRemove.add(variableInFile.get(j));
                        }
                    }
                    variableInFile.removeAll(toRemove);
                }
                // line contains ( ) { -> example. if (varName == 1) {
                else if (temp.get(i).contains("(") && temp.get(i).contains(")") && temp.get(i).contains("{")){
                    List<String> toRemove = new ArrayList<>();
                    for (int j=0; j<variableInFile.size(); j++){
                        //if line contains variableName -> means variable call method
                        if (temp.get(i).contains(variableInFile.get(j))){
                            System.out.println("searchDeadInFile 1.4 = " + temp.get(i));
                            System.out.println("add to aliveInFile 1.4 = " + variableInFile.get(j));
                            aliveVariableInFile.add(variableInFile.get(j));
                            toRemove.add(variableInFile.get(j));
                        }
                    }
                    variableInFile.removeAll(toRemove);
                }
                //return varName;
                else if (temp.get(i).contains("return") && temp.get(i).contains(";")){
                    List<String> toRemove = new ArrayList<>();
                    for (int j=0; j<variableInFile.size(); j++){
                        //if line contains variableName -> means variable call method
                        if (temp.get(i).contains(variableInFile.get(j))){
                            System.out.println("searchDeadInFile 1.5 = " + temp.get(i));
                            System.out.println("add to aliveInFile 1.5 = " + variableInFile.get(j));
                            aliveVariableInFile.add(variableInFile.get(j));
                            toRemove.add(variableInFile.get(j));
                        }
                    }
                    variableInFile.removeAll(toRemove);
                }

                //System.out.print();
                if (temp.get(i).contains("+") && temp.get(i).contains("\"")){
                    List<String> toRemove = new ArrayList<>();
                    for (int j=0; j<variableInFile.size(); j++){
                        //if line contains variableName -> means variable call method
                        if (temp.get(i).contains(variableInFile.get(j))){
                            StringTokenizer token = new StringTokenizer(temp.get(i));
                            List<String> str = new ArrayList<>();

                            for (int k=0; token.hasMoreTokens(); k++){
                                str.add(token.nextToken());
                            }

                            for (int k=0; k<str.size(); k++){
                                if (str.get(k).contains(variableInFile.get(j))){
                                    if (str.get(k-1).equals("+") || str.get(k-1).equals("-") || str.get(k-1).equals("*") || str.get(k-1).equals("/")){
                                        System.out.println("searchDeadInFile 1.6 = " + temp.get(i));
                                        System.out.println("add to aliveInFile 1.6 = " + variableInFile.get(j));
                                        aliveVariableInFile.add(variableInFile.get(j));
                                        toRemove.add(variableInFile.get(j));
                                    }
                                }
                                // duplicate of variable (has 1 time but exist 2 times)
                            /*if (!aliveVariableInFile.contains(variableInFile.get(j))){
                                if (str.get(k).equals(variableInFile.get(j))){
                                    if (str.get(k-1).equals("+")){
                                        System.out.println("test = " + temp.get(i));
                                        System.out.println("add = " + variableInFile.get(j));
                                        aliveVariableInFile.add(variableInFile.get(j));
                                        toRemove.add(variableInFile.get(j));
                                    }
                                }
                            }*/
                            }
                        }
                    }
                    variableInFile.removeAll(toRemove);
                }
            }
        }
    }

    public Map<Integer, String> scanFile(File file) throws FileNotFoundException {
        Scanner scanner = new Scanner(file);
        Map<Integer, String> line = new HashMap<>();
        String temp = null;
        Integer i = 0;

        while(scanner.hasNextLine()){
            temp = scanner.nextLine();
            i++;

            line.put(i, temp);
        }

        return  line;
    }

    public boolean scanFileToFindMainFunc(File file) throws FileNotFoundException {
        Scanner scanner = new Scanner(file);
        String temp = null;

        while(scanner.hasNextLine()){
            temp = scanner.nextLine();

            if (temp.contains("public static void main")) {
                return true;
            }
        }

        return false;
    }

    public static String removeExtension(String file){
        return file.replaceFirst("[.][^.]+$", "");
    }

    public static String removeExtensionForParameter(String parameter){
        return parameter.replaceFirst("[,|)]", "");
    }

    public static String removeExtensionForVariable(String variable){
        return variable.replaceFirst("[,|;]", "");
    }

    //variable[]
    public static String removeExtentionForVariableThatIsArray(String variable){
        return variable.replace("[]","");
    }

    public static String removeExtensionForMethodName(String methodName){
        return methodName.replaceFirst("[(][^(]+$", "");
    }

    public static boolean containsAny(String str, List<String> searchString) {
        if (str == null || str.length() == 0 || searchString == null || searchString.size() == 0) {
            return false;
        }
        for (int i=0; i<searchString.size(); i++){
            String search = searchString.get(i);
            if (str.contains(search)){
                return true;
            }
        }
        return false;
    }
}