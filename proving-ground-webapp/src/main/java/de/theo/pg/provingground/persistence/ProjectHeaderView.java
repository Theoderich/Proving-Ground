package de.theo.pg.provingground.persistence;

import org.springframework.jdbc.core.JdbcTemplate;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

/**
 * Created by a theo on 22.01.2018.
 */
public class ProjectHeaderView {

    private final String name;
    private final Status status;

    private ProjectHeaderView(String name, Status status) {
        this.name = name;
        this.status = status;
    }

    public String getName() {
        return name;
    }

    public Status getStatus() {
        return status;
    }


    public static List<ProjectHeaderView> fetchFromDatabase(JdbcTemplate jdbcTemplate) {
        return jdbcTemplate.query("SELECT name, status FROM project", ProjectHeaderView::convertRow);
    }

    private static ProjectHeaderView convertRow(ResultSet rs, int rowNum) throws SQLException {
        String name = rs.getString("name");
        Status status = Status.values()[rs.getInt("status")];
        return new ProjectHeaderView(name, status);
    }
}
