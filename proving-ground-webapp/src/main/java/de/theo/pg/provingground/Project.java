package de.theo.pg.provingground;


import de.theo.pg.provingground.dto.Status;

import java.util.*;

public class Project {

    private final String name;

    private final Set<Test> knownTests;
    private final SortedSet<TestSuite> testSuites;


    public Project(String name) {
        this.name = name;
        this.knownTests = new HashSet<>();
        this.testSuites = new TreeSet<>();
    }

    public void addTestRun(TestSuite newTestSuite) {
        this.testSuites.add(newTestSuite);
        this.knownTests.addAll(newTestSuite.getAllExecutedTests());
    }

    public String getName() {
        return name;
    }

    public SortedSet<TestSuite> getTestSuites() {
        return Collections.unmodifiableSortedSet(testSuites);
    }

    public Status getStatus() {
        return testSuites.last().getStatus();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Project project = (Project) o;
        return Objects.equals(name, project.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name);
    }

    @Override
    public String toString() {
        return "Project{" +
                "name='" + name + '\'' +
                '}';
    }

}
