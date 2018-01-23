package de.theo.pg.provingground.persistence;

import de.theo.pg.provingground.dto.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;
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


    public List<ProjectView> findProjects() {
        return jdbcTemplate.query("SELECT id, name, status FROM project", this::convertToProjectHeader);
    }

    public ProjectView findProject(int id) {
        ProjectView projectHeaderView =
                jdbcTemplate.queryForObject(
                        "SELECT id, name, status FROM project WHERE id=?",
                        new Object[]{id},
                        this::convertToProjectHeader);

        List<TestSuiteView> suiteHeaders =
                jdbcTemplate.query("SELECT ts.name, ts.start_time, ts.status FROM project p JOIN testsuite ts  ON ts.fk_project_id=p.id WHERE p.id=?",
                new Object[]{id},
                this::convertToTestSuiteHeader);
        return new ProjectView(projectHeaderView, suiteHeaders);
    }


    public TestSuiteView findTestSuite(int id){
        jdbcTemplate.queryForObject("Select ts.name, ts.start_time, ")
    }



    private ProjectView convertToProjectHeader(ResultSet rs, int rowNum) throws SQLException {
        int id = rs.getInt("id");
        String name = rs.getString("name");
        Status status = convertToStatus(rs.getInt("status"));
        return new ProjectView(id, name, status);
    }

    private TestSuiteView convertToTestSuiteHeader(ResultSet rs, int rowNum) throws SQLException {
        int id = rs.getInt("id");
        String name = rs.getString("name");
        LocalDateTime startTime = rs.getTimestamp("start_time").toLocalDateTime();
        Status status = convertToStatus(rs.getInt("status"));
        return new TestSuiteView(id, name, startTime, status);
    }

    private Status convertToStatus(int ordinal) throws SQLException {
        return Status.values()[ordinal];
    }


}
