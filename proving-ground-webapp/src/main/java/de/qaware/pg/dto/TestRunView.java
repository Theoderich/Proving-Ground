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
public class TestRunView {

    private final long id;
    private final long testSuiteId;
    private final Long lastSuccessBuildId;
    private final String lastSuccessBuildName;
    private final String name;
    private final TestResult result;
    private final Duration duration;


    public TestRunView(long id, long testSuiteId, Long lastSuccessBuildId, String lastSuccessBuildName, String name, TestResult result, Duration duration) {
        this.id = id;
        this.testSuiteId = testSuiteId;
        this.lastSuccessBuildId = lastSuccessBuildId;
        this.lastSuccessBuildName = lastSuccessBuildName;
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

    public Long getLastSuccessBuildId() {
        return lastSuccessBuildId;
    }

    public boolean hasSuccessfulBuild() {
        return lastSuccessBuildId != null;
    }

    public String getLastSuccessBuildName() {
        return lastSuccessBuildName;
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

    public String getFormattedDuration() {
        if (duration.toMinutes() > 0) {
            long secondsLeft = duration.getSeconds() % 60;
            return duration.toMinutes() + "min " + secondsLeft + "s";
        }
        if (duration.getSeconds() > 0) {
            long milliesLeft = duration.toMillis() % 1000;
            return duration.getSeconds() + "s " + milliesLeft + "ms";
        }
        return duration.toMillis() + "ms";
    }
}
