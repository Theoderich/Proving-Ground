package de.theo.pg.provingground.mvc;

import de.theo.pg.provingground.ElementNotFoundException;
import de.theo.pg.provingground.Project;
import de.theo.pg.provingground.TestExecution;
import de.theo.pg.provingground.TestRun;
import de.theo.pg.provingground.persistence.Persistence;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import javax.ws.rs.QueryParam;
import java.util.List;

@Controller
@RequestMapping("/projects")
public class ProjectsController {

    private final Persistence persistence;

    public ProjectsController(Persistence persistence) {
        this.persistence = persistence;
    }

    @GetMapping
    public ModelAndView listProjects() {
        List<String> projectNames = persistence.findProjectNames();
        return new ModelAndView("projects", "projectNames", projectNames);
    }

    @GetMapping("{projectName}")
    public ModelAndView projectView(@PathVariable("projectName") String projectName) {
        Project project = persistence.findProject(projectName);
        return new ModelAndView("project", "project", project);
    }

    @GetMapping("{projectName}/{runIndex}")
    public ModelAndView runView(@PathVariable("projectName") String projectName,
                                @PathVariable("runIndex") long runIndex,
                                @QueryParam("failedOnly") boolean failedOnly) throws ElementNotFoundException {
        Project project = persistence.findProject(projectName);
        TestRun testRun = project.getTestRun(runIndex);
        ModelAndView modelAndView = new ModelAndView("testRun");
        modelAndView.addObject("testRun", testRun);
        modelAndView.addObject("failedOnly", failedOnly);
        return modelAndView;
    }

    @GetMapping("{projectName}/{runIndex}/{testName:.+}")
    public ModelAndView singleTestView(@PathVariable("projectName") String projectName,
                                       @PathVariable("runIndex") long runIndex,
                                       @PathVariable("testName") String testName) throws ElementNotFoundException {
        Project project = persistence.findProject(projectName);
        TestRun testRun = project.getTestRun(runIndex);
        TestExecution testExecution = testRun.getTestExecution(testName);
        ModelAndView modelAndView = new ModelAndView("singleTest");
        modelAndView.addObject("testExecution", testExecution);
        return modelAndView;
    }
}
