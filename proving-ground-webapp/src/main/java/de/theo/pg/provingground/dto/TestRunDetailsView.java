/**
 * ﻿Copyright 2012, Deutsche Telekom AG, DTAG GHS GIS. All rights reserved.
 */

package de.theo.pg.provingground.dto;

import de.theo.pg.provingground.TestResult;

/**
 * TODO describe type.
 *
 * @author Andreas Janning andreas.janning@qaware.de
 */
public class TestRunDetailsView {

    private String name;
    private TestResult result;
    private String output;
    private String errorType;
    private String errorMessage;
    private String stacktrace;

    public TestRunDetailsView(String name, TestResult result, String output, String errorType, String errorMessage, String stacktrace) {
        this.name = name;
        this.result = result;
        this.output = output;
        this.errorType = errorType;
        this.errorMessage = errorMessage;
        this.stacktrace = stacktrace;
    }

    public String getName() {
        return name;
    }

    public TestResult getResult() {
        return result;
    }

    public String getOutput() {
        return output;
    }

    public String getErrorType() {
        return errorType;
    }

    public String getErrorMessage() {
        return errorMessage;
    }

    public String getStacktrace() {
        return stacktrace;
    }
}
