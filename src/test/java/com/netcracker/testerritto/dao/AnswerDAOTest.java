package com.netcracker.testerritto.dao;

import static org.junit.Assert.assertEquals;
import com.netcracker.testerritto.ApplicationConfiguration;
import com.netcracker.testerritto.models.Answer;
import com.netcracker.testerritto.properties.ListsAttr;
import java.math.BigInteger;
import java.util.List;
import java.util.Locale;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.test.context.junit4.SpringRunner;

@SpringBootTest(classes = ApplicationConfiguration.class)
@RunWith(SpringRunner.class)
public class AnswerDAOTest {
  @Autowired
  AnswerDAO answerDAO;

  Answer createdAnswer;
  private BigInteger isCreated;
  Answer expectedAnswer;;
  List<Answer> expAnswer;

  @Before
  public void setUp() throws Exception {
    Locale.setDefault(Locale.ENGLISH);
    createdAnswer = getNewAnswer();
    isCreated = answerDAO.createAnswer(createdAnswer);
    createdAnswer.setId(BigInteger.valueOf(-911));
  }

  @After
  public void tearDown() throws Exception {
    answerDAO.deleteAnswer(createdAnswer.getId());
  }

  @Test
  public void createAnswer()throws DataAccessException  {
    Assert.assertNotEquals(null, createdAnswer.getId());
  }

  @Test
  public void updateAnswer() {
    int newScore = 60;
    createdAnswer.setTextAnswer("Do you like films?");
    createdAnswer.setScore(newScore);
    answerDAO.updateAnswer(createdAnswer);
    expectedAnswer = answerDAO.getAnswerById(createdAnswer.getId());

    Assert.assertEquals(expectedAnswer.getScore(), createdAnswer.getScore());
    Assert.assertEquals(expectedAnswer.getTextAnswer(), createdAnswer.getTextAnswer());
  }

  @Test(expected = EmptyResultDataAccessException.class)
  public void deleteAnswer(){
    Answer newAnswer = getNewAnswer();
    BigInteger deletedId = answerDAO.createAnswer(newAnswer);
    answerDAO.deleteAnswer(deletedId);
    answerDAO.getAnswerById(deletedId);
  }

  @Test
  public void getAnswerById() throws DataAccessException {
    expectedAnswer = answerDAO.getAnswerById(isCreated);

    assertEquals(expectedAnswer.getId(), createdAnswer.getId());
    assertEquals(expectedAnswer.getTextAnswer(), createdAnswer.getTextAnswer());
  }


  private Answer getNewAnswer(){
    Answer answer = new Answer();
    answer.setTextAnswer("Do you like pizza?");
    answer.setScore(25);
    answer.setQuestionId(new BigInteger("-910"));
    return answer;
  }
}
