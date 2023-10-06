package gradle.plugin.git.tag.task;

import gradle.plugin.git.tag.util.Branch;
import gradle.plugin.git.tag.util.CommandExecutor;
import gradle.plugin.git.tag.util.fartory.CommandExecutorSingleton;
import org.gradle.api.DefaultTask;
import org.gradle.api.tasks.TaskAction;

public class GitTagTask extends DefaultTask {

    public static final CommandExecutor commandExecutor;

    static {
        commandExecutor = CommandExecutorSingleton.getInstance();
    }

    public static final String GIT_BRANCH = "git rev-parse --abbrev-ref HEAD";
    public static final String LAST_PUBLISHED_VERSION = "git describe --abbrev=0 --tags";
    public static final String HAS_TAG = "git describe --exact-match --tags HEAD";
    public static final String TAG = "git tag ";
    public static final String PUSH_ORIGIN = "git push origin ";
    public static final String STATUS = "git status --porcelain";

    @TaskAction
    public void customTaskAction() {
        String branch = getCurrentGitBranch();

        if (hasGitTag()) {
            getLogger().info("The current state of the project is already assigned a git tag. A new git tag will not be created.");
            return;
        }

        if (hasUncommittedChanges()) {
            getLogger().warn("There are uncommitted changes in the working directory. Build number: " + getLastPublishedVersion() + ".uncommitted.");
            return;
        }

        String version;
        if (branch.equals(Branch.DEV.getName()) || branch.equals(Branch.QA.getName())) {
            version = incrementVersion(getLastPublishedVersion(), Boolean.FALSE);
        } else if (branch.equals(Branch.STAGE.getName())) {
            version = incrementVersion(getLastPublishedVersion(), Boolean.FALSE) + "-rc";
        } else if (branch.equals(Branch.MASTER.getName())) {
            version = incrementVersion(getLastPublishedVersion(), Boolean.TRUE);
        } else {
            version = getLastPublishedVersion() + "-SNAPSHOT";
        }

        commandExecutor.executeGitCommand(TAG + version);
        commandExecutor.executeGitCommand(PUSH_ORIGIN + version);
    }

    private String getCurrentGitBranch(){
        return commandExecutor.getResultGitCommand(GIT_BRANCH);
    }

    private String getLastPublishedVersion() {
        return commandExecutor.getResultGitCommand(LAST_PUBLISHED_VERSION);
    }

    private String incrementVersion(String version, boolean isMajor) {
        if(version == null){
            version = "v0.0";
        }
        if (!version.startsWith("v")) {
            throw new IllegalArgumentException("Invalid version format: " + version);
        }

        String[] components = version.substring(1).split("\\.");
        if (components.length != 2) {
            throw new IllegalArgumentException("Invalid version format: " + version);
        }

        int major = Integer.parseInt(components[0]);
        int minor = Integer.parseInt(components[1]);

        if(isMajor){
            major++;
        } else {
            minor++;
        }

        return "v" + major + "." + minor;
    }

    private boolean hasUncommittedChanges() {
        return commandExecutor.getResultGitCommand(STATUS) != null;
    }

    private boolean hasGitTag() {
        return commandExecutor.getResultGitCommand(HAS_TAG) != null;
    }

}
