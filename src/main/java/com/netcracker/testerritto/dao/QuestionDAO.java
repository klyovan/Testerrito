package com.netcracker.testerritto.dao;

import com.netcracker.testerritto.mappers.QuestionRowMapper;
import com.netcracker.testerritto.models.Question;
import com.netcracker.testerritto.properties.AttrtypeProperties;
import com.netcracker.testerritto.properties.ObjtypeProperties;
import java.math.BigInteger;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
@Transactional
public class QuestionDAO {
  @Autowired
  public JdbcTemplate jdbcTemplate;

  @Autowired
  private QuestionRowMapper questionRowMapper;

  public QuestionDAO(JdbcTemplate jdbcTemplate) {
    this.jdbcTemplate = jdbcTemplate;
  }
  public Question getQuestionById(BigInteger id) {
    String query ="select "
        + "question.object_id as id,"
        + "question_text.value as text,"
        + "question_type.list_value_id as question_type,"
        + "question.parent_id as test_id,"
        + "match_category.reference as category_id,"
        + "caused_by_question.reference as remark_id"
        + "from"
        + "objects question,"
        + "attributes question_type,"
        + "attributes question_text,"
        + "objreference match_category,"
        + "objreference caused_by_question"
        + "where"
        + "question.object_id = ?"
        + "and question.object_type_id = " + ObjtypeProperties.QUESTION
        + "and question.object_id = question_text.object_id"
        + "and question_text.attr_id = " + AttrtypeProperties.TEXT_QUESTION
        + "and question.object_id = question_text.object_id"
        + "and question.object_id = question_type.object_id"
        + "and question_type.attr_id = " + AttrtypeProperties.TYPE_QUESTION
        + "and question.object_id = match_category.object_id"
        + "and match_category.attr_id = " + AttrtypeProperties.MATCH_CATEGORY
        + "and question.object_id = caused_by_question.object_id"
        + "and caused_by_question.attr_id = " + AttrtypeProperties.CAUSED_BY;
    return jdbcTemplate.queryForObject(query, new Object[]{id}, questionRowMapper);
  }

  public List<Question> getAllQuestionInTest(BigInteger test_id){
      String query = "select "
          + "question.object_id as id,"
          + "question_text.value as text,"
          + "question_type.list_value_id as type_question,"
          + "question.parent_id as test_id,"
          + "match_category.reference as category_id,"
          + "caused_by_question.reference as remark_id"
          + "from"
          + "objects question,"
          + "attributes question_text,"
          + "attributes question_type,"
          + "objreference match_category,"
          + "objreference caused_by_question"
          + "where"
          + "question.parent_id = ?"
          + "and question.object_type_id = " + ObjtypeProperties.QUESTION
          + "and question.object_id = question_text.object_id"
          + "and question_text.attr_id = " + AttrtypeProperties.TEXT_QUESTION
          + "and question.object_id = question_type.object_id\n"
          + "and question_type.attr_id = " + AttrtypeProperties.TYPE_QUESTION
          + "and question.object_id = match_category.object_id"
          + "and match_category.attr_id = " + AttrtypeProperties.MATCH_CATEGORY
          + "and question.object_id = caused_by_question.object_id"
          + "and caused_by_question.attr_id = " + AttrtypeProperties.CAUSED_BY;
    return jdbcTemplate.query(query, new Object[]{test_id}, questionRowMapper);
  }

  public Question updateQuestion(Question question){
    new ObjectEavBuilder.Builder(jdbcTemplate)
        .setObjectId(question.getId())
        .setStringAttribute(AttrtypeProperties.NAME_CATEGORY, question.getTextQuestion())
        .update();
    return getQuestionById(question.getId());
  }

  public BigInteger createQuestion(Question newQuestion){
    return new ObjectEavBuilder.Builder(jdbcTemplate)
      .setName(newQuestion.getTextQuestion())
      .setObjectTypeId(ObjtypeProperties.QUESTION)
      .setParentId(newQuestion.getTestId())
      .setStringAttribute(AttrtypeProperties.TEXT_QUESTION, newQuestion.getTextQuestion())
      .setListAttribute(AttrtypeProperties.TYPE_QUESTION,newQuestion.getTypeQuestion().getId())
      .create();
  }

  public void deleteQuestionById(BigInteger id){
    new ObjectEavBuilder.Builder(jdbcTemplate)
        .setObjectId(id)
        .delete();
  }
}
