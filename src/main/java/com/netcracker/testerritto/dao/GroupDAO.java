package com.netcracker.testerritto.dao;

import com.netcracker.testerritto.mappers.GroupRowMapper;
import com.netcracker.testerritto.mappers.RemarkRowMapper;
import com.netcracker.testerritto.mappers.TestRowMapper;
import com.netcracker.testerritto.mappers.UserRowMapper;
import com.netcracker.testerritto.models.Group;
import com.netcracker.testerritto.models.Remark;
import com.netcracker.testerritto.models.Test;
import com.netcracker.testerritto.models.User;
import com.netcracker.testerritto.properties.AttrtypeProperties;
import com.netcracker.testerritto.properties.ObjtypeProperties;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigInteger;
import java.util.List;

@Repository
@Transactional
public class GroupDAO{

    private String GET_GROUP_BY_ID =
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
        "    and groups.object_id = group_name.object_id\n" +
        "    and group_name.attr_id = 6 /* NAME_GROUP */\n" +
        "    and groups.object_id = group_link.object_id\n" +
        "    and group_link.attr_id = 7 /* LINK */\n" +
        "    and groups.object_id = creator.object_id\n" +
        "    and creator.attr_id = 25 /* CREATE_GROUP_BY */";

    private  String GET_USERS_IN_GROUP =
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
        "    and group2users.attr_id = 22 /* CONSIST */\n" +
        "    and group2users.object_id = users.object_id\n" +
        "    and users.object_id = user_lastname.object_id\n" +
        "    and user_lastname.attr_id = 1 /* LAST_NAME */\n" +
        "    and users.object_id = user_firstname.object_id\n" +
        "    and user_firstname.attr_id = 2 /* FIRST_NAME */\n" +
        "    and users.object_id = user_email.object_id\n" +
        "    and user_email.attr_id = 3 /* EMAIL */\n" +
        "    and users.object_id = user_password.object_id\n" +
        "    and user_password.attr_id = 4 /* PASSWORD */\n" +
        "    and users.object_id = user_phone.object_id\n" +
        "    and user_phone.attr_id = 5 /* PHONE */";

    private  String GET_ALL_TEST_IN_GROUP =
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
        "    and tests.object_id = test_name.object_id\n" +
        "    and test_name.attr_id = 9 /* NAME_TEST */\n" +
        "    and tests.object_id = test2creator.object_id\n" +
        "    and test2creator.attr_id = 24 /* CREATE_TEST_BY */";

    private String GET_ALL_REMARKS =
        "select    \n" +
        "     question2remark.object_id as id,    \n" +
        "     remark_text.value as text,    \n" +
        "     sender.reference as user_id,    \n" +
        "     question2remark.reference as question_id,    \n" +
        "     recipient.reference as recipient_id,\n" +
        "     viewed_status.value as viewed\n" +
        " from    \n" +
        "     objects tests,        \n" +
        "     objects questions,        \n" +
        "     objreference question2remark,    \n" +
        "     attributes remark_text,    \n" +
        "     attributes viewed_status,\n" +
        "     objreference sender,    \n" +
        "     objreference recipient     \n" +
        " where        \n" +
        "     tests.parent_id = ? /*groupId*/        \n" +
        "     and questions.parent_id = tests.object_id        \n" +
        "     and questions.object_type_id = 10 /*Question*/        \n" +
        "     and question2remark.attr_id = 28    /*CAUSED_BY*/        \n" +
        "     and question2remark.reference = questions.object_id    \n" +
        "     and remark_text.object_id = question2remark.object_id    \n" +
        "     and remark_text.attr_id = 8 /* REMARK_TEXT */  \n" +
        "     and viewed_status.object_id = question2remark.object_id\n" +
        "     and viewed_status.attr_id = 39 /* REMARK_VIEWED */\n" +
        "     and sender.object_id = question2remark.object_id    \n" +
        "     and sender.attr_id = 26 /* SEND */    \n" +
        "     and recipient.attr_id = 27  /*PROCESS_BY*/        \n" +
        "     and recipient.object_id = question2remark.object_id ";

    private String CHECK_UNIQUE_GROUP_NAME=
        "select \n"+
        "    count(*) \n"+
        "from\n"+
        "    objreference group2user,\n"+
        "    attributes group_attributes\n"+
        "where\n"+
        "    group2user.attr_id = 25 /* CREATE_GROUP_BY */\n"+
        "    and group2user.reference = ? /* userId */\n"+
        "    and group2user.object_id = group_attributes.object_id\n"+
        "    and group_attributes.attr_id = 6 /* NAME_GROUP */\n"+
        "    and group_attributes.value = ? /* groupName */";

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Autowired
    public GroupDAO(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public boolean isGroupNameExist(BigInteger userId, String groupName) {
        Integer i = jdbcTemplate.queryForObject(CHECK_UNIQUE_GROUP_NAME, new Object[]{userId.toString(), groupName}, Integer.class);
        return (i == 1);
    }

    public Group getGroupById(BigInteger groupId) {
        return jdbcTemplate.queryForObject(GET_GROUP_BY_ID, new Object[]{groupId.toString()}, new GroupRowMapper());
    }

    public BigInteger createGroup(Group group) {
        BigInteger objectId = new ObjectEavBuilder.Builder(jdbcTemplate)
            .setObjectTypeId(ObjtypeProperties.GROUP)
            .setName("Group")
            .setStringAttribute(AttrtypeProperties.NAME_GROUP, group.getName())
            .setStringAttribute(AttrtypeProperties.LINK, group.getLink())
            .setReference(AttrtypeProperties.CREATE_GROUP_BY, group.getCreatorUserId())
            .create();
        new ObjectEavBuilder.Builder(jdbcTemplate)
            .setObjectId(group.getCreatorUserId())
            .setReference(AttrtypeProperties.CONSIST, objectId)
            .createReference();
        return objectId;
    }

    public Group updateGroup(Group group) {
        new ObjectEavBuilder.Builder(jdbcTemplate)
            .setObjectId(group.getId())
            .setStringAttribute(AttrtypeProperties.NAME_GROUP, group.getName())
            .update();
        return getGroupById(group.getId());
    }

    public void deleteGroup(BigInteger groupId) {
        new ObjectEavBuilder.Builder(jdbcTemplate)
            .setObjectId(groupId)
            .delete();
    }

    public List<User> getUsersInGroup(BigInteger groupId) {
        return jdbcTemplate.query(GET_USERS_IN_GROUP, new Object[]{groupId.toString()}, new UserRowMapper());
    }

    public List<Test> getAllTestsInGroup(BigInteger groupId) {
        return jdbcTemplate.query(GET_ALL_TEST_IN_GROUP, new Object[]{groupId.toString()}, new TestRowMapper());
    }

    public List<Remark> getAllRemarksInGroup(BigInteger groupId) {
        List<Remark> remarks = jdbcTemplate.query(GET_ALL_REMARKS, new Object[]{groupId.toString()}, new RemarkRowMapper());
        return remarks;
    }
}
