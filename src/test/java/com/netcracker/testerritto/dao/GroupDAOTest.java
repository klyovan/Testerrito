package com.netcracker.testerritto.dao;

import com.netcracker.testerritto.ApplicationConfiguration;
import com.netcracker.testerritto.DataSourceConfig;
import com.netcracker.testerritto.models.Group;
import com.netcracker.testerritto.models.User;
import org.junit.After;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import static org.junit.Assert.assertEquals;

@RunWith(SpringJUnit4ClassRunner.class)
@WebAppConfiguration
@ContextConfiguration(classes = { ApplicationConfiguration.class, DataSourceConfig.class })
public class GroupDAOTest {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Autowired
    public GroupDAO groupDAO;

    private BigInteger sequenceId;
    private BigInteger creatorId;
    private static String CURRENT_SEQUENCE = "select object_id_pr.currval from dual";

    @Before
    public void setUp(){
        Locale.setDefault(Locale.ENGLISH);
        jdbcTemplate.update(
                    "insert all\n" +
                        "    into objects(object_id, parent_id, object_type_id, name, description)\n" +
                        "        values(object_id_pr.nextval, null, 1, 'USER_CREATOR '|| object_id_pr.currval, null)\n" +
                        "select * from dual");
        creatorId = jdbcTemplate.queryForObject(CURRENT_SEQUENCE, BigInteger.class);
    }

    @After
    public void setDown(){
        String sql =
                        "delete from\n" +
                        "    objects\n" +
                        "where\n" +
                        "    object_id = ?";
        jdbcTemplate.update(sql, creatorId.toString());
        if(sequenceId != null)
            jdbcTemplate.update(sql, sequenceId.toString());
    }

    @Test
    public void insertGetGroup() throws Exception{
        groupDAO.createGroup(creatorId, "New Link http...", "Very cool group");
        sequenceId = jdbcTemplate.queryForObject(CURRENT_SEQUENCE, BigInteger.class);

        Group groupExpected = new Group(sequenceId, creatorId, "Very cool group","New Link http...", null, null);

        Group group = groupDAO.getGroupById(sequenceId);

        assertEquals(groupExpected, group);
    }

    @Test(expected = IllegalArgumentException.class)
    public void insertGroupByCreatorIdNull() throws Exception{
        groupDAO.createGroup(null, "New Link http...", "Very cool group");
    }

    @Test(expected = IllegalArgumentException.class)
    public void insertGroupByLinkNull() throws Exception{
        groupDAO.createGroup(creatorId, "", "Very cool group");
    }

    @Test(expected = IllegalArgumentException.class)
    public void insertGroupByNameNull() throws Exception{
        groupDAO.createGroup(creatorId, "New Link http...", "");
    }

    @Test
    public void updateGroupById() throws Exception{
        groupDAO.createGroup(creatorId, "New Link http...", "Very cool group");
        sequenceId = jdbcTemplate.queryForObject(CURRENT_SEQUENCE, BigInteger.class);

        groupDAO.updateGroup(sequenceId, "New very-very cool name");
        Group group = groupDAO.getGroupById(sequenceId);

        Group groupExpected = new Group(sequenceId, creatorId, "New very-very cool name", "New Link http...", null, null);

        assertEquals(groupExpected, group);
    }

    @Test(expected = IllegalArgumentException.class)
    public void updateGroupByGroupIdNull() throws Exception{
        groupDAO.updateGroup(null, "New very-very cool name");
    }

    @Test(expected = IllegalArgumentException.class)
    public void updateGroupByNewNameNull() throws Exception{
        groupDAO.createGroup(creatorId, "New Link http...", "Very cool group");
        sequenceId = jdbcTemplate.queryForObject(CURRENT_SEQUENCE, BigInteger.class);
        groupDAO.updateGroup(sequenceId, "");
    }

    @Test(expected = EmptyResultDataAccessException.class)
    public void deleteGroupById() throws Exception{
        groupDAO.createGroup(creatorId, "New Link http...", "Very cool group");
        sequenceId = jdbcTemplate.queryForObject(CURRENT_SEQUENCE, BigInteger.class);

        groupDAO.deleteGroup(sequenceId);

        Group group = groupDAO.getGroupById(sequenceId);
    }

    @Test(expected = IllegalArgumentException.class)
    public void deleteGroupByIdNull() throws Exception{
        groupDAO.deleteGroup(null);
    }

    @Ignore//пока не поменятться сущность User не открывтаь тест
    @Test
    public void getAllUsersInGroup() throws Exception{
        jdbcTemplate.update(
                    "insert all\n" +
                        "    into attributes(object_id, attr_id, value, date_value, list_value_id)\n" +
                        "        values(?,1,'User_lastname',null,null)\n" +
                        "    into attributes(object_id, attr_id, value, date_value, list_value_id)\n" +
                        "        values(?,2,'User_firstname',null,null)\n" +
                        "    into attributes(object_id, attr_id, value, date_value, list_value_id)\n" +
                        "        values(?,3,'User_email',null,null)\n" +
                        "    into attributes(object_id, attr_id, value, date_value, list_value_id)\n" +
                        "        values(?,4,'User_password',null,null)\n" +
                        "    into attributes(object_id, attr_id, value, date_value, list_value_id)\n" +
                        "        values(?,5,'User_phone',null,null)\n" +
                        "select * from dual", creatorId, creatorId, creatorId, creatorId, creatorId);
        List<User> users = groupDAO.getUsersInGroup(sequenceId);

        List<User> usersExpected = new ArrayList();
        User user = new User();
       // user.setId(creatorId);
       // user.setLast_name("User_lastname");
      //  user.setFirst_name("User_firstname");
       // user.setEmail("User_email");
      //  user.setPassword("User_password");
      //  user.setPhone("User_phone");
      //  usersExpected.add(user);

        assertEquals(usersExpected, users);
    }

    @Ignore//пока не поменятться сущность Test не открывтаь тест
    @Test
    public void getAllTestsInGroup() throws Exception{
        jdbcTemplate.update(
                    "insert all\n" +
                        "    into objects(object_id, parent_id, object_type_id, name, description)\n" +
                        "        values(object_id_pr.nextval, ?, 4, 'MyTest', null)\n" +
                        "    into attributes(object_id, attr_id, value, date_value, list_value_id)\n" +
                        "        values(object_id_pr.currval, 9, 'MyTest', null, null)\n" +
                        "    into objreference(attr_id, object_id, reference)\n" +
                        "        values(24, object_id_pr.currval, ?)\n" +
                        "select * from dual", sequenceId, creatorId);
        Integer testId = jdbcTemplate.queryForObject("select object_id_pr.currval from dual", Integer.class);
        List<com.netcracker.testerritto.models.Test> tests = groupDAO.getAllTestsInGroup(sequenceId);

        List<com.netcracker.testerritto.models.Test> testsExpected = new ArrayList<>();
        com.netcracker.testerritto.models.Test test = new com.netcracker.testerritto.models.Test();
     //   test.setId(testId);
     //   test.setTestName("MyTest");
      //  test.setTestCreator(creatorId);
       // testsExpected.add(test);

        assertEquals(testsExpected, tests);
    }
}
