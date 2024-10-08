package de.qaware.pg.maven.plugin;


import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import de.qaware.pg.input.BuildInput;
import de.qaware.pg.input.TestRunInput;
import de.qaware.pg.parse.surefire.JunitResultParser;
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
import java.util.Arrays;
import java.util.List;

@Mojo(name = "report", aggregator = true)
@Execute(phase = LifecyclePhase.NONE)
public class ReportTestsMojo extends AbstractMojo {


    @Parameter(property = "reportUrl", required = true)
    private String reportUrl;


    @Parameter(property = "projectName", required = true, defaultValue = "${project.name}")
    private String projectName;

    @Parameter(property = "buildName", required = true)
    private String buildName;

    @Parameter(property = "commitId", required = false)
    private String commitId;

    @Parameter(property = "branchName", required = true)
    private String branchName;

    @Parameter(required = true)
    private File[] reportsDirectories;

    @Parameter(defaultValue = "${reactorProjects}", readonly = true)
    private List<MavenProject> reactorProjects;

    @Parameter(defaultValue = "${project}", readonly = true)
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
                    getLog().info("Scanning directory " + directory);
                    List<TestRunInput> parse = parser.parse(directory.toPath());
                    allRuns.addAll(parse);
                } catch (IOException e) {
                    getLog().error("Could not scan directory " + directory, e);
                }

            }
            BuildInput suiteInput = new BuildInput();
            suiteInput.setProjectName(getProjectName());
            suiteInput.setTestSuiteName(getBuildName());
            suiteInput.setStartTime(LocalDateTime.now());
            suiteInput.setCommitIdentifier(commitId);
            suiteInput.setBranchName(branchName);
            suiteInput.setTestRuns(allRuns);


            getLog().info("Sending report to " + url);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("POST");
            conn.setRequestProperty("Content-Type", "application/json");
            conn.setDoOutput(true);
            ObjectMapper mapper = new ObjectMapper();
            mapper.registerModule(new JavaTimeModule());
            DataOutputStream wr = new DataOutputStream(conn.getOutputStream());
            mapper.writeValue((DataOutput) wr, suiteInput);
            wr.flush();
            wr.close();

            // 6. Get the response
            int responseCode = conn.getResponseCode();

            if (responseCode != 200) {
                throw new MojoExecutionException("Could not send report. Server responded with status code " + responseCode);
            }

        } catch (MalformedURLException e) {

            throw new MojoExecutionException("Invalid URL " + getReportUrl(), e);
        } catch (IOException e) {
            throw new MojoExecutionException("Error sending report", e);
        }
    }


    private List<File> getReportsDirectories() {
        if (resolvedReportsDirectories != null) {
            return resolvedReportsDirectories;
        }

        resolvedReportsDirectories = new ArrayList<>();
        getLog().info("Configured reportsDirectories: " + Arrays.toString(reportsDirectories));

        if (this.reportsDirectories != null) {
            for (File reportsDirectory : reportsDirectories) {
                addReportsDirIfExists(reportsDirectory);
            }
        }
        if (!project.isExecutionRoot()) {
            getLog().info("This project is not the execution root, skipping!");
            return null;
        }
        // Multiple report directories are configured.
        // Let's see if those directories exist in each sub-module to fix SUREFIRE-570
        String parentBaseDir = project.getBasedir().getAbsolutePath();
        for (MavenProject subProject : getProjectsWithoutRoot()) {
            String moduleBaseDir = subProject.getBasedir().getAbsolutePath();
            getLog().info("Found module: " + moduleBaseDir);
            for (File reportsDirectory1 : this.reportsDirectories) {
                String reportDir = reportsDirectory1.getPath();
                if (reportDir.startsWith(parentBaseDir)) {
                    reportDir = reportDir.substring(parentBaseDir.length());
                }
                File reportsDirectory = new File(moduleBaseDir, reportDir);
                addReportsDirIfExists(reportsDirectory);
            }
        }
        return resolvedReportsDirectories;
    }

    private void addReportsDirIfExists(File reportsDirectory) {
        if (reportsDirectory.exists() && reportsDirectory.isDirectory()) {
            getLog().info("Adding report dir : " + reportsDirectory);
            resolvedReportsDirectories.add(reportsDirectory);
        } else {
            getLog().info("Reports directory not found: " + reportsDirectory);
        }
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

    public String getBuildName() {
        return buildName;
    }

    public void setBuildName(String buildName) {
        this.buildName = buildName;
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

    public void setProject(MavenProject project) {
        this.project = project;
    }

    public String getCommitId() {
        return commitId;
    }

    public void setCommitId(String commitId) {
        this.commitId = commitId;
    }

    public String getBranchName() {
        return branchName;
    }

    public void setBranchName(String branchName) {
        this.branchName = branchName;
    }
}
