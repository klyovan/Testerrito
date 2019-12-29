package com.netcracker.testerritto.controllers;

import com.netcracker.testerritto.exceptions.ApiRequestException;
import com.netcracker.testerritto.exceptions.ServiceException;
import com.netcracker.testerritto.models.Category;
import com.netcracker.testerritto.models.GradeCategory;
import com.netcracker.testerritto.models.Reply;
import com.netcracker.testerritto.models.Test;
import com.netcracker.testerritto.services.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.math.BigInteger;
import java.util.List;

@RestController
@RequestMapping("test")
public class TestController {

    @Autowired
    private TestService testService;

    @Autowired
    private CategoryService categoryService;

    @Autowired
    private GradeCategoryService gradeCategoryService;

    @Autowired
    private GroupService groupService;
    @Autowired
    private ReplyService replyService;

    @PostMapping("/test")
    public BigInteger createTest(@RequestBody Test test) {
        try {
            return testService.createTest(test);
        } catch (IllegalArgumentException | ServiceException e) {
            throw new ApiRequestException(e.getMessage(), e);
        }
    }

    @PutMapping("/test")
    public BigInteger updateTest(@RequestBody Test test) {
        try {
            return testService.updateTest(test);
        } catch (IllegalArgumentException | ServiceException e) {
            throw new ApiRequestException(e.getMessage(), e);
        }
    }

    @DeleteMapping("/{id}")
    public void deleteTest(@PathVariable BigInteger id) {
        try {
            testService.deleteTest(id);
        } catch (IllegalArgumentException | ServiceException e) {
            throw new ApiRequestException(e.getMessage(), e);
        }
    }


    @GetMapping("/category/{id}")
    public Category getCategoryById(@PathVariable BigInteger id) {
        try {
            return categoryService.getCategoryById(id);
        } catch (IllegalArgumentException | ServiceException e) {
            throw new ApiRequestException(e.getMessage(), e);
        }
    }

    @DeleteMapping("/category/{id}")
    public void deleteCategory(@PathVariable BigInteger id) {
        try {
            categoryService.deleteCategoryById(id);
        } catch (IllegalArgumentException | ServiceException e) {
            throw new ApiRequestException(e.getMessage(), e);
        }
    }

    @PutMapping("/category")
    public Category updateCategory(@RequestBody Category updateCategory) {
        try {
            return categoryService.updateCategory(updateCategory);
        } catch (IllegalArgumentException | ServiceException e) {
            throw new ApiRequestException(e.getMessage(), e);
        }
    }

    @PostMapping("/category")
    public BigInteger createCategory(@RequestBody Category newCategory) {
        try {
            return categoryService.createCategory(newCategory);
        } catch (IllegalArgumentException | ServiceException e) {
            throw new ApiRequestException(e.getMessage(), e);
        }
    }

    @GetMapping("/category/grade/{id}")
    public GradeCategory getGradeCategoryById(@PathVariable BigInteger id) {
        try {
            return gradeCategoryService.getGradeCategoryById(id);
        } catch (IllegalArgumentException | ServiceException e) {
            throw new ApiRequestException(e.getMessage(), e);
        }
    }

    @GetMapping("/category/grade/test/{id}")
    public List<GradeCategory> getGradeCategoryByTestId(@PathVariable BigInteger id) {
        try {
            return gradeCategoryService.getGradeCategoryByTestId(id);
        } catch (IllegalArgumentException | ServiceException e) {
            throw new ApiRequestException(e.getMessage(), e);
        }
    }

    @GetMapping("/category/grade/category/{id}")
    public List<GradeCategory> getGradeCategoryByCategoryId(@PathVariable BigInteger id) {
        try {
            return gradeCategoryService.getGradeCategoryByCategoryId(id);
        } catch (IllegalArgumentException | ServiceException e) {
            throw new ApiRequestException(e.getMessage(), e);
        }
    }

    @PostMapping("/category/grade")
    public BigInteger createGradeCategory(@RequestBody GradeCategory newGradeCategory) {
        try {
            return gradeCategoryService.createGradeCategory(newGradeCategory);
        } catch (IllegalArgumentException | ServiceException e) {
            throw new ApiRequestException(e.getMessage(), e);
        }
    }

    @PutMapping("/category/grade")
    public void updateGradeCategory(@RequestBody GradeCategory updateGradeCategory) {
        try {
            gradeCategoryService.updateGradeCategory(updateGradeCategory);
        } catch (IllegalArgumentException | ServiceException e) {
            throw new ApiRequestException(e.getMessage(), e);
        }
    }

    @DeleteMapping("/category/grade/{id}")
    public void deleteGradeCategoryById(@PathVariable BigInteger id) {
        try {
            gradeCategoryService.deleteGradeCategoryById(id);
        } catch (IllegalArgumentException | ServiceException e) {
            throw new ApiRequestException(e.getMessage(), e);
        }
    }

    @DeleteMapping("/category/grade/test/{id}")
    public void deleteGradeCategoryByTestId(@PathVariable BigInteger id) {
        try {
            gradeCategoryService.deleteGradeCategoryByTestId(id);
        } catch (IllegalArgumentException | ServiceException e) {
            throw new ApiRequestException(e.getMessage(), e);
        }
    }

    @PostMapping("/reply")
    public BigInteger createReply(@RequestBody Reply reply){
        try {
         return replyService.createReply(reply);
        } catch (IllegalArgumentException | ServiceException e) {
            throw new ApiRequestException(e.getMessage(), e);
        }
    }
}
