package de.qaware.pg;

import de.qaware.pg.dto.TestRunView;
import de.qaware.pg.info.EmptyExecutionInfo;
import de.qaware.pg.info.ExecutionInfo;
import de.qaware.pg.persistence.Persistence;

import java.time.Duration;
import java.util.Objects;

public class TestRun {

    private final String testName;
    private final TestResult result;
    private final Duration executionTime;

    private final ExecutionInfo executionInfo;


    public TestRun(String testName, TestResult result, Duration executionTime, ExecutionInfo executionInfo) {
        this.testName = testName;
        this.result = result;
        this.executionTime = executionTime;
        this.executionInfo = executionInfo;
    }

    public String getTestName() {
        return testName;
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
        return Objects.equals(testName, that.testName) &&
                result == that.result &&
                Objects.equals(executionTime, that.executionTime) &&
                Objects.equals(executionInfo, that.executionInfo);
    }

    @Override
    public int hashCode() {

        return Objects.hash(testName, result, executionTime, executionInfo);
    }

    @Override
    public String toString() {
        return "TestRun{" +
                "test=" + testName +
                ", result=" + result +
                ", executionTime=" + executionTime +
                ", executionInfo=" + executionInfo +
                '}';
    }

    public static TestRun loadFromPersistence(Persistence persistence, TestRunView testRunView) {
        return new TestRun(testRunView.getName(), testRunView.getResult(), testRunView.getDuration(), new EmptyExecutionInfo());
    }
}
