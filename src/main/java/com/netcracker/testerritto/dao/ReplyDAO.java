package com.netcracker.testerritto.dao;

import com.netcracker.testerritto.dao.ObjectEavBuilder.Builder;
import com.netcracker.testerritto.mappers.ReplyRowMapper;
import com.netcracker.testerritto.models.Reply;
import com.netcracker.testerritto.properties.AttrtypeProperties;
import com.netcracker.testerritto.properties.ObjtypeProperties;
import java.math.BigInteger;
import java.sql.ResultSet;
import java.sql.SQLException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

@Repository
public class ReplyDAO {

  @Autowired
  private JdbcTemplate jdbcTemplate;

  public Reply getReply(BigInteger id) {
    String sql =
        "select \n " +
            "    reply.object_id id,\n" +
            "    results.object_id result_id,\n" +
            "    answer.object_id answer_id,\n" +
            "    text_answer.value text_answer \n" +
            "from \n" +
            "    objects results,\n" +
            "    objects answer,\n" +
            "    objects reply,\n" +
            "    objreference answer_2_reply,\n" +
            "    objreference replys_2_results,\n" +
            "    attributes text_answer\n" +
            "where \n" +
            "    reply.object_id = ?              /*reply_id*/          \n" +
            "    and results.object_type_id= 5\n" +
            "    and replys_2_results.attr_id = 31\n" +
            "    and replys_2_results.object_id = reply.object_id\n" +
            "    and replys_2_results.reference = results.object_id \n" +
            "    and answer.object_type_id = 11     \n" +
            "    and answer_2_reply.attr_id = 32\n" +
            "    and answer_2_reply.object_id = answer.object_id\n" +
            "    and answer_2_reply.reference = reply.object_id\n" +
            "    and text_answer.attr_id = 20\n" +
            "    and text_answer.object_id = answer.object_id";

    return jdbcTemplate.queryForObject(sql, new Object[]{id.toString()}, new RowMapper<Reply>() {
      @Override
      public Reply mapRow(ResultSet resultSet, int i) throws SQLException {
        Reply reply = new Reply();
        reply.setId(new BigInteger(resultSet.getString("id")));
        reply.setResultId(new BigInteger(resultSet.getString("result_id")));
        reply.setAnswerId(new BigInteger(resultSet.getString("answer_id")));
        reply.setAnswer(resultSet.getString("text_answer"));
        return reply;
      }
    });
  }


  public void updateReply(BigInteger replyId, BigInteger answerId) {

    Reply reply = getReply(replyId);
    new Builder(jdbcTemplate)
        .setObjectId(reply.getAnswerId())
        .setReference(AttrtypeProperties.ANSWER_BELONGS, replyId)
        .delete();

    new Builder(jdbcTemplate)
        .setObjectId(answerId)
        .setReference(AttrtypeProperties.ANSWER_BELONGS, replyId)
        .createReference();
  }

  public BigInteger createReply(BigInteger resultId, BigInteger answerId) {
    String objectName = "Reply for question " + getQuestionId(answerId);

    BigInteger replyId = new Builder(jdbcTemplate)
        .setObjectTypeId(ObjtypeProperties.REPLY)
        .setName(objectName)
        .setReference(AttrtypeProperties.REPLY_BELONGS, resultId)
        .create();

    new Builder(jdbcTemplate)
        .setObjectId(answerId)
        .setReference(AttrtypeProperties.ANSWER_BELONGS, replyId)
        .createReference();

    return replyId;

  }

  public void deleteReply(BigInteger id) {
    new ObjectEavBuilder.Builder(jdbcTemplate)
        .setObjectId(id)
        .delete();
  }

  private Integer getQuestionId(BigInteger answerId) {
    String sql = ""
        + "select answer.parent_id\n"
        + "from objects answer\n"
        + "where answer.object_id = ?";

    return jdbcTemplate
        .queryForObject(sql, new Object[]{answerId.toString()}, new RowMapper<Integer>() {
          @Override
          public Integer mapRow(ResultSet rs, int rowNum) throws SQLException {
            return rs.getInt(1);
          }
        });
  }

}
