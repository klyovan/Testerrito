package com.netcracker.testerritto.dao;

import com.netcracker.testerritto.mappers.GroupRowMapper;
import com.netcracker.testerritto.mappers.TestRowMapper;
import com.netcracker.testerritto.mappers.UserRowMapper;
import com.netcracker.testerritto.models.Group;
import com.netcracker.testerritto.models.Test;
import com.netcracker.testerritto.models.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.math.BigInteger;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

@Repository
public class GroupDAO{

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Autowired
    public GroupDAO(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    private static Logger log=Logger.getLogger(GroupDAO.class.getName());

    public Group getGroupById(BigInteger groupId) {
        if(groupId == null){
            IllegalArgumentException exception = new IllegalArgumentException("GroupId can not be NULL.");
            log.log(Level.SEVERE, exception.getMessage(), groupId);
            throw exception;
        }
        String query =
                        "select\n" +
                        "    groups.object_id as id,\n" +
                        "    group_name.value as name,\n" +
                        "    group_link.value as link,\n" +
                        "    creator.reference as creatorId\n" +
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

    public void createGroup(BigInteger userId, String link, String name) {
        if(userId == null || link.equals("") || name.equals("")){
            IllegalArgumentException exception = new IllegalArgumentException("UserId OR Link OR Name can not be NULL.");
            log.log(Level.SEVERE, exception.getMessage());
            throw exception;
        }
        String query =
                        "insert all\n" +
                        "    into objects(object_id, parent_id, object_type_id, name, description)\n" +
                        "        values(object_id_pr.nextval, null, 2, 'GROUP '|| object_id_pr.currval, null)\n" +
                        "    into attributes(object_id, attr_id, value, date_value, list_value_id)\n" +
                        "        values(object_id_pr.currval, 6, ?, null, null) /* name */\n" +
                        "    into attributes(object_id, attr_id, value, date_value, list_value_id)\n" +
                        "        values(object_id_pr.currval, 7, ?, null, null) /* link */\n" +
                        "    into objreference(attr_id, object_id, reference) /* CREATE_GROUP_BY */\n" +
                        "        values(25, object_id_pr.currval, ?) /* userId */\n" +
                        "    into objreference(attr_id, object_id, reference) /* CONSIST */\n" +
                        "        values(22, ?, object_id_pr.currval) /* userId */\n" +
                        "select * from dual";
        jdbcTemplate.update(query, name, link, userId.toString(), userId.toString());
    }

    public void updateGroup(BigInteger groupId, String newName) {
        if(groupId == null || newName.equals("")){
            IllegalArgumentException exception = new IllegalArgumentException("GroupId OR NewName can not be NULL.");
            log.log(Level.SEVERE, exception.getMessage(), groupId);
            throw exception;
        }
        String query =
                        "update\n" +
                        "    attributes \n" +
                        "set\n" +
                        "    value = ? /* new_name */\n" +
                        "where\n" +
                        "    object_id = ? /* groupId */\n" +
                        "and attr_id = 6 /* NAME_GROUP */";
        jdbcTemplate.update(query, newName, groupId.toString());
    }

    public void deleteGroup(BigInteger groupId) {
        if(groupId == null){
            IllegalArgumentException exception = new IllegalArgumentException("GroupId can not be NULL.");
            log.log(Level.SEVERE, exception.getMessage(), groupId);
            throw exception;
        }
        String query =
                        "delete from\n" +
                        "    objects\n" +
                        "where\n" +
                        "    object_id = ? /* groupId */";
        jdbcTemplate.update(query, groupId.toString());
    }

    public List<User> getUsersInGroup(BigInteger groupId) {
        if(groupId == null){
            IllegalArgumentException exception = new IllegalArgumentException("GroupId can not be NULL.");
            log.log(Level.SEVERE, exception.getMessage(), groupId);
            throw exception;
        }
        String query =
                        "select\n" +
                        "    users.object_id as id,\n" +
                        "    user_lastname.value as lastName,\n" +
                        "    user_firstname.value as firstName,\n" +
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
        if(groupId == null){
            IllegalArgumentException exception = new IllegalArgumentException("GroupId can not be NULL.");
            log.log(Level.SEVERE, exception.getMessage(), groupId);
            throw exception;
        }
        String query =
                        "select\n" +
                        "    tests.object_id as id,\n" +
                        "    test_name.value as testName,\n" +
                        "    test2creator.reference as testCreator, \n" +
                        "case when\n" +
                        "    test2expert.reference is not null\n" +
                        "then\n" +
                        "    test2expert.reference\n" +
                        "end as testExpert\n" +
                        "from\n" +
                        "    objects tests\n" +
                        "left join objreference test2expert\n" +
                        "on (tests.object_id = test2expert.object_id and test2expert.attr_id = 23),\n" +
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
