package com.netcracker.testerritto.services;

import com.netcracker.testerritto.dao.GradeCategoryDAO;
import com.netcracker.testerritto.models.GradeCategory;
import java.math.BigInteger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class GradeCategoryService {
  @Autowired
  private GradeCategoryDAO gradeCategoryDAO;

  public GradeCategory getCategoryById(BigInteger id) {
    return gradeCategoryDAO.getGradeCategoryById(id);
  }

  public BigInteger createGradeCategory(GradeCategory newGradeCategory) {
    return gradeCategoryDAO.createGradeCategory(newGradeCategory);
  }
}
