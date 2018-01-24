package de.theo.pg.provingground.persistence;

import de.theo.pg.provingground.ElementNotFoundException;
import de.theo.pg.provingground.TestResult;
import de.theo.pg.provingground.dto.ProjectView;
import de.theo.pg.provingground.dto.TestRunDetailsView;
import de.theo.pg.provingground.dto.TestRunView;
import de.theo.pg.provingground.dto.TestSuiteView;

import java.util.List;

public interface Persistence {

    List<ProjectView> listAllProjects();

    ProjectView findProject(int id) throws ElementNotFoundException;

    List<TestSuiteView> findTestSuitesForProject(int projectId) throws ElementNotFoundException;

    TestSuiteView findTestSuite(int testSuiteId) throws ElementNotFoundException;

    List<TestRunView> findTestRunsForSuite(int testSuiteId) throws ElementNotFoundException;

    List<TestRunView> findTestRunsForSuite(int testSuiteId, TestResult filter) throws ElementNotFoundException;

    TestRunDetailsView findTestRun(int testRunId) throws ElementNotFoundException;


}
