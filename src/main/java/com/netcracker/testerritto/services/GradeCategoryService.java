package com.netcracker.testerritto.services;

import com.netcracker.testerritto.dao.GradeCategoryDAO;
import com.netcracker.testerritto.exceptions.ServiceException;
import com.netcracker.testerritto.handlers.ServiceExceptionHandler;
import com.netcracker.testerritto.models.GradeCategory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;

import java.math.BigInteger;
import java.util.List;

@Service
public class GradeCategoryService {
    @Autowired
    private ServiceExceptionHandler serviceExceptionHandler;
    @Autowired
    private GradeCategoryDAO gradeCategoryDAO;

    public GradeCategory getCategoryById(BigInteger id) throws IllegalArgumentException, ServiceException {
        if (id == null) {
            serviceExceptionHandler.logAndThrowIllegalException("Parameter(id) can't be null");
        }
        try {
            return gradeCategoryDAO.getGradeCategoryById(id);
        } catch (DataAccessException e) {
            serviceExceptionHandler.logAndThrowServiceException("Getting grade category by id was failed.", e);
        }
        return null;
    }

    public List<GradeCategory> getGradeCategoryByTestId(BigInteger id) throws IllegalArgumentException, ServiceException {
        if (id == null) {
            serviceExceptionHandler.logAndThrowIllegalException("Parameter(testId) can't be null");
        }
        try {
            return gradeCategoryDAO.getGradeCategoryByTestId(id);
        } catch (DataAccessException e) {
            serviceExceptionHandler.logAndThrowServiceException("Getting grade category by testId was failed.", e);
        }
        return null;
    }

    public List<GradeCategory> getGradeCategoryByCategoryId(BigInteger id) throws IllegalArgumentException, ServiceException {
        if (id == null) {
            serviceExceptionHandler.logAndThrowIllegalException("Parameter(categoryId) can't be null");
        }
        try {
            return gradeCategoryDAO.getGradeCategoryByCategoryId(id);
        } catch (DataAccessException e) {
            serviceExceptionHandler.logAndThrowServiceException("Getting grade category by categoryId was failed.", e);
        }
        return null;
    }

    public BigInteger createGradeCategory(GradeCategory newGradeCategory) throws IllegalArgumentException, ServiceException {
        checkParamsForCreateUpdate(newGradeCategory);
        try {
            return gradeCategoryDAO.createGradeCategory(newGradeCategory);
        } catch (DataAccessException e) {
            serviceExceptionHandler.logAndThrowServiceException("Creating grade category was failed", e);
        }
        return null;
    }

    public void deleteGradeCategoryById(BigInteger id) throws IllegalArgumentException, ServiceException {
        if (id == null) {
            serviceExceptionHandler.logAndThrowIllegalException("Parameter(id) can't be null");
        }
        try {
            gradeCategoryDAO.deleteGradeCategoryById(id);
        } catch (DataAccessException e) {
            serviceExceptionHandler.logAndThrowServiceException("Deleting grade category by id was failed.", e);
        }
    }

    public void deleteGradeCategoryByTestId(BigInteger id) throws IllegalArgumentException, ServiceException {
        if (id == null) {
            serviceExceptionHandler.logAndThrowIllegalException("Parameter(id) can't be null");
        }
        try {
            gradeCategoryDAO.deleteGradeCategoryByTestId(id);
        } catch (DataAccessException e) {
            serviceExceptionHandler.logAndThrowServiceException("Deleting grade category by testId was failed.", e);
        }
    }

    public void updateGradeCategory(GradeCategory updatedGradeCategory) throws IllegalArgumentException, ServiceException {
        checkParamsForCreateUpdate(updatedGradeCategory);
        if (updatedGradeCategory.getId() == null) {
            serviceExceptionHandler.logAndThrowIllegalException("Parameter(id) can't be null");
        }
        try {
            gradeCategoryDAO.updateGradeCategory(updatedGradeCategory);
        } catch (DataAccessException e) {
            serviceExceptionHandler.logAndThrowServiceException("Updating grade category was failed.", e);
        }
    }

    private void checkParamsForCreateUpdate(GradeCategory checkedCategory) throws IllegalArgumentException {
        if (checkedCategory == null) {
            serviceExceptionHandler.logAndThrowIllegalException("Can't pass null object in method");
        }
        if (checkedCategory.getMeaning() == null || checkedCategory.getTestId() == null || checkedCategory.getCategoryId() == null) {
            serviceExceptionHandler.logAndThrowIllegalException("Parameters(id, meaning, testId, categoryId) can't be null");
        }
        if (checkedCategory.getMeaning().trim().isEmpty()) {
            serviceExceptionHandler.logAndThrowIllegalException("Parameter(meaning) can't be empty");
        }
        if (checkedCategory.getMinScore() < 0 || checkedCategory.getMaxScore() < 0) {
            serviceExceptionHandler.logAndThrowIllegalException("Parameters(maxScore, minScore) can't be less then 0");
        }
        if (checkedCategory.getMaxScore() < checkedCategory.getMinScore()) {
            serviceExceptionHandler.logAndThrowIllegalException("Parameters(maxScore, minScore) minScore can't be bigger than maxScore");
        }
    }
}
