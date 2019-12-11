package com.netcracker.testerritto.dao;

import static org.junit.Assert.assertEquals;
import com.netcracker.testerritto.ApplicationConfiguration;
import com.netcracker.testerritto.models.Question;
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
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.junit4.SpringRunner;

@SpringBootTest(classes = ApplicationConfiguration.class)
@RunWith(SpringRunner.class)
public class QuestionDAOTest {
  @Autowired
  QuestionDAO questionDAO;
  @Autowired
  private JdbcTemplate jdbcTemplate;

  private Question expectedQuestion;
  private BigInteger questionId;
  private Question createdQuestion;
  private List<Question> expQuestions;
 Question questionForTesting = new Question();

  @Before
  public void setUp() throws Exception {
    Locale.setDefault(Locale.ENGLISH);
    createdQuestion = getNewQuestion();
    questionId = questionDAO.createQuestion(createdQuestion);
    createdQuestion.setId(questionId);
  }
  @After
  public void tearDown(){
    questionDAO.deleteQuestionById(createdQuestion.getId());
  }


  @Test
  public void createQuestion() {
    Assert.assertNotEquals(null, createdQuestion.getId());
  }

  @Test
  public void getQuestionById() throws DataAccessException {
    expectedQuestion = questionDAO.getQuestionById(questionId);
    assertEquals(expectedQuestion.getId(), createdQuestion.getId());
    assertEquals(expectedQuestion.getTextQuestion(), createdQuestion.getTextQuestion());
  }

  @Test
  public void updateQuestion() throws DataAccessException {
    String newTextQuestion = "Do you like bananas?";
    createdQuestion.setTextQuestion(newTextQuestion);
    createdQuestion.setTypeQuestion(ListsAttr.MULTIPLE_ANSWER);
    questionDAO.updateQuestion(createdQuestion);
    expectedQuestion = questionDAO.getQuestionById(createdQuestion.getId());

    assertEquals(expectedQuestion.getTypeQuestion(), createdQuestion.getTypeQuestion());
    assertEquals(expectedQuestion.getTextQuestion(), createdQuestion.getTextQuestion());
  }

  @Test(expected = EmptyResultDataAccessException.class)
  public void deleteCategoryById() throws DataAccessException {
    Question questForDeleting = getNewQuestion();
    BigInteger deletedId = questionDAO.createQuestion(questForDeleting);
    questionDAO.deleteQuestionById(deletedId);
    questionDAO.getQuestionById(deletedId);
  }

  private Question getNewQuestion() {
    Question question = new Question();
    question.setTextQuestion("What?");
    question.setTypeQuestion(ListsAttr.ONE_ANSWER);
    question.setTestId(new BigInteger("-10025"));
    return question;
  }
}
