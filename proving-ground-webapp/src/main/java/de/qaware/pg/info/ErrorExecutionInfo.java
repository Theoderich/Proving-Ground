package de.qaware.pg.info;

public class ErrorExecutionInfo implements ExecutionInfo {

    private final String errorMessage;
    private final String exceptionType;
    private final String stackTrace;
    private final String standardOut;
    private final String standardErr;

    public ErrorExecutionInfo(String errorMessage, String exceptionType, String stackTrace, String standardOut, String standardErr) {
        this.errorMessage = errorMessage;
        this.exceptionType = exceptionType;
        this.stackTrace = stackTrace;
        this.standardOut = standardOut;
        this.standardErr = standardErr;
    }

    @Override
    public String getErrorMessage() {
        return errorMessage;
    }

    @Override
    public String getErrorType() {
        return exceptionType;
    }

    @Override
    public String getStackTrace() {
        return stackTrace;
    }

    @Override
    public String getStandardOut() {
        return standardOut;
    }

    @Override
    public String getStandardErr() {
        return standardErr;
    }

    @Override
    public boolean hasErrorInfo() {
        return true;
    }
}
