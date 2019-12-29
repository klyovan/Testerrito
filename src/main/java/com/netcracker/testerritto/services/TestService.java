package com.netcracker.testerritto.services;

import com.netcracker.testerritto.dao.TestDAO;
import com.netcracker.testerritto.exceptions.ServiceException;
import com.netcracker.testerritto.handlers.ServiceExceptionHandler;
import com.netcracker.testerritto.models.Test;
import java.math.BigInteger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


@Transactional
@Service
public class TestService {

    @Autowired
    private TestDAO testDAO;

    @Autowired
    private ServiceExceptionHandler serviceExceptionHandler;

    public Test getTest(BigInteger testId)  {
        checkIdNotNull(testId);
        try {
            return testDAO.getTest(testId);
        } catch (DataAccessException e) {
            serviceExceptionHandler.logAndThrowServiceException("Failed getTest()", e);
        }
        return null;
    }

    public BigInteger createTest(Test test)  {
        checkParamsForCreateUpdateTest(test);
        try {
            return testDAO.createTest(test);
        } catch (DataAccessException e) {
            serviceExceptionHandler.logAndThrowServiceException("Failed createTest()", e);
        }
        return null;
    }

    public Test updateTest(Test test)  {
        checkParamsForCreateUpdateTest(test);
        checkIdNotNull(test.getId());
        try {
            return testDAO.updateTest(test);
        } catch (DataAccessException e) {
            serviceExceptionHandler.logAndThrowServiceException("Failed updateTest()", e);
        }
        return null;
    }

    public void deleteTest(BigInteger testId)  {
        checkIdNotNull(testId);
        try {
            testDAO.deleteTest(testId);
        } catch (DataAccessException e) {
            serviceExceptionHandler.logAndThrowServiceException("Failed createTest()", e);
        }
    }


    private void checkParamsForCreateUpdateTest(Test checkTest) {
        if (checkTest == null) {
            serviceExceptionHandler.logAndThrowIllegalException("Can't pass null object in method!");
        } else if ("".equals(checkTest.getNameTest()) || checkTest.getNameTest() == null) {
            serviceExceptionHandler.logAndThrowIllegalException("nameTest can not be null or empty!");
        } else if (checkTest.getCreatorUserId() == null || checkTest.getGroupId() == null) {
            serviceExceptionHandler.logAndThrowIllegalException(
                "Parameters of test creatorUserId, groupId can not be null)");
        }
    }

    private void checkIdNotNull(BigInteger id) {
        if (id == null) {
            serviceExceptionHandler.logAndThrowIllegalException("Parameter id can not be null");
        }
    }
}
