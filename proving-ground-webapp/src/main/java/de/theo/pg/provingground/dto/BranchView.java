/**
 * ﻿Copyright 2012, Deutsche Telekom AG, DTAG GHS GIS. All rights reserved.
 */

package de.theo.pg.provingground.dto;

/**
 * TODO describe type.
 *
 * @author Andreas Janning andreas.janning@qaware.de
 */
public class BranchView {

    private final long branchId;
    private final long projectId;
    private final String name;
    private final Status status;


    public BranchView(long branchId, long projectId, String name, Status status) {
        this.branchId = branchId;
        this.name = name;
        this.projectId = projectId;
        this.status = status;
    }


    public long getBranchId() {
        return branchId;
    }

    public long getProjectId() {
        return projectId;
    }

    public String getName() {
        return name;
    }

    public Status getStatus() {
        return status;
    }
}
