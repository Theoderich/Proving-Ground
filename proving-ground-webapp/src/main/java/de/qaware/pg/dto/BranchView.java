/**
 * ﻿Copyright 2012, Deutsche Telekom AG, DTAG GHS GIS. All rights reserved.
 */

package de.qaware.pg.dto;

import de.qaware.pg.EncodingUtil;

/**
 * TODO describe type.
 *
 * @author Andreas Janning andreas.janning@qaware.de
 */
public class BranchView implements NavigationPart {

    private final long id;
    private final long projectId;
    private final String name;
    private final Status status;


    public BranchView(long id, long projectId, String name, Status status) {
        this.id = id;
        this.name = name;
        this.projectId = projectId;
        this.status = status;
    }


    public long getId() {
        return id;
    }

    @Override
    public String getLinkId() {
        return EncodingUtil.encode(name);
    }

    public long getProjectId() {
        return projectId;
    }

    @Override
    public String getName() {
        return name;
    }

    public Status getStatus() {
        return status;
    }
}
