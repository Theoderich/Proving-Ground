package de.theo.pg.provingground.persistence;

import de.theo.pg.provingground.Build;
import de.theo.pg.provingground.ElementNotFoundException;
import de.theo.pg.provingground.TestResult;
import de.theo.pg.provingground.dto.*;

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
