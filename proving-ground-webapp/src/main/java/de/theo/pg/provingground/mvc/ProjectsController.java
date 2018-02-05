package de.theo.pg.provingground.mvc;

import de.theo.pg.provingground.ElementNotFoundException;
import de.theo.pg.provingground.TestResult;
import de.theo.pg.provingground.dto.ProjectView;
import de.theo.pg.provingground.dto.TestRunDetailsView;
import de.theo.pg.provingground.dto.TestRunView;
import de.theo.pg.provingground.dto.TestSuiteView;
import de.theo.pg.provingground.persistence.Persistence;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import javax.ws.rs.QueryParam;
import java.util.List;

@Controller
@RequestMapping("/projects/")
public class ProjectsController {

    private final Persistence persistence;

    public ProjectsController(Persistence persistence) {
        this.persistence = persistence;
    }

    @GetMapping
    public ModelAndView listProjects() {
        List<ProjectView> allProjects = persistence.listAllProjects();
        return new ModelAndView("projects", "allProjects", allProjects);
    }

    @GetMapping("{projectId}")
    public ModelAndView projectView(@PathVariable("projectId") int projectId) throws ElementNotFoundException {
        ProjectView project = persistence.findProject(projectId);
        List<TestSuiteView> testSuites = persistence.findTestSuitesForProject(projectId);
        ModelAndView modelAndView = new ModelAndView("project");
        modelAndView.addObject("project", project);
        modelAndView.addObject("testSuites", testSuites);
        return modelAndView;

    }

    @GetMapping("{projectId}/{runId}")
    public ModelAndView testSuiteView(@PathVariable("projectId") int projectId,
                                      @PathVariable("runId") int runId,
                                      @QueryParam("failedOnly") boolean failedOnly) throws ElementNotFoundException {
        ProjectView project = persistence.findProject(projectId);
        TestSuiteView testSuite = persistence.findTestSuite(runId);
        List<TestRunView> testRunsForSuite;
        if (failedOnly) {
            testRunsForSuite = persistence.findTestRunsForSuite(runId, TestResult.FAILED);
        } else {
            testRunsForSuite = persistence.findTestRunsForSuite(runId);
        }

        if (testSuite.getProjectId() != projectId) {
            throw new ElementNotFoundException("testSuite is not part of this project");
        }
        ModelAndView modelAndView = new ModelAndView("testSuite");
        modelAndView.addObject("project", project);
        modelAndView.addObject("testSuite", testSuite);
        modelAndView.addObject("testRuns", testRunsForSuite);
        modelAndView.addObject("failedOnly", failedOnly);
        return modelAndView;
    }

    @GetMapping("{projectId}/{runId}/{testId}")
    public ModelAndView singleTestView(@PathVariable("projectId") int projectId,
                                       @PathVariable("runId") int runId,
                                       @PathVariable("testId") int testId) throws ElementNotFoundException {
        ProjectView project = persistence.findProject(projectId);
        TestSuiteView testSuite = persistence.findTestSuite(runId);
        TestRunDetailsView runDetailsView = persistence.findTestRun(testId);
        if (testSuite.getProjectId() != projectId) {
            throw new ElementNotFoundException("testSuite is not part of this project");
        }
        if (runDetailsView.getTestSuiteId() != runId) {
            throw new ElementNotFoundException("testRun is not part of this testSuite");
        }
        ModelAndView modelAndView = new ModelAndView("singleTest");
        modelAndView.addObject("project", project);
        modelAndView.addObject("testSuite", testSuite);
        modelAndView.addObject("testRunDetails", runDetailsView);

        return modelAndView;
    }
}
