package com.netcracker.testerritto.dao;

import com.netcracker.testerritto.models.Group;
import com.netcracker.testerritto.models.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

@Repository
@Transactional
public class UserDAO {
  @Autowired
  private PasswordEncoder passwordEncoder;

  @Autowired
  private JdbcTemplate jdbcTemplate;

  //there is not object whit id on db
  public User getUser(int user_id)  {

    String sql = "SELECT user_last_name.value, user_firs_name.value, user_email.value,\n" +
        "            user_password.value, user_phone.value\n" +
        "FROM   attributes user_last_name, attributes user_firs_name, attributes user_email,\n" +
        "       attributes user_password, attributes user_phone , objects users\n" +
        "WHERE  users.object_id = " + user_id + "                                    /*USER_ID*/   \n" +
        "       and user_last_name.attr_id = 1                                       /*LAST_NAME*/                                             \n" +
        "       and user_last_name.object_id = users.object_id                                     \n" +
        "       and user_firs_name.attr_id = 2                                       /*FIRST_NAME*/\n" +
        "       and user_firs_name.object_id = users.object_id                                     \n" +
        "       and user_email.attr_id =3                                            /*EMAIL*/     \n" +
        "       and user_email.object_id = users.object_id                                         \n" +
        "       and user_password.attr_id = 4                                        /*PASSWORD*/  \n" +
        "       and user_password.object_id = users.object_id                                      \n" +
        "       and user_phone.attr_id = 5                                           /*PHONE*/     \n" +
        "       and user_phone.object_id = users.object_id ";

    return jdbcTemplate.query(sql, new ResultSetExtractor<User>() {
      @Override
      public User extractData (ResultSet resultSet) throws SQLException, DataAccessException {

        if (resultSet.next()) {
          User user = new User();
          user.setLast_name(resultSet.getString(1));
          user.setFirst_name(resultSet.getString(2));
          user.setEmail(resultSet.getString(3));
          user.setPassword(resultSet.getString(4));
          user.setPhone(resultSet.getString(5));
          user.setId(user_id);
          return user;
        }

        return null;
      }
    });
  }



  public void deleteUser(int user_id) {
    String sql = "DELETE FROM objects WHERE object_id = ?";
    try {
      jdbcTemplate.update(sql, user_id);
    }catch (DataAccessException ex){}
  }


  public User createUser(String first_name, String last_name, String email,
      String password, String phone) {
    int sequenceCount = getObjectSequenceCount();

    String object_name = "'" + first_name + " " + last_name + "')\n";

    String sql = "INSERT ALL       \n" +
        "    INTO objects(object_id, parent_id, object_type_id, name)\n" +
        "        VALUES (" + sequenceCount + ", null, 1, " + object_name +
        "    INTO attributes(object_id, attr_id, value)\n" +
        "        VALUES (" + sequenceCount + ", 1, ?)\n" +
        "    INTO attributes(object_id, attr_id, value)\n" +
        "        VALUES (" + sequenceCount + ", 2, ?)\n" +
        "    INTO attributes(object_id, attr_id, value)\n" +
        "        VALUES (" + sequenceCount + ", 3, ?)\n" +
        "    INTO attributes(object_id, attr_id, value)\n" +
        "        VALUES (" + sequenceCount + ", 4, ?)\n" +
        "    INTO attributes(object_id, attr_id, value)\n" +
        "        VALUES (" + sequenceCount + ", 5, ?)\n" +
        "SELECT * FROM DUAL";
    jdbcTemplate.update(sql, first_name, last_name, email,
        password, phone);

    User user = new User(sequenceCount, first_name, last_name,
        email, password, phone);
    return user;
  }


  public void updateLast_name(int user_id, String last_name) {
    String sql = "update attributes set value = ? \n" +
        "where object_id=? and attr_id =1";
    jdbcTemplate.update(sql, last_name, user_id);

  }

  public void updateFirst_name(int user_id, String first_name) {
    String sql = "UPDATE attributes SET value = ? \n" +
        "WHERE object_id=? and attr_id =2";
    jdbcTemplate.update(sql, first_name, user_id);

  }

  public void updateEmail(int user_id, String email) {
    String sql = "UPDATE attributes SET value = ? \n" +
        "WHERE object_id=? and attr_id =3";
    jdbcTemplate.update(sql, email, user_id);

  }

  public void updatePassword(int user_id, String password) {
    String sql = "UPDATE attributes SET value = ? \n" +
        "WHERE object_id=? and attr_id =4";
    jdbcTemplate.update(sql, password, user_id);

  }

  public void updatePhone(int user_id, String phone) {
    String sql = "UPDATE attributes SET value = ? \n" +
        "WHERE object_id=? and attr_id =5";
    jdbcTemplate.update(sql, phone, user_id);

  }

  public List<Group> getGroups(int user_id) {

    String sql = "SELECT groups.object_id, groups_name.value, groups_link.value\n" +
        "FROM  objects groups, attributes groups_name, attributes groups_link,\n" +
        "      objreference USER_2_GROUPS                                                     \n" +
        "WHERE groups.object_type_id = 2                                                       \n" +
        "      and groups_name.attr_id = 6                                       /*NAME_GROUP*/\n" +
        "      and groups_name.object_id = groups.object_id                                    \n" +
        "      and groups_link.attr_id = 7                                       /*LINK*/      \n" +
        "      and groups_link.object_id = groups.object_id                                    \n" +
        "      and user_2_groups.attr_id = 22                                    /*CONSIST*/   \n" +
        "      and user_2_groups.object_id = " + user_id + "                     /*USER_ID*/   \n" +
        "      and user_2_groups.reference = groups.object_id";

    List<Group> listGroups = jdbcTemplate.query(sql, new RowMapper<Group>() {

      public Group mapRow(ResultSet rs, int rowNum) throws SQLException {
        Group group = new Group();
        group.setGroup_id(rs.getInt(1));
        group.setName(rs.getString(2));
        group.setLink(rs.getString(3));
        return group;
      }
    });
    return listGroups;
  }

  public List<Group> getCreatedGroups(int user_id) {
    String sql = "SELECT groups_name.object_id, groups_name.value, groups_link.value\n" +
        "FROM   objects users, objects groups, attributes groups_link,\n" +
        "       attributes groups_name, objreference groups_2_user\n" +
        "WHERE  users.object_id = " + user_id + "\n" +
        "       and groups.object_type_id = 2\n" +
        "       and groups_name.object_id = GROUPS.object_id\n" +
        "       and groups_name.attr_id = 6                                  /*groupName*/\n" +
        "       and groups_link.object_id = GROUPS.object_id\n" +
        "       and groups_link.attr_id = 7                                  /*groupLink*/\n" +
        "       and groups_2_user.attr_id = 25                               /*CREATE_GROUP_BY*/\n" +
        "       and groups_2_user.object_id = GROUPS.object_id\n" +
        "       and groups_2_user.reference = USERS.object_id ";

    List<Group> listGroups = jdbcTemplate.query(sql, new RowMapper<Group>() {

      public Group mapRow(ResultSet rs, int rowNum) throws SQLException {
        Group group = new Group();
        group.setGroup_id(rs.getInt(1));
        group.setName(rs.getString(2));
        group.setLink(rs.getString(3));
        return group;
      }
    });
    return listGroups;
  }

  public void deleteCreatedGroup(int user_id, int createdGroup_id) {
    String sql = "DELETE FROM objects groups              \n" +
        "WHERE groups.object_id =                         \n" +
        "(                                                \n" +
        "      SELECT object_id                           \n" +
        "      FROM objreference groups_2_creator         \n" +
        "      WHERE groups_2_creator.attr_id = 25        \n" +
        "            and groups_2_creator.reference = ?   \n" +                  /* User_id */
        "            and groups_2_creator.object_id = ?   \n" +                  /* group_ id */
        ")";
    jdbcTemplate.update(sql, user_id, createdGroup_id);
  }


  public void enterInGroup(int user_id, int group_id) {
    String sql = "INSERT INTO objreference (attr_id, object_id ,reference) VALUES (22, ? , ?)";
    jdbcTemplate.update(sql, user_id, group_id);
  }

  public void exitFromGroup(int user_id, int group_id) {
    String sql = "DELETE FROM objreference where attr_id = 22 and object_id = ? and reference = ? ";
    jdbcTemplate.update(sql, user_id, group_id);
  }

  private Integer getObjectSequenceCount() {
    String sql = " SELECT object_id_pr.NEXTVAL from dual";

    int result = jdbcTemplate.queryForObject(
        "SELECT object_id_pr.NEXTVAL from dual", Integer.class);
    return result;
  }

  public User getUserByEmail(String s) {
    String email = "'" + s + "'\n";
    String sql = "SELECT user_last_name.value, user_first_name.value,\n"
        + "       user_password.value, user_phone.value, user_last_name.object_id \n"
        + "FROM   attributes user_last_name, attributes user_first_name, attributes users_email,\n"
        + "       attributes user_password, attributes user_phone\n"
        + "WHERE  USERS_EMAIL.value = " + email + "\n"
        + "       AND user_last_name.attr_id = 1                         /*LAST_NAME*/     \n"
        + "       AND user_last_name.object_id = users_email.object_id                     \n"
        + "       AND user_first_name.attr_id = 2                        /*FIRST_NAME*/    \n"
        + "       AND user_first_name.object_id = users_email.object_id                     \n"
        + "       AND user_password.attr_id = 4                          /*PASSWORD*/      \n"
        + "       AND user_password.object_id = users_email.object_id                      \n"
        + "       AND user_phone.attr_id = 5                             /*PHONE*/         \n"
        + "       AND user_phone.object_id = users_email.object_id";

    return jdbcTemplate.query(sql, new ResultSetExtractor<User>() {
      @Override
      public User extractData(ResultSet resultSet) throws SQLException, DataAccessException {

        if (resultSet.next()) {
          User user = new User();
          user.setLast_name(resultSet.getString(1));
          user.setFirst_name(resultSet.getString(2));
          user.setPassword(passwordEncoder.encode(resultSet.getString(3)));
          user.setPhone(resultSet.getString(4));
          user.setId(resultSet.getInt(5));
          user.setEmail(s);
          return user;
        }
        return null;
      }
    });
  }
}







