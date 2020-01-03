package com.netcracker.testerritto.dao;

import com.netcracker.testerritto.ApplicationConfiguration;
import com.netcracker.testerritto.DataSourceConfig;
import com.netcracker.testerritto.models.Category;
import com.netcracker.testerritto.models.Group;
import com.netcracker.testerritto.models.Question;
import com.netcracker.testerritto.models.Reply;
import com.netcracker.testerritto.models.Result;
import com.netcracker.testerritto.models.User;
import com.netcracker.testerritto.properties.Status;
import java.math.BigInteger;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;

@RunWith(SpringJUnit4ClassRunner.class)
@WebAppConfiguration
@ContextConfiguration(classes = {ApplicationConfiguration.class, DataSourceConfig.class})
public class ResultDAOTest {

    @Autowired
    private ResultDAO resultDAO;
    @Autowired
    private TestDAO testDAO;
    @Autowired
    private UserDAO userDAO;
    @Autowired
    private GroupDAO groupDAO;
    @Autowired
    private CategoryDAO categoryDAO;


    private BigInteger isCreated;
    private Result createdResult;
    private Result expectedResult;
    private BigInteger testUserId;
    private BigInteger testTestId;
    private BigInteger testGroupId;
    private BigInteger testCategoryId;


    @Before
    public void setUp() {
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
        testGroupId = groupDAO.createGroup(group);

        com.netcracker.testerritto.models.Test test = new com.netcracker.testerritto.models.Test();
        test.setNameTest("Test..");
        test.setGroupId(testGroupId);
        test.setCreatorUserId(testUserId);
        testTestId = testDAO.createTest(test);

        Category category = new Category();
        category.setNameCategory("NameCategory...");
        testCategoryId = categoryDAO.createCategory(category);

        createdResult = getNewResult();
        isCreated = resultDAO.createResult(createdResult);
        createdResult.setId(isCreated);
    }

    @After
    public void tearDown() {
        resultDAO.deleteResult(createdResult.getId());
        userDAO.deleteUser(testUserId);
        groupDAO.deleteGroup(testGroupId);
        testDAO.deleteTest(testTestId);
        categoryDAO.deleteCategoryById(testCategoryId);
    }

    @Test
    public void getResult() {

        expectedResult = resultDAO.getResult(isCreated);

        Assert.assertEquals(expectedResult.getId(), createdResult.getId());
        Assert.assertEquals(expectedResult.getScore(), createdResult.getScore());
        Assert.assertEquals(expectedResult.getTestId(), createdResult.getTestId());
        Assert.assertEquals(expectedResult.getUserId(), createdResult.getUserId());
        Assert.assertEquals(expectedResult.getReplies(), createdResult.getReplies());
    }

    @Test
    public void getResultsByUser() {
        List<Result> results = resultDAO.getResultsByUser(createdResult.getUserId());

        Assert.assertEquals(1, results.size());
        Assert.assertEquals(results.get(0).getId(), createdResult.getId());
    }

    @Test
    public void getResultsByTest() {
        List<Result> results = resultDAO.getResultsByTest(createdResult.getTestId());

        Assert.assertEquals(1, results.size());
        Assert.assertEquals(results.get(0).getId(), createdResult.getId());
    }

    @Test(expected = EmptyResultDataAccessException.class)
    public void deleteResult() {
        Result resultDeleted = getNewResult();
        BigInteger deletedId = resultDAO.createResult(resultDeleted);
        resultDAO.deleteResult(deletedId);
        resultDAO.getResult(deletedId);
    }

    @Test
    public void createResult() {
        Assert.assertNotEquals(null, createdResult.getId());
    }

    @Test
    public void updateResult() {
        int changedScore = 8;
        createdResult.setScore(changedScore);
        createdResult.setStatus(Status.NOT_PASSED);
        resultDAO.updateResult(createdResult);
        expectedResult = resultDAO.getResult(createdResult.getId());

        Assert.assertEquals(expectedResult.getScore(), createdResult.getScore());
        Assert.assertEquals(expectedResult.getStatus(), createdResult.getStatus());
    }


    private Result getNewResult() {

        Date date = new Date();
        HashMap<Question, Reply> replies = new HashMap<>();

        Result result = new Result();
        result.setDate(date);
        result.setScore(10);
        result.setStatus(Status.PASSED);
        result.setTestId(testTestId);
        result.setUserId(testUserId);
        result.setCategoryId(testCategoryId);
        result.setReplies(replies);

        return result;
    }
}