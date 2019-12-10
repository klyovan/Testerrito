package com.netcracker.testerritto.dao;

import com.netcracker.testerritto.ApplicationConfiguration;
import com.netcracker.testerritto.models.Group;
import com.netcracker.testerritto.models.User;
import java.math.BigInteger;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

@SpringBootTest(classes = ApplicationConfiguration.class)
@RunWith(SpringRunner.class)
public class UserDAOTest {

  private User user1;

  @Autowired
  private UserDAO userDAO;

  @Before
  public void init() {
   BigInteger id = userDAO.createUser("Allina", "Verde",
        "verde.@gmail", "1111", "12345");
    user1 = userDAO.getUser(id);

  }

  @Test
  public void creatUserAndGetUserTest() {
    User user2 = userDAO.getUser(user1.getId());
    assertNotNull(user1);

    assertTrue(user1.equals(user2));

  }

  @Test(expected = EmptyResultDataAccessException.class)
  public void deleteUserTest() {
    userDAO.deleteUser(user1.getId());
    User user3 = userDAO.getUser(user1.getId());
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

    userDAO.updateLastName(user1.getId(), userNewFildes.get(1));
    testUser = userDAO.getUser(user1.getId());
    assertTrue(testUser.getLastName().equals(userNewFildes.get(1)));

    userDAO.updateFirstName(user1.getId(), userNewFildes.get(0));
    testUser = userDAO.getUser(user1.getId());
    assertTrue(testUser.getFirstName().equals(userNewFildes.get(0)));

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
  public void getCreatedGroupAndDeleteCreatedGroupTest() {
    BigInteger id = BigInteger.valueOf(2);
    User user1 = userDAO.getUser(id);
    int createdGroupCount = 0;

    List<Group> groupsList = new ArrayList<>();
    user1.setGroups(userDAO.getCreatedGroups(user1.getId()));
    groupsList = user1.getGroups();
    createdGroupCount = groupsList.size();

    if (groupsList.size() != 0) {
      userDAO.deleteCreatedGroup(user1.getId(), groupsList.get(0).getId());
      user1.setGroups(userDAO.getCreatedGroups(user1.getId()));
      int createdGroupsAfterDelete = user1.getGroups().size();
      assertTrue(user1.getGroups().size() + 1 == createdGroupCount);
    }
  }

  @Test
  public void enterInGroupAndExitFromGroupTest() {

    int groupsCountBeforeDelete = 0;
    int groupsCountAfterDelte = 0;
    int groupsCountAfterEnter = 0;
    BigInteger tempGroupId;
    user1.setGroups(userDAO.getGroups(user1.getId()));
    List<Group> groupsList = user1.getGroups();
    groupsCountBeforeDelete = groupsList.size();

    if (groupsCountBeforeDelete > 0) {
      tempGroupId = groupsList.get(0).getId();

      userDAO.exitFromGroup(user1.getId(), groupsList.get(0).getId());
      user1.setGroups(userDAO.getGroups(user1.getId()));
      groupsCountAfterDelte = user1.getGroups().size();

      userDAO.enterInGroup(user1.getId(), tempGroupId);
      user1.setGroups(userDAO.getGroups(user1.getId()));
      groupsCountAfterEnter = user1.getGroups().size();

      assertTrue(groupsCountBeforeDelete == groupsCountAfterDelte + 1);
      assertTrue(groupsCountAfterEnter == groupsCountBeforeDelete);
    }
  }
  /*
    @Test
    public void getUserByEmailTest() {
    User user2 = userDAO.getUserByEmail(user1.getEmail());
    assertTrue(user2.equals(user1));

    }
*/

  @After
  public void tearDown() throws Exception {
    userDAO.deleteUser(user1.getId());
  }
}