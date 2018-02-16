/**
 * ﻿Copyright 2012, Deutsche Telekom AG, DTAG GHS GIS. All rights reserved.
 */

package de.qaware.pg;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.URLEncoder;

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
            return URLEncoder.encode(s, ENCODING);
        } catch (UnsupportedEncodingException e) {
            throw new IllegalStateException("Cannot encode URL", e);
        }
    }


    public static String decode(String s) {
        try {
            return URLDecoder.decode(s, ENCODING);
        } catch (UnsupportedEncodingException e) {
            throw new IllegalStateException("Cannot decode URL", e);
        }
    }
}
