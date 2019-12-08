package com.netcracker.testerritto.dao;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;

import com.netcracker.testerritto.ApplicationConfiguration;
import com.netcracker.testerritto.models.Category;
import java.math.BigInteger;
import java.util.Locale;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.test.context.junit4.SpringRunner;

@SpringBootTest(classes = ApplicationConfiguration.class)
@RunWith(SpringRunner.class)
public class CategoryDAOTest {
  @Autowired
  CategoryDAO categoryDAO;

  Category categoryExampleForTesting = new Category();

  @Before
  public void setUp() throws Exception {
    Locale.setDefault(Locale.ENGLISH);
    categoryExampleForTesting.setNameCategory("Introvert");
    BigInteger categoryId = categoryDAO.createCategory(categoryExampleForTesting);
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
    String newAttrNameCategory = "Introvert with a tendency to depression";
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