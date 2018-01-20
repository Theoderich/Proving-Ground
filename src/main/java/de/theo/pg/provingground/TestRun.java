package de.theo.pg.provingground;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

public class TestRun implements Comparable<TestRun> {

    private final long index;

    private final LocalDateTime start;

    private final Set<TestExecution> testExecutions;
    private final Set<TestExecution> failedTests;

    private TestRunResult result;


    public TestRun(long index, LocalDateTime start) {
        this.index = index;
        this.start = start;
        this.result = TestRunResult.SUCCESS;

        testExecutions = new HashSet<>();
        failedTests = new HashSet<>();
    }

    public Set<TestExecution> getTestExecutions() {
        return Collections.unmodifiableSet(testExecutions);
    }

    public List<TestExecution> getSortedTestExecutions(boolean failedOnly) {
        Set<TestExecution> source;
        if (failedOnly) {
            source = failedTests;
        } else {
            source = testExecutions;
        }
        return source.stream().sorted(Comparator.comparing(execution -> execution.getTest().getFullName())).collect(Collectors.toList());
    }

    public LocalDateTime getStart() {
        return start;
    }

    public long getIndex() {
        return index;
    }

    public TestRunResult getResult() {
        return result;
    }

    public void addExecution(TestExecution newExecution) {
        this.testExecutions.add(newExecution);
        if(newExecution.getResult().isFailure()){
            this.result = TestRunResult.FAILED;
            failedTests.add(newExecution);
        }
    }

    public void addExecutions(Collection<TestExecution> newExecutions) {
        newExecutions.forEach(this::addExecution);
    }

    public Set<Test> getAllExecutedTests() {
        return testExecutions.stream().map(TestExecution::getTest).collect(Collectors.toSet());
    }

    public Set<TestExecution> getFailedTests() {
        return Collections.unmodifiableSet(failedTests);
    }

    @Override
    public int compareTo(TestRun other) {
        return Long.compare(this.index, other.index);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        TestRun testRun = (TestRun) o;
        return index == testRun.index &&
                Objects.equals(start, testRun.start) &&
                Objects.equals(testExecutions, testRun.testExecutions) &&
                result == testRun.result;
    }

    @Override
    public int hashCode() {

        return Objects.hash(index, start, testExecutions, result);
    }

    @Override
    public String toString() {
        return "TestRun{" +
                "index=" + index +
                ", start=" + start +
                ", testExecutions=" + testExecutions +
                ", result=" + result +
                '}';
    }
}
