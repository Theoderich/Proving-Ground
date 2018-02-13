/**
 * ﻿Copyright 2012, Deutsche Telekom AG, DTAG GHS GIS. All rights reserved.
 */

package de.qaware.pg;

import de.qaware.pg.dto.TestChangeInformation;
import org.hamcrest.Description;
import org.hamcrest.TypeSafeMatcher;

/**
 * TODO describe type.
 *
 * @author Andreas Janning andreas.janning@qaware.de
 */
public class TestChangeMatcher extends TypeSafeMatcher<TestChangeInformation> {

    private final String name;
    private final TestChangeInformation.TestChange type;

    public TestChangeMatcher(String name, TestChangeInformation.TestChange type) {
        this.name = name;
        this.type = type;
    }

    public static TestChangeMatcher testChangeMatching(String name, TestChangeInformation.TestChange type) {
        return new TestChangeMatcher(name, type);
    }

    @Override
    protected boolean matchesSafely(TestChangeInformation item) {
        return item.getTest().getName().equals(name) && item.getChange() == type;
    }

    @Override
    public void describeTo(Description description) {
        description.appendText("A TestChangeInformation for the test with the name ").appendValue(name);
        description.appendText(" and the type ").appendValue(type);
    }
}
