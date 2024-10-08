package de.qaware.pg.persistence;

import de.qaware.pg.*;
import de.qaware.pg.dto.*;

import java.util.List;

public interface Persistence {

    List<ProjectView> listAllProjects();

    ProjectView findProject(long id) throws ElementNotFoundException;

    ProjectView findProjectByName(String projectName) throws ElementNotFoundException;

    List<BranchWithNewestBuildView> listBranchesForProject(long projectId);

    BranchView findBranch(long branchId);

    BranchView findBranchByName(long projectId, String branchName);

    List<BuildView> listBuildsForBranch(long branchId) throws ElementNotFoundException;

    BuildView findBuild(long buildId) throws ElementNotFoundException;

    BuildView findBuildByName(long branchId, String buildName) throws ElementNotFoundException;


    void deleteBuild(long buildId);

    void updateTest(long testId, long lastBuildId, Long lastSuccessBuildId);

    void deleteTest(long testId);

    List<TestRunView> listTestRunsForBuild(long buildId) throws ElementNotFoundException;

    List<TestRunView> listTestRunsForBuild(long buildId, TestResult filter) throws ElementNotFoundException;

    List<TestView> listTestsForBranch(long branchId);

    TestRunDetailsView findTestRun(long testRunId) throws ElementNotFoundException;

    void persist(Project project, Branch branch, Build build);
}
