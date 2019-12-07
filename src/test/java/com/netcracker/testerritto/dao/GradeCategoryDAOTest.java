package com.netcracker.testerritto.dao;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import com.netcracker.testerritto.ApplicationConfiguration;
import com.netcracker.testerritto.comparators.ObjectEavIdComparator;
import com.netcracker.testerritto.models.Category;
import com.netcracker.testerritto.models.GradeCategory;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.test.context.junit4.SpringRunner;

@SpringBootTest(classes = ApplicationConfiguration.class)
@RunWith(SpringRunner.class)
public class GradeCategoryDAOTest {
  @Autowired
  GradeCategoryDAO gradeCategoryDAO;

  @Autowired
  CategoryDAO categoryDAO;

  GradeCategory gradeCategoryExampleForTest;
  List<GradeCategory> expGradeCategories;

  @Before
  public void setUp() throws Exception {
    Locale.setDefault(Locale.ENGLISH);
  }

  @Test
  public void getGradeCategoryByTestId() {
    expGradeCategories = createGradeCategoriesForTest();
    expGradeCategories.sort(new ObjectEavIdComparator());

    List<GradeCategory> selectedGradeCategories = gradeCategoryDAO.getGradeCategoryByTestId(expGradeCategories.get(0).getTestId());
    selectedGradeCategories.sort(new ObjectEavIdComparator());

    assertEquals(expGradeCategories.size(), selectedGradeCategories.size());

    for (int i = 0; i < expGradeCategories.size(); i++) {
      assertEquals(expGradeCategories.get(i).getId(), selectedGradeCategories.get(i).getId());
      assertEquals(expGradeCategories.get(i).getTestId(), selectedGradeCategories.get(i).getTestId());
      assertEquals(expGradeCategories.get(i).getMinScore(), selectedGradeCategories.get(i).getMinScore());
      assertEquals(expGradeCategories.get(i).getMaxScore(), selectedGradeCategories.get(i).getMaxScore());
      assertEquals(expGradeCategories.get(i).getMeaning(), selectedGradeCategories.get(i).getMeaning());
      assertEquals(expGradeCategories.get(i).getCategoryId(), selectedGradeCategories.get(i).getCategoryId());
    }

    categoryDAO.deleteCategoryById(expGradeCategories.get(0).getCategoryId());
    for (GradeCategory expGradeCategory: expGradeCategories) {
      gradeCategoryDAO.deleteGradeCategoryById(expGradeCategory.getId());
    }
  }

  @Test
  public void getGradeCategoryByTestId_TestIdDoesntExistINDB() {
    List<GradeCategory> selectedGradeCategories = gradeCategoryDAO.getGradeCategoryByTestId(new BigInteger("-1"));
    assertTrue(selectedGradeCategories.isEmpty());
  }

  @Test
  public void getGradeCategoryById() {
    gradeCategoryExampleForTest = createGradeCategoryForTest();
    GradeCategory gradeCategoryFromDB = gradeCategoryDAO.getGradeCategoryById(gradeCategoryExampleForTest.getId());

    assertEquals(gradeCategoryExampleForTest.getId(), gradeCategoryFromDB.getId());
    assertEquals(gradeCategoryExampleForTest.getTestId(), gradeCategoryFromDB.getTestId());
    assertEquals(gradeCategoryExampleForTest.getMinScore(), gradeCategoryFromDB.getMinScore());
    assertEquals(gradeCategoryExampleForTest.getMaxScore(), gradeCategoryFromDB.getMaxScore());
    assertEquals(gradeCategoryExampleForTest.getMeaning(), gradeCategoryFromDB.getMeaning());
    assertEquals(gradeCategoryExampleForTest.getCategoryId(), gradeCategoryFromDB.getCategoryId());

    gradeCategoryDAO.deleteGradeCategoryById(gradeCategoryExampleForTest.getId());
    categoryDAO.deleteCategoryById(gradeCategoryExampleForTest.getCategoryId());

  }

  @Test
  public void getGradeCategoryByCategoryId() {
    expGradeCategories = createGradeCategoriesForTest();
    expGradeCategories.sort(new ObjectEavIdComparator());

    List<GradeCategory> selectedGradeCategories = gradeCategoryDAO.getGradeCategoryByCategoryId(expGradeCategories.get(0).getCategoryId());
    System.out.println(selectedGradeCategories.toString());
    selectedGradeCategories.sort(new ObjectEavIdComparator());

    assertEquals(expGradeCategories.size(), selectedGradeCategories.size());

    for (int i = 0; i < expGradeCategories.size(); i++) {
      assertEquals(expGradeCategories.get(i).getId(), selectedGradeCategories.get(i).getId());
      assertEquals(expGradeCategories.get(i).getTestId(), selectedGradeCategories.get(i).getTestId());
      assertEquals(expGradeCategories.get(i).getMinScore(), selectedGradeCategories.get(i).getMinScore());
      assertEquals(expGradeCategories.get(i).getMaxScore(), selectedGradeCategories.get(i).getMaxScore());
      assertEquals(expGradeCategories.get(i).getMeaning(), selectedGradeCategories.get(i).getMeaning());
      assertEquals(expGradeCategories.get(i).getCategoryId(), selectedGradeCategories.get(i).getCategoryId());
    }

    categoryDAO.deleteCategoryById(expGradeCategories.get(0).getCategoryId());
    for (GradeCategory expGradeCategory: expGradeCategories) {
      gradeCategoryDAO.deleteGradeCategoryById(expGradeCategory.getId());
    }
  }

  @Test
  public void createGradeCategory() {
    gradeCategoryExampleForTest = createGradeCategoryForTest();
    GradeCategory gradeCategoryFromDB = gradeCategoryDAO.getGradeCategoryById(gradeCategoryExampleForTest.getId());

    assertEquals(gradeCategoryExampleForTest.getId(), gradeCategoryFromDB.getId());
    assertEquals(gradeCategoryExampleForTest.getTestId(), gradeCategoryFromDB.getTestId());
    assertEquals(gradeCategoryExampleForTest.getMinScore(), gradeCategoryFromDB.getMinScore());
    assertEquals(gradeCategoryExampleForTest.getMaxScore(), gradeCategoryFromDB.getMaxScore());
    assertEquals(gradeCategoryExampleForTest.getMeaning(), gradeCategoryFromDB.getMeaning());
    assertEquals(gradeCategoryExampleForTest.getCategoryId(), gradeCategoryFromDB.getCategoryId());

    gradeCategoryDAO.deleteGradeCategoryById(gradeCategoryExampleForTest.getId());
    categoryDAO.deleteCategoryById(gradeCategoryExampleForTest.getCategoryId());
  }

  @Test(expected = EmptyResultDataAccessException.class)
  public void deleteGradeCategoryById() {
    gradeCategoryExampleForTest = createGradeCategoryForTest();
    gradeCategoryDAO.deleteGradeCategoryById(gradeCategoryExampleForTest.getId());
    categoryDAO.deleteCategoryById(gradeCategoryExampleForTest.getCategoryId());
    gradeCategoryDAO.getGradeCategoryById(gradeCategoryExampleForTest.getId());
  }

  @Test
  public void deleteGradeCategoryByTestId() {
    expGradeCategories = createGradeCategoriesForTest();
    gradeCategoryDAO.deleteGradeCategoryByTestId(expGradeCategories.get(0).getTestId());
    categoryDAO.deleteCategoryById(expGradeCategories.get(0).getCategoryId());
    List<GradeCategory> selectedGradeCategories = gradeCategoryDAO.getGradeCategoryByTestId(expGradeCategories.get(0).getTestId());
    assertTrue(selectedGradeCategories.isEmpty());
  }

  @Test
  public void updateGradeCategory() {
    gradeCategoryExampleForTest = createGradeCategoryForTest();
    gradeCategoryExampleForTest.setMeaning("new meaning");
    gradeCategoryExampleForTest.setMinScore(100);
    gradeCategoryExampleForTest.setMaxScore(200);

    GradeCategory updatedGradeCategoryExampleForTest = gradeCategoryDAO.updateGradeCategory(gradeCategoryExampleForTest);

    assertEquals(gradeCategoryExampleForTest.getId(), updatedGradeCategoryExampleForTest.getId());
    assertEquals(gradeCategoryExampleForTest.getTestId(), updatedGradeCategoryExampleForTest.getTestId());
    assertEquals(gradeCategoryExampleForTest.getCategoryId(), updatedGradeCategoryExampleForTest.getCategoryId());
    assertEquals(gradeCategoryExampleForTest.getMeaning(), updatedGradeCategoryExampleForTest.getMeaning());
    assertEquals(gradeCategoryExampleForTest.getMinScore(), updatedGradeCategoryExampleForTest.getMinScore());
    assertEquals(gradeCategoryExampleForTest.getMaxScore(), updatedGradeCategoryExampleForTest.getMaxScore());

    gradeCategoryDAO.deleteGradeCategoryById(gradeCategoryExampleForTest.getId());
    categoryDAO.deleteCategoryById(gradeCategoryExampleForTest.getCategoryId());

  }

  private BigInteger createCategoryForTest() {
    Category categoryExampleForTesting = new Category();
    categoryExampleForTesting.setNameCategory("Test Category");
    return categoryDAO.createCategory(categoryExampleForTesting);
  }

  private GradeCategory createGradeCategoryForTest() {
    BigInteger testId = new BigInteger("-10025");
    gradeCategoryExampleForTest = new GradeCategory();
    gradeCategoryExampleForTest.setTestId(testId);
    gradeCategoryExampleForTest.setMinScore(2);
    gradeCategoryExampleForTest.setMaxScore(22);
    gradeCategoryExampleForTest.setMeaning("Все не так плохо");
    gradeCategoryExampleForTest.setCategoryId(createCategoryForTest());
    BigInteger gradeCategoryId = gradeCategoryDAO.createGradeCategory(gradeCategoryExampleForTest);
    gradeCategoryExampleForTest.setId(gradeCategoryId);
    return gradeCategoryExampleForTest;
  }

  private List<GradeCategory> createGradeCategoriesForTest() {
    // DELETE ALL GRADE CATEGORIES WHERE TEST_ID = -10025
    BigInteger testId = new BigInteger("-10025");
    gradeCategoryDAO.deleteGradeCategoryByTestId(testId);
    GradeCategory gradeCategory1 = new GradeCategory();
    GradeCategory gradeCategory2 = new GradeCategory();
    GradeCategory gradeCategory3 = new GradeCategory();
    GradeCategory gradeCategory4 = new GradeCategory();
    BigInteger category = createCategoryForTest();
    List<GradeCategory> gradeCategories = new ArrayList<>();

    gradeCategory1.setTestId(testId);
    gradeCategory1.setMinScore(2);
    gradeCategory1.setMaxScore(4);
    gradeCategory1.setMeaning("Grade1");
    gradeCategory1.setCategoryId(category);
    gradeCategory1.setId(gradeCategoryDAO.createGradeCategory(gradeCategory1));

    gradeCategory2.setTestId(testId);
    gradeCategory2.setMinScore(5);
    gradeCategory2.setMaxScore(7);
    gradeCategory2.setMeaning("Grade2");
    gradeCategory2.setCategoryId(category);
    gradeCategory2.setId(gradeCategoryDAO.createGradeCategory(gradeCategory2));

    gradeCategory3.setTestId(testId);
    gradeCategory3.setMinScore(8);
    gradeCategory3.setMaxScore(10);
    gradeCategory3.setMeaning("Grade3");
    gradeCategory3.setCategoryId(category);
    gradeCategory3.setId(gradeCategoryDAO.createGradeCategory(gradeCategory3));

    gradeCategory4.setTestId(testId);
    gradeCategory4.setMinScore(11);
    gradeCategory4.setMaxScore(13);
    gradeCategory4.setMeaning("Grade4");
    gradeCategory4.setCategoryId(category);
    gradeCategory4.setId(gradeCategoryDAO.createGradeCategory(gradeCategory4));

    gradeCategories.add(gradeCategory1);
    gradeCategories.add(gradeCategory4);
    gradeCategories.add(gradeCategory2);
    gradeCategories.add(gradeCategory3);

    return gradeCategories;
  }
}

























