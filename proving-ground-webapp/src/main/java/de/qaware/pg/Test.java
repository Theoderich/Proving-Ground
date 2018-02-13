package de.qaware.pg;

import de.qaware.pg.dto.TestView;

import java.util.Map;
import java.util.Objects;

public class Test {

    private Long databaseId;
    private final String name;
    private Build lastRun;
    private Build lastSuccess;

    public Test(String name) {
        this.name = name;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Test test = (Test) o;
        return Objects.equals(name, test.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name);
    }

    @Override
    public String toString() {
        return "Test{" +
                "databaseId=" + databaseId +
                ", name='" + name + '\'' +
                ", lastRun=" + lastRun +
                ", lastSuccess=" + lastSuccess +
                '}';
    }

    public Long getDatabaseId() {
        return databaseId;
    }

    public String getName() {
        return name;
    }

    public Build getLastRun() {
        return lastRun;
    }

    public boolean setLastRun(Build lastRun) {
        if (Objects.equals(lastRun, this.lastRun)) {
            return false;
        }
        this.lastRun = lastRun;
        return true;
    }

    public Build getLastSuccess() {
        return lastSuccess;
    }


    public boolean setLastSuccess(Build lastSuccess) {
        if (Objects.equals(lastSuccess, this.lastSuccess)) {
            return false;
        }
        this.lastSuccess = lastSuccess;
        return true;
    }


    public static Test loadFromPersistence(TestView testView, Map<Long, Build> buildsById) {
        Test test = new Test(testView.getName());
        test.databaseId = testView.getId();
        test.setLastRun(buildsById.get(testView.getLastRun()));
        if (testView.getLastSuccess() != null) {
            test.setLastSuccess(buildsById.get(testView.getLastSuccess()));
        }
        return test;
    }
}
