package com.netcracker.testerritto.services;

import com.netcracker.testerritto.ApplicationConfiguration;
import com.netcracker.testerritto.DataSourceConfig;
import com.netcracker.testerritto.dao.ObjectEavBuilder;
import com.netcracker.testerritto.exceptions.ServiceException;
import com.netcracker.testerritto.models.Answer;
import java.math.BigInteger;
import java.util.Locale;
import org.junit.After;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;

@RunWith(SpringJUnit4ClassRunner.class)
@WebAppConfiguration
@ContextConfiguration(classes = {ApplicationConfiguration.class, DataSourceConfig.class})
public class AnswerServiceTest {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Autowired
    private AnswerService answerService;
    private BigInteger sequenceId;
    private Answer answerExpected = new Answer();

    @After
    public void setDown(){
        if(sequenceId != null)
        new ObjectEavBuilder.Builder(jdbcTemplate)
            .setObjectId(sequenceId)
            .delete();
    }

    @Test(expected = IllegalArgumentException.class)
    public void getAnswerByIdNull() throws Exception {
        answerService.getAnswerById(null);
    }

    @Test(expected = ServiceException.class)
    public void getAnswerByWrongId() throws Exception {
        answerService.getAnswerById(new BigInteger("-555"));
    }


//    @Test(expected = IllegalArgumentException.class)
//    public void createAnswerTextNull() throws Exception {
//        Answer answer = new Answer();
//        answer.setTextAnswer(null);
//        answer.setScore(0);
//        answerService.createAnswer(answer);
//    }


//    @Test(expected = IllegalArgumentException.class)
//    public void createAnswerTextEmpty() throws Exception {
//        Answer answer = new Answer();
//        answer.setTextAnswer("");
//        answer.setScore(0);
//        answerService.createAnswer(answer);
//    }

    @Test(expected = IllegalArgumentException.class)
    public void updateAnswerNull() throws Exception {
        Answer answer = new Answer();
        answerService.updateAnswer(answer);
    }

    @Test(expected = IllegalArgumentException.class)
    public void updateAnswerIdNull() throws Exception {
        Answer answer = new Answer();
        answer.setId(null);
        answer.setTextAnswer("New Text");
        answerService.updateAnswer(answer);
    }

    @Ignore
    @Test(expected = IllegalArgumentException.class)
    public void updateAnswerTextNull() throws Exception {
        createTestValues();
        sequenceId = answerService.createAnswer(answerExpected);
        Answer answer = new Answer();
        answer.setId(sequenceId);
        answer.setTextAnswer(null);
        answerService.updateAnswer(answer);
    }

    @Test(expected = IllegalArgumentException.class)
    public void updateAnswerTextEmpty() throws Exception {
        createTestValues();
        sequenceId = answerService.createAnswer(answerExpected);
        Answer answer = new Answer();
        answer.setId(null);
        answer.setTextAnswer("");
        answerService.updateAnswer(answer);
    }

    @Test(expected = IllegalArgumentException.class)
    public void deleteAnswerNullId() throws Exception {
        answerService.deleteAnswer(null);
    }
  

    private void createTestValues(){
        Locale.setDefault(Locale.ENGLISH);
        answerExpected.setScore(5);
        answerExpected.setTextAnswer("Text answer");
    }

}
