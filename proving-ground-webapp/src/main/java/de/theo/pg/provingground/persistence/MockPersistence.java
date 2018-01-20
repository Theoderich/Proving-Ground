package de.theo.pg.provingground.persistence;

import de.theo.pg.provingground.Project;
import de.theo.pg.provingground.Test;
import de.theo.pg.provingground.TestExecution;
import de.theo.pg.provingground.TestRun;
import de.theo.pg.provingground.parse.surefire.JunitResultParser;

import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.time.Month;
import java.util.Collections;
import java.util.List;

public class MockPersistence implements Persistence {

    private static final LocalDateTime START_RUN_1 = LocalDateTime.of(2018, Month.JANUARY, 17, 20, 30, 7);
    private static final LocalDateTime START_RUN_2 = LocalDateTime.of(2018, Month.JANUARY, 18, 21, 30, 7);
    private static final Test TEST_1 = new Test("de.theo.test", "MyLittleTestClass", "testTheStuff");
    private static final Test TEST_2 = new Test("de.theo.test", "MyLittleTestClass", "testTheOtherStuff");


    @Override
    public List<String> findProjectNames() {
        return Collections.singletonList("MyLittleProject");
    }

    @Override
    public Project findProject(String name) {
        try {
            Project project = new Project("MyLittleProject");
            TestRun testRun1 = new TestRun(1, START_RUN_1);
            Path testPath = Paths.get(ClassLoader.getSystemResource("input").toURI());
            JunitResultParser junitResultParser = new JunitResultParser();

            List<TestExecution> parse = junitResultParser.parse(testPath);

            testRun1.addExecutions(parse);
            project.addTestRun(testRun1);
            return project;
        } catch (IOException | URISyntaxException e) {
            throw new IllegalStateException("Unable to get mock data", e);
        }
    }
}
