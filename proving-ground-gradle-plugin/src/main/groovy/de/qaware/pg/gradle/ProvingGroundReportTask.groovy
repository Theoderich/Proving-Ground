package de.qaware.pg.gradle

import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule
import de.qaware.pg.input.BuildInput
import de.qaware.pg.input.TestRunInput
import de.qaware.pg.parse.surefire.JunitResultParser
import org.gradle.api.DefaultTask
import org.gradle.api.GradleException
import org.gradle.api.InvalidUserDataException
import org.gradle.api.Project
import org.gradle.api.tasks.TaskAction
import org.gradle.api.tasks.testing.Test

import java.time.LocalDateTime

class ProvingGroundReportTask extends DefaultTask {

    String reportUrl;

    String projectName = project.name;

    String buildName = System.getProperty("provingGround.buildName");

    String commitId = System.getProperty("provingGround.commitId", "");

    String branchName = System.getProperty("provingGround.branchName"); ;


    @TaskAction
    def report() {
        checkRequiredParameter(reportUrl, "reportUrl")
        checkRequiredParameter(projectName, "projectName")
        checkRequiredParameter(buildName, "buildName")
        checkRequiredParameter(branchName, "branchName")

        try {
            URL url = new URL(reportUrl)


            logger.quiet("Proving Ground Plugin started!")

            ArrayList<File> reportDestinations = extract(project)

            project.subprojects.each { subProject ->
                reportDestinations.addAll(extract(subProject))
            }
            reportDestinations.forEach({ dest -> logger.quiet(dest.path) })

            def surefireScanner = new JunitResultParser()
            List<TestRunInput> allRuns = new ArrayList<>();

            reportDestinations.each { reportPath ->
                if (reportPath.exists()) {
                    logger.quiet("Scanning report directory: " + reportPath)
                    allRuns.addAll(surefireScanner.parse(reportPath.toPath()))
                }
            }

            BuildInput suiteInput = new BuildInput()
            suiteInput.setProjectName(projectName)
            suiteInput.setTestSuiteName(buildName)
            suiteInput.setStartTime(LocalDateTime.now())
            suiteInput.setCommitIdentifier(commitId)
            suiteInput.setBranchName(branchName)
            suiteInput.setTestRuns(allRuns)

            logger.quiet("Sending report to " + url)


            HttpURLConnection conn = (HttpURLConnection) url.openConnection()
            conn.setRequestMethod("POST")
            conn.setRequestProperty("Content-Type", "application/json")
            conn.setDoOutput(true)
            ObjectMapper mapper = new ObjectMapper()
            mapper.registerModule(new JavaTimeModule())
            DataOutputStream wr = new DataOutputStream(conn.getOutputStream())
            mapper.writeValue((DataOutput) wr, suiteInput)
            wr.flush()
            wr.close()

            int responseCode = conn.getResponseCode()

            if (responseCode != 200) {
                throw new GradleException("Could not send report. Server responded with status code " + responseCode)
            }
        } catch (MalformedURLException e) {
            throw new InvalidUserDataException("The provided reportUrl " + reportUrl + " is invalid", e)
        }
    }

    private static void checkRequiredParameter(String param, String paramName) {
        if (param == null || param.isEmpty()) {
            throw new InvalidUserDataException("Required Parameter " + paramName + " is missing")
        }
    }

    ArrayList<File> extract(Project curProject) {
        logger.info("Processing Project " + curProject)
        def testTasks = curProject.tasks.withType(Test.class)
        def reportDestinations = new ArrayList<File>()
        testTasks.all { task -> reportDestinations.add(task.reports.junitXml.destination) }
        logger.info("Found report destinations:  " + reportDestinations)
        reportDestinations
    }

}
