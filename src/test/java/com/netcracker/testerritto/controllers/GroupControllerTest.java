package com.netcracker.testerritto.controllers;

import com.netcracker.testerritto.ApplicationConfiguration;
import com.netcracker.testerritto.DataSourceConfig;
import com.netcracker.testerritto.dao.ObjectEavBuilder;
import com.netcracker.testerritto.exceptions.ApiRequestException;
import com.netcracker.testerritto.models.Group;
import com.netcracker.testerritto.models.Remark;
import com.netcracker.testerritto.models.User;
import com.netcracker.testerritto.properties.AttrtypeProperties;
import com.netcracker.testerritto.properties.ObjtypeProperties;
import com.netcracker.testerritto.services.GroupService;
import com.netcracker.testerritto.services.RemarkService;
import java.security.Key;
import java.util.UUID;
import javax.crypto.Cipher;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.DESedeKeySpec;
import javax.crypto.spec.IvParameterSpec;
import org.apache.commons.codec.binary.Base64;
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
public class GroupControllerTest {

    @Autowired
    private GroupController groupController;

    @Autowired
    private RemarkService remarkService;

    @Autowired
    private GroupService groupService;

    @Autowired
    private JdbcTemplate jdbcTemplate;

    private BigInteger authorId;
    private BigInteger groupId;
    private BigInteger testId;
    private BigInteger questionId;
    private BigInteger remarkerId;
    private BigInteger sequenceId;
    private BigInteger creatorId;
    private BigInteger recipientId;
    private Remark remarkExpected = new Remark();
    private Group groupExpected = new Group();

    @After
    public void setDown(){
        ArrayList<BigInteger> listForDelete = new ArrayList<>();
        if (creatorId != null)
            listForDelete.add(creatorId);
        if (authorId != null)
            listForDelete.add(authorId);
        if (groupId != null)
            listForDelete.add(groupId);
        if (testId != null)
            listForDelete.add(testId);
        if (questionId != null)
            listForDelete.add(questionId);
        if (remarkerId != null)
            listForDelete.add(remarkerId);
        if (sequenceId != null)
            listForDelete.add(sequenceId);
        if (recipientId != null)
            listForDelete.add(recipientId);
        for (BigInteger id : listForDelete) {
            new ObjectEavBuilder.Builder(jdbcTemplate)
                .setObjectId(id)
                .delete();
        }
    }

    @Test
    public void getRemark() throws Exception {
        createTestValuesRemark();
        sequenceId = remarkService.createRemark(remarkExpected);
        remarkExpected.setId(sequenceId);
        Remark remark = groupController.getRemarkById(sequenceId);
        assertEquals(remarkExpected.getId(), remark.getId());
        assertEquals(remarkExpected.getText(), remark.getText());
        assertEquals(remarkExpected.getQuestionId(), remark.getQuestionId());
        assertEquals(remarkExpected.getUserSenderId(), remark.getUserSenderId());
        assertEquals(remarkExpected.getUserRecipientId(), remark.getUserRecipientId());
    }

    @Test(expected = ApiRequestException.class)
    public void getRemarkNullId() throws Exception {
        groupController.getRemarkById(null);
    }

    @Test(expected = ApiRequestException.class)
    public void getRemarkWrongId() throws Exception {
        groupController.getRemarkById(new BigInteger("-666"));
    }

    @Test(expected = ApiRequestException.class)
    public void deleteRemark() throws Exception {
        createTestValuesRemark();
        sequenceId = remarkService.createRemark(remarkExpected);
        groupController.deleteRemark(sequenceId);
        groupController.getRemarkById(sequenceId);
    }

    @Test(expected = ApiRequestException.class)
    public void deleteRemarkNullId() throws Exception {
        groupController.deleteRemark(null);
    }

    @Test
    public void updateGroup() throws Exception {
        createTestValuesGroup();
        sequenceId = groupService.createGroup(groupExpected);
        Group group = new Group();
        group.setId(sequenceId);
        group.setName("Very cool new group name");
        group.setCreatorUserId(creatorId);
        Group checkGroup = groupController.updateGroup(group);
        assertEquals(group.getId(), checkGroup.getId());
        assertEquals(group.getName(), checkGroup.getName());
    }

    @Test(expected = ApiRequestException.class)
    public void updateGroupNull() throws Exception {
        Group group = new Group();
        groupController.updateGroup(group);
    }

    @Test(expected = ApiRequestException.class)
    public void updateGroupNullId() throws Exception {
        Group group = new Group();
        group.setId(null);
        group.setName("New Name");
        groupController.updateGroup(group);
    }

    @Test(expected = ApiRequestException.class)
    public void updateGroupNullName() throws Exception {
        createTestValuesGroup();
        sequenceId = groupService.createGroup(groupExpected);
        Group group = new Group();
        group.setId(sequenceId);
        group.setName(null);
        groupController.updateGroup(group);
    }

    @Test(expected = ApiRequestException.class)
    public void updateGroupEmptyName() throws Exception {
        createTestValuesGroup();
        sequenceId = groupService.createGroup(groupExpected);
        Group group = new Group();
        group.setId(sequenceId);
        group.setName("");
        groupController.updateGroup(group);
    }

    @Test(expected = ApiRequestException.class)
    public void getAllTestsInGroupNullId() throws Exception {
        groupController.getAllTestsInGroup(null);
    }

    @Test
    public void getAllTestsInGroupWrongId() throws Exception {
        List<com.netcracker.testerritto.models.Test> list = groupController.getAllTestsInGroup(new BigInteger("-666"));
        assertEquals(0, list.size());
    }

    @Test
    public void getAllTestsInGroup() throws Exception {
        createTestValuesGroup();
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

    @Test(expected = ApiRequestException.class)
    public void getUsersInGroupNullId() throws Exception {
        groupController.getUsersInGroup(null);
    }

    @Test
    public void getUsersInGroupWrongId() throws Exception {
        List<User> list = groupController.getUsersInGroup(new BigInteger("-666"));
        assertEquals(0, list.size());
    }


    @Test
    public void getUsersInGroup() throws Exception {
        createTestValuesGroup();
        sequenceId = groupService.createGroup(groupExpected);
        new ObjectEavBuilder.Builder(jdbcTemplate)
            .setObjectId(creatorId)
            .setStringAttribute(AttrtypeProperties.LAST_NAME, "User_lastname")
            .setStringAttribute(AttrtypeProperties.FIRST_NAME, "User_firstname")
            .setStringAttribute(AttrtypeProperties.EMAIL, "User_email")
            .setStringAttribute(AttrtypeProperties.PASSWORD, "User_password")
            .setStringAttribute(AttrtypeProperties.PHONE, "User_phone")
            .update();
        List<User> users = groupController.getUsersInGroup(sequenceId);

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

    private void createTestValuesRemark(){
        Locale.setDefault(Locale.ENGLISH);
        authorId = new ObjectEavBuilder.Builder(jdbcTemplate)
            .setObjectTypeId(ObjtypeProperties.USER)
            .setName("USER_AUTHOR")
            .create();
        groupId = new ObjectEavBuilder.Builder(jdbcTemplate)
            .setObjectTypeId(ObjtypeProperties.GROUP)
            .setName("GROUP")
            .create();
        testId = new ObjectEavBuilder.Builder(jdbcTemplate)
            .setParentId(groupId)
            .setObjectTypeId(ObjtypeProperties.TEST)
            .setName("TEST")
            .setReference(AttrtypeProperties.CREATE_TEST_BY, authorId)
            .create();
        questionId = new ObjectEavBuilder.Builder(jdbcTemplate)
            .setParentId(testId)
            .setObjectTypeId(ObjtypeProperties.QUESTION)
            .setName("QUESTION")
            .create();
        remarkerId = new ObjectEavBuilder.Builder(jdbcTemplate)
            .setObjectTypeId(ObjtypeProperties.USER)
            .setName("USER_REMARKER")
            .create();
        recipientId = new ObjectEavBuilder.Builder(jdbcTemplate)
            .setObjectTypeId(ObjtypeProperties.USER)
            .setName("USER_REMARKER")
            .create();
        remarkExpected.setText("New Remark Text");
        remarkExpected.setUserSenderId(remarkerId);
        remarkExpected.setUserRecipientId(authorId);
        remarkExpected.setQuestionId(questionId);
        remarkExpected.setUserRecipientId(recipientId);
    }

    private void createTestValuesGroup(){
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
