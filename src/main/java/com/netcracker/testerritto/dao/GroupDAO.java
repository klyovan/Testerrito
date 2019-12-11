package com.netcracker.testerritto.dao;

import com.netcracker.testerritto.mappers.GroupRowMapper;
import com.netcracker.testerritto.mappers.TestRowMapper;
import com.netcracker.testerritto.mappers.UserRowMapper;
import com.netcracker.testerritto.models.Group;
import com.netcracker.testerritto.models.Test;
import com.netcracker.testerritto.models.User;
import com.netcracker.testerritto.properties.AttrtypeProperties;
import com.netcracker.testerritto.properties.ObjtypeProperties;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.math.BigInteger;
import java.util.List;

@Repository
public class GroupDAO{

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Autowired
    public GroupDAO(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public Group getGroupById(BigInteger groupId) {
        String query =
                "select\n" +
                "    groups.object_id as id,\n" +
                "    group_name.value as name,\n" +
                "    group_link.value as link,\n" +
                "    creator.reference as creator_id\n" +
                "from\n" +
                "    objects groups,\n" +
                "    attributes group_name,\n" +
                "    attributes group_link,\n" +
                "    objreference creator\n" +
                "where\n" +
                "    groups.object_id = ? /* groupId */\n" +
                "and groups.object_id = group_name.object_id\n" +
                "and group_name.attr_id = 6 /* NAME_GROUP */\n" +
                "and groups.object_id = group_link.object_id\n" +
                "and group_link.attr_id = 7 /* LINK */\n" +
                "and groups.object_id = creator.object_id\n" +
                "and creator.attr_id = 25 /* CREATE_GROUP_BY */";
        return jdbcTemplate.queryForObject(query, new Object[]{groupId.toString()}, new GroupRowMapper());
    }

    public BigInteger createGroup(BigInteger userId, String link, String name) {
        BigInteger object_id = new ObjectEavBuilder.Builder(jdbcTemplate)
                .setObjectTypeId(ObjtypeProperties.GROUP)
                .setName("Group")
                .setStringAttribute(AttrtypeProperties.NAME_GROUP, name)
                .setStringAttribute(AttrtypeProperties.LINK, link)
                .setReference(AttrtypeProperties.CREATE_GROUP_BY, userId)
                .create();
        new ObjectEavBuilder.Builder(jdbcTemplate)
                .setObjectId(userId)
                .setReference(AttrtypeProperties.CONSIST, object_id)
                .update();
        return object_id;
    }

    public void updateGroup(BigInteger groupId, String newName) {
        new ObjectEavBuilder.Builder(jdbcTemplate)
                .setObjectId(groupId)
                .setStringAttribute(AttrtypeProperties.NAME_GROUP, newName)
                .update();
    }

    public void deleteGroup(BigInteger groupId) {
        new ObjectEavBuilder.Builder(jdbcTemplate)
                .setObjectId(groupId)
                .delete();
    }

    public List<User> getUsersInGroup(BigInteger groupId) {
        String query =
                "select\n" +
                "    users.object_id as id,\n" +
                "    user_lastname.value as last_name,\n" +
                "    user_firstname.value as first_name,\n" +
                "    user_email.value as email,\n" +
                "    user_password.value as password,\n" +
                "    user_phone.value as phone\n" +
                "from\n" +
                "    objreference group2users,\n" +
                "    objects users,\n" +
                "    attributes user_lastname,\n" +
                "    attributes user_firstname,\n" +
                "    attributes user_email,  \n" +
                "    attributes user_password,\n" +
                "    attributes user_phone\n" +
                "where\n" +
                "    group2users.reference = ? /* groupId */\n" +
                "and group2users.attr_id = 22 /* CONSIST */\n" +
                "and group2users.object_id = users.object_id\n" +
                "and users.object_id = user_lastname.object_id\n" +
                "and user_lastname.attr_id = 1 /* LAST_NAME */\n" +
                "and users.object_id = user_firstname.object_id\n" +
                "and user_firstname.attr_id = 2 /* FIRST_NAME */\n" +
                "and users.object_id = user_email.object_id\n" +
                "and user_email.attr_id = 3 /* EMAIL */\n" +
                "and users.object_id = user_password.object_id\n" +
                "and user_password.attr_id = 4 /* PASSWORD */\n" +
                "and users.object_id = user_phone.object_id\n" +
                "and user_phone.attr_id = 5 /* PHONE */";
        return jdbcTemplate.query(query, new Object[]{groupId.toString()}, new UserRowMapper());
    }

    public List<Test> getAllTestsInGroup(BigInteger groupId) {
        String query =
                "select\n" +
                "    tests.object_id as test_id,\n" +
                "    tests.parent_id as group_id,\n" +
                "    test_name.value as test_name,\n" +
                "    test2creator.reference as creator_id\n" +
                "from\n" +
                "    objects tests,\n" +
                "    attributes test_name,\n" +
                "    objreference test2creator\n" +
                "where\n" +
                "    tests.parent_id = ? /* groupId */\n" +
                "and tests.object_id = test_name.object_id\n" +
                "and test_name.attr_id = 9 /* NAME_TEST */\n" +
                "and tests.object_id = test2creator.object_id\n" +
                "and test2creator.attr_id = 24 /* CREATE_TEST_BY */";
        return jdbcTemplate.query(query, new Object[]{groupId.toString()}, new TestRowMapper());
    }
}
