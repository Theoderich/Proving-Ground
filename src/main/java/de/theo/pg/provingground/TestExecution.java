package de.theo.pg.provingground;

import de.theo.pg.provingground.info.ExecutionInfo;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.Objects;

public class TestExecution {

    private final Test test;
    private final TestResult result;
    private final Duration executionTime;

    private final ExecutionInfo executionInfo;


    public TestExecution(Test test, TestResult result, Duration executionTime, ExecutionInfo executionInfo) {
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        TestExecution that = (TestExecution) o;
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
        return "TestExecution{" +
                "test=" + test.getFullName() +
                ", result=" + result +
                ", executionTime=" + executionTime +
                ", executionInfo=" + executionInfo +
                '}';
    }
}
