package de.theo.pg.provingground.info;

public class FailedExecutionInfo implements ExecutionInfo {

    private String message;
    private String log;

    public FailedExecutionInfo(String message, String log) {
        this.message = message;
        this.log = log;
    }
}
