package de.theo.pg.provingground.persistence;

import de.theo.pg.provingground.ElementNotFoundException;
import de.theo.pg.provingground.Project;
import de.theo.pg.provingground.TestResult;
import de.theo.pg.provingground.dto.ProjectView;
import de.theo.pg.provingground.dto.TestRunDetailsView;
import de.theo.pg.provingground.dto.TestRunView;
import de.theo.pg.provingground.dto.TestSuiteView;

import java.util.List;

public interface Persistence {

    List<ProjectView> listAllProjects();

    ProjectView findProject(long id) throws ElementNotFoundException;

    List<TestSuiteView> findTestSuitesForProject(long projectId) throws ElementNotFoundException;

    TestSuiteView findTestSuite(long testSuiteId) throws ElementNotFoundException;

    List<TestRunView> findTestRunsForSuite(long testSuiteId) throws ElementNotFoundException;

    List<TestRunView> findTestRunsForSuite(long testSuiteId, TestResult filter) throws ElementNotFoundException;

    TestRunDetailsView findTestRun(long testRunId) throws ElementNotFoundException;


    void persist(Project project);
}
