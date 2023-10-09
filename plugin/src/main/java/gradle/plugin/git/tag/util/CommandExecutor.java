package gradle.plugin.git.tag.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class CommandExecutor {

    private static final Logger log = LoggerFactory.getLogger(CommandExecutor.class);

    public void executeGitCommand(String... commands) {
        try {
            List<String> commandList = new ArrayList<>(List.of("git"));
            commandList.addAll(Arrays.asList(commands));

            ProcessBuilder processBuilder = new ProcessBuilder(commandList);
            processBuilder.redirectErrorStream(true);

            Process process = processBuilder.start();
            BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));

            String line;
            while ((line = reader.readLine()) != null) {
                log.info(line);
            }

            int exitCode = process.waitFor();
            if (exitCode != 0) {
                log.error("Command execution error: {}", commandList.get(1));
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
