package com.netcracker.testerritto.dao;

import com.netcracker.testerritto.ApplicationConfiguration;
import com.netcracker.testerritto.DataSourceConfig;
import com.netcracker.testerritto.models.Question;
import com.netcracker.testerritto.models.Reply;
import com.netcracker.testerritto.models.Result;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;

import java.math.BigInteger;
import java.util.Date;
import java.util.HashMap;

import static org.junit.Assert.*;

@RunWith(SpringJUnit4ClassRunner.class)
@WebAppConfiguration
@ContextConfiguration(classes = {ApplicationConfiguration.class, DataSourceConfig.class})
public class ResultDAOTest {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Autowired
    private ResultDAO resultDAO;

    @Test
    public void getResult() {

        Result result = resultDAO.getResult(BigInteger.valueOf(120));
    }

    @Test
    public void getReplies() {

        HashMap<Reply, Question> replies = resultDAO.getReplies(BigInteger.valueOf(120));

    }

    @Test
    public void deleteResult() {
        Date date = new Date();
        System.out.println(date);
    }

    @Test
    public void createResult() {
    }

    @Test
    public void updateResult() {
    }
}