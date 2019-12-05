package com.netcracker.testerritto.dao;

import com.netcracker.testerritto.mappers.RemarkRowMapper;
import com.netcracker.testerritto.models.Remark;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigInteger;
import java.util.logging.Level;
import java.util.logging.Logger;

@Repository
public class RemarkDAO  {

    @Autowired
    public JdbcTemplate jdbcTemplate;

    @Autowired
    public RemarkDAO(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    private static Logger log=Logger.getLogger(RemarkDAO.class.getName());

    public Remark getRemarkById(BigInteger remarkId) {
        if(remarkId == null){
            IllegalArgumentException exception = new IllegalArgumentException("RemarkId can not be NULL.");
            log.log(Level.SEVERE, exception.getMessage(), remarkId);
            throw exception;
        }
        String query =
                            "select\n" +
                                "remarks.object_id as id,\n" +
                                "remark_text.value as text,\n" +
                                "sender.reference as userId,\n" +
                                "caused_by_question.reference  as questionId\n" +
                            "from\n" +
                            "    objects remarks,\n" +
                            "    attributes remark_text,\n" +
                            "    objreference sender,\n" +
                            "    objreference caused_by_question\n" +
                            "where\n" +
                            "    remarks.object_id = ? /* remarkId */\n" +
                            "and remarks.object_id = remark_text.object_id\n" +
                            "and remark_text.attr_id = 8 /* REMARK_TEXT */\n" +
                            "and remarks.object_id = sender.object_id\n" +
                            "and sender.attr_id = 26 /* SEND */\n" +
                            "and remarks.object_id = caused_by_question.object_id\n" +
                            "and caused_by_question.attr_id = 28 /* CAUSED_BY */";
        return jdbcTemplate.queryForObject(query, new Object[]{remarkId.toString()}, new RemarkRowMapper());
    }

    @Transactional
    public void createRemark(BigInteger userId, BigInteger questionId, String remarkText) {
        if(userId == null || questionId == null || remarkText.equals("")){
            IllegalArgumentException exception = new IllegalArgumentException("UserId OR QuestionId OR RemarkText can not be NULL.");
            log.log(Level.SEVERE, exception.getMessage());
            throw exception;
        }
        String query =
                        "insert all\n" +
                        "    into objects(object_id, parent_id, object_type_id, name, description)\n" +
                        "        values(object_id_pr.nextval, null, 3, 'REMARK '|| object_id_pr.currval, null)\n" +
                        "    into attributes(object_id, attr_id, value, date_value, list_value_id)\n" +
                        "        values(object_id_pr.currval, 8, ?, null, null) /* remarkText */\n" +
                        "    into objreference(attr_id, object_id, reference) /* CAUSED_BY */\n" +
                        "        values(28, object_id_pr.currval, ?) /* questionId */\n" +
                        "    into objreference(attr_id, object_id, reference) /* SEND */\n" +
                        "        values(26, object_id_pr.currval, ?) /* userId */\n" +
                        "    into objreference(attr_id, object_id, reference) /* PROCESS_BY */\n" +
                        "        values(27, object_id_pr.currval, author_id)\n" +
                        "select\n" +
                        "    test2author.reference as author_id\n" +
                        "from \n" +
                        "    objects questions,\n" +
                        "    objects tests,\n" +
                        "    objreference test2author\n" +
                        "where\n" +
                        "    questions.object_id = ? /* questionId */\n" +
                        "and questions.parent_id = tests.object_id\n" +
                        "and test2author.object_id = tests.object_id\n" +
                        "and test2author.attr_id = 24 /* CREATE_TEST_BY */";
        jdbcTemplate.update(query, remarkText, questionId.toString(), userId.toString(), questionId.toString());
    }

    @Transactional
    public void deleteRemark(BigInteger remarkId) {
        if(remarkId == null){
            IllegalArgumentException exception = new IllegalArgumentException("RemarkId can not be NULL.");
            log.log(Level.SEVERE, exception.getMessage(), remarkId);
            throw exception;
        }
        String query =
                "delete from\n" +
                "    objects\n" +
                "where\n" +
                "    object_id = ? /* remarkId */";
        jdbcTemplate.update(query, remarkId.toString());
    }
}
