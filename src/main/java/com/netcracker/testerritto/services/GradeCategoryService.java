package com.netcracker.testerritto.services;

import com.netcracker.testerritto.dao.GradeCategoryDAO;
import com.netcracker.testerritto.exceptions.ServiceException;
import com.netcracker.testerritto.models.GradeCategory;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;

import java.math.BigInteger;
import java.util.List;

@Service
public class GradeCategoryService {
  private static Logger logger = Logger.getLogger(CategoryService.class.getName());

  @Autowired
  private GradeCategoryDAO gradeCategoryDAO;

  public GradeCategory getCategoryById(BigInteger id) throws Exception {
    if (id == null) {
      Exception ex = new IllegalArgumentException("Parameter(id) can't be null");
      logAndThrowException(ex);
    }
    try {
      return gradeCategoryDAO.getGradeCategoryById(id);
    } catch (DataAccessException e) {
      Exception ex = new ServiceException("Getting grade category by id was failed.", e);
      logAndThrowException(ex);
    }
    return null;
  }

  public List<GradeCategory> getGradeCategoryByTestId(BigInteger id) throws Exception {
    if (id == null) {
      Exception ex = new IllegalArgumentException("Parameter(testId) can't be null");
      logAndThrowException(ex);
    }
    try {
      return gradeCategoryDAO.getGradeCategoryByTestId(id);
    } catch (DataAccessException e) {
      Exception ex = new ServiceException("Getting grade category by testId was failed.", e);
      logAndThrowException(ex);
    }
    return null;
  }

  public List<GradeCategory> getGradeCategoryByCategoryId(BigInteger id) throws Exception {
    if (id == null) {
      Exception ex = new IllegalArgumentException("Parameter(categoryId) can't be null");
      logAndThrowException(ex);
    }
    try {
      return gradeCategoryDAO.getGradeCategoryByCategoryId(id);
    } catch (DataAccessException e) {
      Exception ex = new ServiceException("Getting grade category by categoryId was failed.", e);
      logAndThrowException(ex);
    }
    return null;
  }

  public BigInteger createGradeCategory(GradeCategory newGradeCategory) throws Exception {
    checkParamsForCreateUpdate(newGradeCategory);
    try {
      return gradeCategoryDAO.createGradeCategory(newGradeCategory);
    } catch (DataAccessException e) {
      Exception ex = new ServiceException("Creating grade category was failed", e);
      logAndThrowException(ex);
    }
    return null;
  }

  public void deleteGradeCategoryById(BigInteger id) throws Exception {
    if (id == null) {
      Exception ex = new IllegalArgumentException("Parameter(id) can't be null");
      logAndThrowException(ex);
    }
    try {
      gradeCategoryDAO.deleteGradeCategoryById(id);
    } catch (DataAccessException e) {
      Exception ex = new ServiceException("Deleting grade category by id was failed.", e);
      logAndThrowException(ex);
    }

  }

  public void deleteGradeCategoryByTestId(BigInteger id) throws Exception {
    if (id == null) {
      Exception ex = new IllegalArgumentException("Parameter(testId) can't be null");
      logAndThrowException(ex);
    }
    try {
      gradeCategoryDAO.deleteGradeCategoryByTestId(id);
    } catch (DataAccessException e) {
      Exception ex = new ServiceException("Deleting grade category by testId was failed.", e);
      logAndThrowException(ex);
    }
  }

  public void updateGradeCategory(GradeCategory updatedGradeCategory) throws Exception {
    checkParamsForCreateUpdate(updatedGradeCategory);
    try {
      gradeCategoryDAO.updateGradeCategory(updatedGradeCategory);
    } catch (DataAccessException e) {
      Exception ex = new ServiceException("Updating grade category was failed.", e);
      logAndThrowException(ex);
    }
  }

  private void checkParamsForCreateUpdate(GradeCategory checkedCategory) throws Exception {
    if (checkedCategory == null) {
      Exception ex = new IllegalArgumentException("Can't pass null object in method");
      logAndThrowException(ex);
    }
    if (checkedCategory.getId() == null || checkedCategory.getMeaning() == null ||
      checkedCategory.getTestId() == null || checkedCategory.getCategoryId() == null) {
      Exception ex = new IllegalArgumentException("Parameters(id, meaning, testId, categoryId) can't be null");
      logAndThrowException(ex);
    }
    if (checkedCategory.getMeaning().trim().isEmpty()) {
      Exception ex = new IllegalArgumentException("Parameter(meaning) can't be empty");
      logAndThrowException(ex);
    }
    if (checkedCategory.getMinScore() < 0 || checkedCategory.getMaxScore() < 0) {
      Exception ex = new IllegalArgumentException("Parameters(maxScore, minScore) can't be less then 0");
      logAndThrowException(ex);
    }
    if (checkedCategory.getMaxScore() < checkedCategory.getMinScore()) {
      Exception ex = new IllegalArgumentException("Parameters(maxScore, minScore) minScore can't be bigger than maxScore");
      logAndThrowException(ex);
    }
  }

  private void logAndThrowException(Exception ex) throws Exception {
    logger.error(ex.getMessage(), ex);
    throw ex;
  }
}
