package de.qaware.pg.persistence;

import de.qaware.pg.*;
import de.qaware.pg.dto.*;
import de.qaware.pg.info.ExecutionInfo;
import de.qaware.pg.persistence.entity.tables.records.*;
import org.jooq.DSLContext;
import org.jooq.Record7;
import org.jooq.SelectConditionStep;
import org.jooq.UpdateSetMoreStep;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.time.Duration;
import java.util.List;

import static de.qaware.pg.persistence.entity.Sequences.S_ID;
import static de.qaware.pg.persistence.entity.tables.Branch.BRANCH;
import static de.qaware.pg.persistence.entity.tables.Build.BUILD;
import static de.qaware.pg.persistence.entity.tables.ErrorInfo.ERROR_INFO;
import static de.qaware.pg.persistence.entity.tables.Project.PROJECT;
import static de.qaware.pg.persistence.entity.tables.Test.TEST;
import static de.qaware.pg.persistence.entity.tables.TestRun.TEST_RUN;


@Component
public class JooqPersistence implements Persistence {

    private final ProjectViewRecordMapper projectViewRecordMapper;
    private final BuildViewRecordMapper buildViewRecordMapper;
    private final TestRunViewRecordMapper testRunViewRecordMapper;
    private final TestRunDetailsViewRecordMapper testRunDetailsViewRecordMapper;
    private final BranchViewRecordMapper branchViewRecordMapper;
    private final TestViewRecordMapper testViewRecordMapper;

    private DSLContext db;

    @Autowired
    public JooqPersistence(DSLContext db) {
        this.db = db;
        projectViewRecordMapper = new ProjectViewRecordMapper();
        buildViewRecordMapper = new BuildViewRecordMapper();
        testRunViewRecordMapper = new TestRunViewRecordMapper();
        testRunDetailsViewRecordMapper = new TestRunDetailsViewRecordMapper();
        branchViewRecordMapper = new BranchViewRecordMapper();
        testViewRecordMapper = new TestViewRecordMapper();
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
    public List<BuildView> listBuildsForBranch(long branchId) {
        return db.selectFrom(BUILD).where(BUILD.FK_BRANCH_ID.eq(branchId)).fetch().map(buildViewRecordMapper);
    }

    @Override
    public BuildView findBuild(long buildId) {
        return db.selectFrom(BUILD).where(BUILD.ID.eq(buildId)).fetchOne(buildViewRecordMapper);
    }

    @Override
    public void deleteBuild(long buildId) {
        db.delete(BUILD).where(BUILD.ID.eq(buildId)).execute();
    }

    @Override
    public void updateTest(long testId, long lastBuildId, Long lastSuccessBuildId) {
        int rows = db.update(TEST).set(TEST.FK_BUILD_LAST_RUN, lastBuildId).set(TEST.FK_BUILD_LAST_SUCCESS, lastSuccessBuildId)
                .where(TEST.ID.eq(testId)).execute();
        if (rows != 1) {
            throw new IllegalStateException("Unable to update test " + testId + " with lastBuildId " + lastBuildId + " and lastSuccessId " + lastSuccessBuildId);
        }
    }

    @Override
    public void deleteTest(long testId) {
        db.delete(TEST_RUN).where(TEST_RUN.FK_TEST_ID.eq(testId)).execute();
        int deletedRows = db.delete(TEST).where(TEST.ID.eq(testId)).execute();
        if (deletedRows != 1) {
            throw new IllegalStateException("Unable to delete test " + testId);
        }
    }


    @Override
    public List<TestRunView> listTestRunsForBuild(long buildId) {
        return listTestRunsForBuild(buildId, null);
    }

    @Override
    public List<TestRunView> listTestRunsForBuild(long buildId, TestResult filter) {
        SelectConditionStep<Record7<Long, Long, String, Long, String, TestResult, Duration>> query =
                db.select(TEST_RUN.ID, TEST_RUN.FK_BUILD_ID, TEST.NAME, TEST.FK_BUILD_LAST_SUCCESS, BUILD.NAME, TEST_RUN.RESULT, TEST_RUN.DURATION)
                .from(TEST_RUN)
                .join(TEST)
                .on(TEST_RUN.FK_TEST_ID.eq(TEST.ID))
                        .leftJoin(BUILD)
                        .on(TEST.FK_BUILD_LAST_SUCCESS.eq(BUILD.ID))
                        .where(TEST_RUN.FK_BUILD_ID.eq(buildId));
        if (filter != null) {
            query.and(TEST_RUN.RESULT.eq(filter));
        }

        return query.fetch(testRunViewRecordMapper);
    }

    @Override
    public List<TestView> listTestsForBranch(long branchId) {
        return db.selectFrom(TEST).where(TEST.FK_BRANCH_ID.eq(branchId)).fetch(testViewRecordMapper);
    }

    @Override
    public TestRunDetailsView findTestRun(long testRunId) {
        return db.select(TEST_RUN.fields())
                .select(TEST.NAME)
                .select(ERROR_INFO.fields())
                .from(TEST_RUN)
                .join(TEST)
                .on(TEST_RUN.FK_TEST_ID.eq(TEST.ID))
                .leftJoin(ERROR_INFO)
                .on(ERROR_INFO.FK_TEST_RUN_ID.eq(TEST_RUN.ID))
                .where(TEST_RUN.ID.eq(testRunId)).fetchOne(testRunDetailsViewRecordMapper);
    }


    @Override
    @Transactional
    public void persist(Project project, Branch branch, Build build) {
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
        long testId = persistTest(testRun.getTestName(), branchId, buildId, testRun.getResult().isSuccess());
        long testRunId = db.nextval(S_ID);
        ExecutionInfo info = testRun.getExecutionInfo();
        TestRunRecord testrunRecord =
                new TestRunRecord(testRunId, buildId, testId, testRun.getResult(), testRun.getExecutionTime(),
                        info.getStandardOut(), info.getStandardErr());
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

    private long persistTest(String testName, long branchId, long buildId, boolean success) {
        Long testId = findTestIdByName(testName, branchId);
        if (testId == null) {
            testId = db.nextval(S_ID);
            TestRecord testRecord = new TestRecord();
            testRecord.setId(testId);
            testRecord.setFkBranchId(branchId);
            testRecord.setName(testName);
            testRecord.setFkBuildLastRun(buildId);
            if (success) {
                testRecord.setFkBuildLastSuccess(buildId);
            }
            db.insertInto(TEST).set(testRecord).execute();
        } else {
            UpdateSetMoreStep<TestRecord> query = db.update(TEST).set(TEST.FK_BUILD_LAST_RUN, buildId);
            if (success) {
                query.set(TEST.FK_BUILD_LAST_SUCCESS, buildId);
            }
            query.where(TEST.ID.eq(testId));
            query.execute();
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

