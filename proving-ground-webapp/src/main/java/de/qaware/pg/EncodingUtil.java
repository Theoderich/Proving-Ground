/**
 * ﻿Copyright 2012, Deutsche Telekom AG, DTAG GHS GIS. All rights reserved.
 */

package de.qaware.pg;

import org.springframework.web.util.UriUtils;

import java.io.UnsupportedEncodingException;

/**
 * TODO describe type.
 *
 * @author Andreas Janning andreas.janning@qaware.de
 */
public final class EncodingUtil {

    protected static final String ENCODING = "UTF-8";

    private EncodingUtil() {
    }

    public static String encode(String s) {
        try {
            String encode = UriUtils.encode(s, ENCODING);
            return encode.replace("%2F", "~2F");
        } catch (UnsupportedEncodingException e) {
            throw new IllegalStateException("Cannot encode URL", e);
        }
    }


    public static String decodeForwardSlash(String s) {
        return s.replace("~2F", "/");
    }
}
