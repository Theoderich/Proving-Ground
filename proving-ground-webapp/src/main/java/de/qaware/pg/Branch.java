/**
 * ﻿Copyright 2012, Deutsche Telekom AG, DTAG GHS GIS. All rights reserved.
 */

package de.qaware.pg;

import de.qaware.pg.dto.*;
import de.qaware.pg.persistence.Persistence;

import java.util.*;

/**
 * TODO describe type.
 *
 * @author Andreas Janning andreas.janning@qaware.de
 */
public class Branch {

    private final Set<Test> knownTests;
    private final SortedSet<Build> builds;
    private final String name;


    public Branch(String name) {
        this.name = name;
        this.knownTests = new HashSet<>();
        this.builds = new TreeSet<>();
    }


    public void addBuild(Build newBuild) {
        this.builds.add(newBuild);
        this.knownTests.addAll(newBuild.getAllTests());
    }

    public Collection<TestChangeInformation> removeBuild(long buildId) throws ElementNotFoundException {
        Optional<Build> buildToRemove = builds.stream().filter(build -> build.getDatabaseId() == buildId).findFirst();
        if (!buildToRemove.isPresent()) {
            throw new ElementNotFoundException("Branch " + this + " does not contain buildId " + buildId);
        }
        this.builds.remove(buildToRemove.get());
        return recalculateTests();
    }

    public Collection<TestChangeInformation> recalculateTests() {
        Set<TestChangeInformation> testChanges = new HashSet<>();

        Set<Test> testsNeedingLastRun = new HashSet<>(knownTests);
        Set<Test> testsNeedingLastSuccess = new HashSet<>(knownTests);


        builds.stream().sorted(Collections.reverseOrder()).forEachOrdered(build -> {
            Iterator<Test> testIterator = testsNeedingLastRun.iterator();
            while (testIterator.hasNext()) {
                Test next = testIterator.next();
                if (build.hasRunForTest(next)) {
                    boolean changed = next.setLastRun(build);
                    if (changed) {
                        testChanges.add(new TestChangeInformation(next, TestChangeInformation.TestChange.MODIFIED));
                    }
                    testIterator.remove();
                }
            }
            testIterator = testsNeedingLastSuccess.iterator();
            while (testIterator.hasNext()) {
                Test next = testIterator.next();
                if (build.hasSuccessfulRunForTest(next)) {
                    boolean changed = next.setLastSuccess(build);
                    if (changed) {
                        testChanges.add(new TestChangeInformation(next, TestChangeInformation.TestChange.MODIFIED));
                    }
                    testIterator.remove();
                }
            }

        });

        testsNeedingLastRun.forEach(test -> {
            knownTests.remove(test);
            testsNeedingLastSuccess.remove(test);
            testChanges.add(new TestChangeInformation(test, TestChangeInformation.TestChange.DELETED));
        });
        testsNeedingLastSuccess.forEach(test -> {
            test.setLastSuccess(null);
            testChanges.add(new TestChangeInformation(test, TestChangeInformation.TestChange.MODIFIED));
        });
        return testChanges;
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

    public Set<Test> getKnownTests() {
        return Collections.unmodifiableSet(knownTests);
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

    public static Branch loadFromPersistence(Persistence persistence, long branchId) {
        BranchView branchView = persistence.findBranch(branchId);
        return loadFromPersistence(persistence, branchView);
    }

    public static Branch loadFromPersistence(Persistence persistence, BranchView branchView) {
        try {
            Branch branch = new Branch(branchView.getName());
            long branchId = branchView.getId();
            List<BuildView> buildViews = persistence.listBuildsForBranch(branchId);
            Map<Long, Build> buildsById = new HashMap<>(buildViews.size());
            buildViews.stream().map(buildView -> Build.loadFromPersistence(persistence, buildView)).forEach(newBuild -> {
                branch.builds.add(newBuild);
                buildsById.put(newBuild.getDatabaseId(), newBuild);
            });
            List<TestView> testViews = persistence.listTestsForBranch(branchId);

            testViews.stream().map(testView -> Test.loadFromPersistence(testView, buildsById)).forEach(branch.knownTests::add);
            return branch;
        } catch (ElementNotFoundException e) {
            throw new RuntimeException("Unable to list branches for builds for branch " + branchView);
        }
    }
}
