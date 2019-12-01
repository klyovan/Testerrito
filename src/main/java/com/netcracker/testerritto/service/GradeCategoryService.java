package com.netcracker.testerritto.service;

import com.netcracker.testerritto.dao.GradeCategoryDAO;
import com.netcracker.testerritto.models.GradeCategory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class GradeCategoryService {
    @Autowired
    private GradeCategoryDAO gradeCategoryDAO;

    public GradeCategory getCategoryById(int id) {
        return gradeCategoryDAO.getGradeCategoryById(id);
    }

    public int createGradeCategory(GradeCategory newGradeCategory) {
        return gradeCategoryDAO.createGradeCategory(newGradeCategory);
    }
}
