/**
 * ﻿Copyright 2012, Deutsche Telekom AG, DTAG GHS GIS. All rights reserved.
 */

package de.qaware.pg.mvc;

import de.qaware.pg.EncodingUtil;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerMapping;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.Map;

/**
 * Since forward slashes are not allowed in Spring MVC URLs, we custom encode/decode them
 *
 * @author Andreas Janning andreas.janning@qaware.de
 */
@Component
public class ForwardSlashParamInterceptor extends HandlerInterceptorAdapter {


    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
        Map<?, ?> oldPathVariables = (Map) request.getAttribute(HandlerMapping.URI_TEMPLATE_VARIABLES_ATTRIBUTE);
        Map<Object, Object> newPathVariables = new HashMap<>(oldPathVariables.size());

        oldPathVariables.forEach((key, value) -> {
            Object newValue = value;
            if (value instanceof String) {
                newValue = EncodingUtil.decodeForwardSlash((String) value);
            }
            newPathVariables.put(key, newValue);
        });

        request.setAttribute(HandlerMapping.URI_TEMPLATE_VARIABLES_ATTRIBUTE, newPathVariables);
        return true;
    }
}
