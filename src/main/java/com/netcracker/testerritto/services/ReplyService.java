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

    public BigInteger createReply(BigInteger resultId, BigInteger... answerId) throws ServiceException {
        checkResultId(resultId);

        for (BigInteger id : answerId) {
            checkAnswerId(id);
        }

        //  checkQuestionTypeOfAnswer( answerId);
        checkAllAnswerIdHaveSameQuestionId(answerId);
        checkQuestionTypeOfAnswer(answerId);

        Result result = resultDAO.getResult(resultId);
        Answer answer = answerDAO.getAnswerById(answerId[0]);
        BigInteger questionId = answer.getQuestionId();
        Question question = questionDAO.getQuestionById(questionId);

        if (question.getTestId().equals(result.getTestId())) {
            return replyDAO.createReply(resultId, answerId);
        } else {
            serviceExceptionHandler.logAndThrowIllegalException("Parameter resultId: " + resultId + " is in testId: "
                + result.getTestId() + " and Parameter answerId: " + answerId[0] + " is in testId: " + question.getTestId()
                + " parameters are in different tests objects");
        }

        return null;
    }

    public void updateReply(BigInteger replyId, BigInteger oldAnswerId, BigInteger newAnswerId) throws ServiceException {
        checkAnswerId(oldAnswerId);
        checkAnswerId(newAnswerId);
        checkReplyId(replyId);

        checkAllAnswerIdHaveSameQuestionId(oldAnswerId, newAnswerId);
        replyDAO.updateReply(replyId, oldAnswerId, newAnswerId);

    }

    public void deleteReply(BigInteger id) throws ServiceException {
        checkReplyId(id);
        replyDAO.deleteReply(id);

    }

    // только для мульти ансверов
    public void addAnswer(BigInteger replyId, BigInteger answerId) {
        checkAnswerId(answerId);
        checkReplyId(replyId);

        Reply reply = getReply(replyId);
        BigInteger replyAnswerId = reply.getReplyList().get(0).getId();

        checkQuestionTypeOfReply(replyId, "You can not add answer for ONE_ANSWER Question ");

        checkAllAnswerIdHaveSameQuestionId(replyAnswerId, answerId);
        replyDAO.addAnswer(replyId, answerId);
    }

    // только для мульти ансверов
    public void deleteAnswer(BigInteger replyId, BigInteger answerId) {
        checkAnswerId(answerId);
        checkReplyId(replyId);

        Reply reply = getReply(replyId);
        if (reply.getReplyList().size() <= 1) {
            serviceExceptionHandler.logAndThrowIllegalException("Reply must contain at least one value ");
        }

        replyDAO.deleteAnswer(replyId, answerId);

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

    private void checkQuestionTypeOfAnswer(BigInteger... answerId) {
        Answer answer = answerDAO.getAnswerById(answerId[0]);
        BigInteger questionId = answer.getQuestionId();
        Question question = questionDAO.getQuestionById(questionId);
        if (question.getTypeQuestion().equals(ListsAttr.ONE_ANSWER) && answerId.length > 1) {
            serviceExceptionHandler.logAndThrowIllegalException("You can not create Reply with a lot of answer to question type "
                + question.getTypeQuestion());
        }
    }

    private void checkAllAnswerIdHaveSameQuestionId(BigInteger... answerId) {

        Answer answ = answerDAO.getAnswerById(answerId[0]);
        BigInteger constantQuestionId = answ.getQuestionId();
        StringBuilder allAnswerIds = new StringBuilder("answerIds : ");
        for (BigInteger id : answerId) {
            allAnswerIds.append(id);
            allAnswerIds.append(" ");
        }

        for (BigInteger id : answerId) {
            Answer answer = answerDAO.getAnswerById(id);
            BigInteger questionId = answer.getQuestionId();
            if (!(constantQuestionId.equals(questionId))) {
                serviceExceptionHandler.logAndThrowIllegalException(" all answers must have same Question answerId "
                    + allAnswerIds);
            }

        }
    }

    private void checkQuestionTypeOfReply(BigInteger replyId, String message) {
        Reply reply = getReply(replyId);
        BigInteger replyAnswerId = reply.getReplyList().get(0).getId();
        BigInteger replyQuestionId = answerDAO.getAnswerById(replyAnswerId).getQuestionId();
        // BigInteger answerQuestionId = answerDAO.getAnswerById(answerId).getQuestionId();

        if (!(ListsAttr.MULTIPLE_ANSWER.equals(questionDAO.getQuestionById(replyQuestionId).getTypeQuestion()))) {
            serviceExceptionHandler.logAndThrowIllegalException(message);
        }

    }

}
