package com.netcracker.testerritto.dao;


import com.netcracker.testerritto.mappers.QuestionRowMapper;
import com.netcracker.testerritto.mappers.ReplyRowMapper;
import com.netcracker.testerritto.mappers.ResultRowMapper;
import com.netcracker.testerritto.models.Question;
import com.netcracker.testerritto.models.Reply;
import com.netcracker.testerritto.models.Result;
import com.netcracker.testerritto.properties.AttrtypeProperties;
import com.netcracker.testerritto.properties.ObjtypeProperties;
import java.math.BigInteger;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

@Repository
public class ResultDAO {

    @Autowired
    JdbcTemplate jdbcTemplate;

    BigInteger status = null;

    public Result getResult(BigInteger resultId) {
        String sql = "select  results.object_id id, result2test.reference test_id,\n" +
            "    result_date.date_value result_date, result_score.value result_score ,\n" +
            "    result_status.list_value_id result_status, result2user.reference user_id    \n" +
            "from  objects results,\n" +
            "    attributes result_date,\n" +
            "    attributes result_score,\n" +
            "    attributes result_status,\n" +
            "    objreference result2test,\n" +
            "    objreference result2user                        \n" +
            "where results.object_id = ?\n" +
            "    and result2test.attr_id = ? /*results_belongs*/\n" +
            "    and result2test.object_id = results.object_id\n" +
            "    and result_date.attr_id = ? /*date*/\n" +
            "    and result_date.object_id = results.object_id\n" +
            "    and result_score.attr_id = ? /*score*/\n" +
            "    and result_score.object_id = results.object_id\n" +
            "    and result_status.attr_id = ? /*status*/\n" +
            "    and result_status.object_id = results.object_id\n" +
            "    and result2user.attr_id = ? /*look by*/\n" +
            "    and result2user.object_id = results.object_id";
        Result result = jdbcTemplate.queryForObject(sql, new Object[]{resultId.toString(), AttrtypeProperties.RESULT_BELONGS.toString(), AttrtypeProperties.DATE.toString(),
            AttrtypeProperties.SCORE_RESULT.toString(), AttrtypeProperties.STATUS.toString(), AttrtypeProperties.LOOK_BY.toString()}, new ResultRowMapper());

        result.setReplies(getReplies(resultId));


        return result;
    }

    public HashMap<Reply, Question> getReplies(BigInteger resultId) {
        HashMap<BigInteger, BigInteger> resultHashMapId = new HashMap<>();
        HashMap<Reply, Question> resultHashMap = new HashMap<>();


        String sql = "select  results.object_id res_id, reply.object_id rep_id, questions.object_id ques_id \n" +
            "         \n" +
            "  from objects results,\n" +
            "    objreference answer2reply,\n" +
            "    objects reply,\n" +
            "    objreference replys2results,\n" +
            "    objects questions,\n" +
            "    objects tests,\n" +
            "    objects answer   \n" +
            "where replys2results.attr_id = ? \n" +
            "    and replys2results.object_id = reply.object_id\n" +
            "    and replys2results.reference = results.object_id\n" +
            "    \n" +
            "    and answer2reply.attr_id = ?\n" +
            "    and answer2reply.reference = reply.object_id\n" +
            "    and answer2reply.object_id = answer.object_id\n" +
            "    \n" +
            "    \n" +
            "    and questions.object_id = answer.parent_id\n" +
            "    and tests.object_id = questions.parent_id\n" +
            "    and results.object_id = ?";

        jdbcTemplate.query(sql, new Object[]{AttrtypeProperties.REPLY_BELONGS.toString(), AttrtypeProperties.ANSWER_BELONGS.toString(), resultId.toString()}, new RowMapper<Map<BigInteger, BigInteger>>() {
                @Override
                public Map<BigInteger, BigInteger> mapRow(ResultSet resultSet, int i) throws SQLException {
                    resultHashMapId.put(BigInteger.valueOf(resultSet.getInt("rep_id")), BigInteger.valueOf(resultSet.getInt("ques_id")));
                    return resultHashMapId;
                }
            }
        );

        String sqlForQues = "select questions.object_id id, text_question.value text,\n" +
            "    type_question.value type, tests.parent_id testId\n" +
            "from objects questions,\n" +
            "    attributes type_question,\n" +
            "    attributes text_question,\n" +
            "    objects tests\n" +
            "where\n" +
            "    questions.object_id = ?\n" +
            "    and type_question.attr_id = ?  /*type_question*/\n" +
            "    and type_question.object_id = questions.object_id\n" +
            "    and text_question.attr_id = ?  /*text_question*/\n" +
            "    and text_question.object_id = questions.object_id\n" +
            "    and questions.object_id = tests.object_id   /*match_category*/";


        String sqlForRep = "select  reply.object_id id, results.object_id result_id, answer.object_id answer_id     \n" +
            "from objects users,\n" +
            "objects results,\n" +
            "objreference results_2_users,\n" +
            "objects groups,\n" +
            "objreference users_2_groups,\n" +
            "objects reply,\n" +
            "objreference replys_2_results,\n" +
            "objects answer,\n" +
            "objreference answer_2_reply,\n" +
            "objects questions,\n" +
            "objects tests\n" +
            "where  \n" +
            "    users.object_type_id = ?\n" +
            "    and results.object_type_id = ?\n" +
            "    and results_2_users.object_id= results.object_id\n" +
            "    and results_2_users.reference = users.object_id\n" +
            "    and groups.object_type_id = ?\n" +
            "    and users_2_groups.attr_id = ?\n" +
            "    and users_2_groups.object_id = users.object_id\n" +
            "    and users_2_groups.reference = groups.object_id\n" +
            "       \n" +
            "     and reply.object_id = ?  \n" +
            "    and reply.object_type_id = ?\n" +
            "    and replys_2_results.attr_id = ?\n" +
            "    and replys_2_results.object_id = reply.object_id\n" +
            "    and replys_2_results.reference = results.object_id\n" +
            "       \n" +
            "    and answer.object_type_id = ?\n" +
            "    and answer_2_reply.attr_id = ?\n" +
            "    and answer_2_reply.object_id = answer.object_id\n" +
            "    and answer_2_reply.reference = reply.object_id\n" +
            "       \n" +
            "    and questions.object_id = answer.parent_id\n" +
            "    and tests.object_id = questions.parent_id"; //TODO change with replyDao.createReply();


        for (Map.Entry<BigInteger, BigInteger> entry : resultHashMapId.entrySet()) {
            resultHashMap.put(
                jdbcTemplate.queryForObject(sqlForRep, new Object[]{
                    ObjtypeProperties.USER, ObjtypeProperties.RESULT,
                    ObjtypeProperties.GROUP, AttrtypeProperties.CONSIST, entry.getKey().toString(),
                    ObjtypeProperties.REPLY, AttrtypeProperties.REPLY_BELONGS,
                    ObjtypeProperties.ANSWER, AttrtypeProperties.ANSWER_BELONGS}, new ReplyRowMapper()),    //TODO change with ReplyDao.createReply();
                jdbcTemplate.queryForObject(sqlForQues, new Object[]{entry.getValue().toString(), AttrtypeProperties.TYPE_QUESTION,
                    AttrtypeProperties.TEXT_QUESTION}, new QuestionRowMapper())                             //TODO change with QuestionDao.createQuestion();
            );
        }
        return resultHashMap;


    }

    public void deleteResult(BigInteger resultId) {
        new ObjectEavBuilder.Builder(jdbcTemplate)
            .setObjectId(resultId)
            .delete();
    }

    public BigInteger createResult(Result result) throws IllegalArgumentException {

        return new ObjectEavBuilder.Builder(jdbcTemplate)
            .setName("Result " + result.getId())
            .setObjectTypeId(ObjtypeProperties.RESULT)
            .setDateAttribute(AttrtypeProperties.DATE, result.getDate())
            .setStringAttribute(AttrtypeProperties.SCORE_RESULT, String.valueOf(result.getScore()))
            .setListAttribute(AttrtypeProperties.STATUS, result.getStatus().getId())
            .setReference(AttrtypeProperties.LOOK_BY, result.getUserId())
            .setReference(AttrtypeProperties.RESULT_BELONGS, result.getTestId())
            .create();
    }

    public BigInteger updateResult(Result result) {

        new ObjectEavBuilder.Builder(jdbcTemplate)
            .setObjectId(result.getId())
            .setObjectTypeId(ObjtypeProperties.RESULT)
            .setDateAttribute(AttrtypeProperties.DATE, result.getDate())
            .setStringAttribute(AttrtypeProperties.SCORE_RESULT, String.valueOf(result.getScore()))
            .setListAttribute(AttrtypeProperties.STATUS, result.getStatus().getId())
            .setReference(AttrtypeProperties.LOOK_BY, result.getUserId())
            .setReference(AttrtypeProperties.RESULT_BELONGS, result.getTestId())
            .update();

        return result.getId();

    }


}