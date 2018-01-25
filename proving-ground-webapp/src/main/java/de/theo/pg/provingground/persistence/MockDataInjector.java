package de.theo.pg.provingground.persistence;

import de.theo.pg.provingground.TestExecution;
import de.theo.pg.provingground.info.ExecutionInfo;
import de.theo.pg.provingground.input.TestResultInput;
import de.theo.pg.provingground.input.TestRunInput;
import de.theo.pg.provingground.input.TestSuiteInput;
import de.theo.pg.provingground.parse.surefire.JunitResultParser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.time.Month;
import java.util.ArrayList;
import java.util.List;

@Component
public class MockDataInjector implements ApplicationListener<ApplicationReadyEvent> {

    private static final LocalDateTime START_RUN_1 = LocalDateTime.of(2018, Month.JANUARY, 17, 20, 30, 7);


    private final TestSuiteInput testSuiteInput;

    @Autowired
    public MockDataInjector() throws URISyntaxException, IOException {


        testSuiteInput = new TestSuiteInput();
        testSuiteInput.setProjectName("MyLittleProject");
        testSuiteInput.setTestSuiteName("MyLittleTestRun");
        testSuiteInput.setStartTime(START_RUN_1);

        List<TestRunInput> testRunInputList = new ArrayList<>();



        Path testPath = Paths.get(ClassLoader.getSystemResource("input").toURI());
        JunitResultParser junitResultParser = new JunitResultParser();

        List<TestExecution> parse = junitResultParser.parse(testPath);

        for (TestExecution testExecution : parse) {
            TestRunInput testRunInput = new TestRunInput();
            testRunInput.setName(testExecution.getTest().getFullName());
            testRunInput.setDuration(testExecution.getExecutionTime());
            testRunInput.setResult(TestResultInput.valueOf(testExecution.getResult().name()));
            ExecutionInfo info = testExecution.getExecutionInfo();
            testRunInput.setOutput(info.getStandardOut());
            testRunInput.setErrorType(info.getErrorType());
            testRunInput.setErrorMessage(info.getErrorMessage());
            testRunInput.setOutput(info.getStandardOut());
            testRunInputList.add(testRunInput);
        }
        testSuiteInput.setTestRuns(testRunInputList);
    }

    @Override
    public void onApplicationEvent(ApplicationReadyEvent applicationReadyEvent) {
        RestTemplate restTemplate = new RestTemplate();
        restTemplate.postForObject("http://localhost:8080/input",testSuiteInput,Void.class);
    }
}
