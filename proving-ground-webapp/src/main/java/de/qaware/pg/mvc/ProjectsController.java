package de.qaware.pg.mvc;

import de.qaware.pg.ElementNotFoundException;
import de.qaware.pg.TestResult;
import de.qaware.pg.business.ActionsManager;
import de.qaware.pg.dto.*;
import de.qaware.pg.persistence.Persistence;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

@Controller
@RequestMapping("/projects/")
public class ProjectsController {

    private static final Logger LOGGER = LoggerFactory.getLogger(ProjectsController.class);

    private final Persistence persistence;
    private final ActionsManager actionsManager;

    public ProjectsController(Persistence persistence, ActionsManager actionsManager) {
        this.persistence = persistence;
        this.actionsManager = actionsManager;
    }

    @GetMapping
    public ModelAndView listProjects() {
        List<ProjectView> allProjects = persistence.listAllProjects();
        ModelAndView modelAndView = new ModelAndView("projects", "allProjects", allProjects);
        addNavigation(modelAndView);
        return modelAndView;
    }

    @GetMapping("{projectName}")
    public ModelAndView branchesView(@PathVariable("projectName") String projectName) throws ElementNotFoundException {
        ProjectView project = persistence.findProjectByName(projectName);
        List<BranchWithNewestBuildView> branches = persistence.listBranchesForProject(project.getId());
        branches.sort(Comparator.comparing(b -> b.getNewestBuild().getStartTime()));
        ModelAndView modelAndView = new ModelAndView("branches");
        modelAndView.addObject("project", project);
        modelAndView.addObject("branches", branches);
        addNavigation(modelAndView, project);
        return modelAndView;
    }

    @GetMapping("{projectName}/{branchName}")
    public ModelAndView buildsView(@PathVariable("projectName") String projectName,
                                   @PathVariable("branchName") String branchName,
                                   @RequestParam(value = "showActions", required = false, defaultValue = "false") boolean showActions) throws ElementNotFoundException {
        ProjectView project = persistence.findProjectByName(projectName);
        BranchView branch = persistence.findBranchByName(project.getId(), branchName);
        List<BuildView> builds = persistence.listBuildsForBranch(branch.getId());
        builds.sort(Comparator.comparing(BuildView::getStartTime));
        ModelAndView modelAndView = new ModelAndView("builds");
        modelAndView.addObject("project", project);
        modelAndView.addObject("branch", branch);
        modelAndView.addObject("builds", builds);
        modelAndView.addObject("showActions", showActions);
        addNavigation(modelAndView, project, branch);
        return modelAndView;
    }


    @DeleteMapping("{projectName}/{branchName}/{buildName}")
    @ResponseBody
    public ResponseEntity<Void> deleteBuild(@PathVariable("projectName") String projectName,
                                            @PathVariable("branchName") String branchName,
                                            @PathVariable("buildName") String buildName) {
        try {
            ProjectView project = persistence.findProjectByName(projectName);
            BranchView branch = persistence.findBranchByName(project.getId(), branchName);
            BuildView build = persistence.findBuildByName(branch.getId(), buildName);
            actionsManager.deleteBuild(branch.getId(), build.getId());
            return ResponseEntity.status(HttpStatus.OK).build();
        } catch (ElementNotFoundException | RuntimeException e) {
            LOGGER.error("Error deleting build with id " + buildName, e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @GetMapping("{projectName}/{branchName}/{buildName}")
    public ModelAndView testRunsView(@PathVariable("projectName") String projectName,
                                     @PathVariable("branchName") String branchName,
                                     @PathVariable("buildName") String buildName,
                                     @RequestParam(value = "showAll", required = false, defaultValue = "false") boolean showAll) throws ElementNotFoundException {
        ProjectView project = persistence.findProjectByName(projectName);
        BranchView branch = persistence.findBranchByName(project.getId(), branchName);
        BuildView build = persistence.findBuildByName(branch.getId(), buildName);
        List<TestRunView> testRunsForBuild;
        long buildId = build.getId();
        if (showAll) {
            testRunsForBuild = persistence.listTestRunsForBuild(buildId);
        } else {
            testRunsForBuild = persistence.listTestRunsForBuild(buildId, TestResult.FAILED);
        }

        ModelAndView modelAndView = new ModelAndView("testRuns");
        modelAndView.addObject("project", project);
        modelAndView.addObject("branch", branch);
        modelAndView.addObject("build", build);
        modelAndView.addObject("testRuns", testRunsForBuild);
        modelAndView.addObject("showAll", showAll);
        addNavigation(modelAndView, project, branch, build);
        return modelAndView;
    }

    @GetMapping("{projectName}/{branchName}/{buildName}/{testId}")
    public ModelAndView singleTestRunView(@PathVariable("projectName") String projectName,
                                          @PathVariable("branchName") String branchName,
                                          @PathVariable("buildName") String buildName,
                                          @PathVariable("testId") long testId) throws ElementNotFoundException {
        ProjectView project = persistence.findProjectByName(projectName);
        BranchView branch = persistence.findBranchByName(project.getId(), branchName);
        BuildView build = persistence.findBuildByName(branch.getId(), buildName);

        TestRunDetailsView runDetailsView = persistence.findTestRun(testId);
        ModelAndView modelAndView = new ModelAndView("singleTestRun");
        modelAndView.addObject("project", project);
        modelAndView.addObject("branch", branch);
        modelAndView.addObject("build", build);
        modelAndView.addObject("testRunDetails", runDetailsView);
        addNavigation(modelAndView, project, branch, build, runDetailsView);
        return modelAndView;
    }

    private void addNavigation(ModelAndView modelAndView, NavigationPart... parts) {
        List<NavigationItem> items = new ArrayList<>(parts.length);
        NavigationItem firstItem = new NavigationItem("/projects/", "Home");
        items.add(firstItem);
        NavigationItem previousItem = firstItem;
        for (NavigationPart part : parts) {
            NavigationItem nextItem = new NavigationItem(part, previousItem);
            items.add(nextItem);
            previousItem = nextItem;
        }
        modelAndView.addObject("navigation", items);
        modelAndView.addObject("currentUrl", previousItem.getLink());
    }
}
