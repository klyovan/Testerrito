package com.netcracker.testerritto.services;

import com.netcracker.testerritto.ApplicationConfiguration;
import com.netcracker.testerritto.DataSourceConfig;
import com.netcracker.testerritto.dao.ObjectEavBuilder;
import com.netcracker.testerritto.exceptions.ServiceException;
import com.netcracker.testerritto.models.Group;
import com.netcracker.testerritto.models.User;
import com.netcracker.testerritto.properties.AttrtypeProperties;
import com.netcracker.testerritto.properties.ObjtypeProperties;
import org.junit.After;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
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
@ContextConfiguration(classes = {ApplicationConfiguration.class, DataSourceConfig.class})
public class GroupServiceTest {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Autowired
    private GroupService groupService;

    private BigInteger sequenceId;
    private BigInteger creatorId;
    private Group groupExpected = new Group();

    @After
    public void setDown(){
        if(creatorId != null)
            new ObjectEavBuilder.Builder(jdbcTemplate)
                .setObjectId(creatorId)
                .delete();
        if(sequenceId != null)
            new ObjectEavBuilder.Builder(jdbcTemplate)
                .setObjectId(sequenceId)
                .delete();
    }

    @Test(expected = IllegalArgumentException.class)
    public void getGroupByIdNull() throws Exception {
        groupService.getGroupById(null);
    }

    @Test(expected = ServiceException.class)
    public void getGroupByWrongId() throws Exception {
        groupService.getGroupById(new BigInteger("-666"));
    }

    @Test
    public void createAndGetGroupById() throws Exception {
        createTestValues();
        sequenceId = groupService.createGroup(groupExpected);
        groupExpected.setId(sequenceId);
        Group group = groupService.getGroupById(sequenceId);
        assertEquals(groupExpected.getId(), group.getId());
        assertEquals(groupExpected.getName(), group.getName());
        assertEquals(groupExpected.getLink(), group.getLink());
        assertEquals(groupExpected.getCreatorUserId(), group.getCreatorUserId());
    }

    @Test(expected = IllegalArgumentException.class)
    public void createGroupNull() throws Exception {
        Group group = new Group();
        groupService.createGroup(group);
    }

    @Test(expected = IllegalArgumentException.class)
    public void createGroupCreatorIdNull() throws Exception {
        Group group = new Group();
        group.setCreatorUserId(null);
        group.setName("New name");
        group.setLink("New link");
        groupService.createGroup(group);
    }

    @Test(expected = IllegalArgumentException.class)
    public void createGroupNameNull() throws Exception {
        Group group = new Group();
        group.setCreatorUserId(new BigInteger("1"));
        group.setName(null);
        group.setLink("New link");
        groupService.createGroup(group);
    }

    @Test(expected = IllegalArgumentException.class)
    public void createGroupLinkNull() throws Exception {
        Group group = new Group();
        group.setCreatorUserId(new BigInteger("1"));
        group.setName("New Name");
        group.setLink(null);
        groupService.createGroup(group);
    }

    @Test(expected = IllegalArgumentException.class)
    public void createGroupNameEmpty() throws Exception {
        Group group = new Group();
        group.setCreatorUserId(new BigInteger("1"));
        group.setName("");
        group.setLink("New link");
        groupService.createGroup(group);
    }

    @Test(expected = IllegalArgumentException.class)
    public void createGroupLinkEmpty() throws Exception {
        Group group = new Group();
        group.setCreatorUserId(new BigInteger("1"));
        group.setName("New Name");
        group.setLink(null);
        groupService.createGroup(group);
    }

    @Test(expected = ServiceException.class)
    public void createGroupWrongId() throws Exception {
        Group group = new Group();
        group.setCreatorUserId(new BigInteger("-666"));
        group.setName("New Name");
        group.setLink("New Link");
        groupService.createGroup(group);
    }

    @Test(expected = IllegalArgumentException.class)
    public void updateGroupNull() throws Exception {
        Group group = new Group();
        groupService.updateGroup(group);
    }

    @Test(expected = IllegalArgumentException.class)
    public void updateGroupIdNull() throws Exception {
        Group group = new Group();
        group.setId(null);
        group.setName("New Name");
        groupService.updateGroup(group);
    }

    @Test(expected = IllegalArgumentException.class)
    public void updateGroupNameNull() throws Exception {
        createTestValues();
        sequenceId = groupService.createGroup(groupExpected);
        Group group = new Group();
        group.setId(sequenceId);
        group.setName(null);
        groupService.updateGroup(group);
    }

    @Test(expected = IllegalArgumentException.class)
    public void updateGroupNameEmpty() throws Exception {
        createTestValues();
        sequenceId = groupService.createGroup(groupExpected);
        Group group = new Group();
        group.setId(sequenceId);
        group.setName("");
        groupService.updateGroup(group);
    }

    @Test
    public void updateGroup() throws Exception {
        createTestValues();
        sequenceId = groupService.createGroup(groupExpected);
        Group group = new Group();
        group.setId(sequenceId);
        group.setName("Very cool new group name");
        group.setCreatorUserId(creatorId);
        Group checkGroup = groupService.updateGroup(group);
        assertEquals(group.getId(), checkGroup.getId());
        assertEquals(group.getName(), checkGroup.getName());
    }

    @Test(expected = IllegalArgumentException.class)
    public void deleteGroupNullId() throws Exception {
        groupService.deleteGroup(null);
    }

    @Test(expected = ServiceException.class)
    public void deleteGroup() throws Exception {
        createTestValues();
        sequenceId = groupService.createGroup(groupExpected);
        groupService.deleteGroup(sequenceId);
        Group group = groupService.getGroupById(sequenceId);
    }

    @Test(expected = IllegalArgumentException.class)
    public void getUsersInGroupNullId() throws Exception {
        groupService.getUsersInGroup(null);
    }

    @Test
    public void getUsersInGroupWrongId() throws Exception {
        List<User> list = groupService.getUsersInGroup(new BigInteger("-666"));
        assertEquals(0, list.size());
    }

    @Test
    public void getUsersInGroup() throws Exception {
        createTestValues();
        sequenceId = groupService.createGroup(groupExpected);
        new ObjectEavBuilder.Builder(jdbcTemplate)
            .setObjectId(creatorId)
            .setStringAttribute(AttrtypeProperties.LAST_NAME, "User_lastname")
            .setStringAttribute(AttrtypeProperties.FIRST_NAME, "User_firstname")
            .setStringAttribute(AttrtypeProperties.EMAIL, "User_email")
            .setStringAttribute(AttrtypeProperties.PASSWORD, "User_password")
            .setStringAttribute(AttrtypeProperties.PHONE, "User_phone")
            .update();
        List<User> users = groupService.getUsersInGroup(sequenceId);

        List<User> usersExpected = new ArrayList();
        User user = new User();
        user.setId(creatorId);
        user.setLastName("User_lastname");
        user.setFirstName("User_firstname");
        user.setEmail("User_email");
        user.setPassword("User_password");
        user.setPhone("User_phone");
        usersExpected.add(user);

        assertEquals(usersExpected, users);
    }

    @Test(expected = IllegalArgumentException.class)
    public void getAllTestsInGroupNullId() throws Exception {
        groupService.getAllTestsInGroup(null);
    }

    @Test
    public void getAllTestsInGroupWrongId() throws Exception {
        List<com.netcracker.testerritto.models.Test> list = groupService.getAllTestsInGroup(new BigInteger("-666"));
        assertEquals(0, list.size());
    }

    @Test
    public void getAllTestsInGroup() throws Exception {
        createTestValues();
        sequenceId = groupService.createGroup(groupExpected);
        BigInteger testId = new ObjectEavBuilder.Builder(jdbcTemplate)
            .setParentId(sequenceId)
            .setObjectTypeId(ObjtypeProperties.TEST)
            .setName("MyTest")
            .setStringAttribute(AttrtypeProperties.NAME_TEST, "MyTest")
            .setReference(AttrtypeProperties.CREATE_TEST_BY, creatorId)
            .create();
        List<com.netcracker.testerritto.models.Test> tests = groupService.getAllTestsInGroup(sequenceId);

        List<com.netcracker.testerritto.models.Test> testsExpected = new ArrayList<>();
        com.netcracker.testerritto.models.Test test = new com.netcracker.testerritto.models.Test();
        test.setId(testId);
        test.setGroupId(sequenceId);
        test.setNameTest("MyTest");
        test.setCreatorUserId(creatorId);
        testsExpected.add(test);

        assertEquals(testsExpected, tests);
    }

    private void createTestValues(){
        Locale.setDefault(Locale.ENGLISH);
        creatorId = new ObjectEavBuilder.Builder(jdbcTemplate)
        .setObjectTypeId(ObjtypeProperties.USER)
        .setName("USER_CREATOR")
        .create();
        groupExpected.setCreatorUserId(creatorId);
        groupExpected.setName("Very cool group");
        groupExpected.setLink("New Link http...");
    }
}
