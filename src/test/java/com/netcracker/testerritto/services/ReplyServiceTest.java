package com.netcracker.testerritto.services;

import com.netcracker.testerritto.ApplicationConfiguration;
import com.netcracker.testerritto.dao.*;
import com.netcracker.testerritto.exceptions.ServiceException;
import com.netcracker.testerritto.models.*;
import com.netcracker.testerritto.properties.ListsAttr;
import com.netcracker.testerritto.properties.Status;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.dao.DataAccessException;
import org.springframework.test.context.junit4.SpringRunner;

import java.math.BigInteger;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

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
    private BigInteger answer3Id;
    private BigInteger question1Id;
    private BigInteger question2Id;
    private BigInteger multipleQuestion1Id;
    private BigInteger multipleQuestion2Id;
    private BigInteger multipleAnswerId1;
    private BigInteger multipleAnswerId2;
    private BigInteger multipleAnswerId3;
    private BigInteger multipleReplyId;
    private Result result;
    private Answer answer1;
    private Answer answer2;
    private Answer answer3;
    private Answer multipleAnswer1;
    private Answer multipleAnswer2;
    private Answer multipleAnswer3;
    private Reply reply1;

    @Before
    public void init() {

        User user = new User("ln", "fn", "email",
            "password", "8800555");

        userId = userService.createUser(user);
        groupId = groupService.createGroup(getNewGroup());
        test1Id = testService.createTest(getNewTest());
        result = getNewResult();
        resultId = resultService.createResult(result);
        result.setId(resultId);
        question1Id = questionDAO.createQuestion(getNewQuestion(test1Id));

        answer1 = getNewAnswer(question1Id);
        answer1Id = answerDAO.createAnswer(answer1);
        answer1.setId(answer1Id);

        answer3 = getNewAnswer(question1Id);
        answer3Id = answerDAO.createAnswer(answer3);
        answer3.setId(answer3Id);

        List<Answer> answerList = Arrays.asList(answer1);
        reply1 = new Reply(resultId, answerList);
        //   replyId = replyService.createReply(resultId, answer1Id);
        replyId = replyService.createReply(reply1);
        reply1.setId(replyId);
        //reply.setId();


        test2Id = testService.createTest(getNewTest());
        question2Id = questionDAO.createQuestion(getNewQuestion(test2Id));
        answer2 = getNewAnswer(question2Id);
        answer2Id = answerDAO.createAnswer(answer2);
        answer2.setId(answer2Id);

        multipleQuestion1Id = questionDAO.createQuestion(getNewMultipleAnswerQuestion(test1Id));
        multipleQuestion2Id = questionDAO.createQuestion(getNewMultipleAnswerQuestion(test1Id));

        multipleAnswer1 = getNewAnswer(multipleQuestion1Id);
        multipleAnswerId1 = answerDAO.createAnswer(multipleAnswer1);
        multipleAnswer1.setId(multipleAnswerId1);

        multipleAnswer2 = getNewAnswer(multipleQuestion1Id);
        multipleAnswerId2 = answerDAO.createAnswer(multipleAnswer2);
        multipleAnswer2.setId(multipleAnswerId2);

        multipleAnswer3 = getNewAnswer(multipleQuestion2Id);
        multipleAnswerId3 = answerDAO.createAnswer(multipleAnswer2);
        multipleAnswer3.setId(multipleAnswerId3);
        // multipleReplyId = replyService.createReply(resultId,multipleAnswerId1, multipleAnswerId2)

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

        questionDAO.deleteQuestionById(multipleQuestion1Id);
        questionDAO.deleteQuestionById(multipleQuestion2Id);
        answerDAO.deleteAnswer(multipleAnswerId1);
        answerDAO.deleteAnswer(multipleAnswerId2);
        answerDAO.deleteAnswer(multipleAnswerId3);
        answerDAO.deleteAnswer(answer3Id);

    }

    @Test
    public void getReplyTest() {
        Reply reply = replyService.getReply(replyId);
        Answer answer = answerDAO.getAnswerById(answer1Id);
        //assertTrue(reply.getAnswer().equals(answer.getTextAnswer()));
        assertTrue(reply.getReplyList().get(0).getTextAnswer().equals(answer.getTextAnswer()));

    }

    @Test(expected = ServiceException.class)
    public void getReplyBadIdTest() {
        replyService.getReply(answer1Id);
    }

    @Test(expected = ServiceException.class)
    public void deleteReplyBadIdTest() {
        BigInteger newResultId = resultService.createResult(getNewResult());
        BigInteger newAnswerId = answerDAO.createAnswer(getNewAnswer(question1Id));
        List<Answer> answerList = Arrays.asList(answerDAO.getAnswerById(newAnswerId));
        Reply reply = new Reply(newResultId, answerList);
//        BigInteger newReplyId = replyService.createReply(newResultId, newAnswerId);

        BigInteger newReplyId = replyService.createReply(reply);
        replyService.deleteReply(newReplyId);
        resultService.deleteResult(newResultId);
        answerDAO.deleteAnswer(newAnswerId);

        replyService.deleteReply(newReplyId);
    }

    @Test
    public void deleteReplyTest() {
        BigInteger newResultId = resultService.createResult(getNewResult());

        Answer newAnswer = getNewAnswer(question1Id);
        newAnswer.setId(answerDAO.createAnswer(newAnswer));

        List<Answer> newAnswerList = Arrays.asList(newAnswer);
        Reply newReply = new Reply(newResultId, newAnswerList);

        BigInteger newReplyId = replyService.createReply(newReply);

        replyService.deleteReply(newReplyId);
        answerDAO.deleteAnswer(newAnswer.getId());
        resultService.deleteResult(newResultId);
    }


    @Test
    public void createReplyTest() {
        Reply reply = replyService.getReply(replyId);
        assertTrue(reply.getReplyList().get(0).getTextAnswer().equals("Kompot"));

    }

    @Test(expected = IllegalArgumentException.class)
    public void createReplyWithResultAndAnswerInDifferentTests() {
        List<Answer> newAnswerList = Arrays.asList(answer2);
        Reply reply = new Reply(resultId, newAnswerList);
        replyService.createReply(reply);

    }

    @Test(expected = ServiceException.class)
    public void createReplyWithBadResultIdTest() {
        BigInteger newResultId = resultService.createResult(getNewResult());
        Answer newAnswer = getNewAnswer(question1Id);
        BigInteger newAnswerId = answerDAO.createAnswer(newAnswer);

        List<Answer> newAnswerList = Arrays.asList(newAnswer);
        resultService.deleteResult(newResultId);
        answerDAO.deleteAnswer(newAnswerId);

        Reply reply = new Reply(newResultId, newAnswerList);
        replyService.createReply(reply);
    }

    @Test
    public void createReplyForMultipleAnswerTest() {
        BigInteger newMultipleQuestionId = questionDAO.createQuestion(getNewMultipleAnswerQuestion(test1Id));

        Answer newAnswer1 = getNewAnswer(newMultipleQuestionId);
        BigInteger newAnswerId1 = answerDAO.createAnswer(newAnswer1);
        newAnswer1.setId(newAnswerId1);

        Answer newAnswer2 = getNewAnswer(newMultipleQuestionId);
        BigInteger newAnswerId2 = answerDAO.createAnswer(newAnswer2);
        newAnswer2.setId(newAnswerId2);

        Answer newAnswer3 = getNewAnswer(newMultipleQuestionId);
        BigInteger newAnswerId3 = answerDAO.createAnswer(newAnswer3);
        newAnswer3.setId(newAnswerId3);

        List<Answer> newAnswerList = Arrays.asList(newAnswer1, newAnswer2, newAnswer3);

        Reply newReply = new Reply(resultId, newAnswerList);
        BigInteger newReplyId = replyService.createReply(newReply);
        Reply reply = replyService.getReply(newReplyId);

        assertTrue(reply.getReplyList().size() == 3);

        answerDAO.deleteAnswer(newAnswerId1);
        answerDAO.deleteAnswer(newAnswerId2);
        answerDAO.deleteAnswer(newAnswerId3);
        questionDAO.deleteQuestionById(newMultipleQuestionId);
        replyService.deleteReply(newReplyId);


    }


    @Test(expected = IllegalArgumentException.class)
    public void createReplyForMultipleAnswerWithDifferentAnswerIdTest() {
        List<Answer> newAnswerList = Arrays.asList(multipleAnswer1, multipleAnswer2, multipleAnswer3);
        Reply newReply = new Reply(resultId,newAnswerList);
        multipleReplyId = replyService.createReply(newReply);
    }

    @Test(expected = IllegalArgumentException.class)
    public void createReplyForOneAnswerWithManyAnswerIdsTest() {
        List<Answer> newAnswerList = Arrays.asList(answer1, answer3);
        Reply newReply = new Reply(resultId, newAnswerList);
        BigInteger replayId = replyService.createReply(newReply);

    }

    @Test
    public void addAnswerTest() {
        List<Answer> newAnswerList = Arrays.asList(multipleAnswer1);
        Reply newReply = new Reply(resultId, newAnswerList);
        multipleReplyId = replyService.createReply(newReply);
        newReply.setId(multipleReplyId);
        replyService.addAnswer(newReply, multipleAnswer2);

        assertTrue(replyService.getReply(multipleReplyId).getReplyList().size() == 2);
        replyService.deleteReply(multipleReplyId);
    }

    @Test(expected = IllegalArgumentException.class)
    public void addAnswerForOneAnswerTest() {
        replyService.addAnswer(reply1, answer3);
    }

    @Test(expected = IllegalArgumentException.class)
    public void deleteAnswerForOneAnswerTest() {
        replyService.deleteAnswer(reply1, answer1);
    }

    @Test
    public void deleteAnswerTest() {
        List<Answer> newAnswerList = Arrays.asList(multipleAnswer1, multipleAnswer2);
        Reply newReply = new Reply(resultId, newAnswerList);
        multipleReplyId = replyService.createReply(newReply);
        newReply.setId(multipleReplyId);
        replyService.deleteAnswer(newReply, multipleAnswer2);

        assertTrue(replyService.getReply(multipleReplyId).getReplyList().size() == 1);
        replyService.deleteReply(multipleReplyId);
    }


    @Test
    public void updateReplyTest() {
        Answer newAnswer = new Answer();
        newAnswer.setTextAnswer("pizza");
        newAnswer.setScore(25);
        newAnswer.setQuestionId(question1Id);

        BigInteger newAnswerId = answerDAO.createAnswer(newAnswer);
        newAnswer.setId(newAnswerId);

        replyService.updateReply(reply1, newAnswer);

        Reply reply = replyService.getReply(replyId);
        assertTrue(reply.getReplyList().get(0).getTextAnswer().equals("pizza"));

        answerDAO.deleteAnswer(newAnswerId);

    }

    @Test(expected = IllegalArgumentException.class)
    public void updateReplyWithNullReplyIdTest() {
        replyService.updateReply(null,  null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void updateReplyWithNullAnswerIdTest() {
        replyService.updateReply(reply1, null);
    }

    @Test(expected = ServiceException.class)
    public void updateReplyWithBadIdTest() {
        BigInteger newResultId = resultService.createResult(getNewResult());
        Answer newAnswer = getNewAnswer(question1Id);
        BigInteger newAnswerId = answerDAO.createAnswer(newAnswer);
        newAnswer.setId(newAnswerId);

        List<Answer> newAnswerList = Arrays.asList(newAnswer);
        Reply newReply = new Reply(newResultId, newAnswerList);

        BigInteger newReplyId = replyService.createReply(newReply);

        replyService.deleteReply(newReplyId);
        resultService.deleteResult(newResultId);
        answerDAO.deleteAnswer(newAnswerId);
        replyService.updateReply(newReply, newAnswer);

    }


    private Result getNewResult() {

        Date date = new Date();
        HashMap<Question, Reply> replies = new HashMap<>();

        Result result = new Result();
        result.setDate(date);
        result.setScore(10);
        result.setStatus(Status.PASSED);
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

    private Question getNewMultipleAnswerQuestion(BigInteger testId) {
        Question question = new Question();
        question.setTextQuestion("What?");
        question.setTypeQuestion(ListsAttr.MULTIPLE_ANSWER);
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
//