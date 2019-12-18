package com.netcracker.testerritto.services;

import com.netcracker.testerritto.ApplicationConfiguration;
import com.netcracker.testerritto.exceptions.ServiceException;
import com.netcracker.testerritto.models.Category;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.math.BigInteger;

import static org.junit.Assert.assertEquals;

@SpringBootTest(classes = ApplicationConfiguration.class)
@RunWith(SpringRunner.class)
public class CategoryServiceTest {
    @Autowired
    CategoryService categoryService;

    private Category testCategory;

    @Before
    public void setUp() {
        testCategory = createValidCategory();
        BigInteger testCategoryId = categoryService.createCategory(testCategory);
        testCategory.setId(testCategoryId);
    }

    @Test(expected = IllegalArgumentException.class)
    public void getCategoryById_idIsNull() {
        categoryService.getCategoryById(null);
    }

    @Test(expected = ServiceException.class)
    public void getCategoryById_idNotExist() {
        categoryService.deleteCategoryById(testCategory.getId());
        categoryService.getCategoryById(testCategory.getId());
    }

    public void getCategoryById() {
        Category categoryFromDb = categoryService.getCategoryById(testCategory.getId());
        assertEquals(testCategory.getId(), categoryFromDb.getId());
        assertEquals(testCategory.getNameCategory(), categoryFromDb.getNameCategory());
    }

    @Test(expected = IllegalArgumentException.class)
    public void deleteCategoryById_idIsNull() {
        categoryService.deleteCategoryById(null);
    }

    @Test(expected = ServiceException.class)
    public void deleteCategoryById() {
        categoryService.deleteCategoryById(testCategory.getId());
        categoryService.getCategoryById(testCategory.getId());
    }

    @Test(expected = IllegalArgumentException.class)
    public void updateCategory_idIsNull() {
        categoryService.updateCategory(null);
    }

    @Test(expected = ServiceException.class)
    public void updateCategory_CategoryNotExist() {
        categoryService.deleteCategoryById(testCategory.getId());
        categoryService.updateCategory(testCategory);
    }

    @Test
    public void createCategory() {
        Category testCategory = new Category();
        testCategory.setNameCategory("Жизненная позиция");
        BigInteger idCreatedCategory = categoryService.createCategory(testCategory);
        testCategory.setId(idCreatedCategory);
        Category createdCategory = categoryService.getCategoryById(idCreatedCategory);
        assertEquals(testCategory.getId(), createdCategory.getId());
        assertEquals(testCategory.getNameCategory(), createdCategory.getNameCategory());
        categoryService.deleteCategoryById(testCategory.getId());
    }

    @Test(expected = IllegalArgumentException.class)
    public void createCategory_categoryNameParamIsNull() {
        Category incorrectCategoryForCreation = new Category();
        categoryService.createCategory(incorrectCategoryForCreation);
    }

    @After
    public void tearDown() {
        categoryService.deleteCategoryById(testCategory.getId());
    }

    private Category createValidCategory() {
        Category category = new Category();
        category.setNameCategory("Темперамент");
        return category;
    }
}