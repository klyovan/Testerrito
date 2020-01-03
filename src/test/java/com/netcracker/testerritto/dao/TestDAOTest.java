package com.netcracker.testerritto.dao;

import com.netcracker.testerritto.ApplicationConfiguration;
import com.netcracker.testerritto.DataSourceConfig;
import com.netcracker.testerritto.models.GradeCategory;
import com.netcracker.testerritto.models.Group;
import com.netcracker.testerritto.models.Question;
import com.netcracker.testerritto.models.User;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
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


@RunWith(SpringJUnit4ClassRunner.class)
@WebAppConfiguration
@ContextConfiguration(classes = {ApplicationConfiguration.class, DataSourceConfig.class})
public class TestDAOTest {

    @Autowired
    private TestDAO testDAO;
    @Autowired
    private UserDAO userDAO;
    @Autowired
    private GroupDAO groupDAO;


    private BigInteger isCreated;
    private com.netcracker.testerritto.models.Test createdTest;
    private com.netcracker.testerritto.models.Test expectedTest;
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
        testGroupId = groupDAO.createGroup(group);

        createdTest = getNewTest();
        isCreated = testDAO.createTest(createdTest);
        createdTest.setId(isCreated);
    }

    @After
    public void tearDown() throws Exception {
        testDAO.deleteTest(isCreated);
        userDAO.deleteUser(testUserId);
        groupDAO.deleteGroup(testGroupId);
    }


    @Test
    public void getTest() {
        expectedTest = testDAO.getTest(isCreated);
        Assert.assertEquals(expectedTest, createdTest);
    }


    @Test(expected = EmptyResultDataAccessException.class)
    public void deleteTest() {
        com.netcracker.testerritto.models.Test testDelete = getNewTest();
        BigInteger deletedId = testDAO.createTest(testDelete);
        testDAO.deleteTest(deletedId);
        testDAO.getTest(deletedId);
    }

    @Test
    public void  getCategoriesIdByTest(){
        List<BigInteger> salo = testDAO.getCategoriesIdByTest(BigInteger.valueOf(25));
        System.out.println(salo.get(0)+ "-lol)");
    }


    @Test
    public void createTest() {
        Assert.assertNotEquals(null, createdTest.getId());
    }

    @Test
    public void updateTest() {
        String changeName = "Changed";
        createdTest.setNameTest(changeName);
        testDAO.updateTest(createdTest);
        expectedTest = testDAO.getTest(createdTest.getId());
        Assert.assertEquals(changeName, testDAO.getTest(createdTest.getId()).getNameTest());
    }


    private com.netcracker.testerritto.models.Test getNewTest() {
        List<GradeCategory> grades = new ArrayList<>();
        List<User> experts = new ArrayList<>();
        List<Question> questions = new ArrayList<>();

        return new com.netcracker.testerritto.models.Test(null, testGroupId, "JustTest", testUserId,
            grades, experts, questions);
    }
}
