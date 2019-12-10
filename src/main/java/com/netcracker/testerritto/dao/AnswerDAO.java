package com.netcracker.testerritto.dao;

import com.netcracker.testerritto.mappers.AnswerRowMapper;
import com.netcracker.testerritto.models.Answer;
import com.netcracker.testerritto.models.Question;
import com.netcracker.testerritto.properties.AttrtypeProperties;
import com.netcracker.testerritto.properties.ListsAttr;
import com.netcracker.testerritto.properties.ObjtypeProperties;
import java.math.BigInteger;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import org.w3c.dom.Attr;

@Repository
@Transactional
public class AnswerDAO {
  @Autowired
  public JdbcTemplate jdbcTemplate;
  @Autowired
  private AnswerRowMapper answerRowMapper;

  public AnswerDAO(JdbcTemplate jdbcTemplate) {
    this.jdbcTemplate = jdbcTemplate;
  }

  public Answer getAnswerById(BigInteger answerId){
    String query ="select"
        + "        answer.object_id id,"
        + "        answer.parent_id question_id,"
        + "        answer_text.list_value_id text,"
        + "        answer_score.value score,"
        + "        answer2reply.reference reply_id, "
        + "        answer2remark.reference remark_id,"
        + "        next_question. reference next_question"
        + "        from"
        + "        objects answer,"
        + "        attributes answer_text,"
        + "        attributes answer_score,"
        + "        objreference answer2reply,"
        + "        objreference answer2remark,"
        + "        objreference next_question"
        + "        where"
        + "        answer.object_id = ?"
        + "        and answer.object_type_id = " + ObjtypeProperties.ANSWER
        + "        and answer_text.attr_id = " + AttrtypeProperties.TEXT_ANSWER
        + "        and answer_text.object_id = answer.object_id"
        + "        and answer_score.attr_id = " + AttrtypeProperties.SCORE_ANSWER
        + "        and answer_score.object_id = answer.object_id"
        + "        and answer2reply.object_id = answer.object_id"
        + "        and answer2reply.attr_id = " + AttrtypeProperties.ANSWER_BELONGS
        + "        and answer2remark.object_id = answer.object_id"
        + "        and answer2remark.attr_id = " + AttrtypeProperties.CAUSED_BY
        + "        and next_question.object_id = answer.object_id"
        + "        and next_question.attr_id = " + AttrtypeProperties.NEXT_QUESTION;
    return jdbcTemplate.queryForObject(query, new Object[]{answerId.toString()}, answerRowMapper);
  }

  public List<Answer> getAllAnswerInQuestion(BigInteger questionId){
    String query ="select"
        + "answer.object_id id,"
        + "answer.parent_id question_id,"
        + "answer_text.list_value_id text,"
        + "answer_score.value score,"
        + "answer2reply.reference reply_id, "
        + "answer2remark.reference remark_id,"
        + "next_question. reference next_question"
        + "from"
        + "objects answer,"
        + "attributes answer_text,"
        + "attributes answer_score,"
        + "objreference answer2reply,"
        + "objreference answer2remark,"
        + "objreference next_question"
        + "where"
        + "answer.parent_id = ?"
        + "and answer.object_type_id = " + ObjtypeProperties.ANSWER
        + "and answer_text.attr_id = " + AttrtypeProperties.TEXT_ANSWER
        + "and answer_text.object_id = answer.object_id"
        + "and answer_score.attr_id = " + AttrtypeProperties.SCORE_ANSWER
        + "and answer_score.object_id = answer.object_id"
        + "and answer2reply.object_id = answer.object_id"
        + "and answer2reply.attr_id = " + AttrtypeProperties.ANSWER_BELONGS
        + "and answer2remark.object_id = answer.object_id"
        + "and answer2remark.attr_id = " + AttrtypeProperties.CAUSED_BY
        + "and next_question.object_id = answer.object_id"
        + "and next_question.attr_id = " + AttrtypeProperties.NEXT_QUESTION;
    return jdbcTemplate.query(query, new Object[]{questionId.toString()}, answerRowMapper);
  }
  public Answer getAnswerForReply(BigInteger questionId) {
    String query ="select "
        + "answer.object_id id,"
        + "answer.parent_id question_id,"
        + "answer_text.list_value_id text,"
        + "answer_score.value score,"
        + "answer2reply.reference"
        + "from"
        + "objects answer,"
        + "attributes answer_text,"
        + "attributes answer_score,"
        + "objreference answer2reply"
        + "where "
        + "answer2reply.reference = ?"
        + "and answer2reply.attr_id = " + AttrtypeProperties.ANSWER_BELONGS
        + "and answer2reply.object_id = answer.object_id"
        + "and answer.object_type_id = " + ObjtypeProperties.ANSWER
        + "and answer_text.attr_id = " + AttrtypeProperties.TEXT_ANSWER
        + "and answer_text.object_id = answer.object_id"
        + "and answer_score.attr_id = " + AttrtypeProperties.SCORE_ANSWER
        + "and answer_score.object_id = answer.object_id";
    return jdbcTemplate.queryForObject(query, new Object[]{questionId.toString()}, answerRowMapper);
  }

  /*public Question getNextQuestion(BigInteger id){
    String query = "select "
        + "answer.object_id id,"
        + "answer.parent_id question_id,"
        + "answer_text.value text,"
        + "answer_score.value score,"
        + "        answer2reply.reference"
        + "        from"
        + "        objects answer,"
        + "        attributes answer_text,"
        + "        attributes answer_score,"
        + "        objreference answer2reply"
        + "        where "
        + "        answer2reply.reference = ?"
        + "        and answer2reply.attr_id = " + AttrtypeProperties.NEXT_QUESTION
        + "        and answer2reply.object_id = answer.object_id"
        + "        and answer.object_type_id = " + ObjtypeProperties.ANSWER
        + "        and answer_text.attr_id = " + AttrtypeProperties.TEXT_ANSWER
        + "        and answer_text.object_id = answer.object_id"
        + "        and answer_score.attr_id = " + AttrtypeProperties.SCORE_ANSWER
        + "        and answer_score.object_id = answer.object_id";
    return null;
  }*/

  public Answer updateAnswer(Answer answer){
    new ObjectEavBuilder.Builder(jdbcTemplate)
        .setObjectId(answer.getId())
        .setStringAttribute(new BigInteger(String.valueOf(AttrtypeProperties.TEXT_ANSWER)), String.valueOf(answer.getTextAnswer()))
        .setStringAttribute(new BigInteger(String.valueOf(AttrtypeProperties.SCORE_ANSWER)), String.valueOf(answer.getScore()))
        .update();
    return getAnswerById(answer.getId());
  }

  public BigInteger createAnswer(Answer newAnswer){
    return new ObjectEavBuilder.Builder(jdbcTemplate)
        .setName("Answer" + newAnswer.getId())
        .setObjectTypeId(ObjtypeProperties.ANSWER)
        .setParentId(newAnswer.getQuestionId())
        .setStringAttribute(AttrtypeProperties.TEXT_ANSWER, String.valueOf(newAnswer.getTextAnswer()))
        .setStringAttribute(new BigInteger(String.valueOf(AttrtypeProperties.SCORE_ANSWER)), String.valueOf(newAnswer.getScore()))
        .setReference(new BigInteger(String.valueOf(AttrtypeProperties.NEXT_QUESTION)), newAnswer.getNextQuestionId())
        .setReference(new BigInteger(String.valueOf(AttrtypeProperties.ANSWER_BELONGS)), newAnswer.getReplyId())
        .create();
  }

  public void deleteAnswer(BigInteger answerId){
    new ObjectEavBuilder.Builder(jdbcTemplate)
        .setObjectId(answerId)
        .delete();
  }

  public void deleteAllAnswerInQuestion(BigInteger questionId){
    new ObjectEavBuilder.Builder(jdbcTemplate)
        .setParentId(questionId)
        .setObjectTypeId(new BigInteger(String.valueOf(ObjtypeProperties.ANSWER)))
        .delete();
  }
}
