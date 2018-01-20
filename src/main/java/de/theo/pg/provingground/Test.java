package de.theo.pg.provingground;

import java.util.Objects;

public class Test {

    private final String pkg;
    private final String name;
    private final String classname;

    private TestRun lastSuccess;


    public Test(String pkg, String classname, String name) {
        this.pkg = pkg;
        this.name = name;
        this.classname = classname;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Test test = (Test) o;
        return Objects.equals(pkg, test.pkg) &&
                Objects.equals(name, test.name) &&
                Objects.equals(classname, test.classname);
    }

    @Override
    public int hashCode() {

        return Objects.hash(pkg, name, classname);
    }

    @Override
    public String toString() {
        return "Test{" +
                "pkg='" + pkg + '\'' +
                ", name='" + name + '\'' +
                ", classname='" + classname + '\'' +
                '}';
    }

    public String getFullName() {
        return pkg + ":" + classname + ":" + name;
    }
}
