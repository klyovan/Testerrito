package com.netcracker.testerritto.mappers;

import com.netcracker.testerritto.models.Test;
import org.springframework.jdbc.core.RowMapper;

import java.math.BigInteger;
import java.sql.ResultSet;
import java.sql.SQLException;

public class TestRowMapper implements RowMapper<Test> {
    @Override
    public Test mapRow(ResultSet resultSet, int i) throws SQLException {
        Test test = new Test();
        test.setId(new BigInteger(resultSet.getString("test_id")));
        test.setGroupId(new BigInteger(resultSet.getString("group_id")));
        test.setNameTest(resultSet.getString("test_name"));
        test.setCreatorUserId(new BigInteger(resultSet.getString("creator_id")));
        return test;
    }
}
