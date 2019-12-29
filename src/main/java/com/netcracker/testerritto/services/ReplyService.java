package com.netcracker.testerritto.services;

import com.netcracker.testerritto.dao.AnswerDAO;
import com.netcracker.testerritto.dao.QuestionDAO;
import com.netcracker.testerritto.dao.ReplyDAO;
import com.netcracker.testerritto.dao.ResultDAO;
import com.netcracker.testerritto.exceptions.ServiceException;
import com.netcracker.testerritto.handlers.ServiceExceptionHandler;
import com.netcracker.testerritto.models.Answer;
import com.netcracker.testerritto.models.Question;
import com.netcracker.testerritto.models.Reply;
import com.netcracker.testerritto.models.Result;
import com.netcracker.testerritto.properties.ListsAttr;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;


import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

@Service
public class ReplyService {

    @Autowired
    ReplyDAO replyDAO;
    @Autowired
    AnswerDAO answerDAO;
    @Autowired
    QuestionDAO questionDAO;
    @Autowired
    ResultDAO resultDAO;
    @Autowired
    ServiceExceptionHandler serviceExceptionHandler;


    public Reply getReply(BigInteger id) throws ServiceException {
        checkReplyId(id);
        return replyDAO.getReply(id);
    }


    public BigInteger createReply(Reply reply) throws ServiceException {
        if (reply == null) {
            serviceExceptionHandler.logAndThrowIllegalException("Object reply can't be null");
        }
        checkResultId(reply.getResultId());


        for (Answer answer : reply.getReplyList()) {
            checkAnswerId(answer.getId());
        }

        checkAllAnswerIdHaveSameQuestionId(reply.getReplyList());
        checkQuestionTypeOfAnswer(reply.getReplyList());

        Result result = resultDAO.getResult(reply.getResultId());
        BigInteger questionId = reply.getReplyList().get(0).getQuestionId();
        Question question = questionDAO.getQuestionById(questionId);

        if (question.getTestId().equals(result.getTestId())) {
            return replyDAO.createReply(reply);
        } else {
            serviceExceptionHandler.logAndThrowIllegalException("Parameter resultId: " + reply.getResultId() + " is in testId: "
                + result.getTestId() + " and Parameter answerId: " + reply.getReplyList().get(0).getId() + " is in testId: " + question.getTestId()
                + " parameters are in different tests objects");
        }

        return null;
    }


    // только для oneAnswer type
    public Reply updateReply(Reply reply, Answer answer) throws ServiceException {
        if (reply == null) {
            serviceExceptionHandler.logAndThrowIllegalException("Object reply can't be null");
        }
        if (answer == null) {
            serviceExceptionHandler.logAndThrowIllegalException("Object answer can't be null");
        }
        checkAnswerId(answer.getId());
        checkReplyId(reply.getId());

        ArrayList<Answer> answerList = new ArrayList<>();
        for(Answer answ :reply.getReplyList() ){
            answerList.add(answ);
        }
        answerList.add(answer);
        checkAllAnswerIdHaveSameQuestionId(answerList);

        checkQuestionTypeOfAnswer(reply.getReplyList());
        return replyDAO.updateReply(reply, answer);


    }

    public void deleteReply(BigInteger id) throws ServiceException {
        checkReplyId(id);
        replyDAO.deleteReply(id);

    }


    public Reply addAnswer(Reply reply, Answer answer) throws ServiceException {
        if (reply == null) {
            serviceExceptionHandler.logAndThrowIllegalException("Object reply can't be null");
        }
        if (answer == null) {
            serviceExceptionHandler.logAndThrowIllegalException("Object answer can't be null");
        }
        checkAnswerId(answer.getId());
        checkReplyId(reply.getId());

        BigInteger replyAnswerId = reply.getReplyList().get(0).getId();

        checkQuestionTypeOfReply(reply, "You can not add answer for ONE_ANSWER Question ");

        ArrayList<Answer> answerList = new ArrayList<>();
        for(Answer answ :reply.getReplyList() ){
            answerList.add(answ);
        }
        answerList.add(answer);

        checkAllAnswerIdHaveSameQuestionId(answerList);
        return replyDAO.addAnswer(reply, answer);
    }



    public Reply deleteAnswer(Reply reply, Answer answer) throws ServiceException {
        if (reply == null) {
            serviceExceptionHandler.logAndThrowIllegalException("Object reply can't be null");
        }
        if (answer == null) {
            serviceExceptionHandler.logAndThrowIllegalException("Object answer can't be null");
        }
        checkAnswerId(answer.getId());
        checkReplyId(reply.getId());

        if (reply.getReplyList().size() <= 1) {
            serviceExceptionHandler.logAndThrowIllegalException("Reply must contain at least one value ");
        }

       return replyDAO.deleteAnswer(reply, answer);

    }

    private void checkReplyId(BigInteger id) {
        if (id == null) {
            serviceExceptionHandler.logAndThrowIllegalException("Parameter reply_id can't be null");
        }
        try {
            replyDAO.getReply(id);
        } catch (DataAccessException ex) {
            serviceExceptionHandler
                .logAndThrowServiceException("Getting reply by id:" + id + " was fail", ex);
        }
    }

    private void checkAnswerId(BigInteger id) {
        if (id == null) {
            serviceExceptionHandler.logAndThrowIllegalException("Parameter answer_id can't be null");
        }
        try {
            answerDAO.getAnswerById(id);
        } catch (DataAccessException ex) {
            serviceExceptionHandler
                .logAndThrowServiceException("Getting answer by id:" + id + " was fail", ex);
        }

    }

    private void checkResultId(BigInteger id) {
        if (id == null) {
            serviceExceptionHandler.logAndThrowIllegalException("Parameter result_id can't be null");
        }
        try {
            resultDAO.getResult(id);
        } catch (DataAccessException ex) {
            serviceExceptionHandler
                .logAndThrowServiceException("Getting result by id:" + id + " was fail", ex);
        }

    }


    private void checkQuestionTypeOfAnswer(List<Answer> answerList) {
        BigInteger questionId = answerList.get(0).getQuestionId();
        Question question = questionDAO.getQuestionById(questionId);
        if (question.getTypeQuestion().equals(ListsAttr.ONE_ANSWER) && answerList.size() > 1) {
            serviceExceptionHandler.logAndThrowIllegalException("You can not create Reply with a lot of answer to question type "
                + question.getTypeQuestion());
        }
    }



    private void checkAllAnswerIdHaveSameQuestionId(List<Answer> answerList) {

        BigInteger constantQuestionId = answerList.get(0).getQuestionId();
        StringBuilder allAnswerIds = new StringBuilder("answerIds : ");
        for (Answer answer : answerList) {
            allAnswerIds.append(answer.getId());
            allAnswerIds.append(" ");
        }

        for (Answer answer : answerList) {
            BigInteger questionId = answer.getQuestionId();
            if (!(constantQuestionId.equals(questionId))) {
                serviceExceptionHandler.logAndThrowIllegalException(" all answers must have same Question answerId "
                    + allAnswerIds);
            }

        }
    }



    private void checkQuestionTypeOfReply(Reply reply, String message) {
        BigInteger replyAnswerId = reply.getReplyList().get(0).getId();
        BigInteger replyQuestionId = answerDAO.getAnswerById(replyAnswerId).getQuestionId();

        if (!(ListsAttr.MULTIPLE_ANSWER.equals(questionDAO.getQuestionById(replyQuestionId).getTypeQuestion()))) {
            serviceExceptionHandler.logAndThrowIllegalException(message);
        }

    }

}
