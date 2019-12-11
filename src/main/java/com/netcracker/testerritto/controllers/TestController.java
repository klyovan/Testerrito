package com.netcracker.testerritto.controllers;

import com.netcracker.testerritto.models.Category;
import com.netcracker.testerritto.models.GradeCategory;
import com.netcracker.testerritto.services.CategoryService;
import com.netcracker.testerritto.services.GradeCategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.math.BigInteger;

@RestController
@RequestMapping("test")
public class TestController {
  @Autowired
  private CategoryService categoryService;

  @Autowired
  private GradeCategoryService gradeCategoryService;

  @GetMapping("/category/{categoryId}")
  public Category getCategory(@PathVariable("categoryId") BigInteger id) throws Exception {
    return categoryService.getCategoryById(id);
  }

  @DeleteMapping("/category/{categoryId}")
  public void deleteCategory(@PathVariable("categoryId") BigInteger id) throws Exception {
    categoryService.deleteCategoryById(id);
  }

  @PutMapping("/category")
  public Category updateCategory(Category updateCategory) throws Exception {
    return categoryService.updateCategory(updateCategory);
  }

  @PostMapping("/category")
  public BigInteger createCategory(Category newCategory) throws Exception {
    return categoryService.createCategory(newCategory);
  }

  @GetMapping("/category/grade/{id}")
  public GradeCategory getGradeCategory(@PathVariable BigInteger id) throws Exception {
    return gradeCategoryService.getCategoryById(id);
  }

  @PostMapping("/category/grade")
  public BigInteger createGradeCategory(GradeCategory newGradeCategory) throws Exception {
    return gradeCategoryService.createGradeCategory(newGradeCategory);
  }

  @GetMapping("/hello")
  public String getHello() {
    return "Hello";
  }
}
