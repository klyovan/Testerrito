package com.netcracker.testerritto.dao;

import com.netcracker.testerritto.ApplicationConfiguration;
import com.netcracker.testerritto.DataSourceConfig;
import com.netcracker.testerritto.models.Group;
import com.netcracker.testerritto.models.User;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import static org.junit.Assert.assertEquals;

@RunWith(SpringJUnit4ClassRunner.class)
@WebAppConfiguration
@ContextConfiguration(classes = { ApplicationConfiguration.class, DataSourceConfig.class })
public class TestGroupDAO {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Autowired
    public GroupDAO groupDAO;

    private Integer sequenceId;
    private Integer creatorId;

    @Before
    public void setUp(){
        Locale.setDefault(Locale.ENGLISH);
        jdbcTemplate.update(
                "INSERT ALL\n" +
                        "  INTO objects(object_id, parent_id, object_type_id, name, description)\n" +
                        "      VALUES(object_id_PR.NEXTVAL, null, 1, 'USER_CREATOR '|| object_id_PR.CURRVAL, null)\n" +
                        "SELECT * FROM DUAL");
        creatorId = jdbcTemplate.queryForObject("SELECT object_id_pr.CURRVAL FROM DUAL", Integer.class);
        groupDAO.createGroup(creatorId, "New Link http...", "Very cool group");
        sequenceId = jdbcTemplate.queryForObject("SELECT object_id_pr.CURRVAL FROM DUAL", Integer.class);
    }

    @After
    public void setDown(){
        jdbcTemplate.update(
                "DELETE FROM objects WHERE object_id = ?", creatorId);
        jdbcTemplate.update(
                "DELETE FROM objects WHERE object_id = ?", sequenceId);
    }

    @Test
    public void insertGetGroup() throws Exception{
        Group groupExpected = new Group();
        groupExpected.setGroup_id(sequenceId);
        groupExpected.setCreatorUserId(creatorId);
        groupExpected.setName("Very cool group");
        groupExpected.setLink("New Link http...");

        Group group = groupDAO.getGroupById(sequenceId);

        assertEquals(groupExpected, group);
    }

    @Test
    public void updateGroupById() throws Exception{
        groupDAO.updateGroup(sequenceId, "New very-very cool name");
        Group group = groupDAO.getGroupById(sequenceId);

        Group groupExpected = new Group();
        groupExpected.setGroup_id(sequenceId);
        groupExpected.setCreatorUserId(creatorId);
        groupExpected.setName("New very-very cool name");
        groupExpected.setLink("New Link http...");

        assertEquals(groupExpected, group);
    }

    @Test(expected = EmptyResultDataAccessException.class)
    public void deleteGroupById() throws Exception{
        groupDAO.deleteGroup(sequenceId);

        Group group = groupDAO.getGroupById(sequenceId);
    }

    @Test
    public void getAllUsersInGroup() throws Exception{
        jdbcTemplate.update(
                "INSERT ALL\n" +
                        "    INTO attributes(object_id, attr_id, value, date_value, list_value_id)\n" +
                        "        VALUES(?,1,'User_lastname',null,null)\n" +
                        "    INTO attributes(object_id, attr_id, value, date_value, list_value_id)\n" +
                        "        VALUES(?,2,'User_firstname',null,null)\n" +
                        "    INTO attributes(object_id, attr_id, value, date_value, list_value_id)\n" +
                        "        VALUES(?,3,'User_email',null,null)\n" +
                        "    INTO attributes(object_id, attr_id, value, date_value, list_value_id)\n" +
                        "        VALUES(?,4,'User_password',null,null)\n" +
                        "    INTO attributes(object_id, attr_id, value, date_value, list_value_id)\n" +
                        "        VALUES(?,5,'User_phone',null,null)\n" +
                        "SELECT * FROM DUAL", creatorId, creatorId, creatorId, creatorId, creatorId);
        List<User> users = groupDAO.getUsersInGroup(sequenceId);

        List<User> usersExpected = new ArrayList();
        User user = new User();
        user.setId(creatorId);
        user.setLast_name("User_lastname");
        user.setFirst_name("User_firstname");
        user.setEmail("User_email");
        user.setPassword("User_password");
        user.setPhone("User_phone");
        usersExpected.add(user);

        assertEquals(usersExpected, users);
    }

    @Test
    public void getAllTestsInGroup() throws Exception{
        jdbcTemplate.update(
                "INSERT ALL\n" +
                        "    INTO objects(object_id, parent_id, object_type_id, name, description)\n" +
                        "        VALUES(object_id_PR.NEXTVAL, ?, 4, 'MyTest', null)\n" +
                        "    INTO attributes(object_id, attr_id, value, date_value, list_value_id)\n" +
                        "        VALUES(object_id_PR.CURRVAL, 9, 'MyTest', null, null)\n" +
                        "    INTO objreference(attr_id, object_id, reference)\n" +
                        "        VALUES(24, object_id_PR.CURRVAL, ?)\n" +
                        "SELECT * FROM DUAL", sequenceId, creatorId);
        Integer testId = jdbcTemplate.queryForObject("SELECT object_id_pr.CURRVAL FROM DUAL", Integer.class);
        List<com.netcracker.testerritto.models.Test> tests = groupDAO.getAllTestsInGroup(sequenceId);

        List<com.netcracker.testerritto.models.Test> testsExpected = new ArrayList<>();
        com.netcracker.testerritto.models.Test test = new com.netcracker.testerritto.models.Test();
        test.setId(testId);
        test.setTestName("MyTest");
        test.setTestCreator(creatorId);
        testsExpected.add(test);

        assertEquals(testsExpected, tests);
    }
}
