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
public class ProjectView implements NavigationPart {

    private final long id;
    private final String name;

    public ProjectView(long id, String name) {
        this.id = id;
        this.name = name;
    }

    public long getId() {
        return id;
    }

    @Override
    public String getLinkId() {
        return EncodingUtil.encode(name);
    }

    @Override
    public String getName() {
        return name;
    }
}
