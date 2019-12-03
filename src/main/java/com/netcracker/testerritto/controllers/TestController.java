package com.netcracker.testerritto.controllers;

import com.netcracker.testerritto.models.Category;
import com.netcracker.testerritto.models.GradeCategory;
import com.netcracker.testerritto.services.CategoryService;
import com.netcracker.testerritto.services.GradeCategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
public class TestController {
  @Autowired
  private CategoryService categoryService;

  @Autowired
  private GradeCategoryService gradeCategoryService;

  @GetMapping("/category/{categoryId}")
  public Category getCategory(@PathVariable("categoryId") int id) {
    return categoryService.getCategoryById(id);
  }

  @DeleteMapping("/category/{categoryId}")
  public void deleteCategory(@PathVariable("categoryId") int id) {
    categoryService.deleteCategoryById(id);
  }

  @PutMapping("/category")
  public Category updateCategory(Category updateCategory) {
    return categoryService.updateCategory(updateCategory);
  }

  @PostMapping("/category")
  public int createCategory(Category newCategory) {
    return categoryService.createCategory(newCategory);
  }

  @GetMapping("/category/grade/{id}")
  public GradeCategory getGradeCategory(@PathVariable int id) {
    return gradeCategoryService.getCategoryById(id);
  }

  @PostMapping("/category/grade")
  public int createGradeCategory(GradeCategory newGradeCategory) {
    return gradeCategoryService.createGradeCategory(newGradeCategory);
  }
}
