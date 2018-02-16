package de.qaware.pg.gradle

import org.gradle.api.Project
import org.gradle.testfixtures.ProjectBuilder
import org.junit.Test

import static org.junit.Assert.assertTrue

class ProvingGroundPluginTest {

    @Test
    public void greeterPluginAddsGreetingTaskToProject() {
        Project project = ProjectBuilder.builder().build()
        project.pluginManager.apply 'de.qaware.pg.proving-ground'

        assertTrue(project.tasks.report instanceof ProvingGroundReportTask)
    }


}
