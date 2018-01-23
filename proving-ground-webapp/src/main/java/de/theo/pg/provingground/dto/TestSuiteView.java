/**
 * ﻿Copyright 2012, Deutsche Telekom AG, DTAG GHS GIS. All rights reserved.
 */

package de.theo.pg.provingground.dto;

import java.time.LocalDateTime;

/**
 * TODO describe type.
 *
 * @author Andreas Janning andreas.janning@qaware.de
 */
public class TestSuiteView {

    private int id;
    private String name;
    private LocalDateTime startTime;
    private Status status;
    private int num_total;
    private int num_success;
    private int num_failed;
    private int num_error;
    private int num_skipped;

    public TestSuiteView(int id, String name, LocalDateTime startTime, Status status, int num_total, int num_success, int num_failed, int num_error, int num_skipped) {
        this.id = id;
        this.name = name;
        this.startTime = startTime;
        this.status = status;
        this.num_total = num_total;
        this.num_success = num_success;
        this.num_failed = num_failed;
        this.num_error = num_error;
        this.num_skipped = num_skipped;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public LocalDateTime getStartTime() {
        return startTime;
    }

    public Status getStatus() {
        return status;
    }

    public int getNum_total() {
        return num_total;
    }

    public int getNum_success() {
        return num_success;
    }

    public int getNum_failed() {
        return num_failed;
    }

    public int getNum_error() {
        return num_error;
    }

    public int getNum_skipped() {
        return num_skipped;
    }
}
