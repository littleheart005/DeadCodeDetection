package DeadClassInterface;

public class InterfaceToken {
    private String name;
    private String fileName;
    private String packageName;
    private String path;
    private int line;
    private Boolean isDead;

    public InterfaceToken(String name, String fileName, String packageName, String path, int line){
        this.name = name;
        this.fileName = fileName;
        this.packageName = packageName;
        this.path = path;
        this.line = line;
        // First set all the interface dead.
        this.isDead = true;
    }

    public String getName() {
        return name;
    }

    public String getPackageName() {
        return packageName;
    }

    public String getPath() {
        return path;
    }

    public int getLine() {
        return line;
    }

    public Boolean getDead() {
        return isDead;
    }

    public void setDead(Boolean dead) {
        isDead = dead;
    }

    public String getFileName() {
        return fileName;
    }
}
