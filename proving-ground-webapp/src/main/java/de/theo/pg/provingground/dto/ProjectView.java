/**
 * ﻿Copyright 2012, Deutsche Telekom AG, DTAG GHS GIS. All rights reserved.
 */

package de.theo.pg.provingground.dto;

/**
 * TODO describe type.
 *
 * @author Andreas Janning andreas.janning@qaware.de
 */
public class ProjectView {

    private final long id;
    private final String name;

    public ProjectView(long id, String name) {
        this.id = id;
        this.name = name;
    }

    public long getId() {
        return id;
    }

    public String getName() {
        return name;
    }
}
