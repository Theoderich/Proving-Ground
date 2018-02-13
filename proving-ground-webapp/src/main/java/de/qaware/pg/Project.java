package de.qaware.pg;


import de.qaware.pg.dto.BranchView;
import de.qaware.pg.dto.ProjectView;
import de.qaware.pg.persistence.Persistence;

import java.util.HashMap;
import java.util.List;
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
        Branch newBranch = new Branch(branchName);
        addBranch(newBranch);
        return newBranch;
    }

    public Branch getOrAddBranch(String branchName) {
        Branch result = branches.get(branchName);
        if (result == null) {
            result = addBranch(branchName);
        }
        return result;
    }

    private void addBranch(Branch newBranch) {
        branches.put(newBranch.getName(), newBranch);
    }


    public static Project loadFromPersistence(Persistence persistence, long id) throws ElementNotFoundException {
        ProjectView projectView = persistence.findProject(id);
        Project project = new Project(projectView.getName());

        List<BranchView> branchViews = persistence.listBranchesForProject(id);
        branchViews.stream().map(branchView -> Branch.loadFromPersistence(persistence, branchView)).forEach(project::addBranch);
        return project;
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
