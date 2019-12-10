package com.netcracker.testerritto.dao;

import com.netcracker.testerritto.mappers.RemarkRowMapper;
import com.netcracker.testerritto.models.Remark;
import com.netcracker.testerritto.properties.AttrtypeProperties;
import com.netcracker.testerritto.properties.ObjtypeProperties;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigInteger;

@Repository
public class RemarkDAO  {

    @Autowired
    public JdbcTemplate jdbcTemplate;

    @Autowired
    public RemarkDAO(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public Remark getRemarkById(BigInteger remarkId) {
        String query =
                "select\n" +
                "    remarks.object_id as id,\n" +
                "    remark_text.value as text,\n" +
                "    sender.reference as user_id,\n" +
                "    caused_by_question.reference  as question_id\n" +
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
    public BigInteger createRemark(BigInteger userId, BigInteger questionId, String remarkText) {
        String query =
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
        BigInteger authorId =  jdbcTemplate.queryForObject(query, BigInteger.class, questionId.toString());
        return new ObjectEavBuilder.Builder(jdbcTemplate)
                .setObjectTypeId(ObjtypeProperties.REMARK)
                .setName("Remark")
                .setStringAttribute(AttrtypeProperties.TEXT, remarkText)
                .setReference(AttrtypeProperties.SEND, userId)
                .setReference(AttrtypeProperties.PROCESS_BY, authorId)
                .setReference(AttrtypeProperties.CAUSED_BY, questionId)
                .create();
    }

    @Transactional
    public void deleteRemark(BigInteger remarkId) {
       new ObjectEavBuilder.Builder(jdbcTemplate)
               .setObjectId(remarkId)
               .delete();
    }
}
