package gradle.plugin.git.tag.util.factory;

import gradle.plugin.git.tag.util.CommandExecutor;

public class CommandExecutorSingleton {

    private CommandExecutorSingleton() {
        throw new IllegalStateException("Utility class");
    }

    private static volatile CommandExecutor instance;

    public static CommandExecutor getInstance() {
        if (instance == null) {
            synchronized (CommandExecutor.class) {
                if (instance == null) {
                    instance = new CommandExecutor();
                }
            }
        }
        return instance;
    }
}
