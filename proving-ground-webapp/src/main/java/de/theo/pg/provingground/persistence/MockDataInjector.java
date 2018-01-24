package de.theo.pg.provingground.persistence;

import de.theo.pg.provingground.Project;
import de.theo.pg.provingground.TestExecution;
import de.theo.pg.provingground.TestSuite;
import de.theo.pg.provingground.parse.surefire.JunitResultParser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.time.Month;
import java.util.List;

@Component
public class MockDataInjector implements ApplicationListener<ApplicationReadyEvent> {

    private static final LocalDateTime START_RUN_1 = LocalDateTime.of(2018, Month.JANUARY, 17, 20, 30, 7);


    private final Project project;
    private Persistence persistence;

    @Autowired
    public MockDataInjector(Persistence persistence) throws URISyntaxException, IOException {
        this.persistence = persistence;

        project = new Project("MyLittleProject");
        TestSuite testSuite1 = new TestSuite(START_RUN_1);
        Path testPath = Paths.get(ClassLoader.getSystemResource("input").toURI());
        JunitResultParser junitResultParser = new JunitResultParser();

        List<TestExecution> parse = junitResultParser.parse(testPath);

        testSuite1.addExecutions(parse);
        project.addTestRun(testSuite1);
    }

    @Override
    public void onApplicationEvent(ApplicationReadyEvent applicationReadyEvent) {
          persistence.persist(project);
    }
}
