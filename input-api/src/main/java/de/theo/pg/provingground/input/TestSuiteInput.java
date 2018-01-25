package de.theo.pg.provingground.input;

import java.time.LocalDateTime;
import java.util.List;

public class TestSuiteInput {

    private String projectName;
    private String testSuiteName;
    private LocalDateTime startTime;

    private List<TestRunInput> testRuns;

    public String getProjectName() {
        return projectName;
    }

    public void setProjectName(String projectName) {
        this.projectName = projectName;
    }

    public String getTestSuiteName() {
        return testSuiteName;
    }

    public void setTestSuiteName(String testSuiteName) {
        this.testSuiteName = testSuiteName;
    }

    public LocalDateTime getStartTime() {
        return startTime;
    }

    public void setStartTime(LocalDateTime startTime) {
        this.startTime = startTime;
    }

    public List<TestRunInput> getTestRuns() {
        return testRuns;
    }

    public void setTestRuns(List<TestRunInput> testRuns) {
        this.testRuns = testRuns;
    }
}
