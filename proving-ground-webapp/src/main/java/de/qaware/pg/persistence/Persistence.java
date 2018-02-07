package de.qaware.pg.persistence;

import de.qaware.pg.Build;
import de.qaware.pg.ElementNotFoundException;
import de.qaware.pg.TestResult;
import de.qaware.pg.dto.*;

import java.util.List;

public interface Persistence {

    List<ProjectView> listAllProjects();

    ProjectView findProject(long id) throws ElementNotFoundException;

    List<BranchView> listBranchesForProject(long projectId);

    BranchView findBranch(long branchId);

    List<BuildView> findBuildsForBranch(long branchId) throws ElementNotFoundException;

    BuildView findBuild(long buildId) throws ElementNotFoundException;

    List<TestRunView> listTestRunsForBuild(long buildId) throws ElementNotFoundException;

    List<TestRunView> listTestRunsForBuild(long buildId, TestResult filter) throws ElementNotFoundException;

    TestRunDetailsView findTestRun(long testRunId) throws ElementNotFoundException;

    void persist(Build build);
}
