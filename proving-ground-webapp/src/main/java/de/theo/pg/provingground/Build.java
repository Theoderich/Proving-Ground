package de.theo.pg.provingground;

import de.theo.pg.provingground.dto.Status;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

public class Build implements Comparable<Build> {

    private final LocalDateTime start;
    private final String name;
    private final String commitId;

    private final Set<TestRun> allTestRuns;
    private final Set<TestRun> successTestRuns;
    private final Set<TestRun> failedTestRuns;
    private final Set<TestRun> skippedTestRuns;
    private final Map<String, TestRun> testRunsByFullName;

    private Status status;
    private Branch branch;


    public Build(LocalDateTime start, String name, String commitId) {
        this.start = start;
        this.name = name;
        this.commitId = commitId;

        this.status = Status.OK;

        allTestRuns = new HashSet<>();
        successTestRuns = new HashSet<>();
        failedTestRuns = new HashSet<>();
        skippedTestRuns = new HashSet<>();
        testRunsByFullName = new HashMap<>();
    }

    public Set<TestRun> getAllTestRuns() {
        return Collections.unmodifiableSet(allTestRuns);
    }

    public List<TestRun> getSortedTestRuns(boolean failedOnly) {
        Set<TestRun> source;
        if (failedOnly) {
            source = failedTestRuns;
        } else {
            source = allTestRuns;
        }
        return source.stream().sorted(Comparator.comparing(execution -> execution.getTest().getFullName())).collect(Collectors.toList());
    }

    public TestRun getTestRunForTest(String testName) {
        return testRunsByFullName.get(testName);
    }

    public LocalDateTime getStart() {
        return start;
    }

    public Status getStatus() {
        return status;
    }

    public void addTestRun(TestRun newTestRun) {
        this.allTestRuns.add(newTestRun);
        if (newTestRun.getResult().isFailure()) {
            this.status = Status.NOK;
            failedTestRuns.add(newTestRun);
        } else if (newTestRun.getResult() == TestResult.SKIPPED) {
            skippedTestRuns.add(newTestRun);
        } else {
            successTestRuns.add(newTestRun);
        }
        this.testRunsByFullName.put(newTestRun.getTest().getFullName(), newTestRun);
    }

    public void addTestRuns(Collection<TestRun> newTestRuns) {
        newTestRuns.forEach(this::addTestRun);
    }

    public Set<Test> getAllTests() {
        return allTestRuns.stream().map(TestRun::getTest).collect(Collectors.toSet());
    }

    public Set<TestRun> getFailedTestRuns() {
        return Collections.unmodifiableSet(failedTestRuns);
    }

    public String getName() {
        return name;
    }

    public int getTotalNumberOfTests(){
        return allTestRuns.size();
    }

    public int getNumberOfSuccessfulTests(){
        return successTestRuns.size();
    }

    public int getNumberOfFailedTests() {
        return failedTestRuns.size();
    }

    public int getNumberOfSkippedTests() {
        return skippedTestRuns.size();
    }

    public String getCommitId() {
        return commitId;
    }


    public Branch getBranch() {
        return branch;
    }

    public void setBranch(Branch branch) {
        this.branch = branch;
    }

    @Override
    public int compareTo(Build other) {
        return this.start.compareTo(other.start);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Build build = (Build) o;
        return Objects.equals(name, build.name);
    }

    @Override
    public int hashCode() {

        return Objects.hash(name);
    }

    @Override
    public String toString() {
        return "Build{" +
                "start=" + start +
                ", name='" + name + '\'' +
                ", commitId='" + commitId + '\'' +
                ", allTestRuns=" + allTestRuns +
                ", successTestRuns=" + successTestRuns +
                ", failedTestRuns=" + failedTestRuns +
                ", skippedTestRuns=" + skippedTestRuns +
                ", testRunsByFullName=" + testRunsByFullName +
                ", status=" + status +
                '}';
    }
}
