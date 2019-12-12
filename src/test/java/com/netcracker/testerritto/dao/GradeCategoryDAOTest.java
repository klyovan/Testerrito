package com.netcracker.testerritto.dao;

import com.netcracker.testerritto.ApplicationConfiguration;
import com.netcracker.testerritto.comparators.ObjectEavIdComparator;
import com.netcracker.testerritto.models.Category;
import com.netcracker.testerritto.models.GradeCategory;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.test.context.junit4.SpringRunner;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

@SpringBootTest(classes = ApplicationConfiguration.class)
@RunWith(SpringRunner.class)
public class GradeCategoryDAOTest {
  @Autowired
  GradeCategoryDAO gradeCategoryDAO;
  @Autowired
  CategoryDAO categoryDAO;
  @Autowired
  TestDAO testDAO;
  @Autowired
  UserDAO userDAO;
  @Autowired
  GroupDAO groupDAO;

  GradeCategory gradeCategoryExampleForTest;
  List<GradeCategory> expGradeCategories;
  BigInteger testId;
  BigInteger userId;
  BigInteger groupId;
  com.netcracker.testerritto.models.Test testForGradeCategory;

  @Before
  public void setUp() {
    userId = userDAO.createUser("Karina", "Marinina",
      "marinina.@gmail", "1111", "12345");
    groupId = groupDAO.createGroup(userId, "New Link http...", "Very cool group");

    testForGradeCategory = new com.netcracker.testerritto.models.Test();
    testForGradeCategory.setNameTest("Тест на личность");
    testForGradeCategory.setCreatorUserId(userId);
    testForGradeCategory.setGroupId(groupId);
    testId = testDAO.createTest(testForGradeCategory);
  }

  @Test
  public void getGradeCategoryByTestId() throws DataAccessException {
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
    for (GradeCategory expGradeCategory : expGradeCategories) {
      gradeCategoryDAO.deleteGradeCategoryById(expGradeCategory.getId());
    }
  }

  @Test
  public void getGradeCategoryByTestId_TestIdDoesntExistINDB() {
    List<GradeCategory> selectedGradeCategories = gradeCategoryDAO.getGradeCategoryByTestId(new BigInteger("-1"));
    assertTrue(selectedGradeCategories.isEmpty());
  }

  @Test
  public void getGradeCategoryById() throws DataAccessException {
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
  public void getGradeCategoryByCategoryId() throws DataAccessException {
    expGradeCategories = createGradeCategoriesForTest();
    expGradeCategories.sort(new ObjectEavIdComparator<>());

    List<GradeCategory> selectedGradeCategories = gradeCategoryDAO.getGradeCategoryByCategoryId(expGradeCategories.get(0).getCategoryId());
    selectedGradeCategories.sort(new ObjectEavIdComparator<>());

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
    for (GradeCategory expGradeCategory : expGradeCategories) {
      gradeCategoryDAO.deleteGradeCategoryById(expGradeCategory.getId());
    }
  }

  @Test
  public void createGradeCategory() throws DataAccessException {
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
  public void deleteGradeCategoryById() throws DataAccessException {
    gradeCategoryExampleForTest = createGradeCategoryForTest();
    gradeCategoryDAO.deleteGradeCategoryById(gradeCategoryExampleForTest.getId());
    categoryDAO.deleteCategoryById(gradeCategoryExampleForTest.getCategoryId());
    gradeCategoryDAO.getGradeCategoryById(gradeCategoryExampleForTest.getId());
  }

  @Test
  public void deleteGradeCategoryByTestId() throws DataAccessException {
    expGradeCategories = createGradeCategoriesForTest();
    gradeCategoryDAO.deleteGradeCategoryByTestId(expGradeCategories.get(0).getTestId());
    categoryDAO.deleteCategoryById(expGradeCategories.get(0).getCategoryId());
    List<GradeCategory> selectedGradeCategories = gradeCategoryDAO.getGradeCategoryByTestId(expGradeCategories.get(0).getTestId());
    assertTrue(selectedGradeCategories.isEmpty());
  }

  @Test
  public void updateGradeCategory() throws DataAccessException {
    gradeCategoryExampleForTest = createGradeCategoryForTest();
    gradeCategoryExampleForTest.setMeaning("Холерик");
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

  @After
  public void tearDown() {
    testDAO.deleteTest(testId);
    groupDAO.deleteGroup(groupId);
    userDAO.deleteUser(userId);
  }

  private BigInteger createCategoryForTest() throws DataAccessException {
    Category categoryExampleForTesting = new Category();
    categoryExampleForTesting.setNameCategory("Темперамент ");
    return categoryDAO.createCategory(categoryExampleForTesting);
  }

  private GradeCategory createGradeCategoryForTest() throws DataAccessException {
    gradeCategoryExampleForTest = new GradeCategory();
    gradeCategoryExampleForTest.setTestId(testId);
    gradeCategoryExampleForTest.setMinScore(2);
    gradeCategoryExampleForTest.setMaxScore(22);
    gradeCategoryExampleForTest.setMeaning("Экстраверт");
    gradeCategoryExampleForTest.setCategoryId(createCategoryForTest());
    BigInteger gradeCategoryId = gradeCategoryDAO.createGradeCategory(gradeCategoryExampleForTest);
    gradeCategoryExampleForTest.setId(gradeCategoryId);
    return gradeCategoryExampleForTest;
  }

  private List<GradeCategory> createGradeCategoriesForTest() throws DataAccessException {
    GradeCategory gradeCategory1 = new GradeCategory();
    GradeCategory gradeCategory2 = new GradeCategory();
    GradeCategory gradeCategory3 = new GradeCategory();
    GradeCategory gradeCategory4 = new GradeCategory();
    BigInteger category = createCategoryForTest();
    List<GradeCategory> gradeCategories = new ArrayList<>();

    gradeCategory1.setTestId(testId);
    gradeCategory1.setMinScore(2);
    gradeCategory1.setMaxScore(4);
    gradeCategory1.setMeaning("Интроверт");
    gradeCategory1.setCategoryId(category);
    gradeCategory1.setId(gradeCategoryDAO.createGradeCategory(gradeCategory1));

    gradeCategory2.setTestId(testId);
    gradeCategory2.setMinScore(5);
    gradeCategory2.setMaxScore(7);
    gradeCategory2.setMeaning("Экстраверт");
    gradeCategory2.setCategoryId(category);
    gradeCategory2.setId(gradeCategoryDAO.createGradeCategory(gradeCategory2));

    gradeCategory3.setTestId(testId);
    gradeCategory3.setMinScore(8);
    gradeCategory3.setMaxScore(10);
    gradeCategory3.setMeaning("Сангвиник");
    gradeCategory3.setCategoryId(category);
    gradeCategory3.setId(gradeCategoryDAO.createGradeCategory(gradeCategory3));

    gradeCategory4.setTestId(testId);
    gradeCategory4.setMinScore(11);
    gradeCategory4.setMaxScore(13);
    gradeCategory4.setMeaning("Флегматик");
    gradeCategory4.setCategoryId(category);
    gradeCategory4.setId(gradeCategoryDAO.createGradeCategory(gradeCategory4));

    gradeCategories.add(gradeCategory1);
    gradeCategories.add(gradeCategory4);
    gradeCategories.add(gradeCategory2);
    gradeCategories.add(gradeCategory3);

    return gradeCategories;
  }
}

























