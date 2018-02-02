package de.theo.pg.provingground.persistence;

import de.theo.pg.provingground.*;
import de.theo.pg.provingground.dto.ProjectView;
import de.theo.pg.provingground.dto.TestRunDetailsView;
import de.theo.pg.provingground.dto.TestRunView;
import de.theo.pg.provingground.dto.TestSuiteView;
import de.theo.pg.provingground.info.ExecutionInfo;
import org.jooq.DSLContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.SortedSet;

import static de.theo.pg.provingground.persistence.entity.Sequences.S_ID;
import static de.theo.pg.provingground.persistence.entity.tables.Project.PROJECT;
import static de.theo.pg.provingground.persistence.entity.tables.Testrun.TESTRUN;
import static de.theo.pg.provingground.persistence.entity.tables.Testsuite.TESTSUITE;

@Component
public class JooqPersistence implements Persistence {

    private DSLContext db;

    @Autowired
    public JooqPersistence(DSLContext db) {
        this.db = db;
    }

    @Override
    public List<ProjectView> listAllProjects() {
        return db.selectFrom(PROJECT).fetchInto(ProjectView.class);
    }

    @Override
    public ProjectView findProject(long projectId) {
        return db.selectFrom(PROJECT).where(PROJECT.ID.eq(projectId)).fetchOneInto(ProjectView.class);
    }

    @Override
    public List<TestSuiteView> findTestSuitesForProject(long projectId) {
        return db.selectFrom(TESTSUITE).where(TESTSUITE.FK_PROJECT_ID.eq(projectId)).fetchInto(TestSuiteView.class);
    }

    @Override
    public TestSuiteView findTestSuite(long testSuiteId) {
        return db.selectFrom(TESTSUITE).where(TESTSUITE.ID.eq(testSuiteId)).fetchOneInto(TestSuiteView.class);
    }

    @Override
    public List<TestRunView> findTestRunsForSuite(long testSuiteId) {
        return db.select(TESTRUN.ID, TESTRUN.FK_TESTSUITE_ID, TESTRUN.TEST_NAME, TESTRUN.RESULT, TESTRUN.DURATION).from(TESTRUN).where(TESTRUN.FK_TESTSUITE_ID.eq(testSuiteId)).fetchInto(TestRunView.class);
    }

    @Override
    public List<TestRunView> findTestRunsForSuite(long testSuiteId, TestResult filter) throws ElementNotFoundException {
        return db.select(TESTRUN.ID, TESTRUN.FK_TESTSUITE_ID, TESTRUN.TEST_NAME, TESTRUN.RESULT, TESTRUN.DURATION).from(TESTRUN)
                .where(TESTRUN.FK_TESTSUITE_ID.eq(testSuiteId).and(TESTRUN.RESULT.eq(filter))).fetchInto(TestRunView.class);
    }

    @Override
    public TestRunDetailsView findTestRun(long testRunId) {
        return db.selectFrom(TESTRUN).where(TESTRUN.ID.eq(testRunId)).fetchOneInto(TestRunDetailsView.class);
    }

    @Override
    public void persist(Project source) {
        Long projectId = getProjectIdByName(source.getName());
        if (projectId == null) {
            projectId = db.nextval(S_ID);
            db.insertInto(PROJECT, PROJECT.ID, PROJECT.NAME, PROJECT.STATUS).values(projectId, source.getName(), source.getStatus()).execute();
        } else {
            db.update(PROJECT).set(PROJECT.STATUS, source.getStatus());
        }
        SortedSet<TestSuite> testSuites = source.getTestSuites();
        long finalProjectId = projectId;
        testSuites.forEach(run -> insertTestSuite(run, finalProjectId));
    }

    private void insertTestSuite(TestSuite testSuite, long projectId) {
        long nextId = db.nextval(S_ID);
        db.insertInto(TESTSUITE, TESTSUITE.ID, TESTSUITE.FK_PROJECT_ID, TESTSUITE.NAME, TESTSUITE.STATUS, TESTSUITE.START_TIME,
                TESTSUITE.COMMITID, TESTSUITE.BRANCH,
                TESTSUITE.NUM_FAILED, TESTSUITE.NUM_SKIPPED, TESTSUITE.NUM_SUCCESS, TESTSUITE.NUM_TOTAL)
                .values(nextId, projectId, testSuite.getName(), testSuite.getStatus(), testSuite.getStart(),
                        testSuite.getCommitId(), testSuite.getBranchName(),
                        testSuite.getNumberOfFailedTests(), testSuite.getNumberOfSkippedTests(), testSuite.getNumberOfSuccessfulTests(),
                        testSuite.getTotalNumberOfTests()).execute();

        testSuite.getSortedTestExecutions(false).forEach(test -> insertTest(test, nextId));
    }

    private void insertTest(TestExecution test, long suiteId) {
        long nextId = db.nextval(S_ID);
        ExecutionInfo info = test.getExecutionInfo();
        db.insertInto(TESTRUN, TESTRUN.ID, TESTRUN.FK_TESTSUITE_ID, TESTRUN.TEST_NAME, TESTRUN.RESULT, TESTRUN.DURATION,
                TESTRUN.OUTPUT, TESTRUN.ERRORTYPE, TESTRUN.ERRORMESSAGE, TESTRUN.STACKTRACE)
                .values(nextId, suiteId, test.getTest().getFullName(), test.getResult(), test.getExecutionTime(),
                        info.getStandardOut(), info.getErrorType(), info.getErrorMessage(), info.getStackTrace()).execute();
    }

    private Long getProjectIdByName(String name) {
        return db.select(PROJECT.ID).from(PROJECT).where(PROJECT.NAME.eq(name)).fetchOneInto(Long.class);
    }

}

