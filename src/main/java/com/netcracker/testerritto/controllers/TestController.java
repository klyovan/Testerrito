package com.netcracker.testerritto.controllers;

import com.netcracker.testerritto.exceptions.ApiRequestException;
import com.netcracker.testerritto.exceptions.ServiceException;
import com.netcracker.testerritto.models.*;
import com.netcracker.testerritto.services.AnswerService;
import com.netcracker.testerritto.services.CategoryService;
import com.netcracker.testerritto.services.GradeCategoryService;
import com.netcracker.testerritto.services.GroupService;
import com.netcracker.testerritto.services.QuestionService;
import com.netcracker.testerritto.services.TestService;
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
    private AnswerService answerService;

    @Autowired
    private QuestionService questionService;

    @Autowired
    private GroupService groupService;


    @GetMapping("{id}")
    public Test getTest(@PathVariable BigInteger id) {
        try {
            return testService.getTest(id);
        } catch (IllegalArgumentException | ServiceException e) {
            throw new ApiRequestException(e.getMessage(), e);
        }
    }

    @PostMapping("/create")
    public BigInteger createTest(@RequestBody Test test) {
        try {
            return testService.createTest(test);
        } catch (IllegalArgumentException | ServiceException e) {
            throw new ApiRequestException(e.getMessage(), e);
        }
    }

    @PutMapping("/test")
    public Test updateTest(@RequestBody Test test) {
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

    @PostMapping("/category/create")
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

    @PostMapping("/category/grade/create")
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

    @PostMapping("/question/create")
    public BigInteger createQuestion(@RequestBody Question newQuestion) {
        try {
            return questionService.createQuestion(newQuestion);
        } catch (IllegalArgumentException | ServiceException e) {
            throw new ApiRequestException(e.getMessage(), e);
        }
    }

    @PutMapping("/question")
    public void updateQuestion(@RequestBody Question question) {
        try {
            questionService.updateQuestion(question);
        } catch (IllegalArgumentException | ServiceException e) {
            throw new ApiRequestException(e.getMessage(), e);
        }
    }

    @DeleteMapping("/question/{id}")
    public void deleteQuestionById(@PathVariable BigInteger id) {
        try {
            questionService.deleteQuestionById(id);
        } catch (IllegalArgumentException | ServiceException e) {
            throw new ApiRequestException(e.getMessage(), e);
        }
    }

    @GetMapping("/question/{id}")
    public Question getQuestionById(@PathVariable BigInteger id) {
        try {
            return questionService.getQuestionById(id);
        } catch (IllegalArgumentException | ServiceException e) {
            throw new ApiRequestException(e.getMessage(), e);
        }
    }

    @GetMapping("/question/test/{id}")
    public List<Question> getAllQuestionInTest(@PathVariable BigInteger id) {
        try {
            return questionService.getAllQuestionInTest(id);
        } catch (IllegalArgumentException | ServiceException e) {
            throw new ApiRequestException(e.getMessage(), e);
        }
    }

    @PostMapping("/answer/create")
    public BigInteger createAnswer(@RequestBody Answer newAnswer) {
        try {
            return answerService.createAnswer(newAnswer);
        } catch (IllegalArgumentException | ServiceException e) {
            throw new ApiRequestException(e.getMessage(), e);
        }
    }

    @PutMapping("/answer")
    public void updateAnswer(@RequestBody Answer newAnswer) {
        try {
            answerService.updateAnswer(newAnswer);
        } catch (IllegalArgumentException | ServiceException e) {
            throw new ApiRequestException(e.getMessage(), e);
        }
    }

    @DeleteMapping("/answer/{id}")
    public void deleteAnswerById(@PathVariable BigInteger id) {
        try {
            answerService.deleteAnswer(id);
        } catch (IllegalArgumentException | ServiceException e) {
            throw new ApiRequestException(e.getMessage(), e);
        }
    }

    @GetMapping("/answer/{id}")
    public Answer getAnswerById(@PathVariable BigInteger id) {
        try {
            return answerService.getAnswerById(id);
        } catch (IllegalArgumentException | ServiceException e) {
            throw new ApiRequestException(e.getMessage(), e);
        }
    }

    @GetMapping("/answer/question/{id}")
    public List<Answer> getAllAnswerInQuestion(@PathVariable BigInteger id) {
        try {
            return answerService.getAllAnswerInQuestion(id);
        } catch (IllegalArgumentException | ServiceException e) {
            throw new ApiRequestException(e.getMessage(), e);
        }
    }
}
