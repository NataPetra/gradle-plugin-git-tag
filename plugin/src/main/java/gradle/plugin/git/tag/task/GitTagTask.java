package gradle.plugin.git.tag.task;

import gradle.plugin.git.tag.util.Branch;
import gradle.plugin.git.tag.util.CommandExecutor;
import gradle.plugin.git.tag.util.faсtory.CommandExecutorSingleton;
import org.gradle.api.DefaultTask;
import org.gradle.api.tasks.TaskAction;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.Objects;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class GitTagTask extends DefaultTask {

    private static final Logger log = LoggerFactory.getLogger(GitTagTask.class);
    public static final CommandExecutor commandExecutor;

    static {
        commandExecutor = CommandExecutorSingleton.getInstance();
    }

    public static final String GIT_BRANCH = "git rev-parse --abbrev-ref HEAD";
    public static final String LAST_PUBLISHED_VERSION = "git describe --abbrev=0 --tags";
    public static final String HAS_TAG = "git describe --exact-match --tags HEAD";

    @TaskAction
    public void customTaskAction() {
        String branch = getCurrentGitBranch();

//        if (hasGitTag()) {
//            getLogger().warn("The current state of the project is already assigned a git tag. A new git tag will not be created.");
//            return;
//        }

//        if (hasUncommittedChanges()) {
//            getLogger().warn("There are uncommitted changes in the working directory. Build number: " + getLastPublishedVersion() + ".uncommitted.");
//            return;
//        }

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

        commandExecutor.executeGitCommand("tag", version);
        commandExecutor.executeGitCommandPush("push", "origin", version);
    }

    private String getCurrentGitBranch() {
        String branch = commandExecutor.getResultGitCommand(GIT_BRANCH);
        log.info("Current branch: {}", branch);
        return branch;
    }

    private String getLastPublishedVersion() {
        String lastPublishedV = commandExecutor.getResultGitCommand(LAST_PUBLISHED_VERSION);
        log.info("Last published version: {}", lastPublishedV);
        return Objects.requireNonNullElse(lastPublishedV, "v0.0");
    }

    private String incrementVersion(String version, boolean isMajor) {
        if (version == null) {
            version = "v0.0";
        }

        String versionRegex = "v(\\d+)\\.(\\d+)(?:-(\\w+))?";

        Pattern pattern = Pattern.compile(versionRegex);
        Matcher matcher = pattern.matcher(version);

        if (matcher.matches()) {
            int major = Integer.parseInt(matcher.group(1));
            int minor = Integer.parseInt(matcher.group(2));

            if (isMajor) {
                major++;
            } else {
                minor++;
            }

            String suffix = matcher.group(3);
            if (suffix != null) {
                return "v" + major + "." + minor + "-" + suffix;
            } else {
                return "v" + major + "." + minor;
            }
        } else {
            throw new IllegalArgumentException("Invalid version format: " + version);
        }
    }

    private boolean hasUncommittedChanges() {
        try {
            ProcessBuilder processBuilder = new ProcessBuilder("git", "diff", "--quiet");
            Process process = processBuilder.start();
            int exitCode = process.waitFor();
            return exitCode != 0;
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
            Thread.currentThread().interrupt();
            return false;
        }
    }

    private boolean hasGitTag() {
        return commandExecutor.getResultGitCommand(HAS_TAG) != null;
    }

}
