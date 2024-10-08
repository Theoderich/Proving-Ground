package de.qaware.pg.input;

import java.time.Duration;

public class TestRunInput {
    private String name;
    private TestResultInput result;
    private Duration duration;
    private String output;
    private String errorOutput;
    private String errorType;
    private String errorMessage;
    private String stacktrace;


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public TestResultInput getResult() {
        return result;
    }

    public void setResult(TestResultInput result) {
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

    public String getErrorOutput() {
        return errorOutput;
    }

    public void setErrorOutput(String errorOutput) {
        this.errorOutput = errorOutput;
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
