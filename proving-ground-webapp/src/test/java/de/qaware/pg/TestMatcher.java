/**
 * ﻿Copyright 2012, Deutsche Telekom AG, DTAG GHS GIS. All rights reserved.
 */

package de.qaware.pg;

import org.hamcrest.Description;
import org.hamcrest.TypeSafeMatcher;

import java.util.Objects;

/**
 * TODO describe type.
 *
 * @author Andreas Janning andreas.janning@qaware.de
 */
public class TestMatcher extends TypeSafeMatcher<Test> {

    private final String name;
    private final Build lastRun;
    private final Build lastSuccess;

    private TestMatcher(String name, Build lastRun, Build lastSuccess) {
        this.name = name;
        this.lastRun = lastRun;
        this.lastSuccess = lastSuccess;
    }

    public static TestMatcher testMatching(String name, Build lastRun, Build lastSuccess) {
        return new TestMatcher(name, lastRun, lastSuccess);
    }

    @Override
    protected boolean matchesSafely(Test item) {
        return item.getName().equals(this.name) &&
                Objects.equals(item.getLastRun(), this.lastRun) &&
                Objects.equals(item.getLastSuccess(), this.lastSuccess);
    }

    @Override
    public void describeTo(Description description) {
        description.appendText("A Test with the name ").appendValue(name);
        description.appendText(" and the build ").appendValue(lastRun).appendText(" as the last run");
        description.appendText(" and the build ").appendValue(lastSuccess).appendText(" as the last success");
    }
}
