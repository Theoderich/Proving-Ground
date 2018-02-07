package de.qaware.pg;

public enum TestRunResult {

    SUCCESS,
    FAILED;


    public boolean isSuccess(){
        return this == SUCCESS;
    }




}
