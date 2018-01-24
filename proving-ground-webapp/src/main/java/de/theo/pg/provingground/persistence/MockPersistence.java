package de.theo.pg.provingground.persistence;

import de.theo.pg.provingground.*;
import de.theo.pg.provingground.dto.ProjectView;
import de.theo.pg.provingground.dto.TestRunDetailsView;
import de.theo.pg.provingground.dto.TestRunView;
import de.theo.pg.provingground.dto.TestSuiteView;
import de.theo.pg.provingground.info.ExecutionInfo;
import de.theo.pg.provingground.parse.surefire.JunitResultParser;

import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.time.Month;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

public class MockPersistence implements Persistence {

    private static final LocalDateTime START_RUN_1 = LocalDateTime.of(2018, Month.JANUARY, 17, 20, 30, 7);
    private static final LocalDateTime START_RUN_2 = LocalDateTime.of(2018, Month.JANUARY, 18, 21, 30, 7);
    private static final Test TEST_1 = new Test("de.theo.test", "MyLittleTestClass", "testTheStuff");
    private static final Test TEST_2 = new Test("de.theo.test", "MyLittleTestClass", "testTheOtherStuff");

    private final Project project;

    public MockPersistence() throws IOException, URISyntaxException {
        project = new Project("MyLittleProject");
        TestRun testRun1 = new TestRun(1, START_RUN_1);
        Path testPath = Paths.get(ClassLoader.getSystemResource("input").toURI());
        JunitResultParser junitResultParser = new JunitResultParser();

        List<TestExecution> parse = junitResultParser.parse(testPath);

        testRun1.addExecutions(parse);
        project.addTestRun(testRun1);
    }

    @Override
    public List<ProjectView> listAllProjects() {
        try {
            ArrayList<ProjectView> list = new ArrayList<>();
            list.add(findProject(1));
            return list;
        } catch (ElementNotFoundException e) {
            throw new IllegalStateException("Mock code, should never happen");
        }
    }

    @Override
    public ProjectView findProject(int id) throws ElementNotFoundException {
        if (id == 1) {
            return new ProjectView(1, project.getName(), project.getStatus());
        }
        throw new ElementNotFoundException("Found no project with id " + id);
    }

    @Override
    public List<TestSuiteView> findTestSuitesForProject(int projectId) throws ElementNotFoundException {
        findProject(1);
        return Collections.singletonList(findTestSuite(1));
    }

    @Override
    public TestSuiteView findTestSuite(int testSuiteId) throws ElementNotFoundException {
        TestRun testRun = project.getTestRun(testSuiteId);
        return new TestSuiteView(
                1, 1, "myLittleTestRun",
                testRun.getStart(), testRun.getStatus(), testRun.getTotalNumberOfTests(),
                testRun.getNumberOfSuccessfulTests(), testRun.getNumberOfFailedTests(), testRun.getNumberOfSkippedTests());
    }

    @Override
    public List<TestRunView> findTestRunsForSuite(int testSuiteId) throws ElementNotFoundException {
        TestRun testRun = project.getTestRun(testSuiteId);
        List<TestExecution> testExecutions = testRun.getSortedTestExecutions(false);
        int idCounter = 0;
        List<TestRunView> result = new ArrayList<>();
        for (TestExecution source : testExecutions) {
            result.add(new TestRunView(idCounter++, testSuiteId, source.getTest().getFullName(), source.getResult(), source.getExecutionTime()));
        }
        return result;
    }

    @Override
    public List<TestRunView> findTestRunsForSuite(int testSuiteId, TestResult filter) throws ElementNotFoundException {
        List<TestRunView> allRuns = findTestRunsForSuite(testSuiteId);
        return allRuns.stream().filter(run -> run.getResult() == filter).collect(Collectors.toList());
    }

    @Override
    public TestRunDetailsView findTestRun(int testRunId) throws ElementNotFoundException {
        TestRun testRun = project.getTestRun(1);
        TestExecution source = testRun.getSortedTestExecutions(false).get(testRunId);
        ExecutionInfo executionInfo = source.getExecutionInfo();
        return new TestRunDetailsView(
                testRunId, 1, source.getTest().getFullName(), source.getResult(), source.getExecutionTime(),
                executionInfo.getStandardOut(), executionInfo.getErrorType(), executionInfo.getErrorMessage(), executionInfo.getStackTrace());
    }
}
