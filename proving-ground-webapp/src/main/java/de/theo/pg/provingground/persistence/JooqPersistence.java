package de.theo.pg.provingground.persistence;

import de.theo.pg.provingground.*;
import de.theo.pg.provingground.dto.*;
import de.theo.pg.provingground.info.ExecutionInfo;
import de.theo.pg.provingground.persistence.entity.tables.records.*;
import org.jooq.DSLContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

import static de.theo.pg.provingground.persistence.entity.Sequences.S_ID;
import static de.theo.pg.provingground.persistence.entity.tables.Branch.BRANCH;
import static de.theo.pg.provingground.persistence.entity.tables.Build.BUILD;
import static de.theo.pg.provingground.persistence.entity.tables.ErrorInfo.ERROR_INFO;
import static de.theo.pg.provingground.persistence.entity.tables.Project.PROJECT;
import static de.theo.pg.provingground.persistence.entity.tables.Test.TEST;
import static de.theo.pg.provingground.persistence.entity.tables.TestRun.TEST_RUN;


@Component
public class JooqPersistence implements Persistence {

    private final ProjectViewRecordMapper projectViewRecordMapper;
    private final BuildViewRecordMapper buildViewRecordMapper;
    private final TestRunViewRecordMapper testRunViewRecordMapper;
    private final TestRunDetailsViewRecordMapper testRunDetailsViewRecordMapper;
    private final BranchViewRecordMapper branchViewRecordMapper;

    private DSLContext db;

    @Autowired
    public JooqPersistence(DSLContext db) {
        this.db = db;
        projectViewRecordMapper = new ProjectViewRecordMapper();
        buildViewRecordMapper = new BuildViewRecordMapper();
        testRunViewRecordMapper = new TestRunViewRecordMapper();
        testRunDetailsViewRecordMapper = new TestRunDetailsViewRecordMapper();
        branchViewRecordMapper = new BranchViewRecordMapper();
    }

    @Override
    public List<ProjectView> listAllProjects() {
        return db.selectFrom(PROJECT).fetch().map(projectViewRecordMapper);
    }

    @Override
    public ProjectView findProject(long projectId) {
        return db.selectFrom(PROJECT).where(PROJECT.ID.eq(projectId)).fetchOne(projectViewRecordMapper);
    }

    @Override
    public List<BranchView> listBranchesForProject(long projectId) {
        return db.selectFrom(BRANCH).where(BRANCH.FK_PROJECT_ID.eq(projectId)).fetch(branchViewRecordMapper);
    }

    @Override
    public BranchView findBranch(long branchId) {
        return db.selectFrom(BRANCH).where(BRANCH.ID.eq(branchId)).fetchOne(branchViewRecordMapper);
    }


    @Override
    public List<BuildView> findBuildsForBranch(long branchId) {
        return db.selectFrom(BUILD).where(BUILD.FK_BRANCH_ID.eq(branchId)).fetch().map(buildViewRecordMapper);
    }

    @Override
    public BuildView findBuild(long buildId) {
        return db.selectFrom(BUILD).where(BUILD.ID.eq(buildId)).fetchOne(buildViewRecordMapper);
    }

    @Override
    public List<TestRunView> findTestRunsForBuild(long buildId) {
        return db.select(TEST_RUN.ID, TEST_RUN.FK_BUILD_ID, TEST.NAME, TEST_RUN.RESULT, TEST_RUN.DURATION)
                .from(TEST_RUN)
                .join(TEST)
                .on(TEST_RUN.FK_TEST_ID.eq(TEST.ID))
                .where(TEST_RUN.FK_BUILD_ID.eq(buildId))
                .fetch(testRunViewRecordMapper);
    }

    @Override
    public List<TestRunView> findTestRunsForBuild(long buildId, TestResult filter) throws ElementNotFoundException {
        return db.select(TEST_RUN.ID, TEST_RUN.FK_BUILD_ID, TEST.NAME, TEST_RUN.RESULT, TEST_RUN.DURATION)
                .from(TEST_RUN)
                .join(TEST)
                .on(TEST_RUN.FK_TEST_ID.eq(TEST.ID))
                .where(TEST_RUN.FK_BUILD_ID.eq(buildId))
                .and(TEST_RUN.RESULT.eq(filter))
                .fetch(testRunViewRecordMapper);
    }

    @Override
    public TestRunDetailsView findTestRun(long testRunId) {
        return db.select(TEST_RUN.fields())
                .select(TEST.NAME)
                .select(ERROR_INFO.fields())
                .from(TEST_RUN)
                .join(ERROR_INFO)
                .on(ERROR_INFO.FK_TEST_RUN_ID.eq(TEST_RUN.ID))
                .join(TEST)
                .on(TEST_RUN.FK_TEST_ID.eq(TEST.ID))
                .where(TEST_RUN.ID.eq(testRunId)).fetchOne(testRunDetailsViewRecordMapper);
    }


    @Override
    public void persist(Build build) {
        Branch branch = build.getBranch();
        Project project = branch.getProject();
        long projectId = persist(project);
        long branchId = persist(branch, projectId);
        insertBuild(build, branchId);
    }

    private void insertBuild(Build build, long branchId) {
        long buildId = db.nextval(S_ID);
        BuildRecord buildRecord =
                new BuildRecord(buildId, branchId, build.getName(), build.getCommitId(),
                        build.getStart(), build.getStatus(),
                        build.getTotalNumberOfTests(), build.getNumberOfSuccessfulTests(), build.getNumberOfFailedTests(),
                        build.getNumberOfSkippedTests());
        db.insertInto(BUILD).set(buildRecord).execute();

        build.getSortedTestRuns(false).forEach(test -> insertTestRun(test, buildId, branchId));
    }

    private void insertTestRun(TestRun testRun, long buildId, long branchId) {
        long testId = persistTest(testRun.getTest(), branchId);
        long testRunId = db.nextval(S_ID);
        ExecutionInfo info = testRun.getExecutionInfo();
        TestRunRecord testrunRecord =
                new TestRunRecord(testRunId, buildId, testId, testRun.getResult(), testRun.getExecutionTime(),
                        info.getStandardOut(), null);
        db.insertInto(TEST_RUN).set(testrunRecord).execute();
        if (info.hasErrorInfo()) {
            long errorInfoId = db.nextval(S_ID);
            ErrorInfoRecord errorInfoRecord = new ErrorInfoRecord(errorInfoId, testRunId, info.getErrorType(), info.getErrorMessage(), info.getStackTrace());
            db.insertInto(ERROR_INFO).set(errorInfoRecord).execute();
        }
    }

    private long persist(Project source) {
        Long projectId = findProjectIdByName(source.getName());
        if (projectId == null) {
            projectId = db.nextval(S_ID);
            ProjectRecord projectRecord = new ProjectRecord(projectId, source.getName());
            db.insertInto(PROJECT).set(projectRecord).execute();
        }
        return projectId;
    }

    private long persist(Branch branch, long projectId) {
        String branchName = branch.getName();
        Status branchStatus = branch.getStatus();
        Long branchId = findBranchIdByName(branchName, projectId);
        if (branchId == null) {
            branchId = db.nextval(S_ID);
            BranchRecord branchRecord = new BranchRecord(branchId, projectId, branchName, branchStatus);
            db.insertInto(BRANCH).set(branchRecord).execute();
        } else {
            db.update(BRANCH).set(BRANCH.STATUS, branchStatus).where(BRANCH.ID.eq(branchId)).execute();
        }
        return branchId;
    }

    private long persistTest(Test test, long branchId) {
        String testName = test.getFullName();
        Long testId = findTestIdByName(testName, branchId);
        if (testId == null) {
            testId = db.nextval(S_ID);
            TestRecord testRecord = new TestRecord(testId, branchId, testName);
            db.insertInto(TEST).set(testRecord).execute();
        }
        return testId;
    }

    private Long findProjectIdByName(String name) {
        return db.select(PROJECT.ID).from(PROJECT).where(PROJECT.NAME.eq(name)).fetchOneInto(Long.class);
    }

    private Long findBranchIdByName(String name, long projectId) {
        return db.select(BRANCH.ID).from(BRANCH).where(BRANCH.NAME.eq(name).and(BRANCH.FK_PROJECT_ID.eq(projectId))).fetchOneInto(Long.class);
    }

    private Long findTestIdByName(String name, long branchId) {
        return db.select(TEST.ID).from(TEST).where(TEST.NAME.eq(name).and(TEST.FK_BRANCH_ID.eq(branchId))).fetchOneInto(Long.class);
    }

}

