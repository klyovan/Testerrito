package com.netcracker.testerritto.services;


import com.netcracker.testerritto.ApplicationConfiguration;
import com.netcracker.testerritto.DataSourceConfig;
import com.netcracker.testerritto.dao.ObjectEavBuilder;
import com.netcracker.testerritto.exceptions.ServiceException;

import com.netcracker.testerritto.models.Question;
import com.netcracker.testerritto.properties.ListsAttr;
import java.math.BigInteger;
import java.util.Locale;
import org.junit.After;
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
public class QuestionServiceTest {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Autowired
    private QuestionService questionService;
    private Question questionExpected = new Question();
    private BigInteger sequenceId;

    @After
    public void setDown(){
        if(sequenceId != null)
        new ObjectEavBuilder.Builder(jdbcTemplate)
            .setObjectId(sequenceId)
            .delete();
    }

    @Test(expected = IllegalArgumentException.class)
    public void getQuestionByIdNull() throws Exception {
        questionService.getQuestionById(null);
    }

    @Test(expected = ServiceException.class)
    public void getQuestionByWrongId() throws Exception {
        questionService.getQuestionById(new BigInteger("-555"));
    }

    @Test(expected = IllegalArgumentException.class)
    public void createQuestionTextNull() throws Exception {
        Question question = new Question();
        question.setTextQuestion(null);
        question.setTypeQuestion(ListsAttr.MULTIPLE_ANSWER);
        questionService.createQuestion(question);
    }

    @Test(expected = IllegalArgumentException.class)
    public void createQuestionTextEmpty() throws Exception {
        Question question = new Question();
        question.setTextQuestion("");
        question.setTypeQuestion(ListsAttr.MULTIPLE_ANSWER);
        questionService.createQuestion(question);
    }

    @Test(expected = IllegalArgumentException.class)
    public void updateQuestionNull() throws Exception {
        Question question = new Question();
        questionService.updateQuestion(question);
    }

    @Test(expected = IllegalArgumentException.class)
    public void updateQuestionIdNull() throws Exception {
        Question question = new Question();
        question.setId(null);
        question.setTextQuestion("New Text");
        questionService.updateQuestion(question);
    }

    @Test(expected = IllegalArgumentException.class)
    public void updateQuestionTextNull() throws Exception {
        createTestValues();
        sequenceId = questionService.createQuestion(questionExpected);
        Question question = new Question();
        question.setId(sequenceId);
        question.setTextQuestion(null);
        questionService.updateQuestion(question);
    }

    @Test(expected = IllegalArgumentException.class)
    public void updateQuestionTextEmpty() throws Exception {
        createTestValues();
        sequenceId = questionService.createQuestion(questionExpected);
        Question question = new Question();
        question.setId(null);
        question.setTextQuestion("");
        questionService.updateQuestion(question);
    }

    @Test(expected = IllegalArgumentException.class)
    public void deleteQuestionNullId() throws Exception {
        questionService.deleteQuestionById(null);
    }

    private void createTestValues(){
        Locale.setDefault(Locale.ENGLISH);
        questionExpected.setTypeQuestion(ListsAttr.ONE_ANSWER);
        questionExpected.setTextQuestion("Test question");
    }

}
