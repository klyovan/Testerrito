package com.netcracker.testerritto.controllers;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.netcracker.testerritto.ApplicationConfiguration;
import com.netcracker.testerritto.comparators.ObjectEavIdComparator;
import com.netcracker.testerritto.dao.TestDAO;
import com.netcracker.testerritto.dao.UserDAO;
import com.netcracker.testerritto.models.Category;
import com.netcracker.testerritto.models.GradeCategory;
import com.netcracker.testerritto.models.Group;
import com.netcracker.testerritto.models.User;
import com.netcracker.testerritto.services.GradeCategoryService;
import com.netcracker.testerritto.services.GroupService;
import org.junit.After;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(classes = ApplicationConfiguration.class)
@RunWith(SpringRunner.class)
@AutoConfigureMockMvc
public class TestControllerTest {
    @Autowired
    private MockMvc mvc;
    @Autowired
    private UserDAO userDAO;
    @Autowired
    private GroupService groupService;
    @Autowired
    private TestDAO testDAO;
    @Autowired
    private GradeCategoryService gradeCategoryService;
    @Autowired

    private ObjectMapper objectMapper;
    @Autowired
    private TestController testController;

    private BigInteger userId;
    private User userFromDb;
    private BigInteger groupId;
    private BigInteger categoryId;
    private BigInteger testId;


    @Before
    public void setUp() throws Exception {
        userId = userDAO.createUser("Allina", "Verde",
            "verde.@gmail", "1111", "12345");
        userFromDb = userDAO.getUser(userId);
    }



    @Test
    public void getCategoryById() throws Exception {
        String token = obtainAccessToken();
        Category categoryExpected = buildValidCategory();
        BigInteger categoryId = testController.createCategory(categoryExpected);
        categoryExpected.setId(categoryId);

        try {
            mvc.perform(MockMvcRequestBuilders.get("/test/category/{id}", categoryId)
            .header("Authorization", "Bearer " + token))
            .andExpect(MockMvcResultMatchers.jsonPath("$.id").value(categoryExpected.getId()))
            .andExpect(MockMvcResultMatchers.jsonPath("$.nameCategory").value(categoryExpected.getNameCategory()))
            .andExpect(status().is2xxSuccessful());
        } finally {
            testController.deleteCategory(categoryId);
        }
    }

    @Test
    public void deleteCategory() throws Exception {
        String token = obtainAccessToken();
        Category categoryExpected = buildValidCategory();
        BigInteger categoryId = testController.createCategory(categoryExpected);

        mvc.perform(MockMvcRequestBuilders.delete("/test/category/{id}", categoryId)
            .header("Authorization", "Bearer " + token))
            .andExpect(status().is2xxSuccessful());
        mvc.perform(MockMvcRequestBuilders.get("/test/category/{id}", categoryId)
            .header("Authorization", "Bearer " + token))
            .andExpect(status().is4xxClientError());
    }

    @Test
    public void updateCategory() throws Exception {
        String token = obtainAccessToken();
        Category categoryExpected = buildValidCategory();
        BigInteger categoryId = testController.createCategory(categoryExpected);
        categoryExpected.setId(categoryId);
        categoryExpected.setNameCategory("Тип личности при стрессовых ситуациях");
        String body =
            "{\"id\":\"" + categoryExpected.getId() +
            "\", \"nameCategory\":\"" + categoryExpected.getNameCategory() + "\"}";

        try {
            mvc.perform(MockMvcRequestBuilders.put("/test/category")
                .header("Authorization", "Bearer " + token)
                .contentType(MediaType.APPLICATION_JSON)
                .content(body))
                .andExpect(status().is2xxSuccessful());

            mvc.perform(MockMvcRequestBuilders.get("/test/category/{id}", categoryExpected.getId())
                .header("Authorization", "Bearer " + token))
                .andExpect(MockMvcResultMatchers.jsonPath("$.id").value(categoryExpected.getId()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.nameCategory").value(categoryExpected.getNameCategory()))
                .andExpect(status().is2xxSuccessful());
        } finally {
            mvc.perform(MockMvcRequestBuilders.delete("/test/category/{id}", categoryExpected.getId())
                .header("Authorization", "Bearer " + token));
        }
    }

    @Test
    public void updateCategory_idNull() throws Exception {
        String token = obtainAccessToken();
        String body =
            "{\"id\":\"" + null +
            "\", \"nameCategory\":\"" + "name updated" + "\"}";
        mvc.perform(MockMvcRequestBuilders.put("/test/category")
            .header("Authorization", "Bearer " + token)
            .content(body))
            .andExpect(status().is4xxClientError());
    }

    @Ignore
    @Test
    public void createCategory() throws Exception {
        String token = obtainAccessToken();
        Category newCategory = buildValidCategory();
        String body =
            "{\"nameCategory\":\"" + newCategory.getNameCategory() + "\"}";

        try {
            MvcResult result = mvc.perform(MockMvcRequestBuilders.post("/test/category")
                .header("Authorization", "Bearer " + token)
                .contentType(MediaType.APPLICATION_JSON)
                .content(body))
                .andExpect(status().is2xxSuccessful())
                .andReturn();

            String response = result.getResponse().getContentAsString();
            newCategory.setId(new BigInteger(response));

            mvc.perform(MockMvcRequestBuilders.get("/test/category/{id}", newCategory.getId())
                .header("Authorization", "Bearer " + token))
                .andExpect(MockMvcResultMatchers.jsonPath("$.id").value(newCategory.getId()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.nameCategory").value(newCategory.getNameCategory()))
                .andExpect(status().is2xxSuccessful());
        } finally {
            mvc.perform(MockMvcRequestBuilders.delete("/test/category/{id}", newCategory.getId())
                .header("Authorization", "Bearer " + token));
        }
    }

    @Test
    public void getGradeCategoryById() throws Exception {
        String token = obtainAccessToken();
        GradeCategory gradeCategoryExpected = buildValidGradeCategory();
        BigInteger gradeId = gradeCategoryService.createGradeCategory(gradeCategoryExpected);
        gradeCategoryExpected.setId(gradeId);

        try {
            mvc.perform(MockMvcRequestBuilders.get("/test/category/grade/{id}", gradeId)
                .header("Authorization", "Bearer " + token))
                .andExpect(MockMvcResultMatchers.jsonPath("$.id").value(gradeCategoryExpected.getId()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.minScore").value(gradeCategoryExpected.getMinScore()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.maxScore").value(gradeCategoryExpected.getMaxScore()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.meaning").value(gradeCategoryExpected.getMeaning()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.categoryId").value(gradeCategoryExpected.getCategoryId()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.testId").value(gradeCategoryExpected.getTestId()))
                .andExpect(status().is2xxSuccessful());
        } finally {
            gradeCategoryService.deleteGradeCategoryById(gradeId);
            testController.deleteCategory(categoryId);
            testDAO.deleteTest(testId);
            groupService.deleteGroup(groupId);
            userDAO.deleteUser(userId);
        }
    }

    @Test
    public void getGradeCategoryByTestId() throws Exception {
        try {
            String token = obtainAccessToken();
            List<GradeCategory> gradeCategoriesExpected = createGradeCategoriesForTest();

            MvcResult result = mvc.perform(MockMvcRequestBuilders.get("/test/category/grade/test/{id}", gradeCategoriesExpected.get(0).getTestId())
                .header("Authorization", "Bearer " + token)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().is2xxSuccessful())
                .andReturn();

            String contentAsString = result.getResponse().getContentAsString();
            List<GradeCategory> gradeCategoriesFromDb = objectMapper.readValue(contentAsString, new TypeReference<List<GradeCategory>>() {
            });
            gradeCategoriesFromDb.sort(new ObjectEavIdComparator<>());

            assertEquals(gradeCategoriesExpected.size(), gradeCategoriesFromDb.size());
            for (int i = 0; i < gradeCategoriesExpected.size(); i++) {
                assertEquals(gradeCategoriesExpected.get(i).getId(), gradeCategoriesFromDb.get(i).getId());
                assertEquals(gradeCategoriesExpected.get(i).getTestId(), gradeCategoriesFromDb.get(i).getTestId());
                assertEquals(gradeCategoriesExpected.get(i).getMinScore(), gradeCategoriesFromDb.get(i).getMinScore());
                assertEquals(gradeCategoriesExpected.get(i).getMaxScore(), gradeCategoriesFromDb.get(i).getMaxScore());
                assertEquals(gradeCategoriesExpected.get(i).getMeaning(), gradeCategoriesFromDb.get(i).getMeaning());
                assertEquals(gradeCategoriesExpected.get(i).getCategoryId(), gradeCategoriesFromDb.get(i).getCategoryId());
            }
        } finally {
            testController.deleteGradeCategoryByTestId(testId);
            testController.deleteCategory(categoryId);
            testDAO.deleteTest(testId);
            groupService.deleteGroup(groupId);
            userDAO.deleteUser(userId);
        }
    }

    @Test
    public void getGradeCategoryByCategoryId() throws Exception {
        try {
            String token = obtainAccessToken();
            List<GradeCategory> gradeCategoriesExpected = createGradeCategoriesForTest();

            MvcResult result = mvc.perform(MockMvcRequestBuilders.get("/test/category/grade/category/{id}", gradeCategoriesExpected.get(0).getCategoryId())
                .header("Authorization", "Bearer " + token)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().is2xxSuccessful())
                .andReturn();

            String contentAsString = result.getResponse().getContentAsString();
            List<GradeCategory> gradeCategoriesFromDb = objectMapper.readValue(contentAsString, new TypeReference<List<GradeCategory>>() {});
            gradeCategoriesFromDb.sort(new ObjectEavIdComparator<>());

            assertEquals(gradeCategoriesExpected.size(), gradeCategoriesFromDb.size());
            for (int i = 0; i < gradeCategoriesExpected.size(); i++) {
                assertEquals(gradeCategoriesExpected.get(i).getId(), gradeCategoriesFromDb.get(i).getId());
                assertEquals(gradeCategoriesExpected.get(i).getTestId(), gradeCategoriesFromDb.get(i).getTestId());
                assertEquals(gradeCategoriesExpected.get(i).getMinScore(), gradeCategoriesFromDb.get(i).getMinScore());
                assertEquals(gradeCategoriesExpected.get(i).getMaxScore(), gradeCategoriesFromDb.get(i).getMaxScore());
                assertEquals(gradeCategoriesExpected.get(i).getMeaning(), gradeCategoriesFromDb.get(i).getMeaning());
                assertEquals(gradeCategoriesExpected.get(i).getCategoryId(), gradeCategoriesFromDb.get(i).getCategoryId());
            }
        } finally {
            testController.deleteGradeCategoryByTestId(testId);
            testController.deleteCategory(categoryId);
            testDAO.deleteTest(testId);
            groupService.deleteGroup(groupId);
            userDAO.deleteUser(userId);
        }
    }
    @Ignore
    @Test
    public void createGradeCategory() throws Exception {
        String token = obtainAccessToken();
        GradeCategory newGradeCategory = buildValidGradeCategory();
        String body =
            "{\"minScore\":\"" + newGradeCategory.getMinScore() +
            "\",\"maxScore\":\"" + newGradeCategory.getMaxScore() +
            "\",\"meaning\":\"" + newGradeCategory.getMeaning() +
            "\",\"testId\":\"" + newGradeCategory.getTestId() +
            "\",\"categoryId\":\"" + newGradeCategory.getCategoryId() +
            "\"}";

        try {
            MvcResult result = mvc.perform(MockMvcRequestBuilders.post("/test/category/grade")
                .header("Authorization", "Bearer " + token)
                .contentType(MediaType.APPLICATION_JSON)
                .content(body))
                .andExpect(status().is2xxSuccessful())
                .andReturn();

            String response = result.getResponse().getContentAsString();
            newGradeCategory.setId(new BigInteger(response));

            mvc.perform(MockMvcRequestBuilders.get("/test/category/grade/{id}", newGradeCategory.getId())
                .header("Authorization", "Bearer " + token))
                .andExpect(MockMvcResultMatchers.jsonPath("$.id").value(newGradeCategory.getId()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.minScore").value(newGradeCategory.getMinScore()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.maxScore").value(newGradeCategory.getMaxScore()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.meaning").value(newGradeCategory.getMeaning()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.testId").value(newGradeCategory.getTestId()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.categoryId").value(newGradeCategory.getCategoryId()))
                .andExpect(status().is2xxSuccessful());
        } finally {
            mvc.perform(MockMvcRequestBuilders.delete("/test/category/grade/{id}", newGradeCategory.getId())
                .header("Authorization", "Bearer " + token));
            testController.deleteCategory(categoryId);
            testDAO.deleteTest(testId);
            groupService.deleteGroup(groupId);
            userDAO.deleteUser(userId);
        }
    }

    @Test
    public void updateGradeCategory() throws Exception {
        String token = obtainAccessToken();
        GradeCategory newGradeCategory = buildValidGradeCategory();

        try {
            newGradeCategory.setId(testController.createGradeCategory(newGradeCategory));
            String body =
                "{\"minScore\":\"" + newGradeCategory.getMinScore() +
                "\",\"maxScore\":\"" + newGradeCategory.getMaxScore() +
                "\",\"meaning\":\"" + newGradeCategory.getMeaning() +
                "\",\"testId\":\"" + newGradeCategory.getTestId() +
                "\",\"categoryId\":\"" + newGradeCategory.getCategoryId() +
                "\",\"id\":\"" + newGradeCategory.getId() +
                "\"}";

            mvc.perform(MockMvcRequestBuilders.put("/test/category/grade")
                .header("Authorization", "Bearer " + token)
                .contentType(MediaType.APPLICATION_JSON)
                .content(body))
                .andExpect(status().is2xxSuccessful());

            mvc.perform(MockMvcRequestBuilders.get("/test/category/grade/{id}", newGradeCategory.getId())
                .header("Authorization", "Bearer " + token))
                .andExpect(MockMvcResultMatchers.jsonPath("$.id").value(newGradeCategory.getId()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.minScore").value(newGradeCategory.getMinScore()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.maxScore").value(newGradeCategory.getMaxScore()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.meaning").value(newGradeCategory.getMeaning()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.testId").value(newGradeCategory.getTestId()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.categoryId").value(newGradeCategory.getCategoryId()))
                .andExpect(status().is2xxSuccessful());
        } finally {
            mvc.perform(MockMvcRequestBuilders.delete("/test/category/grade/{id}", newGradeCategory.getId())
                .header("Authorization", "Bearer " + token));
            testController.deleteGradeCategoryById(categoryId);
            testDAO.deleteTest(testId);
            groupService.deleteGroup(groupId);
            userDAO.deleteUser(userId);
        }
    }

    @Test
    public void deleteGradeCategoryById() throws Exception {
        String token = obtainAccessToken();
        GradeCategory gradeCategoryTest = buildValidGradeCategory();
        try {
            gradeCategoryTest.setId(testController.createGradeCategory(gradeCategoryTest));

            mvc.perform(MockMvcRequestBuilders.delete("/test/category/grade/{id}", gradeCategoryTest.getId())
                .header("Authorization", "Bearer " + token)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().is2xxSuccessful());
            mvc.perform(MockMvcRequestBuilders.get("/test/category/grade/{id}", gradeCategoryTest.getId())
                .header("Authorization", "Bearer " + token))
                .andExpect(status().is4xxClientError());
        } finally {
            testController.deleteCategory(categoryId);
            testDAO.deleteTest(testId);
            groupService.deleteGroup(groupId);
            userDAO.deleteUser(userId);
        }
    }

    @Test
    public void deleteGradeCategoryByTestId() throws Exception {
        try {
            String token = obtainAccessToken();
            List<GradeCategory> gradeCategoriesTest = createGradeCategoriesForTest();
            mvc.perform(MockMvcRequestBuilders.delete("/test/category/grade/test/{id}", gradeCategoriesTest.get(0).getTestId())
                .header("Authorization", "Bearer " + token)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().is2xxSuccessful());

            MvcResult result = mvc.perform(MockMvcRequestBuilders.get("/test/category/grade/test/{id}", gradeCategoriesTest.get(0).getTestId())
                .header("Authorization", "Bearer " + token)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().is2xxSuccessful())
                .andReturn();

            String contentAsString = result.getResponse().getContentAsString();
            List<GradeCategory> gradeCategoriesFromDb = objectMapper.readValue(contentAsString, new TypeReference<List<GradeCategory>>(){});
            assertEquals(0, gradeCategoriesFromDb.size());
        } finally {
            testController.deleteCategory(categoryId);
            testDAO.deleteTest(testId);
            groupService.deleteGroup(groupId);
            userDAO.deleteUser(userId);
        }
    }

    @After
    public void tearDown() throws Exception {
        userDAO.deleteUser(userId);
    }

    public String obtainAccessToken() throws Exception {
        String userEmail = userFromDb.getEmail();
        String password = userFromDb.getPassword();

        String body = "{\"userEmail\":\"" + userEmail + "\", \"password\":\""
            + password + "\"}";

        MvcResult result = mvc.perform(MockMvcRequestBuilders.post("/login")
            .contentType(MediaType.APPLICATION_JSON)
            .content(body))
            .andExpect(status().isOk())
            .andReturn();

        String response = result.getResponse().getHeader("Authorization");
        response = response.replace("{\"access_token\": \"", "");
        String token = response.replace("\"}", "");
        return token;
    }

    private Category buildValidCategory() {
        Category category = new Category();
        category.setNameCategory("Тип личности");
        return category;
    }

    private GradeCategory buildValidGradeCategory() {
        GradeCategory gradeCategoryExampleForTest = new GradeCategory();
        gradeCategoryExampleForTest.setTestId(createTestForTest());
        gradeCategoryExampleForTest.setMinScore(2);
        gradeCategoryExampleForTest.setMaxScore(22);
        gradeCategoryExampleForTest.setMeaning("Темперамент");
        Category categoryExpected = buildValidCategory();
        categoryId = testController.createCategory(categoryExpected);
        gradeCategoryExampleForTest.setCategoryId(categoryId);
        return gradeCategoryExampleForTest;
    }

    private BigInteger createTestForTest() {
        Group group = new Group();
        group.setCreatorUserId(userId);
        group.setLink("New Link http...");
        group.setName("Very cool group");
        groupId = groupService.createGroup(group);

        com.netcracker.testerritto.models.Test testForGradeCategory = new com.netcracker.testerritto.models.Test();
        testForGradeCategory.setNameTest("Тест на личность");
        testForGradeCategory.setCreatorUserId(userId);
        testForGradeCategory.setGroupId(groupId);
        testId = testDAO.createTest(testForGradeCategory);
        return testId;
    }

    private List<GradeCategory> createGradeCategoriesForTest() {
        GradeCategory gradeCategory1 = new GradeCategory();
        GradeCategory gradeCategory2 = new GradeCategory();
        GradeCategory gradeCategory3 = new GradeCategory();
        GradeCategory gradeCategory4 = new GradeCategory();
        Category categoryExpected = buildValidCategory();
        categoryId = testController.createCategory(categoryExpected);
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