/**
 * ﻿Copyright 2012, Deutsche Telekom AG, DTAG GHS GIS. All rights reserved.
 */

package de.theo.pg.provingground.dto;

import de.theo.pg.provingground.TestResult;

import java.time.Duration;

/**
 * TODO describe type.
 *
 * @author Andreas Janning andreas.janning@qaware.de
 */
public class TestRunDetailsView {

    private final long id;
    private final long testSuiteId;
    private final String name;
    private final TestResult result;
    private final Duration duration;
    private final String output;
    private final String errorType;
    private final String errorMessage;
    private final String stacktrace;

    public TestRunDetailsView(long id, long testSuiteId, String name, TestResult result, Duration duration, String output, String errorType, String errorMessage, String stacktrace) {
        this.id = id;
        this.testSuiteId = testSuiteId;
        this.name = name;
        this.result = result;
        this.duration = duration;
        this.output = output;
        this.errorType = errorType;
        this.errorMessage = errorMessage;
        this.stacktrace = stacktrace;
    }

    public long getId() {
        return id;
    }

    public long getTestSuiteId() {
        return testSuiteId;
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

    public Duration getDuration() {
        return duration;
    }
}
