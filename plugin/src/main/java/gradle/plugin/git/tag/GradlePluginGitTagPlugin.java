package gradle.plugin.git.tag;

import gradle.plugin.git.tag.task.GitTagTask;
import org.gradle.api.Project;
import org.gradle.api.Plugin;

/**
 * A simple 'hello world' plugin.
 */
public class GradlePluginGitTagPlugin implements Plugin<Project> {
    public void apply(Project project) {
        // Register a task
        project.getTasks().register("gitTask", GitTagTask.class);
    }
}
