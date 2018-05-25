/**
 * ﻿Copyright 2012, Deutsche Telekom AG, DTAG GHS GIS. All rights reserved.
 */

package de.qaware.pg.dto;

/**
 * TODO describe type.
 *
 * @author Andreas Janning andreas.janning@qaware.de
 */
public class BranchWithNewestBuildView extends BranchView {

    private BuildView newestBuild;

    public BranchWithNewestBuildView(long id, long projectId, String name, Status status, BuildView newestBuild) {
        super(id, projectId, name, status);
        this.newestBuild = newestBuild;
    }

    public BuildView getNewestBuild() {
        return newestBuild;
    }
}
