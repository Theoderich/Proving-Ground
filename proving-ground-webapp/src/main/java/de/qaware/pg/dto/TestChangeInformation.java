/**
 * ﻿Copyright 2012, Deutsche Telekom AG, DTAG GHS GIS. All rights reserved.
 */

package de.qaware.pg.dto;

import de.qaware.pg.Test;

import java.util.Objects;

/**
 * TODO describe type.
 *
 * @author Andreas Janning andreas.janning@qaware.de
 */
public class TestChangeInformation {

    private final Test test;
    private final TestChange change;

    public TestChangeInformation(Test test, TestChange change) {
        this.test = test;
        this.change = change;
    }

    public Test getTest() {
        return test;
    }

    public TestChange getChange() {
        return change;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        TestChangeInformation that = (TestChangeInformation) o;
        return Objects.equals(test, that.test) &&
                change == that.change;
    }

    @Override
    public int hashCode() {

        return Objects.hash(test, change);
    }

    @Override
    public String toString() {
        return "TestChangeInformation{" +
                "test=" + test +
                ", change=" + change +
                '}';
    }

    public enum TestChange {
        MODIFIED,
        DELETED;
    }
}
