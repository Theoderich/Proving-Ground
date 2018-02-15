# Proving Ground

Standalone test result visualization webapp for those CI environments that lack this feature

## Usage
### Basics
Proving Ground is a Spring Boot web application. Simply start the proving-ground-webapp.jar with
java -jar proving-ground-webapp.jar --server.port={port} to start the webapp on the given port.

The GUI can be reached via http://{server-url}:{port}/projects/

New Test-Builds can be pushed as a POST-Request to http://{server-url}:{port}/input/ 

### Maven Plugin
The main way to push new Test-Builds into proving-ground is via the provided maven plugin.
Add the following to your pom (or root pom for multi-module builds):

```xml
<plugin>
    <groupId>de.qaware.pg</groupId>
   <artifactId>proving-ground-maven-plugin</artifactId>
    <version>{proving-ground-version}</version>
    <configuration>
        <reportUrl>to http://{server-url}:{port}/input/ </reportUrl>
        <projectName>{project.name}</projectName> <!-- optional, project.name is default -->
        <reportsDirectories>
            <reportsDirectory>target/surefire-reports</reportsDirectory>
            <reportsDirectory>target/failsafe-reports</reportsDirectory>
        </reportsDirectories>
    </configuration>
</plugin>
```
If you use non-standard directories for your test-results, adjust the reportsDirectory parameter accordingly.
The proving-ground-maven-plugin will search for surefire-xml-reports in each of the given paths
(relative to project.basepath) in every in the current project and every submodule of the project.

To use the plugin, first execute all your tests so the test reports are present in the configured folders,
then use the following command line:
```bash
mvn de.qaware.pg:proving-ground-maven-plugin:report -DbuildName={buildName} -DbranchName={branchName} -DcommitId={commitId}
````

You will have to provide three parameters to the plugin on each run:
- buildName: A unique name for the current build. Recommended: Build number of your buildserver
- branchName: The name of the source control branch you are currently operating on
- commitId: The id of the commit this build is based on (commitId is optional and may be omitted) 

## planned features 

- Better user interface
- gradle integration