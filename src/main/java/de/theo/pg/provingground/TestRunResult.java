package de.theo.pg.provingground;

public enum TestRunResult {

    SUCCESS,
    FAILED;


    public boolean isSuccess(){
        return this == SUCCESS;
    }




}
