package de.qaware.pg;

import de.qaware.pg.dto.TestChangeInformation;
import de.qaware.pg.info.EmptyExecutionInfo;
import org.junit.Test;

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.Month;
import java.util.Collection;

import static de.qaware.pg.TestChangeMatcher.testChangeMatching;
import static de.qaware.pg.TestMatcher.testMatching;
import static org.hamcrest.Matchers.containsInAnyOrder;
import static org.junit.Assert.assertThat;

public class BranchTest {

    @Test
    public void deleteBuild() throws ElementNotFoundException {

        Branch branch = new Branch("MyLittleBranch");
        Build firstBuild = new Build(1L, LocalDateTime.of(2018, Month.JANUARY, 1, 13, 0), "firstBuild", "1");
        Build secondBuild = new Build(2L, LocalDateTime.of(2018, Month.JANUARY, 2, 13, 0), "secondBuild", "2");
        Build thirdBuild = new Build(3L, LocalDateTime.of(2018, Month.JANUARY, 3, 13, 0), "thirdBuild", "3");


        firstBuild.addTestRun(new TestRun("test1", TestResult.SUCCESS, Duration.ofSeconds(1), new EmptyExecutionInfo()));
        firstBuild.addTestRun(new TestRun("test2", TestResult.FAILED, Duration.ofSeconds(1), new EmptyExecutionInfo()));

        secondBuild.addTestRun(new TestRun("test1", TestResult.FAILED, Duration.ofSeconds(1), new EmptyExecutionInfo()));
        secondBuild.addTestRun(new TestRun("test2", TestResult.SUCCESS, Duration.ofSeconds(1), new EmptyExecutionInfo()));
        secondBuild.addTestRun(new TestRun("test3", TestResult.SUCCESS, Duration.ofSeconds(1), new EmptyExecutionInfo()));

        thirdBuild.addTestRun(new TestRun("test1", TestResult.SUCCESS, Duration.ofSeconds(1), new EmptyExecutionInfo()));
        thirdBuild.addTestRun(new TestRun("test2", TestResult.FAILED, Duration.ofSeconds(1), new EmptyExecutionInfo()));

        branch.addBuild(firstBuild);
        branch.addBuild(secondBuild);
        branch.addBuild(thirdBuild);

        branch.recalculateTests();

        assertThat(branch.getKnownTests(), containsInAnyOrder(
                testMatching("test1", thirdBuild, thirdBuild),
                testMatching("test2", thirdBuild, secondBuild),
                testMatching("test3", secondBuild, secondBuild)));

        Collection<TestChangeInformation> testChangeInformations = branch.removeBuild(2L);

        assertThat(branch.getKnownTests(), containsInAnyOrder(
                testMatching("test1", thirdBuild, thirdBuild),
                testMatching("test2", thirdBuild, null)));

        assertThat(testChangeInformations, containsInAnyOrder(
                testChangeMatching("test2", TestChangeInformation.TestChange.MODIFIED),
                testChangeMatching("test3", TestChangeInformation.TestChange.DELETED)));
    }
}