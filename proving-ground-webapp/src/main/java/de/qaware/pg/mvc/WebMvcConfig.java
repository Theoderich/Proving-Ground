/**
 * ﻿Copyright 2012, Deutsche Telekom AG, DTAG GHS GIS. All rights reserved.
 */

package de.qaware.pg.mvc;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

/**
 * TODO describe type.
 *
 * @author Andreas Janning andreas.janning@qaware.de
 */
@Configuration
public class WebMvcConfig extends WebMvcConfigurerAdapter {


    private ForwardSlashParamInterceptor forwardSlashParamInterceptor;

    @Autowired
    public WebMvcConfig(ForwardSlashParamInterceptor forwardSlashParamInterceptor) {
        this.forwardSlashParamInterceptor = forwardSlashParamInterceptor;
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(forwardSlashParamInterceptor);
    }
}
