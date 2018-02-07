package de.qaware.pg.info;

public interface ExecutionInfo {


    String getErrorMessage();

    String getErrorType();

    String getStackTrace();

    String getStandardOut();

    String getStandardErr();

    boolean hasErrorInfo();
}
