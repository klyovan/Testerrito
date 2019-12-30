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
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import com.netcracker.testerritto.properties.Status;
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
    private BigInteger answer2Id;
    private BigInteger questionId;
    private Reply reply;


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
        answer.setTextAnswer("I like pizza");
        answer.setScore(25);
        answer.setQuestionId(questionId);
        answerId = answerDAO.createAnswer(answer);
        answer.setId(answerId);

        Answer answer2 = new Answer();
        answer2.setTextAnswer("I like bud");
        answer2.setScore(25);
        answer2.setQuestionId(questionId);
        answer2Id = answerDAO.createAnswer(answer2);
        answer2.setId(answer2Id);
        //  BigInteger[] answers = new BigInteger[2];
        //  answers[0] = (answerId);
        //  answers[1] = (answer2Id);

        List<Answer> answers = Arrays.asList(answer, answer2);
        reply = new Reply(resultId, answers);


        //    replyId = replyDAO.createReply(resultId, answers);
        replyId = replyDAO.createReply(reply);
        reply.setId(replyId);


    }

    @After
    public void tearDown() {
        groupDAO.deleteGroup(groupId);
        userDAO.deleteUser(userId);
        testDAO.deleteTest(testId);
        resultDAO.deleteResult(resultId);
        questionDAO.deleteQuestionById(questionId);
        answerDAO.deleteAnswer(answerId);
        answerDAO.deleteAnswer(answer2Id);
        replyDAO.deleteReply(replyId);
    }

    @Test
    public void createReplyAndGetReplyTest() {

        Reply reply = replyDAO.getReply(replyId);
        assertTrue("I like pizza".equals(reply.getReplyList().get(0).getTextAnswer()));

    }

    @Test
    public void updateReplyTest() {
        Answer answer = new Answer();
        answer.setTextAnswer("I like taco");
        answer.setScore(25);
        answer.setQuestionId(questionId);
        BigInteger newAnswerId = answerDAO.createAnswer(answer);
        //
        answer.setId(newAnswerId);


        Reply oldReply = replyDAO.getReply(replyId);
        BigInteger oldAnswer = oldReply.getReplyList().get(0).getId();
//        replyDAO.updateReply(replyId, oldAnswer, newAnswerId);

        replyDAO.updateReply(reply, answer);

        Reply newReply = replyDAO.getReply(replyId);
        List<Answer> replys = newReply.getReplyList();


        for (int i = 0; i < replys.size(); i++) {
            if (replys.get(i).getId().equals(newAnswerId)) {
                assertTrue("I like taco".equals(replys.get(i).getTextAnswer()));
            }
        }
        answerDAO.deleteAnswer(newAnswerId);
    }

    @Test
    public void addAnswerTest() {
        Answer answer = new Answer();
        answer.setTextAnswer("I like cheese");
        answer.setScore(25);
        answer.setQuestionId(questionId);
        answerId = answerDAO.createAnswer(answer);
        //
        answer.setId(answerId);

        replyDAO.addAnswer(reply, answer);
        Reply reply = replyDAO.getReply(replyId);
        assertTrue(reply.getReplyList().size() == 3);

        answerDAO.deleteAnswer(answerId);


    }

    @Test
    public void deleteAnswerTest() {
        Answer answer = new Answer();
        answer.setTextAnswer("I like pepsi");
        answer.setScore(25);
        answer.setQuestionId(questionId);
        answerId = answerDAO.createAnswer(answer);
        //
        answer.setId(answerId);

        replyDAO.addAnswer(reply, answer);
        replyDAO.deleteAnswer(reply, answer);

        Reply reply = replyDAO.getReply(replyId);
        assertTrue(reply.getReplyList().size() == 2);

    }

    private Result getNewResult() {

        Date date = new Date();
        HashMap<Question, Reply> replies = new HashMap<>();

        Result result = new Result();
        result.setDate(date);
        result.setScore(10);
        result.setStatus(Status.PASSED);
        result.setTestId(testId);
        result.setUserId(userId);
        result.setReplies(replies);

        return result;
    }
}