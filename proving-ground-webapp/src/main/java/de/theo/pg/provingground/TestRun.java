package de.theo.pg.provingground;

import de.theo.pg.provingground.dto.Status;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

public class TestRun implements Comparable<TestRun> {

    private final long index;

    private final LocalDateTime start;

    private final Set<TestExecution> testExecutions;
    private final Set<TestExecution> successTests;
    private final Set<TestExecution> failedTests;
    private final Set<TestExecution> skippedTests;
    private final Map<String, TestExecution> testsByFullName;

    private Status status;


    public TestRun(long index, LocalDateTime start) {
        this.index = index;
        this.start = start;
        this.status = Status.OK;

        testExecutions = new HashSet<>();
        successTests = new HashSet<>();
        failedTests = new HashSet<>();
        skippedTests = new HashSet<>();
        testsByFullName = new HashMap<>();
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

    public TestExecution getTestExecution(String testname) {
        return testsByFullName.get(testname);
    }

    public LocalDateTime getStart() {
        return start;
    }

    public long getIndex() {
        return index;
    }

    public Status getStatus() {
        return status;
    }

    public void addExecution(TestExecution newExecution) {
        this.testExecutions.add(newExecution);
        if(newExecution.getResult().isFailure()){
            this.status = Status.NOK;
            failedTests.add(newExecution);
        }
         else if(newExecution.getResult() == TestResult.SKIPPED){
            skippedTests.add(newExecution);
        } else {
            successTests.add(newExecution);
        }
        this.testsByFullName.put(newExecution.getTest().getFullName(), newExecution);
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


    public int getTotalNumberOfTests(){
        return testExecutions.size();
    }

    public int getNumberOfSuccessfulTests(){
        return successTests.size();
    }

    public int getNumberOfFailedTests() {
        return failedTests.size();
    }

    public int getNumberOfSkippedTests() {
        return skippedTests.size();
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
                status == testRun.status;
    }

    @Override
    public int hashCode() {

        return Objects.hash(index, start, testExecutions, status);
    }

    @Override
    public String toString() {
        return "TestRun{" +
                "index=" + index +
                ", start=" + start +
                ", testExecutions=" + testExecutions +
                ", status=" + status +
                '}';
    }
}
