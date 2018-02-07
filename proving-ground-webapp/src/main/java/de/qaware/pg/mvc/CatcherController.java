/**
 * ﻿Copyright 2012, Deutsche Telekom AG, DTAG GHS GIS. All rights reserved.
 */

package de.qaware.pg.mvc;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;


/**
 * TODO describe type.
 *
 * @author Andreas Janning andreas.janning@qaware.de
 */
@Controller
public class CatcherController {


    @GetMapping({"", "/", "projects"})
    public String redirectRoot() {
        return "redirect:/projects/";
    }
}
