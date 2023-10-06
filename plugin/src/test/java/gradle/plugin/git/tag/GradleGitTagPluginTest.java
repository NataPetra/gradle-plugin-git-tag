package gradle.plugin.git.tag;

import org.gradle.api.Project;
import org.gradle.testfixtures.ProjectBuilder;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertNotNull;

class GradleGitTagPluginTest {
    @Test
    void pluginRegistersATask() {
        // Create a test project and apply the plugin
        Project project = ProjectBuilder.builder().build();
        project.getPlugins().apply("gradle.plugin.git.tag.gitTask");

        // Verify the result
        assertNotNull(project.getTasks().findByName("gitTask"));
    }
}
