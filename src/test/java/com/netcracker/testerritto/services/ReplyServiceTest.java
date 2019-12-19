package com.netcracker.testerritto.services;

import com.netcracker.testerritto.ApplicationConfiguration;
import com.netcracker.testerritto.dao.*;
import com.netcracker.testerritto.exceptions.ServiceException;
import com.netcracker.testerritto.models.*;
import com.netcracker.testerritto.properties.ListsAttr;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.dao.DataAccessException;
import org.springframework.test.context.junit4.SpringRunner;

import java.math.BigInteger;
import java.util.Date;
import java.util.HashMap;

import static org.junit.Assert.assertTrue;

@SpringBootTest(classes = ApplicationConfiguration.class)
@RunWith(SpringRunner.class)
public class ReplyServiceTest {

    @Autowired
    private ReplyService replyService;
    @Autowired
    private UserService userService;
    @Autowired
    private ResultDAO resultService;
    @Autowired
    private GroupDAO groupService;
    @Autowired
    private TestDAO testService;
    @Autowired
    private AnswerDAO answerDAO;
    @Autowired
    private QuestionDAO questionDAO;

    private BigInteger userId;
    private BigInteger groupId;
    private BigInteger test1Id;
    private BigInteger test2Id;
    private BigInteger resultId;
    private BigInteger replyId;
    private BigInteger answer1Id;
    private BigInteger answer2Id;
    private BigInteger question1Id;
    private BigInteger question2Id;

    @Before
    public void init() {

        User user = new User("ln", "fn", "email",
            "password", "8800555");

        userId = userService.createUser(user);
        groupId = groupService.createGroup(getNewGroup());
        test1Id = testService.createTest(getNewTest());
        resultId = resultService.createResult(getNewResult());
        question1Id = questionDAO.createQuestion(getNewQuestion(test1Id));
        answer1Id = answerDAO.createAnswer(getNewAnswer(question1Id));
        replyId = replyService.createReply(resultId, answer1Id);

        test2Id = testService.createTest(getNewTest());
        question2Id = questionDAO.createQuestion(getNewQuestion(test2Id));
        answer2Id = answerDAO.createAnswer(getNewAnswer(question2Id));

    }

    @After
    public void tearDown() {
        replyService.deleteReply(replyId);
        resultService.deleteResult(resultId);
        answerDAO.deleteAnswer(answer1Id);
        questionDAO.deleteQuestionById(question1Id);
        testService.deleteTest(test1Id);
        groupService.deleteGroup(groupId);
        userService.deleteUser(userId);

        testService.deleteTest(test2Id);
        questionDAO.deleteQuestionById(question2Id);
        answerDAO.deleteAnswer(answer2Id);

    }

    @Test
    public void getReplyTest() {
        Reply reply = replyService.getReply(replyId);
        Answer answer = answerDAO.getAnswerById(answer1Id);
        assertTrue(reply.getAnswer().equals(answer.getTextAnswer()));

    }

    @Test(expected = ServiceException.class)
    public void getReplyBadIdTest() {
        replyService.getReply(answer1Id);
    }

    @Test(expected = ServiceException.class)
    public void deleteReplyBadIdTest() {
        BigInteger newResultId = resultService.createResult(getNewResult());
        BigInteger newAnswerId = answerDAO.createAnswer(getNewAnswer(question1Id));
        BigInteger newReplyId = replyService.createReply(newResultId, newAnswerId);

        replyService.deleteReply(newReplyId);
        resultService.deleteResult(newResultId);
        answerDAO.deleteAnswer(newAnswerId);

        replyService.deleteReply(newReplyId);
    }

    @Test
    public void deleteReplyTest() {
        BigInteger newResultId = resultService.createResult(getNewResult());
        BigInteger newAnswerId = answerDAO.createAnswer(getNewAnswer(question1Id));
        BigInteger newReplyId = replyService.createReply(newResultId, newAnswerId);

        replyService.deleteReply(newReplyId);
        answerDAO.deleteAnswer(newAnswerId);
        resultService.deleteResult(newResultId);
    }


    @Test
    public void createReplyTest() {
        Reply reply = replyService.getReply(replyId);
        assertTrue(reply.getAnswer().equals("Kompot"));

    }

    @Test(expected = IllegalArgumentException.class)
    public void createReplyWithResultAndAnswerInDifferentTests() {
        replyService.createReply(resultId, answer2Id);

    }

    @Test(expected = ServiceException.class)
    public void createReplyWithBadResultIdTest(){
        BigInteger newResultId = resultService.createResult(getNewResult());
        BigInteger newAnswerId = answerDAO.createAnswer(getNewAnswer(question1Id));

        resultService.deleteResult(newResultId);
        answerDAO.deleteAnswer(newAnswerId);

        replyService.createReply(newResultId, newAnswerId);
    }

    @Test
    public void updateReplyTest() {
        BigInteger newAnswerId = answerDAO.createAnswer(getNewAnswer(question1Id));
        replyService.updateReply(replyId, newAnswerId);
    }

    @Test(expected = IllegalArgumentException.class)
    public void updateReplyWithNullReplyIdTest() {
        replyService.updateReply(null, answer1Id);
    }

    @Test(expected = IllegalArgumentException.class)
    public void updateReplyWithNullAnswerIdTest() {
        replyService.updateReply(replyId, null);
    }

    @Test(expected = ServiceException.class)
    public void updateReplyWithBadIdTest(){
        BigInteger newResultId = resultService.createResult(getNewResult());
        BigInteger newAnswerId = answerDAO.createAnswer(getNewAnswer(question1Id));
        BigInteger newReplyId = replyService.createReply(newResultId, newAnswerId);

        replyService.deleteReply(newReplyId);
        resultService.deleteResult(newResultId);
        answerDAO.deleteAnswer(newAnswerId);
        replyService.updateReply(newReplyId, newAnswerId);

    }

    @Test(expected = IllegalArgumentException.class)
    public void updateReplyWithDifferentQuestionIdParameters(){
        // новый ансвер с новый квещеном
        replyService.updateReply(replyId , answer2Id);
    }


    private Result getNewResult() {

        Date date = new Date();
        HashMap<Reply, Question> replies = new HashMap<>();

        Result result = new Result();
        result.setDate(date);
        result.setScore(10);
        result.setStatus(ListsAttr.PASSED);
        result.setTestId(test1Id);
        result.setUserId(userId);
        result.setReplies(replies);

        return result;
    }

    private Group getNewGroup() {
        Group group = new Group();
        group.setLink("Link...");
        group.setName("Group...");
        group.setCreatorUserId(userId);
        return group;
    }

    private com.netcracker.testerritto.models.Test getNewTest() {
        com.netcracker.testerritto.models.Test test = new com.netcracker.testerritto.models.Test();
        test.setNameTest("Test..");
        test.setGroupId(groupId);
        test.setCreatorUserId(userId);
        return test;
    }

    private Question getNewQuestion(BigInteger testId) {
        Question question = new Question();
        question.setTextQuestion("What?");
        question.setTypeQuestion(ListsAttr.ONE_ANSWER);
        question.setTestId(testId);
        return question;
    }

    private Answer getNewAnswer(BigInteger quesId) {
        Answer answer = new Answer();
        answer.setTextAnswer("Kompot");
        answer.setScore(25);
        answer.setQuestionId(quesId);
        return answer;
    }

}
