/**
 * ﻿Copyright 2012, Deutsche Telekom AG, DTAG GHS GIS. All rights reserved.
 */

package de.theo.pg.provingground;

import de.theo.pg.provingground.dto.Status;

import java.util.*;

/**
 * TODO describe type.
 *
 * @author Andreas Janning andreas.janning@qaware.de
 */
public class Branch {

    private final Project project;
    private final Set<Test> knownTests;
    private final SortedSet<Build> builds;
    private final String name;


    public Branch(String name, Project project) {
        this.name = name;
        this.project = project;
        this.knownTests = new HashSet<>();
        this.builds = new TreeSet<>();
    }


    public void addBuild(Build newBuild) {
        newBuild.setBranch(this);
        this.builds.add(newBuild);
        this.knownTests.addAll(newBuild.getAllTests());
    }

    public String getName() {
        return name;
    }

    public SortedSet<Build> getBuilds() {
        return Collections.unmodifiableSortedSet(builds);
    }

    public Status getStatus() {
        return builds.last().getStatus();
    }

    public Project getProject() {
        return project;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Branch branch = (Branch) o;
        return Objects.equals(name, branch.name);
    }

    @Override
    public int hashCode() {

        return Objects.hash(name);
    }

    @Override
    public String toString() {
        return "Branch{" +
                "knownTests=" + knownTests +
                ", testSuites=" + builds +
                ", name='" + name + '\'' +
                '}';
    }
}
