package de.qaware.pg.gradle

import org.gradle.api.Plugin
import org.gradle.api.Project

class ProvingGroundPlugin implements Plugin<Project> {

    @Override
    void apply(Project project) {
        project.task('proving-ground-report', type: ProvingGroundReportTask)
    }
}


