package de.theo.pg.provingground.persistence;

import de.theo.pg.provingground.ElementNotFoundException;
import de.theo.pg.provingground.TestResult;
import de.theo.pg.provingground.TestRun;
import de.theo.pg.provingground.dto.ProjectView;
import de.theo.pg.provingground.dto.TestRunDetailsView;
import de.theo.pg.provingground.dto.TestRunView;
import de.theo.pg.provingground.dto.TestSuiteView;
import de.theo.pg.provingground.persistence.entity.tables.Testrun;
import de.theo.pg.provingground.persistence.entity.tables.Testsuite;
import org.jooq.DSLContext;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

import static de.theo.pg.provingground.persistence.entity.tables.Project.PROJECT;
import static de.theo.pg.provingground.persistence.entity.tables.Testrun.TESTRUN;
import static de.theo.pg.provingground.persistence.entity.tables.Testsuite.TESTSUITE;

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
    public ProjectView findProject(int projectId) {
        return db.selectFrom(PROJECT).where(PROJECT.ID.eq(projectId)).fetchOneInto(ProjectView.class);
    }

    @Override
    public List<TestSuiteView> findTestSuitesForProject(int projectId) {
        return db.selectFrom(TESTSUITE).where(TESTSUITE.FK_PROJECT_ID.eq(projectId)).fetchInto(TestSuiteView.class);
    }

    @Override
    public TestSuiteView findTestSuite(int testSuiteId) {
        return db.selectFrom(TESTSUITE).where(TESTSUITE.ID.eq(testSuiteId)).fetchOneInto(TestSuiteView.class);
    }

    @Override
    public List<TestRunView> findTestRunsForSuite(int testSuiteId) {
        return db.select(TESTRUN.ID, TESTRUN.FK_TESTSUITE_ID, TESTRUN.TEST_NAME, TESTRUN.RESULT, TESTRUN.DURATION).from(TESTRUN).where(TESTRUN.FK_TESTSUITE_ID.eq(testSuiteId)).fetchInto(TestRunView.class);
    }

    @Override
    public List<TestRunView> findTestRunsForSuite(int testSuiteId, TestResult filter) throws ElementNotFoundException {
        return db.select(TESTRUN.ID, TESTRUN.FK_TESTSUITE_ID, TESTRUN.TEST_NAME, TESTRUN.RESULT, TESTRUN.DURATION).from(TESTRUN)
                .where(TESTRUN.FK_TESTSUITE_ID.eq(testSuiteId).and(TESTRUN.RESULT.eq(filter))).fetchInto(TestRunView.class);
    }

    @Override
    public TestRunDetailsView findTestRun(int testRunId) {
        return db.selectFrom(TESTRUN).where(TESTRUN.ID.eq(testRunId)).fetchOneInto(TestRunDetailsView.class);
    }
}

