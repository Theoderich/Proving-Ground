/**
 * ﻿Copyright 2012, Deutsche Telekom AG, DTAG GHS GIS. All rights reserved.
 */

package de.theo.pg.provingground.mvc;

import de.theo.pg.provingground.dto.NavigationPart;

/**
 * TODO describe type.
 *
 * @author Andreas Janning andreas.janning@qaware.de
 */
public class NavigationItem {

    private String link;
    private String name;

    public NavigationItem(String link, String name) {
        this.link = link;
        this.name = name;
    }

    public NavigationItem(NavigationPart part) {
        this(Long.toString(part.getId()), part.getName());
    }

    public NavigationItem(NavigationPart part, NavigationItem previous) {
        this(part);
        if (previous != null) {
            this.link = previous.getLink() + this.link + '/';
        }
    }


    public String getLink() {
        return link;
    }

    public String getName() {
        return name;
    }
}
