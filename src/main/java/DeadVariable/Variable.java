package DeadVariable;

public class Variable {
    private String variableName;
    private Integer beginLine;
    private String modifier;
    private String parent;
    private String stringToCheck;

    public String getVariableName() {
        return variableName;
    }

    public void setVariableName(String variableName) {
        this.variableName = variableName;
    }

    public Integer getBeginLine() {
        return beginLine;
    }

    public void setBeginLine(Integer beginLine) {
        this.beginLine = beginLine;
    }

    public String getModifier() {
        return modifier;
    }

    public void setModifier(String modifier) {
        this.modifier = modifier;
    }

    public String getParent() {
        return parent;
    }

    public void setParent(String parent) {
        this.parent = parent;
    }

    public String getStringToCheck() {
        return stringToCheck;
    }

    public void setStringToCheck(String stringToCheck) {
        this.stringToCheck = stringToCheck;
    }
}
