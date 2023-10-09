package gradle.plugin.git.tag.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class CommandExecutor {

    private static final Logger log = LoggerFactory.getLogger(CommandExecutor.class);

    public void executeGitCommand(String command, String command2) {
        try {
            ProcessBuilder processBuilder = new ProcessBuilder("git", command, command2);
            processBuilder.redirectErrorStream(true);

            Process process = processBuilder.start();
            BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));

            String line;
            while ((line = reader.readLine()) != null) {
                log.warn(line);
            }

            int exitCode = process.waitFor();
            if (exitCode != 0) {
                log.error("Command execution error: {}", command);
            }
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
            Thread.currentThread().interrupt();
        }
    }

    public void executeGitCommandPush(String command, String command2, String command3) {
        try {
            ProcessBuilder processBuilder = new ProcessBuilder("git", command, command2, command3);
            processBuilder.redirectErrorStream(true);

            Process process = processBuilder.start();
            BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));

            String line;
            while ((line = reader.readLine()) != null) {
                log.warn(line);
            }

            int exitCode = process.waitFor();
            if (exitCode != 0) {
                log.error("Command execution error: {}", command);
            }
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
            Thread.currentThread().interrupt();
        }
    }

    public String getResultGitCommand(String command) {
        try {
            Process process = Runtime.getRuntime().exec(command);
            BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));

            String result = reader.readLine();
            process.waitFor();

            return result;
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
            Thread.currentThread().interrupt();
            return null;
        }
    }

}
