package de.theo.pg.maven.plugin;


import com.fasterxml.jackson.databind.ObjectMapper;
import de.theo.pg.provingground.input.TestRunInput;
import de.theo.pg.provingground.input.TestSuiteInput;
import de.theo.pg.provingground.parse.surefire.JunitResultParser;
import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;
import org.apache.maven.plugins.annotations.Execute;
import org.apache.maven.plugins.annotations.LifecyclePhase;
import org.apache.maven.plugins.annotations.Mojo;
import org.apache.maven.plugins.annotations.Parameter;
import org.apache.maven.project.MavenProject;

import java.io.DataOutput;
import java.io.DataOutputStream;
import java.io.File;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Mojo(name = "report")
@Execute(phase = LifecyclePhase.NONE)
public class ReportTestsMojo extends AbstractMojo {


    @Parameter(property = "reportUrl", required = true)
    private String reportUrl;


    @Parameter(property = "projectName", required = true, defaultValue = "${project.name}")
    private String projectName;

    @Parameter(property = "runName", required = true)
    private String runName;

    @Parameter(required = true)
    private File[] reportsDirectories;

    @Parameter(defaultValue = "${reactorProjects}", readonly = true)
    private List<MavenProject> reactorProjects;

    @Parameter(defaultValue = "${reactorProjects}", readonly = true)
    private MavenProject project;

    private List<File> resolvedReportsDirectories;

    @Override
    public void execute() throws MojoExecutionException, MojoFailureException {

        try {
            URL url = new URL(getReportUrl());


            List<File> directories = getReportsDirectories();

            if (directories == null || directories.isEmpty()) {
                getLog().info("No reportsDirectory defined. Skipping");
                return;
            }

            JunitResultParser parser = new JunitResultParser();
            List<TestRunInput> allRuns = new ArrayList<>();
            for (File directory : directories) {
                try {
                    List<TestRunInput> parse = parser.parse(directory.toPath());
                    allRuns.addAll(parse);
                } catch (IOException e) {
                    getLog().error("Could not scan directory " + directory, e);
                }

            }
            TestSuiteInput suiteInput = new TestSuiteInput();
            suiteInput.setProjectName(getProjectName());
            suiteInput.setTestSuiteName(getRunName());
            suiteInput.setStartTime(LocalDateTime.now());
            suiteInput.setTestRuns(allRuns);


            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("POST");
            conn.setRequestProperty("Content-Type", "application/json");
            conn.setDoOutput(true);
            ObjectMapper mapper = new ObjectMapper();
            DataOutputStream wr = new DataOutputStream(conn.getOutputStream());
            mapper.writeValue((DataOutput) wr, suiteInput);
            wr.flush();
            wr.close();

            // 6. Get the response
            int responseCode = conn.getResponseCode();

            if (responseCode != 200) {
                getLog().error("Could not send report. Server responded with status code " + responseCode);
            }

        } catch (MalformedURLException e) {
            getLog().error("Invalid URL " + getReportUrl(), e);
        } catch (IOException e) {
            getLog().error("Could not send report", e);
        }
    }


    private List<File> getReportsDirectories() {
        if (resolvedReportsDirectories != null) {
            return resolvedReportsDirectories;
        }

        resolvedReportsDirectories = new ArrayList<File>();

        if (this.reportsDirectories != null) {
            Collections.addAll(resolvedReportsDirectories, this.reportsDirectories);
        }
        if (!project.isExecutionRoot()) {
            return null;
        }
        // Multiple report directories are configured.
        // Let's see if those directories exist in each sub-module to fix SUREFIRE-570
        String parentBaseDir = project.getBasedir().getAbsolutePath();
        for (MavenProject subProject : getProjectsWithoutRoot()) {
            String moduleBaseDir = subProject.getBasedir().getAbsolutePath();
            for (File reportsDirectory1 : this.reportsDirectories) {
                String reportDir = reportsDirectory1.getPath();
                if (reportDir.startsWith(parentBaseDir)) {
                    reportDir = reportDir.substring(parentBaseDir.length());
                }
                File reportsDirectory = new File(moduleBaseDir, reportDir);
                if (reportsDirectory.exists() && reportsDirectory.isDirectory()) {
                    getLog().debug("Adding report dir : " + moduleBaseDir + reportDir);
                    resolvedReportsDirectories.add(reportsDirectory);
                }
            }
        }
        return resolvedReportsDirectories;
    }

    private List<MavenProject> getProjectsWithoutRoot() {
        List<MavenProject> result = new ArrayList<>();
        for (MavenProject subProject : reactorProjects) {
            if (!project.equals(subProject)) {
                result.add(subProject);
            }
        }
        return result;

    }


    public String getReportUrl() {
        return reportUrl;
    }

    public void setReportUrl(String reportUrl) {
        this.reportUrl = reportUrl;
    }

    public String getProjectName() {
        return projectName;
    }

    public void setProjectName(String projectName) {
        this.projectName = projectName;
    }

    public String getRunName() {
        return runName;
    }

    public void setRunName(String runName) {
        this.runName = runName;
    }


    public void setReportsDirectories(File[] reportsDirectories) {
        this.reportsDirectories = reportsDirectories;
    }

    public List<MavenProject> getReactorProjects() {
        return reactorProjects;
    }

    public void setReactorProjects(List<MavenProject> reactorProjects) {
        this.reactorProjects = reactorProjects;
    }
}
