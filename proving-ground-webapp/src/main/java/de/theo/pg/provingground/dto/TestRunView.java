/**
 * ﻿Copyright 2012, Deutsche Telekom AG, DTAG GHS GIS. All rights reserved.
 */

package de.theo.pg.provingground.dto;

import de.theo.pg.provingground.TestResult;

/**
 * TODO describe type.
 *
 * @author Andreas Janning andreas.janning@qaware.de
 */
public class TestRunView {

    private int id;
    private int testSuiteId;
    private String name;
    private TestResult result;

    public TestRunView(int id, int testSuiteId, String name, TestResult result) {
        this.id = id;
        this.testSuiteId = testSuiteId;
        this.name = name;
        this.result = result;
    }

    public int getId() {
        return id;
    }

    public int getTestSuiteId() {
        return testSuiteId;
    }

    public String getName() {
        return name;
    }

    public TestResult getResult() {
        return result;
    }
}
