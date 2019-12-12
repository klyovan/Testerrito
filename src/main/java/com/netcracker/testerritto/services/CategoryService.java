package com.netcracker.testerritto.services;

import com.netcracker.testerritto.dao.CategoryDAO;
import com.netcracker.testerritto.exceptions.ServiceException;
import com.netcracker.testerritto.handlers.ServiceExceptionHandler;
import com.netcracker.testerritto.models.Category;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;

import java.math.BigInteger;

@Service
public class CategoryService {
    @Autowired
    private ServiceExceptionHandler serviceExceptionHandler;
    @Autowired
    private CategoryDAO categoryDAO;

    public Category getCategoryById(BigInteger id) throws IllegalArgumentException, ServiceException {
        if (id == null) {
            serviceExceptionHandler.logAndThrowIllegalException("Parameter(id) can't be null");
        }
        try {
            return categoryDAO.getCategoryById(id);
        } catch (DataAccessException e) {
            serviceExceptionHandler.logAndThrowServiceException("Getting category by id was failed.", e);
        }
        return null;
    }

    public void deleteCategoryById(BigInteger id) throws IllegalArgumentException, ServiceException {
        if (id == null) {
            serviceExceptionHandler.logAndThrowIllegalException("Parameter(id) can't be null");
        }
        try {
            categoryDAO.deleteCategoryById(id);
        } catch (DataAccessException e) {
            serviceExceptionHandler.logAndThrowServiceException("Deleting category by id was failed.", e);
        }
    }

    public Category updateCategory(Category updatedCategory) throws IllegalArgumentException, ServiceException {
        if (updatedCategory == null) {
            serviceExceptionHandler.logAndThrowIllegalException("Can't pass null object in method");
        }
        if (updatedCategory.getId() == null || updatedCategory.getNameCategory() == null) {
            serviceExceptionHandler.logAndThrowIllegalException("Parameters(id, nameCategory) can't be null");
        }
        try {
            return categoryDAO.updateCategory(updatedCategory);
        } catch (DataAccessException e) {
            serviceExceptionHandler.logAndThrowServiceException("Updating category was failed.", e);
        }
        return null;
    }

    public BigInteger createCategory(Category newCategory) throws IllegalArgumentException, ServiceException {
        if (newCategory == null) {
            serviceExceptionHandler.logAndThrowIllegalException("Can't pass null object in method");
        }
        if (newCategory.getNameCategory() == null) {
            serviceExceptionHandler.logAndThrowIllegalException("Parameter(nameCategory) can't be null");
        }
        try {
            return categoryDAO.createCategory(newCategory);
        } catch (DataAccessException e) {
            serviceExceptionHandler.logAndThrowServiceException("Creating category was failed", e);
        }
        return null;
    }

}
