package de.theo.pg.provingground.persistence;

import de.theo.pg.provingground.*;
import de.theo.pg.provingground.dto.ProjectView;
import de.theo.pg.provingground.dto.TestRunDetailsView;
import de.theo.pg.provingground.dto.TestRunView;
import de.theo.pg.provingground.dto.TestSuiteView;
import de.theo.pg.provingground.info.ExecutionInfo;
import de.theo.pg.provingground.persistence.entity.tables.records.ProjectRecord;
import de.theo.pg.provingground.persistence.entity.tables.records.TestrunRecord;
import de.theo.pg.provingground.persistence.entity.tables.records.TestsuiteRecord;
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

    private final ProjectViewRecordMapper projectViewRecordMapper;
    private final TestSuiteViewRecordMapper testSuiteViewRecordMapper;
    private final TestRunViewRecordMapper testRunViewRecordMapper;
    private final TestRunDetailsViewRecordMapper testRunDetailsViewRecordMapper;

    private DSLContext db;

    @Autowired
    public JooqPersistence(DSLContext db) {
        this.db = db;
        projectViewRecordMapper = new ProjectViewRecordMapper();
        testSuiteViewRecordMapper = new TestSuiteViewRecordMapper();
        testRunViewRecordMapper = new TestRunViewRecordMapper();
        testRunDetailsViewRecordMapper = new TestRunDetailsViewRecordMapper();
    }

    @Override
    public List<ProjectView> listAllProjects() {
        return db.selectFrom(PROJECT).fetch().map(projectViewRecordMapper);
    }

    @Override
    public ProjectView findProject(long projectId) {
        ProjectRecord projectRecord = db.selectFrom(PROJECT).where(PROJECT.ID.eq(projectId)).fetchOne();
        return projectViewRecordMapper.map(projectRecord);
    }

    @Override
    public List<TestSuiteView> findTestSuitesForProject(long projectId) {
        return db.selectFrom(TESTSUITE).where(TESTSUITE.FK_PROJECT_ID.eq(projectId)).fetch().map(testSuiteViewRecordMapper);
    }

    @Override
    public TestSuiteView findTestSuite(long testSuiteId) {
        TestsuiteRecord testsuiteRecord = db.selectFrom(TESTSUITE).where(TESTSUITE.ID.eq(testSuiteId)).fetchOne();
        return testSuiteViewRecordMapper.map(testsuiteRecord);
    }

    @Override
    public List<TestRunView> findTestRunsForSuite(long testSuiteId) {
        return db.select(TESTRUN.ID, TESTRUN.FK_TESTSUITE_ID, TESTRUN.TEST_NAME, TESTRUN.RESULT, TESTRUN.DURATION).from(TESTRUN).where(TESTRUN.FK_TESTSUITE_ID.eq(testSuiteId))
                .fetch().map(testRunViewRecordMapper);
    }

    @Override
    public List<TestRunView> findTestRunsForSuite(long testSuiteId, TestResult filter) throws ElementNotFoundException {
        return db.select(TESTRUN.ID, TESTRUN.FK_TESTSUITE_ID, TESTRUN.TEST_NAME, TESTRUN.RESULT, TESTRUN.DURATION).from(TESTRUN)
                .where(TESTRUN.FK_TESTSUITE_ID.eq(testSuiteId).and(TESTRUN.RESULT.eq(filter))).fetch().map(testRunViewRecordMapper);
    }

    @Override
    public TestRunDetailsView findTestRun(long testRunId) {
        TestrunRecord testrunRecord = db.selectFrom(TESTRUN).where(TESTRUN.ID.eq(testRunId)).fetchOne();
        return testRunDetailsViewRecordMapper.map(testrunRecord);
    }

    @Override
    public void persist(Project source) {
        Long projectId = getProjectIdByName(source.getName());
        if (projectId == null) {
            projectId = db.nextval(S_ID);
            ProjectRecord projectRecord = new ProjectRecord(projectId, source.getName(), source.getStatus());
            db.insertInto(PROJECT).set(projectRecord).execute();
        } else {
            db.update(PROJECT).set(PROJECT.STATUS, source.getStatus());
        }
        SortedSet<TestSuite> testSuites = source.getTestSuites();
        long finalProjectId = projectId;
        testSuites.forEach(run -> insertTestSuite(run, finalProjectId));
    }

    private void insertTestSuite(TestSuite testSuite, long projectId) {
        long nextId = db.nextval(S_ID);
        TestsuiteRecord testsuiteRecord =
                new TestsuiteRecord(nextId, projectId, testSuite.getName(), testSuite.getCommitId(), testSuite.getBranchName(),
                        testSuite.getStart(), testSuite.getStatus(),
                        testSuite.getTotalNumberOfTests(), testSuite.getNumberOfSuccessfulTests(), testSuite.getNumberOfFailedTests(),
                        testSuite.getNumberOfSkippedTests());
        db.insertInto(TESTSUITE).set(testsuiteRecord).execute();

        testSuite.getSortedTestExecutions(false).forEach(test -> insertTest(test, nextId));
    }

    private void insertTest(TestExecution test, long suiteId) {
        long nextId = db.nextval(S_ID);
        ExecutionInfo info = test.getExecutionInfo();
        TestrunRecord testrunRecord =
                new TestrunRecord(nextId, suiteId, test.getTest().getFullName(), test.getResult(), test.getExecutionTime(),
                        info.getStandardOut(), info.getErrorType(), info.getErrorMessage(), info.getStackTrace());
        db.insertInto(TESTRUN).set(testrunRecord).execute();
    }

    private Long getProjectIdByName(String name) {
        return db.select(PROJECT.ID).from(PROJECT).where(PROJECT.NAME.eq(name)).fetchOneInto(Long.class);
    }

}

