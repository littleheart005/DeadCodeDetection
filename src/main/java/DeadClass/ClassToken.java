package DeadClass;

public class ClassToken {
    private String name;
    private String packageName;
    private String path;
    private int line;
    private Boolean isDead;

    public ClassToken(String name, String packageName, String path,int line) {
        this.name = name;
        this.packageName = packageName;
        this.path = path;
        this.line = line;
        // First set all the class dead.
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
}
