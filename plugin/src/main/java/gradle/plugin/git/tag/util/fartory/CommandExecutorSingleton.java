package gradle.plugin.git.tag.util.fartory;

import gradle.plugin.git.tag.util.CommandExecutor;

public class CommandExecutorSingleton {

    private volatile static CommandExecutor instance;

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
