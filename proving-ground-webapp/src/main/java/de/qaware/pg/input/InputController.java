package de.qaware.pg.input;

import de.qaware.pg.*;
import de.qaware.pg.info.ErrorExecutionInfo;
import de.qaware.pg.info.ExecutionInfo;
import de.qaware.pg.info.SuccessExecutionInfo;
import de.qaware.pg.persistence.Persistence;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping(value = "/input", consumes = "application/json", produces = "application/json")
public class InputController implements InputApi {


    private Persistence persistence;

    @Autowired
    public InputController(Persistence persistence) {
        this.persistence = persistence;
    }

    @Override
    @PostMapping
    public void addNewTestRun(@RequestBody BuildInput buildInput) {


        Project project = new Project(buildInput.getProjectName());


        String branchName = buildInput.getBranchName();
        Branch branch = project.getOrAddBranch(branchName);
        Build build = new Build(buildInput.getStartTime(), buildInput.getTestSuiteName(), buildInput.getCommitIdentifier());
        List<TestRun> testExecutions = buildInput.getTestRuns().stream().map(this::mapTestRun).collect(Collectors.toList());
        build.addTestRuns(testExecutions);

        branch.addBuild(build);

        persistence.persist(build);
    }


    private TestRun mapTestRun(TestRunInput input) {
        Test test = new Test(input.getName());
        ExecutionInfo executionInfo;
        if (input.getResult() == TestResultInput.FAILED) {
            executionInfo = new ErrorExecutionInfo(input.getErrorMessage(), input.getErrorType(), input.getStacktrace(), input.getOutput(), input.getErrorOutput());
        } else {
            executionInfo = new SuccessExecutionInfo(input.getOutput(), input.getErrorOutput());
        }
        return new TestRun(test, TestResult.fromInput(input.getResult()), input.getDuration(), executionInfo);
    }
}
