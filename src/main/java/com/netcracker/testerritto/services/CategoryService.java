package com.netcracker.testerritto.services;

import com.netcracker.testerritto.dao.CategoryDAO;
import com.netcracker.testerritto.exceptions.CategoryServiceException;
import com.netcracker.testerritto.models.Category;
import java.math.BigInteger;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;

@Service
public class CategoryService {
  private static Logger logger = Logger.getLogger(CategoryService.class.getName());

  @Autowired
  private CategoryDAO categoryDAO;

  public Category getCategoryById(BigInteger id) throws Exception {
    if (id == null) {
      RuntimeException ex = new NullPointerException("id can't be null");
      logExceptionAndThrowException(ex);
    }
    try {
      return categoryDAO.getCategoryById(id);
    } catch (DataAccessException e) {
      throw new CategoryServiceException(CategoryServiceException._FAIL_TO_GET);
    }
  }

  public void deleteCategoryById(BigInteger id) throws Exception {
    if (id == null) {
      RuntimeException ex = new NullPointerException("id can't be null");
      logExceptionAndThrowException(ex);
    }
    try {
      categoryDAO.deleteCategoryById(id);
    } catch (DataAccessException e) {
      throw new CategoryServiceException(CategoryServiceException._FAIL_TO_DELETE);
    }
  }

  public Category updateCategory(Category updatedCategory) throws Exception {
    if (updatedCategory.getId() == null || updatedCategory.getNameCategory() == null) {
      RuntimeException ex = new NullPointerException("Parameters of updated category are invalid");
      logExceptionAndThrowException(ex);
    }
    try {
      return categoryDAO.updateCategory(updatedCategory);
    } catch (DataAccessException e) {
      throw new CategoryServiceException(CategoryServiceException._FAIL_TO_UPDATE);
    }
  }

  public BigInteger createCategory(Category newCategory) throws Exception {
    if (newCategory.getNameCategory() == null) {
      RuntimeException ex = new NullPointerException("Parameters of created category are invalid");
      logExceptionAndThrowException(ex);
    }
    try {
      return categoryDAO.createCategory(newCategory);
    } catch (DataAccessException e) {
      throw new CategoryServiceException(CategoryServiceException._FAIL_TO_CREATE);
    }
  }

  private void logExceptionAndThrowException(Exception ex) throws Exception {
    logger.error(ex.getMessage(), ex);
    throw ex;
  }
}
