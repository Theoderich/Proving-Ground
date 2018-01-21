package de.theo.pg.provingground.persistence;

import de.theo.pg.provingground.Project;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;

import java.util.List;

/**
 * Created by a theo on 21.01.2018.
 */
public class DatabasePersistence implements Persistence {

    private final JdbcTemplate jdbcTemplate;

    @Autowired
    public DatabasePersistence(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }


    @Override
    public List<String> findProjectNames() {
        return jdbcTemplate.query("SELECT name FROM project", (rs, rowNum) -> rs.getString("name"));
    }

    @Override
    public Project findProject(String name) {
        return null;
    }

    public List<String> findProjectTestRunNames(String name) {
        return jdbcTemplate.query("SELECT ts.name AS tsName FROM project p JOIN testsuite ts ON ts.fk_project_id=p.id WHERE p.name=?",
                new Object[]{name},
                (rs, rowNum) -> rs.getString("tsName")
        );
    }
}
