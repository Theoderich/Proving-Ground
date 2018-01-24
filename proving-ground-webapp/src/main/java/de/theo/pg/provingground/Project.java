package de.theo.pg.provingground;


import de.theo.pg.provingground.dto.Status;

import java.util.*;

public class Project {

    private final String name;

    private final Set<Test> knownTests;
    private final SortedSet<TestRun> testRuns;


    public Project(String name) {
        this.name = name;
        this.knownTests = new HashSet<>();
        this.testRuns = new TreeSet<>();
    }

    public void addTestRun(TestRun newTestRun) {
        this.testRuns.add(newTestRun);
        this.knownTests.addAll(newTestRun.getAllExecutedTests());
    }

    public String getName() {
        return name;
    }

    public SortedSet<TestRun> getTestRuns() {
        return Collections.unmodifiableSortedSet(testRuns);
    }

    public TestRun getTestRun(long index) throws ElementNotFoundException {
        Optional<TestRun> run = testRuns.stream().filter(testRun -> testRun.getIndex() == index).findFirst();
        if (!run.isPresent()) {
            throw new ElementNotFoundException();
        }
        return run.get();
    }

    public Status getStatus() {
        return testRuns.last().getStatus();
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
