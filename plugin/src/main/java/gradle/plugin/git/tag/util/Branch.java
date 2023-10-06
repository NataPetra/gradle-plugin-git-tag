package gradle.plugin.git.tag.util;

public enum Branch {

    DEV("dev"),
    QA("qa"),
    STAGE("stage"),
    MASTER("master");

    private final String name;

    Branch(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }
}
