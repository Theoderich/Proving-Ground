package de.theo.pg.provingground.input;

import de.theo.pg.provingground.TestResult;

import java.time.Duration;

public class TestRunInput {
    private String name;
    private TestResult result;
    private Duration duration;
    private String output;
    private String errorType;
    private String errorMessage;
    private String stacktrace;


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public TestResult getResult() {
        return result;
    }

    public void setResult(TestResult result) {
        this.result = result;
    }

    public Duration getDuration() {
        return duration;
    }

    public void setDuration(Duration duration) {
        this.duration = duration;
    }

    public String getOutput() {
        return output;
    }

    public void setOutput(String output) {
        this.output = output;
    }

    public String getErrorType() {
        return errorType;
    }

    public void setErrorType(String errorType) {
        this.errorType = errorType;
    }

    public String getErrorMessage() {
        return errorMessage;
    }

    public void setErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
    }

    public String getStacktrace() {
        return stacktrace;
    }

    public void setStacktrace(String stacktrace) {
        this.stacktrace = stacktrace;
    }
}
