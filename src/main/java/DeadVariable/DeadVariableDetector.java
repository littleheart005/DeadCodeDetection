package DeadVariable;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class DeadVariableDetector {
    List<FileToken> fileTokenList;

    public DeadVariableDetector(List<FileToken> fileTokenList) {
        this.fileTokenList = fileTokenList;
        detectDeadStaticField();
        detectDeadField();
        detectDeadVariable();
    }

    //method to detect alive/dead static field -> set to FileToken
    private void detectDeadStaticField() {
        for (int i=0; i<this.fileTokenList.size(); i++) {
            DetectorUtil detectorUtil = new DetectorUtil();
            List<Variable> aliveStaticField = new ArrayList<>();

            if (!this.fileTokenList.get(i).getStaticField().isEmpty()) {
                //step 1 : check with itself
                detectorUtil.checkRegexForStaticField(this.fileTokenList.get(i), aliveStaticField, this.fileTokenList.get(i));

                //step 2 : check with child classes that extend
                if (this.fileTokenList.get(i).getStaticField().size() > 0) {
                    if (!this.fileTokenList.get(i).getChildClassToDetect().isEmpty()) {
                        for (int j=0; j<this.fileTokenList.get(i).getChildClassToDetect().size(); j++) {
                            //case detect with child class use -> checkRegexForField method (send param 'childCase') -> because we use only variableName to detect (not Class.variableName)
                            detectorUtil.checkRegexForField(this.fileTokenList.get(i), aliveStaticField, this.fileTokenList.get(i).getChildClassToDetect().get(j), "childCase");
                        }
                    }
                }

                //step 3 : check with child classes that import
                if (this.fileTokenList.get(i).getStaticField().size() > 0) {
                    if (!this.fileTokenList.get(i).getClassThatImportToDetect().isEmpty()) {
                        for (int j=0; j<this.fileTokenList.get(i).getClassThatImportToDetect().size(); j++) {
                            detectorUtil.checkRegexForStaticField(this.fileTokenList.get(i), aliveStaticField, this.fileTokenList.get(i).getClassThatImportToDetect().get(j));
                        }
                    }
                }

                //step 4 : check with classes in same package
                if (this.fileTokenList.get(i).getStaticField().size() > 0) {
                    if (!this.fileTokenList.get(i).getFileInSamePackageToDetect().isEmpty()) {
                        for (int j = 0; j < this.fileTokenList.get(i).getFileInSamePackageToDetect().size(); j++) {
                            detectorUtil.checkRegexForStaticField(this.fileTokenList.get(i), aliveStaticField, this.fileTokenList.get(i).getFileInSamePackageToDetect().get(j));
                        }
                    }
                }
            }

            //set aliveStaticField to FileToken
            if (aliveStaticField.size() > 0) {
                this.fileTokenList.get(i).setAliveStaticField(aliveStaticField);
            }

            //set deadStaticField to FileToken
            if (this.fileTokenList.get(i).getStaticField().size() > 0) {
                this.fileTokenList.get(i).setDeadStaticField(this.fileTokenList.get(i).getStaticField());
            }
        }
    }

    //method to detect alive/dead field -> set to FileToken
    private void detectDeadField() {
        for (int i=0; i<this.fileTokenList.size(); i++) {
            DetectorUtil detectorUtil = new DetectorUtil();
            List<Variable> aliveField = new ArrayList<>();

            if (!this.fileTokenList.get(i).getField().isEmpty()) {
                //step 1 : check with itself
                detectorUtil.checkRegexForField(this.fileTokenList.get(i), aliveField, this.fileTokenList.get(i), "general");

                //step 2 : check with child classes that extend
                if (this.fileTokenList.get(i).getField().size() > 0) {
                    if (!this.fileTokenList.get(i).getChildClassToDetect().isEmpty()) {
                        for (int j=0; j<this.fileTokenList.get(i).getChildClassToDetect().size(); j++) {
                            detectorUtil.checkRegexForField(this.fileTokenList.get(i), aliveField, this.fileTokenList.get(i).getChildClassToDetect().get(j), "general");
                        }
                    }
                }

                //step 3 : check with child classes that import
                if (this.fileTokenList.get(i).getField().size() > 0) {
                    if (!this.fileTokenList.get(i).getClassThatImportToDetect().isEmpty()) {
                        for (int j=0; j<this.fileTokenList.get(i).getClassThatImportToDetect().size(); j++) {
                            detectorUtil.checkRegexForField(this.fileTokenList.get(i), aliveField, this.fileTokenList.get(i).getClassThatImportToDetect().get(j), "general");
                        }
                    }
                }

                //step 4 : check with classes in same package
                if (this.fileTokenList.get(i).getField().size() > 0) {
                    if (!this.fileTokenList.get(i).getFileInSamePackageToDetect().isEmpty()) {
                        for (int j=0; j<this.fileTokenList.get(i).getFileInSamePackageToDetect().size(); j++) {
                            detectorUtil.checkRegexForField(this.fileTokenList.get(i), aliveField, this.fileTokenList.get(i).getFileInSamePackageToDetect().get(j), "general");
                        }
                    }
                }
            }

            //set aliveField to FileToken
            if (aliveField.size() > 0) {
                this.fileTokenList.get(i).setAliveField(aliveField);
            }

            //set deadField to FileToken
            if (this.fileTokenList.get(i).getField().size() > 0) {
                this.fileTokenList.get(i).setDeadField(this.fileTokenList.get(i).getField());
            }
        }
    }

    //method to detect alive/dead variable -> set to FileToken
    private void detectDeadVariable() {
        for (int i=0; i<this.fileTokenList.size(); i++) {
            DetectorUtil detectorUtil = new DetectorUtil();
            List<Variable> aliveVariable = new ArrayList<>();

            if (!this.fileTokenList.get(i).getMethodTokenList().isEmpty()) {
                detectorUtil.checkRegexForVariable(this.fileTokenList.get(i), aliveVariable, this.fileTokenList.get(i));
            }

            //set DeadVariable to FileToken
            if (aliveVariable.size() > 0) {
                this.fileTokenList.get(i).setAliveVariable(aliveVariable);
            }

            //add deadVariable to FileToken
            if (!this.fileTokenList.get(i).getMethodTokenList().isEmpty()){
                for (int j=0; j<this.fileTokenList.get(i).getMethodTokenList().size(); j++)  {
                    if (this.fileTokenList.get(i).getMethodTokenList().get(j).getVariable().size() > 0) {
                        this.fileTokenList.get(i).addDeadVariable(this.fileTokenList.get(i).getMethodTokenList().get(j).getVariable());
                    }
                }
            }
        }
    }

    //method to create report specific file name
    public void createReport (String fileName) throws IOException {
        String filename = "DeadVariableDetector" + fileName + "ProjectOutput";
        Output output = new Output();
        output.createFile(filename);
        output.sendInfo(this.fileTokenList);
        output.write();
    }
}