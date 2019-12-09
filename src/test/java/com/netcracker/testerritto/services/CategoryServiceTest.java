package com.netcracker.testerritto.services;

import static org.junit.Assert.assertEquals;

import com.netcracker.testerritto.ApplicationConfiguration;
import com.netcracker.testerritto.exceptions.CategoryServiceException;
import com.netcracker.testerritto.models.Category;
import java.math.BigInteger;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@SpringBootTest(classes = ApplicationConfiguration.class)
@RunWith(SpringRunner.class)
public class CategoryServiceTest {
  private Category testCategory;

  @Autowired
  CategoryService categoryService;

  @Before
  public void setUp() throws Exception {
    testCategory = createValidCategory();
    BigInteger testCategoryId = categoryService.createCategory(testCategory);
    testCategory.setId(testCategoryId);
  }

  @Test( expected = NullPointerException.class )
  public void getCategoryById_idIsNull() throws Exception {
    categoryService.getCategoryById(null);
  }

  @Test( expected = CategoryServiceException.class )
  public void getCategoryById_idNotExist() throws Exception {
    categoryService.deleteCategoryById(testCategory.getId());
    categoryService.getCategoryById(testCategory.getId());
  }

  public void getCategoryById() throws Exception {
    Category categoryFromDb = categoryService.getCategoryById(testCategory.getId());
    assertEquals(testCategory.getId(), categoryFromDb.getId());
    assertEquals(testCategory.getNameCategory(), categoryFromDb.getNameCategory());
  }

  @Test( expected = NullPointerException.class )
  public void deleteCategoryById_idIsNull() throws Exception {
    categoryService.deleteCategoryById(null);
  }

  @Test( expected = CategoryServiceException.class )
  public void deleteCategoryById() throws Exception {
    categoryService.deleteCategoryById(testCategory.getId());
    categoryService.getCategoryById(testCategory.getId());
  }

  @Test( expected = NullPointerException.class )
  public void updateCategory_idIsNull() throws Exception {
    categoryService.updateCategory(null);
  }

  @Test( expected = CategoryServiceException.class )
  public void updateCategory_CategoryNotExist() throws Exception {
    categoryService.deleteCategoryById(testCategory.getId());
    categoryService.updateCategory(testCategory);
  }

  @Test
  public void createCategory() throws Exception {
    Category testCategory = new Category();
    testCategory.setNameCategory("Жизненная позиция");
    BigInteger idCreatedCategory= categoryService.createCategory(testCategory);
    testCategory.setId(idCreatedCategory);
    Category createdCategory = categoryService.getCategoryById(idCreatedCategory);
    assertEquals(testCategory.getId(), createdCategory.getId());
    assertEquals(testCategory.getNameCategory(), createdCategory.getNameCategory());
    categoryService.deleteCategoryById(testCategory.getId());
  }

  @Test( expected = NullPointerException.class)
  public void createCategory_categoryNameParamIsNull() throws Exception {
    Category incorrectCategoryForCreation = new Category();
    categoryService.createCategory(incorrectCategoryForCreation);
  }

  @After
  public void tearDown() throws Exception {
    categoryService.deleteCategoryById(testCategory.getId());
  }

  private Category createValidCategory() {
    Category category = new Category();
    category.setNameCategory("Темперамент");
    return category;
  }
}