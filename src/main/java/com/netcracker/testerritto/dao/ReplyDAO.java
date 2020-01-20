package com.netcracker.testerritto.dao;

import com.netcracker.testerritto.dao.ObjectEavBuilder.Builder;
import com.netcracker.testerritto.mappers.ReplyRowMapper;
import com.netcracker.testerritto.models.Answer;
import com.netcracker.testerritto.models.Reply;
import com.netcracker.testerritto.properties.AttrtypeProperties;
import com.netcracker.testerritto.properties.ObjtypeProperties;

import java.math.BigInteger;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

@Repository
public class ReplyDAO {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    private final String GET_RESULT_ID_OF_REPLY =
        "select \n" +
            "     results.object_id result_id \n" +
            "from \n" +
            "     objects results, \n" +
            "     objects reply, \n" +
            "     objreference replys_2_results \n" +
            "where \n" +
            "     reply.object_id = ?           /*reply_id*/\n" +
            "     and results.object_type_id = 5 \n" +
            "     and replys_2_results.attr_id = 31 \n" +
            "     and replys_2_results.object_id = reply.object_id \n" +
            "     and replys_2_results.reference = results.object_id";

    private final String GET_ANSWER_LIST_FROM_REPLY =
        "    select  \n"
            + "                 answer.object_id answer_id, \n"
            + "                 text_answer.value text_answer,\n"
            + "                 score.value score\n"
            + "            from \n"
            + "                 objects answer, \n"
            + "                 objects reply, \n"
            + "                 objreference answer_2_reply,  \n"
            + "                 attributes text_answer,\n"
            + "                 attributes score\n"
            + "            where \n"
            + "                 reply.object_id = ?     /*reply_id*/ \n"
            + "                 and answer.object_type_id = 11  \n"
            + "                 and answer_2_reply.attr_id = 32 \n"
            + "                 and answer_2_reply.object_id = answer.object_id \n"
            + "                 and answer_2_reply.reference = reply.object_id \n"
            + "                 and text_answer.attr_id = 20\n"
            + "                 and text_answer.object_id = answer.object_id\n"
            + "                 and score.attr_id = 21\n"
            + "                 and score.object_id = answer.object_id";

    private final String ADD_ANSWER_QUERY =
        "insert into objreference (attr_id, object_id, reference) values(32, ?, ? )";

    private final String DELETE_ANSWER_QUERY =
        "delete from objreference where attr_id =32 and object_id = ? /*answer_id*/ and reference = ? /*reply_id*/";

    public Reply getReply(BigInteger id) {

        Reply reply = jdbcTemplate
            .queryForObject(GET_RESULT_ID_OF_REPLY, new Object[]{id.toString()}, new RowMapper<Reply>() {
                @Override
                public Reply mapRow(ResultSet resultSet, int i) throws SQLException {

                    Reply reply = new Reply();                                      //result_id
                    reply.setResultId(new BigInteger(resultSet.getString(1)));
                    return reply;
                }
            });

        List<Answer> answerList = jdbcTemplate
            .query(GET_ANSWER_LIST_FROM_REPLY, new Object[]{id.toString()}, new RowMapper<Answer>() {
                @Override
                public Answer mapRow(ResultSet resultSet, int i) throws SQLException {
                    Answer answer = new Answer();
                    answer.setId(new BigInteger(resultSet.getString("answer_id")));
                    answer.setTextAnswer(resultSet.getString("text_answer"));
                    answer.setScore(resultSet.getInt("score"));
                    return answer;
                }
            });

        reply.setReplyList(answerList);
        return reply;

    }


//    public void updateReply(BigInteger replyId, BigInteger oldAnswerId, BigInteger newAnswerId) {
//
//        Reply reply = getReply(replyId);
//        new Builder(jdbcTemplate)
//            .setObjectId(oldAnswerId)
//            .setReference(AttrtypeProperties.ANSWER_BELONGS, replyId)
//            .delete();
//
//        new Builder(jdbcTemplate)
//            .setObjectId(newAnswerId)
//            .setReference(AttrtypeProperties.ANSWER_BELONGS, replyId)
//            .createReference();
//    }

        public Reply updateReply(Reply reply, Answer answer ) {

      //  Reply reply = getReply(replyId);
        new Builder(jdbcTemplate)
            .setObjectId(reply.getReplyList().get(0).getId())
            .setReference(AttrtypeProperties.ANSWER_BELONGS, reply.getReplyId())
            .delete();

        new Builder(jdbcTemplate)
            .setObjectId(answer.getId())
            .setReference(AttrtypeProperties.ANSWER_BELONGS, reply.getReplyId())
            .createReference();

        return getReply(reply.getId());
    }


/////////////////////////////
    public BigInteger createReply(Reply reply) {
        String objectName = "Reply for question ";
        for (Answer answer: reply.getReplyList()) {
            objectName += getQuestionId(answer.getQuestionId());
            break;
        }
        BigInteger replyId = new Builder(jdbcTemplate)
            .setObjectTypeId(ObjtypeProperties.REPLY)
            .setName(objectName)
            .setReference(AttrtypeProperties.REPLY_BELONGS, reply.getResultId())
            .create();
        for (Answer answer : reply.getReplyList()) {
            new Builder(jdbcTemplate)
                .setObjectId(answer.getId()) //
                .setReference(AttrtypeProperties.ANSWER_BELONGS, replyId)
                .createReference();
        }
        return replyId;
    }

    public void deleteReply(BigInteger id) {
        new ObjectEavBuilder.Builder(jdbcTemplate)
            .setObjectId(id)
            .delete();
    }

    public Reply addAnswer(Reply reply, Answer answer) {
        jdbcTemplate.update(ADD_ANSWER_QUERY, answer.getId().toString(), reply.getId().toString());
        return getReply(reply.getId());
    }

    public Reply deleteAnswer(Reply reply, Answer answer) {
        jdbcTemplate.update(DELETE_ANSWER_QUERY, answer.getId().toString(), reply.getId().toString());
        return getReply(reply.getId());
    }

    private Integer getQuestionId(BigInteger answerId) {
        String sql = ""+
             "select answer.parent_id\n"+
             "from objects answer\n"+
             "where answer.object_id = ?";

        return jdbcTemplate
            .queryForObject(sql, new Object[]{answerId.toString()}, new RowMapper<Integer>() {
                @Override
                public Integer mapRow(ResultSet rs, int rowNum) throws SQLException {
                    return rs.getInt(1);
                }
            });
    }

}
