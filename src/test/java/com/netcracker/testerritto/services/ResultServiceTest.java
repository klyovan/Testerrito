package com.netcracker.testerritto.services;

import com.netcracker.testerritto.ApplicationConfiguration;
import com.netcracker.testerritto.dao.UserDAO;
import com.netcracker.testerritto.exceptions.ServiceException;
import com.netcracker.testerritto.models.Category;
import com.netcracker.testerritto.models.Group;
import com.netcracker.testerritto.models.Question;
import com.netcracker.testerritto.models.Reply;
import com.netcracker.testerritto.models.Result;
import com.netcracker.testerritto.models.User;
import com.netcracker.testerritto.properties.Status;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
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
public class ResultServiceTest {

    @Autowired
    ResultService resultService;

    @Autowired
    UserDAO userDAO; //TODO change with userService

    @Autowired
    GroupService groupService;
    @Autowired
    TestService testService;
    @Autowired
    CategoryService categoryService;

    private BigInteger isCreated;
    private Result createdResult;
    private Result validResult;
    private BigInteger testUserId;
    private BigInteger testTestId;
    private BigInteger testGroupId;
    private BigInteger testCategoryId;


    @Before
    public void setUp() throws ServiceException {
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

        com.netcracker.testerritto.models.Test test = new com.netcracker.testerritto.models.Test();
        test.setNameTest("Test..");
        test.setGroupId(testGroupId);
        test.setCreatorUserId(testUserId);
        testTestId = testService.createTest(test);

        Category category = new Category();
        category.setNameCategory("NameCategory...");
        testCategoryId = categoryService.createCategory(category);

        createdResult = getNewResult();
        isCreated = resultService.createResult(createdResult);
        createdResult.setId(isCreated);
        validResult = getNewResult();
    }

    @After
    public void tearDown() throws ServiceException {
        resultService.deleteResult(createdResult.getId());
        userDAO.deleteUser(testUserId);
        groupService.deleteGroup(testGroupId);
        testService.deleteTest(testTestId);
        categoryService.deleteCategoryById(testCategoryId);
    }

    @Test
    public void getResult() throws ServiceException {
        Result expectedResult = resultService.getResult(isCreated);
        Assert.assertEquals(expectedResult.getStatus(), createdResult.getStatus());
        Assert.assertEquals(expectedResult.getScore(), createdResult.getScore());
        Assert.assertEquals(expectedResult.getUserId(), createdResult.getUserId());
        Assert.assertEquals(expectedResult.getTestId(), createdResult.getTestId());
    }

    @Test(expected = IllegalArgumentException.class)
    public void getResultByIdNull() throws ServiceException {
        resultService.getResult(null);
    }

    @Test(expected = ServiceException.class)
    public void getResultByWrongId() throws ServiceException {
        resultService.getResult(BigInteger.valueOf(-666));
    }

    @Test
    public void getResultsByUser() {
        List<Result> expectedResults = new ArrayList<>();
        expectedResults.add(createdResult);
        List<Result> actualResults = resultService.getResultsByUser(testUserId);
        Assert.assertEquals(expectedResults.size(), actualResults.size());
        Assert.assertEquals(expectedResults.get(0).getScore(), actualResults.get(0).getScore());
        Assert.assertEquals(expectedResults.get(0).getStatus(), actualResults.get(0).getStatus());
        Assert.assertEquals(expectedResults.get(0).getUserId(), actualResults.get(0).getUserId());
        Assert.assertEquals(expectedResults.get(0).getTestId(), actualResults.get(0).getTestId());
    }

    @Test(expected = IllegalArgumentException.class)
    public void getResultsByUserByIdNull() throws ServiceException {
        resultService.getResultsByUser(null);
    }

    @Test
    public void getResultsByUserByWrongId() throws ServiceException {
        List<Result> actualResults = resultService.getResultsByUser(BigInteger.valueOf(-666));
        List<Result> expectedResults = new ArrayList<>();
        Assert.assertEquals(expectedResults, actualResults);
    }


    @Test
    public void getResultsByTest() {
        List<Result> expectedResults = new ArrayList<>();
        expectedResults.add(createdResult);
        List<Result> actualResults = resultService.getResultsByTest(testTestId);
        Assert.assertEquals(expectedResults.size(), actualResults.size());
        Assert.assertEquals(expectedResults.get(0).getScore(), actualResults.get(0).getScore());
        Assert.assertEquals(expectedResults.get(0).getStatus(), actualResults.get(0).getStatus());
        Assert.assertEquals(expectedResults.get(0).getUserId(), actualResults.get(0).getUserId());
        Assert.assertEquals(expectedResults.get(0).getTestId(), actualResults.get(0).getTestId());
    }

    @Test(expected = IllegalArgumentException.class)
    public void getResultsByTestByIdNull() throws ServiceException {
        resultService.getResultsByTest(null);
    }

    @Test
    public void getResultsByTestByWrongId() throws ServiceException {
        List<Result> actualResults = resultService.getResultsByTest(BigInteger.valueOf(-666));
        List<Result> expectedResults = new ArrayList<>();
        Assert.assertEquals(expectedResults, actualResults);
    }

    @Test
    public void createResult() {
        Assert.assertNotEquals(null, createdResult.getId());
    }

    @Test(expected = IllegalArgumentException.class)
    public void createResultNull() throws ServiceException {
        Result result = new Result();
        resultService.createResult(result);
    }

    @Test(expected = IllegalArgumentException.class)
    public void createResultTestIdNull() throws ServiceException {
        validResult.setTestId(null);
        resultService.createResult(validResult);
    }

    @Test(expected = IllegalArgumentException.class)
    public void createResultUserIdNull() throws ServiceException {
        validResult.setUserId(null);
        resultService.createResult(validResult);
    }

    @Test(expected = ServiceException.class)
    public void createResultTestIdWrong() throws ServiceException {
        validResult.setTestId(BigInteger.valueOf(-666));
        resultService.createResult(validResult);
    }

    @Test(expected = ServiceException.class)
    public void createResultUserIdWrong() throws ServiceException {
        validResult.setUserId(BigInteger.valueOf(-666));
        resultService.createResult(validResult);
    }

    @Test(expected = IllegalArgumentException.class)
    public void createResultDateNull() throws ServiceException {
        validResult.setDate(null);
        resultService.createResult(validResult);
    }

    @Test(expected = IllegalArgumentException.class)
    public void createResultScoreNegative() throws ServiceException {
        validResult.setScore(-1);
        resultService.createResult(validResult);
    }

    @Test(expected = IllegalArgumentException.class)
    public void createStatusNull() throws ServiceException {
        validResult.setStatus(null);
        resultService.createResult(validResult);
    }

    @Test
    public void createResultsByCategories(){
        System.out.println(resultService.createResultsByCategories(BigInteger.valueOf(26),BigInteger.valueOf(2)));

    }


    @Test(expected = IllegalArgumentException.class)
    public void updateResultNull() throws ServiceException {
        Result result = new Result();
        resultService.updateResult(result);
    }

    @Test(expected = IllegalArgumentException.class)
    public void updateResultTestIdNull() throws ServiceException {
        createdResult.setTestId(null);
        resultService.updateResult(createdResult);
    }

    @Test(expected = IllegalArgumentException.class)
    public void updateResultUserIdNull() throws ServiceException {
        createdResult.setUserId(null);
        resultService.updateResult(createdResult);
    }


    @Test(expected = IllegalArgumentException.class)
    public void updateResultDateNull() throws ServiceException {
        createdResult.setDate(null);
        resultService.updateResult(createdResult);
    }

    @Test(expected = IllegalArgumentException.class)
    public void updateResultScoreNegative() throws ServiceException {
        createdResult.setScore(-1);
        resultService.updateResult(createdResult);
    }

    @Test(expected = IllegalArgumentException.class)
    public void updateStatusNull() throws ServiceException {
        createdResult.setStatus(null);
        resultService.updateResult(createdResult);
    }


    @Test
    public void updateResult() throws ServiceException {

        createdResult.setScore(11);
        createdResult.setStatus(Status.NOT_PASSED);
        resultService.updateResult(createdResult);

        Result expectedResult = resultService.getResult(createdResult.getId());

        Assert.assertEquals(expectedResult.getId(), createdResult.getId());
        Assert.assertEquals(expectedResult.getStatus(), createdResult.getStatus());
        Assert.assertEquals(expectedResult.getScore(), createdResult.getScore());


    }

    @Test(expected = IllegalArgumentException.class)
    public void deleteResultNullId() throws ServiceException {
        resultService.deleteResult(null);
    }


    @Test(expected = ServiceException.class)
    public void deleteResult() throws ServiceException {
        Result deletedResult = getNewResult();
        BigInteger deletedId = resultService.createResult(deletedResult);
        resultService.deleteResult(deletedId);
        resultService.getResult(deletedId);
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
