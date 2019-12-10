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
        test.setId(BigInteger.valueOf(resultSet.getInt("test_id")));
        test.setGroupId(BigInteger.valueOf(resultSet.getInt("group_id")));
        test.setNameTest(resultSet.getString("test_name"));
        test.setCreatorUserId(BigInteger.valueOf(resultSet.getInt("creator_id")));
        return test;
    }
}
