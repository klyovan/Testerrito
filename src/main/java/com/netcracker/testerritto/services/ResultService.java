package com.netcracker.testerritto.services;

import com.netcracker.testerritto.dao.ResultDAO;
import com.netcracker.testerritto.handlers.ServiceExceptionHandler;
import com.netcracker.testerritto.models.Result;
import com.netcracker.testerritto.properties.Status;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class ResultService {

    @Autowired
    private ResultDAO resultDAO;

    @Autowired
    private ServiceExceptionHandler serviceExceptionHandler;

    public Result getResult(BigInteger resultId) {
        checkIdNotNull(resultId);

        try {
            return resultDAO.getResult(resultId);
        } catch (DataAccessException e) {
            serviceExceptionHandler.logAndThrowServiceException("Failed getResult()", e);
        }
        return null;
    }


    public List<Result> getResultsByUser(BigInteger userId) {
        checkIdNotNull(userId);

        try {
            return resultDAO.getResultsByUser(userId);
        } catch (DataAccessException e) {
            serviceExceptionHandler.logAndThrowServiceException("Failed getResultByUser()", e);
        }
        return new ArrayList<>();
    }

    public List<Result> getResultsByTest(BigInteger testId) {
        checkIdNotNull(testId);

        try {
            return resultDAO.getResultsByTest(testId);
        } catch (DataAccessException e) {
            serviceExceptionHandler.logAndThrowServiceException("Failed getResultByTest()", e);
        }
        return new ArrayList<>();
    }


    public BigInteger createResult(Result result) {
        checkParamsForCreateUpdateResult(result);
        try {
            return resultDAO.createResult(result);
        } catch (DataAccessException e) {
            serviceExceptionHandler.logAndThrowServiceException("Failed createResult()", e);
        }
        return null;

    }

    public BigInteger updateResult(Result result)  {
        checkParamsForCreateUpdateResult(result);
        checkIdNotNull(result.getId());
        try {
            return resultDAO.updateResult(result);
        } catch (DataAccessException e) {
            serviceExceptionHandler.logAndThrowServiceException("Failed updateResult()", e);
        }
        return null;
    }

    public void deleteResult(BigInteger resultId) {
        checkIdNotNull(resultId);
        try {
            resultDAO.deleteResult(resultId);
        } catch (DataAccessException e) {
            serviceExceptionHandler.logAndThrowServiceException("Failed deleteResult()", e);
        }

    }

    private void checkParamsForCreateUpdateResult(Result result) {
        Status[] attributes = Status.values();

        if (result.getDate() == null) {
            serviceExceptionHandler
                .logAndThrowIllegalException("Result parameter date can not be null. ");
        }
        if (result.getScore() < 0) {
            serviceExceptionHandler
                .logAndThrowIllegalException("Result parameter score can not be negative.");
        }
        if (result.getTestId() == null || result.getUserId() == null) {
            serviceExceptionHandler.logAndThrowIllegalException(
                "Result parameters testId and userId can not be null! ");
        }

        int valid = 0;

        for (Status attribute : attributes) {
            if (result.getStatus() == Status.PASSED || result.getStatus() == Status.NOT_PASSED) {
                valid += 1;
            }
        }

        if (valid == 0) {
            serviceExceptionHandler.logAndThrowIllegalException(
                "Wrong result parameter - status. Check enum ListsAttr!");
        }
    }

    private void checkIdNotNull(BigInteger id) {
        if (id == null) {
            serviceExceptionHandler.logAndThrowIllegalException("Parameter id can not be null.");
        }
    }

}
