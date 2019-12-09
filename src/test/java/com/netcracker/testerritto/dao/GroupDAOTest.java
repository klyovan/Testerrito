package com.netcracker.testerritto.dao;

import com.netcracker.testerritto.ApplicationConfiguration;
import com.netcracker.testerritto.DataSourceConfig;
import com.netcracker.testerritto.models.Group;
import com.netcracker.testerritto.models.User;
import com.netcracker.testerritto.properties.AttrtypeProperties;
import com.netcracker.testerritto.properties.ObjtypeProperties;
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

    @Before
    public void setUp(){
        Locale.setDefault(Locale.ENGLISH);
        creatorId = new ObjectEavBuilder.Builder(jdbcTemplate)
                .setObjectTypeId(new BigInteger("1"))
                .setName("USER_CREATOR")
                .create();
    }

    @After
    public void setDown(){
        new ObjectEavBuilder.Builder(jdbcTemplate)
                .setObjectId(creatorId)
                .delete();
        if(sequenceId != null)
            new ObjectEavBuilder.Builder(jdbcTemplate)
                    .setObjectId(sequenceId)
                    .delete();
    }

    @Test
    public void insertGetGroup() throws Exception{
        sequenceId = groupDAO.createGroup(creatorId, "New Link http...", "Very cool group");
        Group groupExpected = new Group(sequenceId, creatorId, "Very cool group","New Link http...", null, null);
        Group group = groupDAO.getGroupById(sequenceId);
        assertEquals(groupExpected, group);
    }

    @Test
    public void updateGroupById() throws Exception{
        sequenceId = groupDAO.createGroup(creatorId, "New Link http...", "Very cool group");
        groupDAO.updateGroup(sequenceId, "New very-very cool name");
        Group group = groupDAO.getGroupById(sequenceId);
        Group groupExpected = new Group(sequenceId, creatorId, "New very-very cool name", "New Link http...", null, null);
        assertEquals(groupExpected, group);
    }

    @Test(expected = EmptyResultDataAccessException.class)
    public void deleteGroupById() throws Exception{
        sequenceId = groupDAO.createGroup(creatorId, "New Link http...", "Very cool group");
        groupDAO.deleteGroup(sequenceId);
        Group group = groupDAO.getGroupById(sequenceId);
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

    @Test
    public void getAllTestsInGroup() throws Exception{
        sequenceId = groupDAO.createGroup(creatorId, "New Link http...", "Very cool group");
        BigInteger testId = new ObjectEavBuilder.Builder(jdbcTemplate)
                .setParentId(sequenceId)
                .setObjectTypeId(new BigInteger(String.valueOf(ObjtypeProperties.TEST)))
                .setName("MyTest")
                .setStringAttribute(new BigInteger(String.valueOf(AttrtypeProperties.NAME_TEST)), "MyTest")
                .setReference(new BigInteger(String.valueOf(AttrtypeProperties.CREATE_TEST_BY)), creatorId)
                .create();
        List<com.netcracker.testerritto.models.Test> tests = groupDAO.getAllTestsInGroup(sequenceId);

        List<com.netcracker.testerritto.models.Test> testsExpected = new ArrayList<>();
        com.netcracker.testerritto.models.Test test = new com.netcracker.testerritto.models.Test();
        test.setId(testId);
        test.setGroupId(sequenceId);
        test.setNameTest("MyTest");
        test.setCreatorUserId(creatorId);
        testsExpected.add(test);

        assertEquals(testsExpected, tests);
    }
}
