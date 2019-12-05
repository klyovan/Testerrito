package com.netcracker.testerritto.dao;

import com.netcracker.testerritto.ApplicationConfiguration;
import com.netcracker.testerritto.DataSourceConfig;
import com.netcracker.testerritto.models.Group;
import com.netcracker.testerritto.models.User;
import org.junit.After;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.context.web.WebAppConfiguration;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

@SpringBootTest(classes = ApplicationConfiguration.class)
@RunWith(SpringRunner.class)
public class UserDAOTest {

  private User user1;
  private User userTest;

  @Autowired
  private UserDAO userDAO;

  @Before
  public void init() {
    user1 = userDAO.createUser("Allina", "Verde",
        "@gmail", "1111", "12345");
  }

  @Test
  public void creatUserAndGetUserTest() {
    User user2 = userDAO.getUser(user1.getId());

    assertNotNull(user1);
    assertTrue(user1.equals(user2));

  }

  @Test
  public void deleteUserTest() {
    userDAO.deleteUser(user1.getId());
    User user3 = userDAO.getUser(user1.getId());
    assertTrue(user3 == null);
  }

  @Test
  public void updateUserFieldsTest() {
    User testUser;
    ArrayList<String> userNewFildes = new ArrayList();

    userNewFildes.add("Dina");
    userNewFildes.add("Ford");
    userNewFildes.add("@yourFord");
    userNewFildes.add("2222");
    userNewFildes.add("7654321");

    userDAO.updateLast_name(user1.getId(), userNewFildes.get(1));
    testUser = userDAO.getUser(user1.getId());
    assertTrue(testUser.getLast_name().equals(userNewFildes.get(1)));

    userDAO.updateFirst_name(user1.getId(), userNewFildes.get(0));
    testUser = userDAO.getUser(user1.getId());
    assertTrue(testUser.getFirst_name().equals(userNewFildes.get(0)));

    userDAO.updateEmail(user1.getId(), userNewFildes.get(2));
    testUser = userDAO.getUser(user1.getId());
    assertTrue(testUser.getEmail().equals(userNewFildes.get(2)));

    userDAO.updatePassword(user1.getId(), userNewFildes.get(3));
    testUser = userDAO.getUser(user1.getId());
    assertTrue(testUser.getPassword().equals(userNewFildes.get(3)));

    userDAO.updatePhone(user1.getId(), userNewFildes.get(4));
    testUser = userDAO.getUser(user1.getId());
    assertTrue(testUser.getPhone().equals(userNewFildes.get(4)));

  }

  @Test
  public void CreatedGroupAndDeleteGroupTest() {
    int createdGroupCount = 0;

    List<Group> groups = new ArrayList<>();
    user1.setGroups(userDAO.getCreatedGroups(user1.getId()));
    groups = user1.getGroups();
    createdGroupCount = groups.size();

    if (groups.size() != 0) {
      userDAO.deleteCreatedGroup(user1.getId(), groups.get(0).getId());
      user1.setGroups(userDAO.getCreatedGroups(user1.getId()));
      assertTrue(user1.getGroups().size() + 1 == createdGroupCount);
    }

  }

  @After
  public void tearDown() throws Exception {
    userDAO.deleteUser(user1.getId());
  }
}