package de.theo.pg.provingground.info;

public interface ExecutionInfo {


    String getErrorMessage();

    String getErrorType();

    String getStackTrace();

    String getStandardOut();

    String getStandardErr();

    boolean hasErrorInfo();
}
