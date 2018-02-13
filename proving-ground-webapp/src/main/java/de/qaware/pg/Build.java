package de.qaware.pg;

import de.qaware.pg.dto.BuildView;
import de.qaware.pg.dto.Status;
import de.qaware.pg.dto.TestRunView;
import de.qaware.pg.persistence.Persistence;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

public class Build implements Comparable<Build> {

    private Long databaseId;

    private final LocalDateTime start;
    private final String name;
    private final String commitId;

    private final Set<TestRun> allTestRuns;
    private final Set<TestRun> successTestRuns;
    private final Set<TestRun> failedTestRuns;
    private final Set<TestRun> skippedTestRuns;
    private final Map<String, TestRun> testRunsByFullName;

    private Status status;

    public Build(Long databaseId, LocalDateTime start, String name, String commitId) {
        this(start, name, commitId);
        this.databaseId = databaseId;
    }

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
        return source.stream().sorted(Comparator.comparing(execution -> execution.getTestName())).collect(Collectors.toList());
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
        this.testRunsByFullName.put(newTestRun.getTestName(), newTestRun);
    }

    public void addTestRuns(Collection<TestRun> newTestRuns) {
        newTestRuns.forEach(this::addTestRun);
    }

    public Set<Test> getAllTests() {
        return allTestRuns.stream().map(TestRun::getTestName).map(Test::new).collect(Collectors.toSet());
    }

    public boolean hasRunForTest(Test test) {
        String testName = test.getName();
        return this.testRunsByFullName.containsKey(testName);
    }

    public boolean hasSuccessfulRunForTest(Test test) {
        String testName = test.getName();
        TestRun testRun = this.testRunsByFullName.get(testName);
        if (testRun == null) {
            return false;
        }
        return testRun.getResult().isSuccess();
    }

    public TestRun getRunForTest(Test test) {
        return testRunsByFullName.get(test.getName());
    }

    public Set<TestRun> getFailedTestRuns() {
        return Collections.unmodifiableSet(failedTestRuns);
    }

    public String getName() {
        return name;
    }

    public int getTotalNumberOfTests() {
        return allTestRuns.size();
    }

    public int getNumberOfSuccessfulTests() {
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


    public Long getDatabaseId() {
        return databaseId;
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
                "databaseId=" + databaseId +
                ", start=" + start +
                ", name='" + name + '\'' +
                ", commitId='" + commitId + '\'' +
                '}';
    }

    public static Build loadFromPersistence(Persistence persistence, BuildView buildView) {
        try {
            Build build = new Build(buildView.getId(), buildView.getStartTime(), buildView.getName(), buildView.getCommitId());
            List<TestRunView> testRunViews = persistence.listTestRunsForBuild(buildView.getId());
            testRunViews.stream().map(testRunView -> TestRun.loadFromPersistence(persistence, testRunView)).forEach(build::addTestRun);
            return build;
        } catch (ElementNotFoundException e) {
            throw new RuntimeException("Unable to list TestRuns for build " + buildView, e);
        }
    }
}
