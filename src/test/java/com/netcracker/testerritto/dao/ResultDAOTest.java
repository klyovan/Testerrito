package com.netcracker.testerritto.dao;

import com.netcracker.testerritto.ApplicationConfiguration;
import com.netcracker.testerritto.DataSourceConfig;
import com.netcracker.testerritto.models.Question;
import com.netcracker.testerritto.models.Reply;
import com.netcracker.testerritto.models.Result;
import com.netcracker.testerritto.properties.ListsAttr;
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
import java.util.Date;
import java.util.HashMap;

@RunWith(SpringJUnit4ClassRunner.class)
@WebAppConfiguration
@ContextConfiguration(classes = {ApplicationConfiguration.class, DataSourceConfig.class})
public class ResultDAOTest {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Autowired
    private ResultDAO resultDAO;


    private BigInteger isCreated;
    private Result createdResult;
    private Result expectedResult;


    @Before
    public void setUp() throws Exception {
        createdResult = getNewResult();
        isCreated = resultDAO.createResult(createdResult);
        createdResult.setId(isCreated);
    }

    @After
    public void tearDown() throws Exception {
        resultDAO.deleteResult(createdResult.getId());
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
        createdResult.setStatus(ListsAttr.NOT_PASSED);
        resultDAO.updateResult(createdResult);
        expectedResult = resultDAO.getResult(createdResult.getId());

        Assert.assertEquals(expectedResult.getScore(), createdResult.getScore());
        Assert.assertEquals(expectedResult.getStatus(), createdResult.getStatus());


    }


    private Result getNewResult() {

        Date date = new Date();
        HashMap<Reply, Question> replies = new HashMap<>();

        Result result = new Result();
        result.setDate(date);
        result.setScore(10);
        result.setStatus(ListsAttr.PASSED);
        result.setTestId(new BigInteger("-10025"));
        result.setUserId(new BigInteger("-88"));
        result.setReplies(replies);

        return result;
    }
}