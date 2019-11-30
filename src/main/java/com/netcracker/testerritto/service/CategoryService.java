package com.netcracker.testerritto.service;

import com.netcracker.testerritto.dao.CategoryDAO;
import com.netcracker.testerritto.models.Category;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CategoryService {
    @Autowired
    private CategoryDAO categoryDAO;

    public Category getCategoryById(int id) {
        return categoryDAO.getCategoryById(id);
    }

    public void deleteCategoryById(int id) {
        categoryDAO.deleteCategoryById(id);
    }

    public Category updateCategory(Category updatedCategory) {
        return categoryDAO.updateCategory(updatedCategory);
    }

    public int createCategory(Category newCategory) {
        return categoryDAO.createCategory(newCategory);
    }
}
