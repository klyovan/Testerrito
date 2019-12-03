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

import java.util.List;

@Repository
public class GroupDAO{

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Autowired
    public GroupDAO(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public Group getGroupById(Integer groupId) {
        String query =
                        "SELECT groups.object_id AS id, group_name.value AS name, \n" +
                        "       group_link.value AS link, creator.reference AS creatorId\n" +
                        "FROM\t\n" +
                        "\tobjects groups,\n" +
                        "\tattributes group_name,\n" +
                        "    attributes group_link,\n" +
                        "    objreference creator\n" +
                        "WHERE\n" +
                        "\tgroups.object_id = ? /* groupId */\n" +
                        "AND groups.object_id = group_name.object_id\n" +
                        "AND group_name.attr_id = 6 /* NAME_GROUP */\n" +
                        "AND groups.object_id = group_link.object_id\n" +
                        "AND group_link.attr_id = 7 /* LINK */\n" +
                        "AND groups.object_id = creator.object_id\n" +
                        "AND creator.attr_id = 25 /* CREATE_GROUP_BY */";
        return jdbcTemplate.queryForObject(query, new Object[]{groupId}, new GroupRowMapper());
    }

    public void createGroup(Integer userId, String link, String name) {
        String query =
                "INSERT ALL\n" +
                        "\tINTO objects(object_id, parent_id, object_type_id, name, description)\n" +
                        "\t\t valueS(object_id_PR.NEXTVAL, null, 2, 'GROUP '|| object_id_PR.CURRVAL, null)\n" +
                        "\tINTO attributes(object_id, attr_id, value, date_value, list_value_id)\n" +
                        "        valueS(object_id_PR.CURRVAL, 6, ?, null, null) /* name */\n" +
                        "\tINTO attributes(object_id, attr_id, value, date_value, list_value_id)\n" +
                        "        valueS(object_id_PR.CURRVAL, 7, ?, null, null) /* link */\n" +
                        "\tINTO objreference(attr_id, object_id, reference) /* CREATE_GROUP_BY */\n" +
                        "        valueS(25, object_id_PR.CURRVAL, ?) /* userId */\n" +
                        "\tINTO objreference(attr_id, object_id, reference) /* CONSIST */\n" +
                        "        valueS(22, ?, object_id_PR.CURRVAL) /* userId */\n" +
                        "SELECT * FROM DUAL";
        jdbcTemplate.update(query, name, link, userId, userId);
    }

    public void updateGroup(Integer groupId, String newName) {
        String query =
                "UPDATE attributes \n" +
                        "    SET value = ? /* newname */\n" +
                        "        WHERE object_id = ? /* groupId */\n" +
                        "            AND attr_id = 6 /* name_GROUP */";
        jdbcTemplate.update(query, newName, groupId);
    }

    public void deleteGroup(Integer groupId) {
        String query =
                "DELETE FROM objects \n" +
                        "\tWHERE object_id = ? /* groupId */";
        jdbcTemplate.update(query, groupId);
    }

    public List<User> getUsersInGroup(Integer groupId) {
        String query =
                "SELECT users.object_id AS id, user_lastname.value AS lastName,\n" +
                        "       user_firstname.value AS firstName, user_email.value AS email,\n" +
                        "        user_password.value AS password, user_phone.value AS phone\n" +
                        "FROM\n" +
                        "    objreference group2users,\n" +
                        "    objects users,\n" +
                        "    attributes user_lastname,\n" +
                        "    attributes user_firstname,\n" +
                        "    attributes user_email,  \n" +
                        "    attributes user_password,\n" +
                        "    attributes user_phone\n" +
                        "WHERE\n" +
                        "    group2users.reference = ? /* groupId */\n" +
                        "AND group2users.attr_id = 22 /* CONSIST */\n" +
                        "AND group2users.object_id = users.object_id\n" +
                        "AND users.object_id = user_lastname.object_id\n" +
                        "AND user_lastname.attr_id = 1 /* LAST_NAME */\n" +
                        "AND users.object_id = user_firstname.object_id\n" +
                        "AND user_firstname.attr_id = 2 /* FIRST_NAME */\n" +
                        "AND users.object_id = user_email.object_id\n" +
                        "AND user_email.attr_id = 3 /* EMAIL */\n" +
                        "AND users.object_id = user_password.object_id\n" +
                        "AND user_password.attr_id = 4 /* PASSWORD */\n" +
                        "AND users.object_id = user_phone.object_id\n" +
                        "AND user_phone.attr_id = 5 /* PHONE */";
        return jdbcTemplate.query(query, new Object[]{groupId}, new UserRowMapper());
    }

    public List<Test> getAllTestsInGroup(Integer groupId) {
        String query =
                "SELECT tests.object_id AS id, test_name.value as testName,\n" +
                        "       test2creator.reference AS testCreator, \n" +
                        "       case when test2expert.reference is not null then test2expert.reference end as testExpert\n" +
                        "FROM\t\n" +
                        "    objects tests left join objreference test2expert \n" +
                        "                    on (tests.object_id = test2expert.object_id AND test2expert.attr_id = 23),\n" +
                        "    attributes test_name,\n" +
                        "    objreference test2creator\n" +
                        "WHERE\n" +
                        "    tests.PARENT_ID = ? /* groupId */\n" +
                        "AND tests.object_id = test_name.object_id\n" +
                        "AND test_name.attr_id = 9 /* NAME_TEST */\n" +
                        "AND tests.object_id = test2creator.object_id\n" +
                        "AND test2creator.attr_id = 24 /* CREATE_TEST_BY */";
        return jdbcTemplate.query(query, new Object[]{groupId}, new TestRowMapper());
    }
}
