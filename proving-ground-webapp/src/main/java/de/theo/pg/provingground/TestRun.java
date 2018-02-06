package de.theo.pg.provingground;

import de.theo.pg.provingground.info.ExecutionInfo;

import java.time.Duration;
import java.util.Objects;

public class TestRun {

    private final Test test;
    private final TestResult result;
    private final Duration executionTime;

    private final ExecutionInfo executionInfo;


    public TestRun(Test test, TestResult result, Duration executionTime, ExecutionInfo executionInfo) {
        this.test = test;
        this.result = result;
        this.executionTime = executionTime;
        this.executionInfo = executionInfo;
    }

    public Test getTest() {
        return test;
    }

    public TestResult getResult() {
        return result;
    }

    public Duration getExecutionTime() {
        return executionTime;
    }

    public boolean hasErrorInfo() {
        return executionInfo.hasErrorInfo();
    }

    public ExecutionInfo getExecutionInfo() {
        return executionInfo;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        TestRun that = (TestRun) o;
        return Objects.equals(test, that.test) &&
                result == that.result &&
                Objects.equals(executionTime, that.executionTime) &&
                Objects.equals(executionInfo, that.executionInfo);
    }

    @Override
    public int hashCode() {

        return Objects.hash(test, result, executionTime, executionInfo);
    }

    @Override
    public String toString() {
        return "TestRun{" +
                "test=" + test.getFullName() +
                ", result=" + result +
                ", executionTime=" + executionTime +
                ", executionInfo=" + executionInfo +
                '}';
    }
}
