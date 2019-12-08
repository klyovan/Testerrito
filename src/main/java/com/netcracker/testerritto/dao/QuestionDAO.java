package com.netcracker.testerritto.dao;

import com.netcracker.testerritto.mappers.QuestionRowMapper;
import com.netcracker.testerritto.models.Question;
import java.math.BigInteger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

@Repository
public class QuestionDAO {
  @Autowired
  public JdbcTemplate jdbcTemplate;

  @Autowired
  private QuestionRowMapper questionRowMapper;

  public QuestionDAO(JdbcTemplate jdbcTemplate) {
    this.jdbcTemplate = jdbcTemplate;
  }
  public Question getQuestionById(BigInteger id) {
    String query ="select questions.object_id as id,\n"
        + "       question_text.value as text\n"
        + "       from \n"
        + "       objects questions,\n"
        + "       attributes question_text\n"
        + "       where\n"
        + "       questions.object_id = ?\n"
        + "       and questions.object_id = question_text.object_id\n"
        + "       and question_text.attr_id = 18";
    return jdbcTemplate.queryForObject(query, new Object[]{id}, questionRowMapper);;
  }
  public void updateQuestion(Question question){}
  public void createQuestion(BigInteger testId, String textQuestion, int typeQuestion, BigInteger categoryId){
    String query ="insert all\n"
        + "    into objects(object_id, parent_id, object_type_id, name) values(object_id_pr.nextval, ?, 10 , 'Question' || object_id_pr.currval)\n"
        + "    into attributes(attr_id, object_id, value ) values (18, object_id_pr.currval,?)\n"
        + "    into attributes(attr_id, object_id, list_value_id) values (19, object_id_pr.currval, ?)";

  }
  public void deleteQuestion(BigInteger id){
    String query ="delete from  \n"
        + "objects questions \n"
        + "where \n"
        + "questions.object_id = ? \n"
        + "and questions.object_type_id = 10;";
    jdbcTemplate.update(query, new Object[]{id});
  }
}
