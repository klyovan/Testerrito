package com.netcracker.testerritto.services;


import com.netcracker.testerritto.dao.AnswerDAO;
import com.netcracker.testerritto.exceptions.ServiceException;
import com.netcracker.testerritto.handlers.ServiceExceptionHandler;
import com.netcracker.testerritto.models.Answer;
import java.math.BigInteger;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;

@Service
public class AnswerService {
    @Autowired
    private ServiceExceptionHandler serviceExceptionHandler;
    @Autowired
    private AnswerDAO answerDAO;

    public Answer getAnswerById(BigInteger answerId) throws ServiceException{
        if (answerId == null) {
            serviceExceptionHandler.logAndThrowIllegalException("Parameter(id) can't be null");
        }
        try {
            return answerDAO.getAnswerById(answerId);
        } catch (DataAccessException e) {
            serviceExceptionHandler.logAndThrowServiceException("Getting answer by id was failed.", e);
        }
        return null;
    }

    public List<Answer> getAllAnswerInQuestion(BigInteger questionId) throws ServiceException{
        if (questionId == null) {
            serviceExceptionHandler.logAndThrowIllegalException("Parameter(testId) can't be null");
        }
        try {
            return answerDAO.getAllAnswerInQuestion(questionId);
        } catch (DataAccessException e) {
            serviceExceptionHandler.logAndThrowServiceException("Getting answer by testId was failed.", e);
        }
        return null;
    }

    public Answer getAnswerForReply(BigInteger questionId) throws ServiceException{
        if (questionId == null) {
            serviceExceptionHandler.logAndThrowIllegalException("Parameter(id) can't be null");
        }
        try {
            answerDAO.getAnswerForReply(questionId);
        } catch (DataAccessException e) {
            serviceExceptionHandler.logAndThrowServiceException("Deleting answer by testId was failed.", e);
        }
        return null;
    }

    public void updateAnswer (Answer answer) throws ServiceException {
        if (answer.getId() == null)
            serviceExceptionHandler.logAndThrowIllegalException("Parameter ID can not be NULL");
        try {
            answerDAO.updateAnswer(answer);
        } catch (DataAccessException exception) {
            serviceExceptionHandler.logAndThrowServiceException("Failed updateAnswer().", exception);
        }
    }

    public BigInteger createAnswer(Answer newAnswer) throws ServiceException{
//        if (newAnswer.getId() == null)
//            serviceExceptionHandler.logAndThrowIllegalException("Parameter ID can not be NULL");
        try {
            return answerDAO.createAnswer(newAnswer);
        } catch (DataAccessException exception) {
            serviceExceptionHandler.logAndThrowServiceException("Failed createAnswer().", exception);
            return null;
        }
    }

    public void deleteAnswer(BigInteger answerId) throws ServiceException{
        if (answerId == null) {
            serviceExceptionHandler.logAndThrowIllegalException("Parameter(id) can't be null");
        }
        try {
            answerDAO.deleteAnswer(answerId);
        } catch (DataAccessException e) {
            serviceExceptionHandler.logAndThrowServiceException("Deleting answer by id was failed.", e);
        }
    }
}
