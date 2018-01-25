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


        Path testPath = Paths.get(ClassLoader.getSystemResource("input").toURI());
        JunitResultParser junitResultParser = new JunitResultParser();

        List<TestRunInput> parse = junitResultParser.parse(testPath);

        testSuiteInput.setTestRuns(parse);
    }

    @Override
    public void onApplicationEvent(ApplicationReadyEvent applicationReadyEvent) {
        RestTemplate restTemplate = new RestTemplate();
        restTemplate.postForObject("http://localhost:8080/input",testSuiteInput,Void.class);
    }
}
