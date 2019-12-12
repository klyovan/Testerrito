package com.netcracker.testerritto.services;

import com.netcracker.testerritto.ApplicationConfiguration;
import com.netcracker.testerritto.comparators.ObjectEavIdComparator;
import com.netcracker.testerritto.dao.CategoryDAO;
import com.netcracker.testerritto.dao.GroupDAO;
import com.netcracker.testerritto.dao.TestDAO;
import com.netcracker.testerritto.dao.UserDAO;
import com.netcracker.testerritto.exceptions.ServiceException;
import com.netcracker.testerritto.models.Category;
import com.netcracker.testerritto.models.GradeCategory;
import com.netcracker.testerritto.models.Group;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

@SpringBootTest(classes = ApplicationConfiguration.class)
@RunWith(SpringRunner.class)
public class GradeCategoryServiceTest {
    @Autowired
    GradeCategoryService gradeCategoryService;
    @Autowired
    UserDAO userDAO;
    @Autowired
    GroupDAO groupDAO;
    @Autowired
    CategoryDAO categoryDAO;
    @Autowired
    TestDAO testDAO;

    private GradeCategory testGradeCategory;
    private List<GradeCategory> testGradeCategories;
    private BigInteger groupId;
    private BigInteger userId;
    private BigInteger categoryId;
    private BigInteger testId;

    @Test
    public void getCategoryById() throws IllegalArgumentException, ServiceException {
        testGradeCategory = buildValidGradeCategory();
        testGradeCategory.setId(gradeCategoryService.createGradeCategory(testGradeCategory));
        GradeCategory gradeCategoryFromDb = gradeCategoryService.getCategoryById(testGradeCategory.getId());
        assertEquals(testGradeCategory.getId(), gradeCategoryFromDb.getId());
        assertEquals(testGradeCategory.getMeaning(), gradeCategoryFromDb.getMeaning());
        assertEquals(testGradeCategory.getMaxScore(), gradeCategoryFromDb.getMaxScore());
        assertEquals(testGradeCategory.getMinScore(), gradeCategoryFromDb.getMinScore());
        assertEquals(testGradeCategory.getTestId(), gradeCategoryFromDb.getTestId());
        assertEquals(testGradeCategory.getCategoryId(), gradeCategoryFromDb.getCategoryId());

        gradeCategoryService.deleteGradeCategoryByTestId(testId);
        categoryDAO.deleteCategoryById(categoryId);
        testDAO.deleteTest(testId);
        groupDAO.deleteGroup(groupId);
        userDAO.deleteUser(userId);
    }

    @Test(expected = IllegalArgumentException.class)
    public void getCategoryById_idIsNull() throws IllegalArgumentException, ServiceException {
        gradeCategoryService.getCategoryById(null);
    }

    @Test
    public void getGradeCategoryByTestId() throws IllegalArgumentException, ServiceException {
        testGradeCategories = createGradeCategoriesForTest();
        List<GradeCategory> gradeCategoriesFromDb = gradeCategoryService.getGradeCategoryByTestId(testGradeCategories.get(0).getTestId());
        assertEquals(testGradeCategories.size(), gradeCategoriesFromDb.size());
        gradeCategoriesFromDb.sort(new ObjectEavIdComparator<>());
        for (int i = 0; i < testGradeCategories.size(); i++) {
            assertEquals(testGradeCategories.get(i).getId(), gradeCategoriesFromDb.get(i).getId());
            assertEquals(testGradeCategories.get(i).getTestId(), gradeCategoriesFromDb.get(i).getTestId());
            assertEquals(testGradeCategories.get(i).getMinScore(), gradeCategoriesFromDb.get(i).getMinScore());
            assertEquals(testGradeCategories.get(i).getMaxScore(), gradeCategoriesFromDb.get(i).getMaxScore());
            assertEquals(testGradeCategories.get(i).getMeaning(), gradeCategoriesFromDb.get(i).getMeaning());
            assertEquals(testGradeCategories.get(i).getCategoryId(), gradeCategoriesFromDb.get(i).getCategoryId());
        }
        gradeCategoryService.deleteGradeCategoryByTestId(testId);
        categoryDAO.deleteCategoryById(categoryId);
        testDAO.deleteTest(testId);
        groupDAO.deleteGroup(groupId);
        userDAO.deleteUser(userId);
    }

    @Test(expected = IllegalArgumentException.class)
    public void getGradeCategoryByTestId_idIsNull() throws IllegalArgumentException, ServiceException {
        gradeCategoryService.getGradeCategoryByTestId(null);
    }

    @Test
    public void getGradeCategoryByCategoryId() throws IllegalArgumentException, ServiceException {
        testGradeCategories = createGradeCategoriesForTest();
        List<GradeCategory> gradeCategoriesFromDb = gradeCategoryService.getGradeCategoryByCategoryId(testGradeCategories.get(0).getCategoryId());
        assertEquals(testGradeCategories.size(), gradeCategoriesFromDb.size());
        gradeCategoriesFromDb.sort(new ObjectEavIdComparator<>());
        for (int i = 0; i < testGradeCategories.size(); i++) {
            assertEquals(testGradeCategories.get(i).getId(), gradeCategoriesFromDb.get(i).getId());
            assertEquals(testGradeCategories.get(i).getTestId(), gradeCategoriesFromDb.get(i).getTestId());
            assertEquals(testGradeCategories.get(i).getMinScore(), gradeCategoriesFromDb.get(i).getMinScore());
            assertEquals(testGradeCategories.get(i).getMaxScore(), gradeCategoriesFromDb.get(i).getMaxScore());
            assertEquals(testGradeCategories.get(i).getMeaning(), gradeCategoriesFromDb.get(i).getMeaning());
            assertEquals(testGradeCategories.get(i).getCategoryId(), gradeCategoriesFromDb.get(i).getCategoryId());
        }
        gradeCategoryService.deleteGradeCategoryByTestId(testId);
        categoryDAO.deleteCategoryById(categoryId);
        testDAO.deleteTest(testId);
        groupDAO.deleteGroup(groupId);
        userDAO.deleteUser(userId);
    }

    @Test(expected = IllegalArgumentException.class)
    public void getGradeCategoryByCategoryId_idIsNull() throws IllegalArgumentException, ServiceException {
        gradeCategoryService.getGradeCategoryByCategoryId(null);
    }

    @Test
    public void createGradeCategory() throws IllegalArgumentException, ServiceException {
        testGradeCategory = buildValidGradeCategory();
        testGradeCategory.setId(gradeCategoryService.createGradeCategory(testGradeCategory));
        GradeCategory gradeCategoryFromDb = gradeCategoryService.getCategoryById(testGradeCategory.getId());
        assertEquals(testGradeCategory.getId(), gradeCategoryFromDb.getId());
        assertEquals(testGradeCategory.getMeaning(), gradeCategoryFromDb.getMeaning());
        assertEquals(testGradeCategory.getMaxScore(), gradeCategoryFromDb.getMaxScore());
        assertEquals(testGradeCategory.getMinScore(), gradeCategoryFromDb.getMinScore());
        assertEquals(testGradeCategory.getTestId(), gradeCategoryFromDb.getTestId());
        assertEquals(testGradeCategory.getCategoryId(), gradeCategoryFromDb.getCategoryId());

        gradeCategoryService.deleteGradeCategoryByTestId(testId);
        categoryDAO.deleteCategoryById(categoryId);
        testDAO.deleteTest(testId);
        groupDAO.deleteGroup(groupId);
        userDAO.deleteUser(userId);
    }

    @Test(expected = IllegalArgumentException.class)
    public void createGradeCategory_nullGradeCategory() throws IllegalArgumentException, ServiceException {
        gradeCategoryService.createGradeCategory(null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void createGradeCategory_nullMeaning() throws IllegalArgumentException, ServiceException {
        try {
            gradeCategoryService.createGradeCategory(buildInvalidGradeCategory_meaningIsNull());
        } finally {
            categoryDAO.deleteCategoryById(categoryId);
            testDAO.deleteTest(testId);
            groupDAO.deleteGroup(groupId);
            userDAO.deleteUser(userId);
        }
    }

    @Test(expected = IllegalArgumentException.class)
    public void createGradeCategory_maxScoreLessThenMinScore() throws IllegalArgumentException, ServiceException {
        try {
            gradeCategoryService.createGradeCategory(buildInvalidGradeCategory_maxScoreLessThenMinScore());
        } finally {
            categoryDAO.deleteCategoryById(categoryId);
            testDAO.deleteTest(testId);
            groupDAO.deleteGroup(groupId);
            userDAO.deleteUser(userId);
        }
    }

    @Test(expected = IllegalArgumentException.class)
    public void createGradeCategory_testIdIsNull() throws IllegalArgumentException, ServiceException {
        try {
            gradeCategoryService.createGradeCategory(buildInvalidGradeCategory_testIdIsNull());
        } finally {
            categoryDAO.deleteCategoryById(categoryId);
        }
    }

    @Test(expected = IllegalArgumentException.class)
    public void createGradeCategory_categoryIdIsNull() throws IllegalArgumentException, ServiceException {
        try {
            gradeCategoryService.createGradeCategory(buildInvalidGradeCategory_categoryIdIsNull());
        } finally {
            testDAO.deleteTest(testId);
            groupDAO.deleteGroup(groupId);
            userDAO.deleteUser(userId);
        }
    }

    @Test(expected = IllegalArgumentException.class)
    public void createGradeCategory_minScoreLessThen0() throws IllegalArgumentException, ServiceException {
        try {
            gradeCategoryService.createGradeCategory(buildInvalidGradeCategory_minScoreLessThen0());
        } finally {
            categoryDAO.deleteCategoryById(categoryId);
            testDAO.deleteTest(testId);
            groupDAO.deleteGroup(groupId);
            userDAO.deleteUser(userId);
        }
    }

    @Test(expected = IllegalArgumentException.class)
    public void createGradeCategory_maxScoreLessThen0() throws IllegalArgumentException, ServiceException {
        try {
            gradeCategoryService.createGradeCategory(buildInvalidGradeCategory_maxScoreLessThen0());
        } finally {
            categoryDAO.deleteCategoryById(categoryId);
            testDAO.deleteTest(testId);
            groupDAO.deleteGroup(groupId);
            userDAO.deleteUser(userId);
        }
    }

    @Test(expected = ServiceException.class)
    public void deleteGradeCategoryById() throws ServiceException {
        testGradeCategory = buildValidGradeCategory();
        testGradeCategory.setId(gradeCategoryService.createGradeCategory(testGradeCategory));

        gradeCategoryService.deleteGradeCategoryById(testGradeCategory.getId());
        try {
            gradeCategoryService.getCategoryById(testGradeCategory.getId());
        } finally {
            categoryDAO.deleteCategoryById(categoryId);
            testDAO.deleteTest(testId);
            groupDAO.deleteGroup(groupId);
            userDAO.deleteUser(userId);
        }
    }

    @Test(expected = IllegalArgumentException.class)
    public void deleteGradeCategoryById_idIsNull() throws ServiceException {
        gradeCategoryService.deleteGradeCategoryById(null);
    }

    @Test
    public void deleteGradeCategoryByTestId() throws ServiceException {
        testGradeCategories = createGradeCategoriesForTest();
        gradeCategoryService.deleteGradeCategoryByTestId(testGradeCategories.get(0).getTestId());
        List<GradeCategory> gradeCategoriesFromDb = gradeCategoryService.getGradeCategoryByTestId(testGradeCategories.get(0).getTestId());
        assertTrue(gradeCategoriesFromDb.isEmpty());

        categoryDAO.deleteCategoryById(categoryId);
        testDAO.deleteTest(testId);
        groupDAO.deleteGroup(groupId);
        userDAO.deleteUser(userId);
    }

    @Test(expected = IllegalArgumentException.class)
    public void deleteGradeCategoryByTestId_idIsNull() throws ServiceException {
        gradeCategoryService.deleteGradeCategoryByTestId(null);
    }

    @Test
    public void updateGradeCategory() throws ServiceException {
        testGradeCategory = buildValidGradeCategory();
        testGradeCategory.setId(gradeCategoryService.createGradeCategory(testGradeCategory));
        testGradeCategory.setMeaning("Экстраверт-сангвиник");
        testGradeCategory.setMinScore(3);
        testGradeCategory.setMaxScore(13);
        gradeCategoryService.updateGradeCategory(testGradeCategory);
        GradeCategory testGradeCategoryUpdated = gradeCategoryService.getCategoryById(testGradeCategory.getId());
        assertEquals(testGradeCategory.getId(), testGradeCategoryUpdated.getId());
        assertEquals(testGradeCategory.getMeaning(), testGradeCategoryUpdated.getMeaning());
        assertEquals(testGradeCategory.getMaxScore(), testGradeCategoryUpdated.getMaxScore());
        assertEquals(testGradeCategory.getMinScore(), testGradeCategoryUpdated.getMinScore());
        assertEquals(testGradeCategory.getTestId(), testGradeCategoryUpdated.getTestId());
        assertEquals(testGradeCategory.getCategoryId(), testGradeCategoryUpdated.getCategoryId());

        gradeCategoryService.deleteGradeCategoryByTestId(testId);
        categoryDAO.deleteCategoryById(categoryId);
        testDAO.deleteTest(testId);
        groupDAO.deleteGroup(groupId);
        userDAO.deleteUser(userId);
    }

    @Test(expected = IllegalArgumentException.class)
    public void updateGradeCategory_idIsNull() throws ServiceException {
        GradeCategory updatedCategory = new GradeCategory();
        gradeCategoryService.updateGradeCategory(updatedCategory);
    }

    @Test(expected = IllegalArgumentException.class)
    public void updateGradeCategory_nullGradeCategory() throws IllegalArgumentException, ServiceException {
        gradeCategoryService.updateGradeCategory(null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void updateGradeCategory_nullMeaning() throws IllegalArgumentException, ServiceException {
        try {
            gradeCategoryService.updateGradeCategory(buildInvalidGradeCategory_meaningIsNull());
        } finally {
            categoryDAO.deleteCategoryById(categoryId);
            testDAO.deleteTest(testId);
            groupDAO.deleteGroup(groupId);
            userDAO.deleteUser(userId);
        }
    }

    @Test(expected = IllegalArgumentException.class)
    public void updateGradeCategory_maxScoreLessThenMinScore() throws IllegalArgumentException, ServiceException {
        try {
            gradeCategoryService.updateGradeCategory(buildInvalidGradeCategory_maxScoreLessThenMinScore());
        } finally {
            categoryDAO.deleteCategoryById(categoryId);
            testDAO.deleteTest(testId);
            groupDAO.deleteGroup(groupId);
            userDAO.deleteUser(userId);
        }
    }

    @Test(expected = IllegalArgumentException.class)
    public void updateGradeCategory_testIdIsNull() throws IllegalArgumentException, ServiceException {
        try {
            gradeCategoryService.updateGradeCategory(buildInvalidGradeCategory_testIdIsNull());
        } finally {
            categoryDAO.deleteCategoryById(categoryId);
        }
    }

    @Test(expected = IllegalArgumentException.class)
    public void updateGradeCategory_categoryIdIsNull() throws IllegalArgumentException, ServiceException {
        try {
            gradeCategoryService.updateGradeCategory(buildInvalidGradeCategory_categoryIdIsNull());
        } finally {
            testDAO.deleteTest(testId);
            groupDAO.deleteGroup(groupId);
            userDAO.deleteUser(userId);
        }
    }

    @Test(expected = IllegalArgumentException.class)
    public void updateGradeCategory_minScoreLessThen0() throws IllegalArgumentException, ServiceException {
        try {
            gradeCategoryService.updateGradeCategory(buildInvalidGradeCategory_minScoreLessThen0());
        } finally {
            categoryDAO.deleteCategoryById(categoryId);
            testDAO.deleteTest(testId);
            groupDAO.deleteGroup(groupId);
            userDAO.deleteUser(userId);
        }
    }

    @Test(expected = IllegalArgumentException.class)
    public void updateGradeCategory_maxScoreLessThen0() throws IllegalArgumentException, ServiceException {
        try {
            gradeCategoryService.updateGradeCategory(buildInvalidGradeCategory_maxScoreLessThen0());
        } finally {
            categoryDAO.deleteCategoryById(categoryId);
            testDAO.deleteTest(testId);
            groupDAO.deleteGroup(groupId);
            userDAO.deleteUser(userId);
        }
    }

    private GradeCategory buildValidGradeCategory() throws IllegalArgumentException, ServiceException {
        GradeCategory gradeCategoryExampleForTest = new GradeCategory();
        gradeCategoryExampleForTest.setTestId(createTestForTest());
        gradeCategoryExampleForTest.setMinScore(2);
        gradeCategoryExampleForTest.setMaxScore(22);
        gradeCategoryExampleForTest.setMeaning("Темперамент");
        gradeCategoryExampleForTest.setCategoryId(createCategoryForTest());
        return gradeCategoryExampleForTest;
    }

    private GradeCategory buildInvalidGradeCategory_meaningIsNull() throws IllegalArgumentException, ServiceException {
        GradeCategory gradeCategoryExampleForTest = new GradeCategory();
        gradeCategoryExampleForTest.setTestId(createTestForTest());
        gradeCategoryExampleForTest.setMinScore(2);
        gradeCategoryExampleForTest.setMaxScore(22);
        gradeCategoryExampleForTest.setCategoryId(createCategoryForTest());
        return gradeCategoryExampleForTest;
    }

    private GradeCategory buildInvalidGradeCategory_maxScoreLessThenMinScore() throws IllegalArgumentException, ServiceException {
        GradeCategory gradeCategoryExampleForTest = new GradeCategory();
        gradeCategoryExampleForTest.setTestId(createTestForTest());
        gradeCategoryExampleForTest.setMinScore(22);
        gradeCategoryExampleForTest.setMaxScore(3);
        gradeCategoryExampleForTest.setMeaning("Шизофрения");
        gradeCategoryExampleForTest.setCategoryId(createCategoryForTest());
        return gradeCategoryExampleForTest;
    }

    private GradeCategory buildInvalidGradeCategory_testIdIsNull() throws IllegalArgumentException, ServiceException {
        GradeCategory gradeCategoryExampleForTest = new GradeCategory();
        gradeCategoryExampleForTest.setTestId(null);
        gradeCategoryExampleForTest.setMinScore(2);
        gradeCategoryExampleForTest.setMaxScore(22);
        gradeCategoryExampleForTest.setMeaning("Шизофрения");
        gradeCategoryExampleForTest.setCategoryId(createCategoryForTest());
        return gradeCategoryExampleForTest;
    }

    private GradeCategory buildInvalidGradeCategory_categoryIdIsNull() throws IllegalArgumentException, ServiceException {
        GradeCategory gradeCategoryExampleForTest = new GradeCategory();
        gradeCategoryExampleForTest.setTestId(createTestForTest());
        gradeCategoryExampleForTest.setMinScore(2);
        gradeCategoryExampleForTest.setMaxScore(22);
        gradeCategoryExampleForTest.setMeaning("Шизофрения");
        gradeCategoryExampleForTest.setCategoryId(null);
        return gradeCategoryExampleForTest;
    }

    private GradeCategory buildInvalidGradeCategory_minScoreLessThen0() throws IllegalArgumentException, ServiceException {
        GradeCategory gradeCategoryExampleForTest = new GradeCategory();
        gradeCategoryExampleForTest.setTestId(createTestForTest());
        gradeCategoryExampleForTest.setMinScore(-2);
        gradeCategoryExampleForTest.setMaxScore(3);
        gradeCategoryExampleForTest.setMeaning("Шизофрения");
        gradeCategoryExampleForTest.setCategoryId(createCategoryForTest());
        return gradeCategoryExampleForTest;
    }

    private GradeCategory buildInvalidGradeCategory_maxScoreLessThen0() throws IllegalArgumentException, ServiceException {
        GradeCategory gradeCategoryExampleForTest = new GradeCategory();
        gradeCategoryExampleForTest.setTestId(createTestForTest());
        gradeCategoryExampleForTest.setMinScore(2);
        gradeCategoryExampleForTest.setMaxScore(-3);
        gradeCategoryExampleForTest.setMeaning("Шизофрения");
        gradeCategoryExampleForTest.setCategoryId(createCategoryForTest());
        return gradeCategoryExampleForTest;
    }

    private BigInteger createCategoryForTest() throws IllegalArgumentException, ServiceException {
        Category categoryExampleForTesting = new Category();
        categoryExampleForTesting.setNameCategory("Темперамент ");
        categoryId = categoryDAO.createCategory(categoryExampleForTesting);
        return categoryId;
    }

    private BigInteger createTestForTest() throws IllegalArgumentException, ServiceException {
        userId = userDAO.createUser("Karina", "Marinina",
            "marinina.@gmail", "1111", "12345");
        Group group = new Group();
        group.setCreatorUserId(userId);
        group.setLink("New Link http...");
        group.setName("Very cool group");
        groupId = groupDAO.createGroup(group);

        com.netcracker.testerritto.models.Test testForGradeCategory = new com.netcracker.testerritto.models.Test();
        testForGradeCategory.setNameTest("Тест на личность");
        testForGradeCategory.setCreatorUserId(userId);
        testForGradeCategory.setGroupId(groupId);
        testId = testDAO.createTest(testForGradeCategory);
        return testId;
    }

    private List<GradeCategory> createGradeCategoriesForTest() throws IllegalArgumentException, ServiceException {
        GradeCategory gradeCategory1 = new GradeCategory();
        GradeCategory gradeCategory2 = new GradeCategory();
        GradeCategory gradeCategory3 = new GradeCategory();
        GradeCategory gradeCategory4 = new GradeCategory();
        categoryId = createCategoryForTest();
        testId = createTestForTest();
        List<GradeCategory> gradeCategories = new ArrayList<>();

        gradeCategory1.setTestId(testId);
        gradeCategory1.setMinScore(2);
        gradeCategory1.setMaxScore(4);
        gradeCategory1.setMeaning("Интроверт");
        gradeCategory1.setCategoryId(categoryId);
        gradeCategory1.setId(gradeCategoryService.createGradeCategory(gradeCategory1));

        gradeCategory2.setTestId(testId);
        gradeCategory2.setMinScore(5);
        gradeCategory2.setMaxScore(7);
        gradeCategory2.setMeaning("Экстраверт");
        gradeCategory2.setCategoryId(categoryId);
        gradeCategory2.setId(gradeCategoryService.createGradeCategory(gradeCategory2));

        gradeCategory3.setTestId(testId);
        gradeCategory3.setMinScore(8);
        gradeCategory3.setMaxScore(10);
        gradeCategory3.setMeaning("Сангвиник");
        gradeCategory3.setCategoryId(categoryId);
        gradeCategory3.setId(gradeCategoryService.createGradeCategory(gradeCategory3));

        gradeCategory4.setTestId(testId);
        gradeCategory4.setMinScore(11);
        gradeCategory4.setMaxScore(13);
        gradeCategory4.setMeaning("Флегматик");
        gradeCategory4.setCategoryId(categoryId);
        gradeCategory4.setId(gradeCategoryService.createGradeCategory(gradeCategory4));

        gradeCategories.add(gradeCategory1);
        gradeCategories.add(gradeCategory4);
        gradeCategories.add(gradeCategory2);
        gradeCategories.add(gradeCategory3);

        gradeCategories.sort(new ObjectEavIdComparator<>());
        return gradeCategories;
    }
}