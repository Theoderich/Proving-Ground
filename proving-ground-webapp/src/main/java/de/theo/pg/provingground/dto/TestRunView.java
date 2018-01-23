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
    private String name;
    private TestResult result;

    private TestRunView() {
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public TestResult getResult() {
        return result;
    }


    public static class TestRunViewBuilder {

        private TestRunView testRunView = new TestRunView();

        public TestRunView withId(int id) {
            testRunView.id = id;
            return testRunView;
        }

        public TestRunView withName(String name) {
            testRunView.name = name;
            return testRunView;
        }

        public TestRunView withResult(TestResult result) {
            testRunView.result = result;
            return testRunView;
        }

        public TestRunView build(){
            return testRunView;
        }

    }
}
