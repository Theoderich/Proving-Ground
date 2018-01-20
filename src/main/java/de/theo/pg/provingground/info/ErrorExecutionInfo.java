package de.theo.pg.provingground.info;

public class ErrorExecutionInfo implements ExecutionInfo {

    private String errorMessage;
    private String exceptionType;
    private String log;

    public ErrorExecutionInfo(String errorMessage, String exceptionType, String log) {
        this.errorMessage = errorMessage;
        this.exceptionType = exceptionType;
        this.log = log;
    }
}
