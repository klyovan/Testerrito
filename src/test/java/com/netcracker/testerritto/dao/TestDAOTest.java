package com.netcracker.testerritto.dao;

import com.netcracker.testerritto.ApplicationConfiguration;
import com.netcracker.testerritto.DataSourceConfig;
import com.netcracker.testerritto.models.Answer;
import com.netcracker.testerritto.models.GradeCategory;
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
    private JdbcTemplate jdbcTemplate;

    @Autowired
    private TestDAO testDAO;


    private BigInteger sequence;
    private BigInteger isCreated;
    private com.netcracker.testerritto.models.Test createdTest;
    private com.netcracker.testerritto.models.Test expectedTest;

    @Before
    public void setUp() throws Exception {
        createdTest = getNewTest();
        isCreated = testDAO.createTest(createdTest);
    }

    @After
    public void tearDown() throws Exception {
        testDAO.deleteTest(createdTest.getId());
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
    public void createTest() {
        Assert.assertEquals(isCreated, createdTest.getId());
    }

    @Test
    public void updateTest() {
        String changeName = "Changed";
        createdTest.setNameTest(changeName);
        BigInteger attrId = BigInteger.valueOf(9);
        testDAO.updateTest(createdTest, attrId);
        expectedTest = testDAO.getTest(createdTest.getId());
        Assert.assertEquals(changeName, testDAO.getTest(createdTest.getId()).getNameTest());
    }

    private BigInteger getObjectSequenceCount() {
        String sql = "select object_id_pr.NEXTVAL from dual";
        return jdbcTemplate.queryForObject(sql, BigInteger.class);
    }

    private com.netcracker.testerritto.models.Test getNewTest() {
        sequence = getObjectSequenceCount();
        BigInteger a = BigInteger.valueOf(-88);
        BigInteger b = BigInteger.valueOf(-77);

        List<GradeCategory> grades = new ArrayList<>();

        List<User> experts = new ArrayList<>();

        List<Question> questions = new ArrayList<>();

        return new com.netcracker.testerritto.models.Test(sequence, a, "JustTest", b, grades, experts, questions);
    }
}