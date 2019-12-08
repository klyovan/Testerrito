package com.netcracker.testerritto.dao;


import com.netcracker.testerritto.mappers.QuestionRowMapper;
import com.netcracker.testerritto.mappers.ReplyRowMapper;
import com.netcracker.testerritto.mappers.ResultRowMapper;
import com.netcracker.testerritto.models.Question;
import com.netcracker.testerritto.models.Reply;
import com.netcracker.testerritto.models.Result;
import com.netcracker.testerritto.properties.AttrtypeProperties;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.math.BigInteger;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

@Repository
public class ResultDAO {

    @Autowired
    JdbcTemplate jdbcTemplate;


    public Result getResult(BigInteger resultId) {
        String sql = "select  results.object_id id, result2test.reference test_id,\n" +
            "    result_date.date_value result_date, result_score.value result_score ,\n" +
            "    result_status_list.value result_status, result2user.reference user_id    \n" +
            "from  objects results,\n" +
            "    attributes result_date,\n" +
            "    attributes result_score,\n" +
            "    lists result_status_list,\n" +
            "    attributes result_status,\n" +
            "    objreference result2test,\n" +
            "    objreference result2user                        \n" +
            "where results.object_id = ? \n" +
            "    and result2test.attr_id = 30 /*results_belongs*/\n" +
            "    and result2test.object_id = results.object_id\n" +
            "    and result_date.attr_id = 10 /*date*/\n" +
            "    and result_date.object_id = results.object_id\n" +
            "    and result_score.attr_id = 11 /*score*/\n" +
            "    and result_score.object_id = results.object_id\n" +
            "    and result_status.attr_id = 12 /*status*/\n" +
            "    and result_status.object_id = results.object_id\n" +
            "    and result_status.list_value_id = result_status_list.list_value_id\n" +
            "    and result2user.attr_id = 29 /*look by*/\n" +
            "    and result2user.object_id = results.object_id\n";
        Result result = jdbcTemplate.queryForObject(sql, new Object[]{resultId.toString()}, new ResultRowMapper());

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

        jdbcTemplate.query(sql, new Object[]{new AttrtypeProperties().REPLY_BELONGS, new AttrtypeProperties().ANSWER_BELONGS,resultId.toString()}, new RowMapper<Map<BigInteger, BigInteger>>() {
                @Override
                public Map<BigInteger, BigInteger> mapRow(ResultSet resultSet, int i) throws SQLException {
                    resultHashMapId.put( BigInteger.valueOf(resultSet.getInt("rep_id")), BigInteger.valueOf(resultSet.getInt("ques_id")));
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
            "    and type_question.attr_id = 19  /*type_question*/\n" +
            "    and type_question.object_id = questions.object_id\n" +
            "    and text_question.attr_id = 18  /*text_question*/\n" +
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
            "    users.object_type_id = 1\n" +
            "    and results.object_type_id = 5\n" +
            "    and results_2_users.object_id= results.object_id\n" +
            "    and results_2_users.reference = users.object_id\n" +
            "    and groups.object_type_id = 2\n" +
            "    and users_2_groups.attr_id =22\n" +
            "    and users_2_groups.object_id = users.object_id\n" +
            "    and users_2_groups.reference = groups.object_id\n" +
            "       \n" +
            "     and reply.object_id = ?  \n" +
            "    and reply.object_type_id = 6\n" +
            "    and replys_2_results.attr_id = 31\n" +
            "    and replys_2_results.object_id = reply.object_id\n" +
            "    and replys_2_results.reference = results.object_id\n" +
            "       \n" +
            "    and answer.object_type_id =11\n" +
            "    and answer_2_reply.attr_id = 32\n" +
            "    and answer_2_reply.object_id = answer.object_id\n" +
            "    and answer_2_reply.reference = reply.object_id\n" +
            "       \n" +
            "    and questions.object_id = answer.parent_id\n" +
            "    and tests.object_id = questions.parent_id"; //TODO change with replyDao.createReply();


        for (Map.Entry<BigInteger, BigInteger> entry : resultHashMapId.entrySet()) {
            resultHashMap.put(
                jdbcTemplate.queryForObject(sqlForRep, new Object[]{entry.getKey().toString()}, new ReplyRowMapper()),    //TODO change with replyDao.createReply();
                jdbcTemplate.queryForObject(sqlForQues, new Object[]{entry.getValue().toString()}, new QuestionRowMapper())
            );
        }
        return resultHashMap;


    }

    public void deleteResult(BigInteger resultId) {
        String sql = "DELETE FROM objects WHERE object_id = ?";

        jdbcTemplate.update(sql, resultId);
    }

    public void createResult(Result result) {
        String sql = " insert all \n" +
            "    into objects(object_id, parent_id, object_type_id, name, description)\n" +
            "        values(object_id_pr.nextval, null, 5, 'result', null)\n" +
            "    into attributes(object_id, attr_id, value, date_value, list_value_id)\n" +
            "        values(object_id_pr.currval, 10,null, sys.date, null)  --date\n" +
            "    into attributes(object_id, attr_id, value, date_value, list_value_id)\n" +
            "        values(object_id_pr.currval, 11, 5, null, null) --score\n" +
            "    into attributes(object_id, attr_id, value, date_value, list_value_id)\n" +
            "        values(object_id_pr.currval, 12, null, null, 7) --status\n" +
            "    into objreference(attr_id, object_id, reference)\t-- look by \n" +
            "        values(29, object_id_, ?) --userid\n" +
            "    into objreference(attr_id, object_id, reference)\t--result_belong\n" +
            "        values(30, object_id_pr.currval, 147)--testid\n" +
            "    select * from dual";

    }

    public void updateResult(BigInteger resultId) {

    }


}
