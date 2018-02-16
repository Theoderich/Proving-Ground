/**
 * ﻿Copyright 2012, Deutsche Telekom AG, DTAG GHS GIS. All rights reserved.
 */

package de.qaware.pg.dto;

import de.qaware.pg.TestResult;

import java.time.Duration;

/**
 * TODO describe type.
 *
 * @author Andreas Janning andreas.janning@qaware.de
 */
public class TestRunDetailsView implements NavigationPart {

    private final long id;
    private final long buildId;
    private final String name;
    private final TestResult result;
    private final Duration duration;
    private final String output;
    private final String stdErr;
    private final String errorType;
    private final String errorMessage;
    private final String stacktrace;

    public TestRunDetailsView(long id, long buildId, String name, TestResult result, Duration duration, String output, String stdErr, String errorType, String errorMessage, String stacktrace) {
        this.id = id;
        this.buildId = buildId;
        this.name = name;
        this.result = result;
        this.duration = duration;
        this.output = output;
        this.stdErr = stdErr;
        this.errorType = errorType;
        this.errorMessage = errorMessage;
        this.stacktrace = stacktrace;
    }

    public long getId() {
        return id;
    }

    @Override
    public String getLinkId() {
        return Long.toString(id);
    }

    public long getBuildId() {
        return buildId;
    }

    @Override
    public String getName() {
        return name;
    }

    public TestResult getResult() {
        return result;
    }

    public String getOutput() {
        return output;
    }

    public String getStdErr() {
        return stdErr;
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
