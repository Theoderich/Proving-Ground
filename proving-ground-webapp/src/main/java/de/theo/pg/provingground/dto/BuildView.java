/**
 * ﻿Copyright 2012, Deutsche Telekom AG, DTAG GHS GIS. All rights reserved.
 */

package de.theo.pg.provingground.dto;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.FormatStyle;

/**
 * TODO describe type.
 *
 * @author Andreas Janning andreas.janning@qaware.de
 */
public class BuildView implements NavigationPart {

    private final long id;
    private final long branchId;
    private final String name;
    private final LocalDateTime startTime;
    private final String commitId;
    private final Status status;
    private final int num_total;
    private final int num_success;
    private final int num_failed;
    private final int num_skipped;
    protected static final DateTimeFormatter DATE_TIME_FORMATTER = DateTimeFormatter.ofLocalizedDateTime(FormatStyle.SHORT, FormatStyle.SHORT);

    public BuildView(long id, long branchId, String name, LocalDateTime startTime, String commitId, Status status, int num_total, int num_success, int num_failed, int num_skipped) {
        this.id = id;
        this.branchId = branchId;
        this.name = name;
        this.startTime = startTime;
        this.commitId = commitId;
        this.status = status;
        this.num_total = num_total;
        this.num_success = num_success;
        this.num_failed = num_failed;
        this.num_skipped = num_skipped;
    }

    public long getId() {
        return id;
    }

    public long getBranchId() {
        return branchId;
    }

    public String getName() {
        return name;
    }

    public LocalDateTime getStartTime() {
        return startTime;
    }

    public String getStartTimeFormatted() {
        return startTime.format(DATE_TIME_FORMATTER);
    }

    public String getCommitId() {
        return commitId;
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

    public int getNum_skipped() {
        return num_skipped;
    }


}
