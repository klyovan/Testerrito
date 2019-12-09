package com.netcracker.testerritto.mappers;

import com.netcracker.testerritto.models.Group;
import org.springframework.jdbc.core.RowMapper;

import java.math.BigInteger;
import java.sql.ResultSet;
import java.sql.SQLException;

public class GroupRowMapper implements RowMapper<Group> {

    @Override
    public Group mapRow(ResultSet resultSet, int i) throws SQLException {
        Group group = new Group();
        group.setId(new BigInteger(resultSet.getString("id")));
        group.setName(resultSet.getString("name"));
        group.setLink(resultSet.getString("link"));
        group.setCreatorUserId(new BigInteger(resultSet.getString("creator_id")));
        return group;
    }
}
