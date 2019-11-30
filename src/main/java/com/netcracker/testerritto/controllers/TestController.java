package com.netcracker.testerritto.controllers;

import com.netcracker.testerritto.models.Category;
import com.netcracker.testerritto.service.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
public class TestController {
    @Autowired
    private CategoryService categoryService;

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
}
