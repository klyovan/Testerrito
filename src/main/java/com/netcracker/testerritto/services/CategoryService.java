package com.netcracker.testerritto.services;

import com.netcracker.testerritto.dao.CategoryDAO;
import com.netcracker.testerritto.exceptions.ServiceException;
import com.netcracker.testerritto.models.Category;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;

import java.math.BigInteger;

@Service
public class CategoryService {
  private static Logger logger = Logger.getLogger(CategoryService.class.getName());

  @Autowired
  private CategoryDAO categoryDAO;

  public Category getCategoryById(BigInteger id) throws Exception {
    if (id == null) {
      Exception ex = new IllegalArgumentException("Parameter(id) can't be null");
      logAndThrowException(ex);
    }
    try {
      return categoryDAO.getCategoryById(id);
    } catch (DataAccessException e) {
      Exception ex = new ServiceException("Getting category by id was failed.", e);
      logAndThrowException(ex);
    }
    return null;
  }

  public void deleteCategoryById(BigInteger id) throws Exception {
    if (id == null) {
      Exception ex = new IllegalArgumentException("Parameter(id) can't be null");
      logAndThrowException(ex);
    }
    try {
      categoryDAO.deleteCategoryById(id);
    } catch (DataAccessException e) {
      Exception ex = new ServiceException("Deleting category by id was failed.", e);
      logAndThrowException(ex);
    }
  }

  public Category updateCategory(Category updatedCategory) throws Exception {
    if (updatedCategory == null) {
      Exception ex = new IllegalArgumentException("Can't pass null object in method");
      logAndThrowException(ex);
    }
    if (updatedCategory.getId() == null || updatedCategory.getNameCategory() == null) {
      Exception ex = new IllegalArgumentException("Parameters(id, nameCategory) can't be null");
      logAndThrowException(ex);
    }
    try {
      return categoryDAO.updateCategory(updatedCategory);
    } catch (DataAccessException e) {
      Exception ex = new ServiceException("Updating category was failed.", e);
      logAndThrowException(ex);
    }
    return null;
  }

  public BigInteger createCategory(Category newCategory) throws Exception {
    if (newCategory == null) {
      Exception ex = new IllegalArgumentException("Can't pass null object in method");
      logAndThrowException(ex);
    }
    if (newCategory.getNameCategory() == null) {
      Exception ex = new IllegalArgumentException("Parameter(nameCategory) can't be null");
      logAndThrowException(ex);
    }
    try {
      return categoryDAO.createCategory(newCategory);
    } catch (DataAccessException e) {
      Exception ex = new ServiceException("Creating category was failed", e);
      logAndThrowException(ex);
    }
    return null;
  }

  private void logAndThrowException(Exception ex) throws Exception {
    logger.error(ex.getMessage(), ex);
    throw ex;
  }
}
