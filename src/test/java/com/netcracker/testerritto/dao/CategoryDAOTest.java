package com.netcracker.testerritto.dao;

import com.netcracker.testerritto.ApplicationConfiguration;
import com.netcracker.testerritto.DataSourceConfig;
import com.netcracker.testerritto.models.Category;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.context.web.WebAppConfiguration;

import java.util.Locale;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;

@SpringBootTest(classes = ApplicationConfiguration.class)
@RunWith(SpringRunner.class)
public class CategoryDAOTest {
  @Autowired
  CategoryDAO categoryDAO;

  Category categoryExampleForTesting = new Category();

  @Before
  public void setUp() throws Exception {
    Locale.setDefault(Locale.ENGLISH);
    categoryExampleForTesting.setNameCategory("Test Category");
    int categoryId = categoryDAO.createCategory(categoryExampleForTesting);
    categoryExampleForTesting.setId(categoryId);
  }

  @Test
  public void createCategory() {
    assertNotEquals(0, categoryExampleForTesting.getId());
  }

  @Test
  public void getCategoryById() {
    Category selectedCategory = categoryDAO.getCategoryById(categoryExampleForTesting.getId());
    assertEquals(categoryExampleForTesting.getId(), selectedCategory.getId());
    assertEquals(categoryExampleForTesting.getNameCategory(), selectedCategory.getNameCategory());
  }

  @Test
  public void updateCategory() {
    String newAttrNameCategory = "new value";
    categoryExampleForTesting.setNameCategory(newAttrNameCategory);
    Category updatedCategory = categoryDAO.updateCategory(categoryExampleForTesting);
    assertEquals(categoryExampleForTesting.getId(), updatedCategory.getId());
    assertEquals(newAttrNameCategory, updatedCategory.getNameCategory());

  }

  @Test(expected = EmptyResultDataAccessException.class)
  public void deleteCategoryById() {
    categoryDAO.deleteCategoryById(categoryExampleForTesting.getId());
    categoryDAO.getCategoryById(categoryExampleForTesting.getId());
  }

  @After
  public void tearDown() throws Exception {
    categoryDAO.deleteCategoryById(categoryExampleForTesting.getId());
  }
}