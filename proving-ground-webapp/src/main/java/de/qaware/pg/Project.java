package de.qaware.pg;


import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class Project {

    private final String name;

    private final Map<String, Branch> branches;


    public Project(String name) {
        this.name = name;
        this.branches = new HashMap<>();

    }

    public Branch addBranch(String branchName) {
        if (branches.containsKey(branchName)) {
            throw new IllegalArgumentException("Branch " + branchName + " already exists in Project " + this.name);
        }
        Branch newBranch = new Branch(branchName, this);
        branches.put(branchName, newBranch);
        return newBranch;
    }

    public Branch getOrAddBranch(String branchName) {
        Branch result = branches.get(branchName);
        if (result == null) {
            result = addBranch(branchName);
        }
        return result;
    }

    public String getName() {
        return name;
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
