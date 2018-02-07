package de.qaware.pg;

import java.util.Objects;

public class Test {

    private final String name;

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
        return Objects.hash( name);
    }

    @Override
    public String toString() {
        return "Test{" +
                ", name='" + name + '\'' +
                '}';
    }

    public String getFullName() {
        return name;
    }
}
