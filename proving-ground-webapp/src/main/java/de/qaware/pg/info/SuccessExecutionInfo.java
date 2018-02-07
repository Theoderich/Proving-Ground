package de.qaware.pg.info;

public class SuccessExecutionInfo implements ExecutionInfo {

    private final String stdOut;
    private final String stdErr;


    public SuccessExecutionInfo(String stdOut, String stdErr) {
        this.stdOut = stdOut;
        this.stdErr = stdErr;
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

    @Override
    public String getStandardErr() {
        return stdErr;
    }

    @Override
    public boolean hasErrorInfo() {
        return false;
    }
}
