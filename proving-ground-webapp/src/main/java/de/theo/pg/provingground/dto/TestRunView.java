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
public class TestRunView {

    private final long id;
    private final long testSuiteId;
    private final String name;
    private final TestResult result;
    private final Duration duration;

    public TestRunView(long id, long testSuiteId, String name, TestResult result, Duration duration) {
        this.id = id;
        this.testSuiteId = testSuiteId;
        this.name = name;
        this.result = result;
        this.duration = duration;
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

    public Duration getDuration() {
        return duration;
    }
}
