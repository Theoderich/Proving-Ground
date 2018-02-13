/**
 * ﻿Copyright 2012, Deutsche Telekom AG, DTAG GHS GIS. All rights reserved.
 */

package de.qaware.pg.dto;

/**
 * TODO describe type.
 *
 * @author Andreas Janning andreas.janning@qaware.de
 */
public class TestView {

    private final long id;
    private final String name;
    private final Long lastRun;
    private final Long lastSuccess;

    public TestView(long id, String name, Long lastRun, Long lastSuccess) {
        this.id = id;
        this.name = name;
        this.lastRun = lastRun;
        this.lastSuccess = lastSuccess;
    }

    public long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public Long getLastRun() {
        return lastRun;
    }

    public Long getLastSuccess() {
        return lastSuccess;
    }
}
