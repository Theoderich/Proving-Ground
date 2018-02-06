package de.theo.pg.provingground.mvc;

import de.theo.pg.provingground.ElementNotFoundException;
import de.theo.pg.provingground.TestResult;
import de.theo.pg.provingground.dto.*;
import de.theo.pg.provingground.persistence.Persistence;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import javax.ws.rs.QueryParam;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

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
    public ModelAndView branchesView(@PathVariable("projectId") long projectId) throws ElementNotFoundException {
        ProjectView project = persistence.findProject(projectId);
        List<BranchView> branches = persistence.listBranchesForProject(projectId);
        ModelAndView modelAndView = new ModelAndView("branches");
        modelAndView.addObject("project", project);
        modelAndView.addObject("branches", branches);
        return modelAndView;
    }

    @GetMapping("{projectId}/{branchId}")
    public ModelAndView buildsView(@PathVariable("projectId") long projectId,
                                   @PathVariable("branchId") long branchId) throws ElementNotFoundException {
        ProjectView project = persistence.findProject(projectId);
        BranchView branch = persistence.findBranch(branchId);
        List<BuildView> builds = persistence.findBuildsForBranch(branchId);
        ModelAndView modelAndView = new ModelAndView("builds");
        modelAndView.addObject("project", project);
        modelAndView.addObject("branch", branch);
        modelAndView.addObject("builds", builds);
        return modelAndView;
    }

    @GetMapping("{projectId}/{branchId}/{buildId}")
    public ModelAndView testRunsView(@PathVariable("projectId") long projectId,
                                     @PathVariable("branchId") long branchId,
                                     @PathVariable("buildId") long buildId,
                                     @QueryParam("failedOnly") boolean failedOnly) throws ElementNotFoundException {
        ProjectView project = persistence.findProject(projectId);
        BranchView branch = persistence.findBranch(branchId);
        BuildView build = persistence.findBuild(buildId);
        List<TestRunView> allTestRuns = persistence.listTestRunsForBuild(buildId);
        Map<Long, TestRunView> testRunMap = allTestRuns.stream().collect(Collectors.toMap(TestRunView::getId, x -> x));
        List<TestRunView> testRunsForBuild;
        if (failedOnly) {
            testRunsForBuild = persistence.listTestRunsForBuild(buildId, TestResult.FAILED);
        } else {
            testRunsForBuild = persistence.listTestRunsForBuild(buildId);
        }


        if (build.getBranchId() != branchId) {
            throw new ElementNotFoundException("build is not part of this branch");
        }


        ModelAndView modelAndView = new ModelAndView("testRuns");
        modelAndView.addObject("project", project);
        modelAndView.addObject("branch", branch);
        modelAndView.addObject("build", build);
        modelAndView.addObject("testRuns", testRunsForBuild);
        modelAndView.addObject("failedOnly", failedOnly);
        modelAndView.addObject("testRunMap", testRunMap);
        return modelAndView;
    }

    @GetMapping("{projectId}/{branchId}/{buildId}/{testId}")
    public ModelAndView singleTestRunView(@PathVariable("projectId") long projectId,
                                          @PathVariable("branchId") long branchId,
                                          @PathVariable("buildId") long buildId,
                                          @PathVariable("testId") long testId) throws ElementNotFoundException {
        ProjectView project = persistence.findProject(projectId);
        BranchView branch = persistence.findBranch(branchId);
        BuildView build = persistence.findBuild(buildId);

        TestRunDetailsView runDetailsView = persistence.findTestRun(testId);
        if (runDetailsView.getBuildId() != buildId) {
            throw new ElementNotFoundException("testRun is not part of this build");
        }
        ModelAndView modelAndView = new ModelAndView("singleTestRun");
        modelAndView.addObject("project", project);
        modelAndView.addObject("branch", branch);
        modelAndView.addObject("build", build);
        modelAndView.addObject("testRunDetails", runDetailsView);

        return modelAndView;
    }
}
