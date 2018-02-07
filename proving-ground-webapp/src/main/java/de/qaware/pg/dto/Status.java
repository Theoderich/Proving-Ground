package de.qaware.pg.dto;

/**
 * Created by a theo on 22.01.2018.
 */
public enum Status {
    OK,
    NOK;

    public boolean isOk(){
        return this == OK;
    }
}
