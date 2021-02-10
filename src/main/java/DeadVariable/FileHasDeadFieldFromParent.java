package DeadVariable;

import TokenGenerator.FileToken;

import java.util.ArrayList;
import java.util.List;

public class FileHasDeadFieldFromParent {
    private String parentName;
    private List<FileToken> childList = new ArrayList<>();

    public String getParentName() {
        return parentName;
    }

    public void setParentName(String parentName) {
        this.parentName = parentName;
    }

    public List<FileToken> getChildList() {
        return childList;
    }

    public void setChildList(List<FileToken> childList) {
        this.childList = childList;
    }
}
