package com.netcracker.testerritto.services;

import com.netcracker.testerritto.ApplicationConfiguration;
import com.netcracker.testerritto.dao.UserDAO;
import com.netcracker.testerritto.exceptions.ServiceException;
import com.netcracker.testerritto.models.GradeCategory;
import com.netcracker.testerritto.models.Group;
import com.netcracker.testerritto.models.Question;
import com.netcracker.testerritto.models.User;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;


@SpringBootTest(classes = ApplicationConfiguration.class)
@RunWith(SpringRunner.class)
public class TestServiceTest {

    @Autowired
    private TestService testService;

    @Autowired
    private UserDAO userDAO; //TODO replace with userService
    @Autowired
    private GroupService groupService;


    private BigInteger isCreated;
    private com.netcracker.testerritto.models.Test createdTest;
    private com.netcracker.testerritto.models.Test validTest;
    private BigInteger testUserId;
    private BigInteger testGroupId;


    @Before
    public void setUp() throws Exception {
        User user = new User();
        user.setEmail("Email..");
        user.setFirstName("FirstName...");
        user.setPassword("Password...");
        user.setLastName("LastName");
        user.setPhone("5555");
        testUserId = userDAO.createUser(user);

        Group group = new Group();
        group.setLink("Link...");
        group.setName("Group...");
        group.setCreatorUserId(testUserId);
        testGroupId = groupService.createGroup(group);

        createdTest = getNewTest();
        isCreated = testService.createTest(createdTest);
        createdTest.setId(isCreated);
        validTest = getNewTest();
    }

    @After
    public void tearDown() throws Exception {
        testService.deleteTest(isCreated);
        userDAO.deleteUser(testUserId);
        groupService.deleteGroup(testGroupId);
    }

    @Test
    public void getTest() throws ServiceException {

        com.netcracker.testerritto.models.Test expectedTest = testService.getTest(isCreated);
        Assert.assertEquals(expectedTest, createdTest);

    }

    @Test(expected = IllegalArgumentException.class)
    public void getTestByIdNull() throws ServiceException {
        testService.getTest(null);
    }

    @Test(expected = ServiceException.class)
    public void getTestByWrongId() throws ServiceException {
        testService.getTest(BigInteger.valueOf(-666));

    }

    @Test
    public void createTest() {
        Assert.assertNotEquals(null, createdTest.getId());
    }

    @Test(expected = IllegalArgumentException.class)
    public void createTestNull() throws ServiceException {
        com.netcracker.testerritto.models.Test test = new com.netcracker.testerritto.models.Test();
        testService.createTest(test);
    }


    @Test(expected = IllegalArgumentException.class)
    public void createTestCreatorUserIdNull() throws ServiceException {

        validTest.setCreatorUserId(null);
        testService.createTest(validTest);
    }

    @Test(expected = IllegalArgumentException.class)
    public void createTestNameNull() throws ServiceException {
        validTest.setNameTest(null);
        testService.createTest(validTest);
    }

    @Test(expected = IllegalArgumentException.class)
    public void createTestGroupIdNull() throws ServiceException {
        validTest.setGroupId(null);
        testService.createTest(validTest);
    }

    @Test(expected = IllegalArgumentException.class)
    public void createTestNameTestIsEmpty() throws ServiceException {
        validTest.setNameTest("");
        testService.createTest(validTest);
    }

    @Test(expected = ServiceException.class)
    public void createTestWrongGroupId() throws ServiceException {
        validTest.setGroupId(BigInteger.valueOf(-666));
        testService.createTest(validTest);
    }

    @Test(expected = ServiceException.class)
    public void createTestWrongCreatorUserId() throws ServiceException {
        validTest.setCreatorUserId(BigInteger.valueOf(-666));
        testService.createTest(validTest);
    }


    @Test(expected = IllegalArgumentException.class)
    public void updateTestNull() throws ServiceException {
        com.netcracker.testerritto.models.Test test = new com.netcracker.testerritto.models.Test();
        testService.updateTest(test);
    }

    @Test(expected = IllegalArgumentException.class)
    public void updateTestCreatorUserIdNull() throws ServiceException {
        createdTest.setCreatorUserId(null);
        testService.updateTest(createdTest);
    }

    @Test(expected = IllegalArgumentException.class)
    public void updateTestNameNull() throws ServiceException {
        createdTest.setNameTest(null);
        testService.updateTest(createdTest);
    }

    @Test(expected = IllegalArgumentException.class)
    public void updateTestGroupIdNull() throws ServiceException {
        createdTest.setGroupId(null);
        testService.updateTest(createdTest);
    }

    @Test(expected = IllegalArgumentException.class)
    public void updateTestNameTestIsEmpty() throws ServiceException {
        createdTest.setNameTest("");
        testService.updateTest(createdTest);
    }

    @Test(expected = IllegalArgumentException.class)
    public void updateTestIdNull() throws ServiceException {
        testService.updateTest(validTest);
    }

    @Test
    public void updateTest() throws ServiceException {

        createdTest.setNameTest("Changed Name");
        testService.updateTest(createdTest);

        com.netcracker.testerritto.models.Test expectedTest = testService
            .getTest(createdTest.getId());

        Assert.assertEquals(expectedTest.getId(), createdTest.getId());
        Assert.assertEquals(expectedTest.getNameTest(), createdTest.getNameTest());
    }


    @Test(expected = IllegalArgumentException.class)
    public void deleteTestNullId() throws ServiceException {
        testService.deleteTest(null);
    }

    @Test(expected = ServiceException.class)
    public void deleteTest() throws ServiceException {
        com.netcracker.testerritto.models.Test deletedTest = getNewTest();
        BigInteger deletedId = testService.createTest(deletedTest);
        testService.deleteTest(deletedId);
        testService.getTest(deletedId);
    }


    private com.netcracker.testerritto.models.Test getNewTest() {
        List<GradeCategory> grades = new ArrayList<>();
        List<User> experts = new ArrayList<>();
        List<Question> questions = new ArrayList<>();

        return new com.netcracker.testerritto.models.Test(null, testGroupId, "JustTest", testUserId,
            grades, experts, questions);
    }

}
