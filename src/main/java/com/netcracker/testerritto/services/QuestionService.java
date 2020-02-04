package com.netcracker.testerritto.services;

import com.netcracker.testerritto.dao.QuestionDAO;
import com.netcracker.testerritto.exceptions.ServiceException;
import com.netcracker.testerritto.handlers.ServiceExceptionHandler;
import com.netcracker.testerritto.models.Question;
import java.math.BigInteger;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;

@Service
public class QuestionService {
    @Autowired
    private ServiceExceptionHandler serviceExceptionHandler;
    @Autowired
    private QuestionDAO questionDAO;

    public Question getQuestionById(BigInteger id) throws ServiceException {
        if (id == null) {
            serviceExceptionHandler.logAndThrowIllegalException("Parameter(id) can't be null");
        }
        try {
            return questionDAO.getQuestionById(id);
        } catch (DataAccessException e) {
            serviceExceptionHandler.logAndThrowServiceException("Getting question by id was failed.", e);
        }
        return null;
    }

    public List<Question> getAllQuestionInTest(BigInteger testId) throws ServiceException{
        if (testId == null) {
            serviceExceptionHandler.logAndThrowIllegalException("Parameter(categoryId) can't be null");
        }
        try {
            return questionDAO.getAllQuestionInTest(testId);
        } catch (DataAccessException e) {
            serviceExceptionHandler.logAndThrowServiceException("Getting question by categoryId was failed.", e);
            return null;
        }

    }

    public Question updateQuestion(Question question) throws ServiceException{
        if (question.getId() == null)
            serviceExceptionHandler.logAndThrowIllegalException("Parameter ID can not be NULL");
        try {
            return questionDAO.updateQuestion(question);
        } catch (DataAccessException exception) {
            serviceExceptionHandler.logAndThrowServiceException("Failed updateQuestion().", exception);
        }
        return null;
    }

    public BigInteger createQuestion(Question newQuestion) throws ServiceException{
        try {
            return questionDAO.createQuestion(newQuestion);
        } catch (DataAccessException exception) {
            serviceExceptionHandler.logAndThrowServiceException("Failed createQuestion().", exception);
            return null;
        }
    }

    public void deleteQuestionById(BigInteger id) throws ServiceException{
        if (id == null) {
            serviceExceptionHandler.logAndThrowIllegalException("Parameter(id) can't be null");
        }
        try {
            questionDAO.deleteQuestionById(id);
        } catch (DataAccessException e) {
            serviceExceptionHandler.logAndThrowServiceException("Deleting question by id was failed.", e);
        }
    }
}
