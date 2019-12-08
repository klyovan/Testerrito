package com.netcracker.testerritto.mappers;

import com.netcracker.testerritto.models.Test;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

public class TestRowMapper implements RowMapper<Test> {
    @Override
    public Test mapRow(ResultSet resultSet, int i) throws SQLException {
        Test test = new Test();
        test.setId(resultSet.getBigDecimal("test_id").toBigInteger());
        test.setGroupId(resultSet.getBigDecimal("group_id").toBigInteger());
        test.setNameTest(resultSet.getString("test_name"));
        test.setCreatorUserId(resultSet.getBigDecimal("creator_id").toBigInteger());
        return test;
    }
}
