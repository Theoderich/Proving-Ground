package de.theo.pg.provingground.persistence;

import de.theo.pg.provingground.Project;

import java.util.List;

public interface Persistence {

    List<String> findProjectNames();

    Project findProject(String name);

}
