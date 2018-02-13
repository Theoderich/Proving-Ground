/**
 * ﻿Copyright 2012, Deutsche Telekom AG, DTAG GHS GIS. All rights reserved.
 */

package de.qaware.pg.info;

/**
 * TODO describe type.
 *
 * @author Andreas Janning andreas.janning@qaware.de
 */
public class EmptyExecutionInfo implements ExecutionInfo {
    @Override
    public String getErrorMessage() {
        return null;
    }

    @Override
    public String getErrorType() {
        return null;
    }

    @Override
    public String getStackTrace() {
        return null;
    }

    @Override
    public String getStandardOut() {
        return null;
    }

    @Override
    public String getStandardErr() {
        return null;
    }

    @Override
    public boolean hasErrorInfo() {
        return false;
    }
}
