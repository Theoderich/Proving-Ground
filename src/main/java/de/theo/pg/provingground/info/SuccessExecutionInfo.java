package de.theo.pg.provingground.info;

public class SuccessExecutionInfo implements ExecutionInfo {

    private final String stdOut;

    public SuccessExecutionInfo(String stdOut) {
        this.stdOut = stdOut;
    }

    @Override
    public String getErrorMessage() {
        return "";
    }

    @Override
    public String getErrorType() {
        return "";
    }

    @Override
    public String getStackTrace() {
        return "";
    }

    @Override
    public String getStandardOut() {
        return stdOut;
    }
}
