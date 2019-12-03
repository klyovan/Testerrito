package com.netcracker.testerritto.mappers;

import com.netcracker.testerritto.models.Test;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

public class TestRowMapper implements RowMapper<Test> {

    @Override
    public Test mapRow(ResultSet resultSet, int i) throws SQLException {
        Test test = new Test();
        test.setId(resultSet.getInt("id"));
        test.setTestName(resultSet.getString("testName"));
        test.setTestCreator(resultSet.getInt("testCreator"));
        test.setTestExpert(resultSet.getInt("testExpert"));
        return test;
    }
}
