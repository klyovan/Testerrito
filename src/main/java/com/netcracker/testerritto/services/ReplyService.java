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

    public BigInteger createReply(BigInteger resultId, BigInteger answerId) throws ServiceException {
        checkResultId(resultId);
        checkAnswerId(answerId);

        Result result = resultDAO.getResult(resultId);
        Answer answer = answerDAO.getAnswerById(answerId);
        BigInteger questionId = answer.getQuestionId();
        Question question = questionDAO.getQuestionById(questionId);

        if (question.getTestId().equals(result.getTestId())) {
            return replyDAO.createReply(resultId, answerId);
        } else {
            serviceExceptionHandler.logAndThrowIllegalException("Parameter resultId: " + resultId + " is in testId: "
                + result.getTestId() + " and Parameter answerId: " + answerId + " is in testId: " + question.getTestId()
                + " parameters are in different tests objects");
        }

        return null;
    }

    public void updateReply(BigInteger replyId, BigInteger answerId) throws ServiceException {
        checkAnswerId(answerId);

        Reply reply = getReply(replyId);
        Answer answerReply = answerDAO.getAnswerById(reply.getAnswerId());
        BigInteger questionReply = answerReply.getQuestionId();

        Answer newAnswer = answerDAO.getAnswerById(answerId);
        if (questionReply.equals(newAnswer.getQuestionId())) {
            replyDAO.updateReply(replyId, answerId);
        } else {
            serviceExceptionHandler.logAndThrowIllegalException("Parameter replId: " + replyId + " is in questionId: "
                + questionReply + " and Parameter answerId: " + answerId + " is in questionId: " + newAnswer.getQuestionId()
                + " parameters are in different question objects");
        }

    }

    public void deleteReply(BigInteger id) throws ServiceException {
        checkReplyId(id);
        replyDAO.deleteReply(id);

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

}
