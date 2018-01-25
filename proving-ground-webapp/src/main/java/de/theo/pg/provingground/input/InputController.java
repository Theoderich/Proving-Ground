package de.theo.pg.provingground.input;

import de.theo.pg.provingground.Project;
import de.theo.pg.provingground.Test;
import de.theo.pg.provingground.TestExecution;
import de.theo.pg.provingground.TestSuite;
import de.theo.pg.provingground.info.ErrorExecutionInfo;
import de.theo.pg.provingground.info.ExecutionInfo;
import de.theo.pg.provingground.info.SuccessExecutionInfo;
import de.theo.pg.provingground.persistence.Persistence;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping(value = "/input", consumes = "application/json", produces = "application/json")
public class InputController {


    private Persistence persistence;

    @Autowired
    public InputController(Persistence persistence) {
        this.persistence = persistence;
    }

    @PostMapping
    public void addNewTestRun(@RequestBody TestSuiteInput testSuiteInput) {
        Project project = new Project(testSuiteInput.getProjectName());
        TestSuite testSuite = new TestSuite(testSuiteInput.getStartTime());
        List<TestExecution> testExecutions = testSuiteInput.getTestRuns().stream().map(this::mapTestRun).collect(Collectors.toList());

        testSuite.addExecutions(testExecutions);
        project.addTestRun(testSuite);
        
        persistence.persist(project);
    }


    private TestExecution mapTestRun(TestRunInput input) {
        Test test = new Test(input.getName());
        ExecutionInfo executionInfo;
        if (input.getResult().isFailure()) {
            executionInfo = new ErrorExecutionInfo(input.getErrorMessage(), input.getErrorType(), input.getStacktrace(), input.getOutput());
        } else {
            executionInfo = new SuccessExecutionInfo(input.getOutput());
        }
        return new TestExecution(test, input.getResult(), input.getDuration(), executionInfo);
    }
}
