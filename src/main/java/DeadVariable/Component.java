package DeadVariable;

import java.util.List;
import java.util.Map;

public class Component {
    private String name;
    private String type;
    private Map<Integer, String> line;
    private Integer lineStart;
    private Integer lineStop;
    private Integer toSearchStart;
    private Integer toSearchStop;
    private List<String> parameters;
    private List<String> variableInMethod;

    public Component(String name, String type, Integer lineStart, Integer lineStop,
                     List<String> parameters, Map<Integer, String> line){
        this.name = name;
        this.lineStart = lineStart;
        this.lineStop = lineStop;
        this.parameters = parameters;
        this.line = line;
        this.type = type;
    }

    public String getName() {
        return name;
    }

    public String getType() {
        return type;
    }

    public Integer getLineStart() {
        return lineStart;
    }

    public Integer getLineStop() {
        return lineStop;
    }

    public List<String> getParameters() {
        return parameters;
    }

    public Map<Integer, String> getLine() {
        return line;
    }

    public List<String> getVariableInMethod() {
        return variableInMethod;
    }

    public void setVariableInMethod(List<String> variableInMethod) {
        this.variableInMethod = variableInMethod;
    }

    public Integer getToSearchStart() {
        return toSearchStart;
    }

    public void setToSearchStart(Integer toSearchStart) {
        this.toSearchStart = toSearchStart;
    }

    public Integer getToSearchStop() {
        return toSearchStop;
    }

    public void setToSearchStop(Integer toSearchStop) {
        this.toSearchStop = toSearchStop;
    }
}
