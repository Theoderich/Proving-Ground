package de.theo.pg.provingground.info;

public class ErrorExecutionInfo implements ExecutionInfo {

    private final String errorMessage;
    private final String exceptionType;
    private final String stackTrace;
    private final String standardOut;

    public ErrorExecutionInfo(String errorMessage, String exceptionType, String stackTrace, String standardOut) {
        this.errorMessage = errorMessage;
        this.exceptionType = exceptionType;
        this.stackTrace = stackTrace;
        this.standardOut = standardOut;
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
    public boolean hasErrorInfo() {
        return true;
    }
}
