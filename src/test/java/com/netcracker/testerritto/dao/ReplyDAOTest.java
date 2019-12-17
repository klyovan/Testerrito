package com.netcracker.testerritto.dao;

import static org.junit.Assert.assertTrue;

import com.netcracker.testerritto.ApplicationConfiguration;
import com.netcracker.testerritto.models.Answer;
import com.netcracker.testerritto.models.Group;
import com.netcracker.testerritto.models.Question;
import com.netcracker.testerritto.models.Reply;
import com.netcracker.testerritto.models.Result;
import com.netcracker.testerritto.models.User;
import com.netcracker.testerritto.properties.ListsAttr;
import java.math.BigInteger;
import java.util.Date;
import java.util.HashMap;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@SpringBootTest(classes = ApplicationConfiguration.class)
@RunWith(SpringRunner.class)
public class ReplyDAOTest {

  @Autowired
  private ReplyDAO replyDAO;
  @Autowired
  private UserDAO userDAO;
  @Autowired
  private ResultDAO resultDAO;
  @Autowired
  private GroupDAO groupDAO;
  @Autowired
  private TestDAO testDAO;
  @Autowired
  private AnswerDAO answerDAO;
  @Autowired
  private QuestionDAO questionDAO;


  private BigInteger userId;
  private BigInteger groupId;
  private BigInteger testId;
  private BigInteger resultId;
  private BigInteger replyId;
  private BigInteger answerId;
  private BigInteger questionId;


  @Before
  public void init() {

    User user = new User("ln", "fn", "email",
        "password", "8800555");
    userId = userDAO.createUser(user);

    Group group = new Group();
    group.setLink("Link...");
    group.setName("Group...");
    group.setCreatorUserId(userId);
    groupId = groupDAO.createGroup(group);

    com.netcracker.testerritto.models.Test test = new com.netcracker.testerritto.models.Test();
    test.setNameTest("Test..");
    test.setGroupId(groupId);
    test.setCreatorUserId(userId);
    testId = testDAO.createTest(test);

    Result result = getNewResult();

    resultId = resultDAO.createResult(result);

    Question question = new Question();
    question.setTextQuestion("What?");
    question.setTypeQuestion(ListsAttr.ONE_ANSWER);
    question.setTestId(testId);
    questionId = questionDAO.createQuestion(question);

    Answer answer = new Answer();
    answer.setTextAnswer("Do you like pizza?");
    answer.setScore(25);
    answer.setQuestionId(questionId);
    answerId = answerDAO.createAnswer(answer);

    replyId = replyDAO.createReply(resultId, answerId);
  }

  @After
  public void tearDown() {
    groupDAO.deleteGroup(groupId);
    userDAO.deleteUser(userId);
    testDAO.deleteTest(testId);
    resultDAO.deleteResult(resultId);
    questionDAO.deleteQuestionById(questionId);
    answerDAO.deleteAnswer(answerId);
    replyDAO.deleteReply(replyId);
  }

  @Test
  public void createReplyAndGetReplyTest() {

    Reply reply = replyDAO.getReply(replyId);
    assertTrue("Do you like pizza?".equals(reply.getAnswer()));

  }

  @Test
  public void updateReplyTest() {
    Answer answer = new Answer();
    answer.setTextAnswer("Do you like taco?");
    answer.setScore(25);
    answer.setQuestionId(questionId);
    BigInteger newAnswerId = answerDAO.createAnswer(answer);

    replyDAO.updateReply(replyId, newAnswerId);
    Reply reply = replyDAO.getReply(replyId);
    assertTrue("Do you like taco?".equals(reply.getAnswer()));

  }


  private Result getNewResult() {

    Date date = new Date();
    HashMap<Reply, Question> replies = new HashMap<>();

    Result result = new Result();
    result.setDate(date);
    result.setScore(10);
    result.setStatus(ListsAttr.PASSED);
    result.setTestId(testId);
    result.setUserId(userId);
    result.setReplies(replies);

    return result;
  }

}

