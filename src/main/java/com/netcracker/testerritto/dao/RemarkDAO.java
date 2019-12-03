package com.netcracker.testerritto.dao;

import com.netcracker.testerritto.mappers.RemarkRowMapper;
import com.netcracker.testerritto.models.Remark;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
public class RemarkDAO  {

    @Autowired
    public JdbcTemplate jdbcTemplate;

    @Autowired
    public RemarkDAO(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public Remark getRemarkById(Integer remarkId) {
        String query =
                    "SELECT  remarks.object_id AS id, remark_text.value AS text, \n" +
                            "        sender.reference AS userId, caused_by_question.reference  AS questionId\n" +
                            "FROM\n" +
                            "    objects remarks,\n" +
                            "    attributes remark_text,\n" +
                            "    objreference sender,\n" +
                            "    objreference caused_by_question\n" +
                            "WHERE\n" +
                            "    remarks.object_id = ? /* remarkId */\n" +
                            "AND remarks.object_id = remark_text.object_id\n" +
                            "AND remark_text.attr_id = 8 /* REMARK_TEXT */\n" +
                            "AND remarks.object_id = sender.object_id\n" +
                            "AND sender.attr_id = 26 /* SEND */\n" +
                            "AND remarks.object_id = caused_by_question.object_id\n" +
                            "AND caused_by_question.attr_id = 28 /* CAUSED_BY */";
        return jdbcTemplate.queryForObject(query, new Object[]{remarkId}, new RemarkRowMapper());
    }

    @Transactional
    public void createRemark(Integer userId, Integer questionId, String remarkText) {
        String query =
                "INSERT ALL\n" +
                        "    INTO objects(object_id, parent_id, object_type_id, name, description)\n" +
                        "        VALUES(object_id_PR.NEXTVAL, null, 3, 'REMARK '|| object_id_PR.CURRVAL, null)\n" +
                        "\t\t\n" +
                        "    INTO attributes(object_id, attr_id, value, date_value, list_value_id)\n" +
                        "        VALUES(object_id_PR.CURRVAL, 8, ?, null, null) /* remarkText */\n" +
                        "\t\t\n" +
                        "    INTO objreference(attr_id, object_id, reference) /* CAUSED_BY */\n" +
                        "        VALUES(28, object_id_PR.CURRVAL, ?) /* questionId */\t\t\t\n" +
                        "\t\t\n" +
                        "    INTO objreference(attr_id, object_id, reference) /* SEND */\n" +
                        "        VALUES(26, object_id_PR.CURRVAL, ?) /* userId */\t\t\t\t\t\n" +
                        "\t\t\n" +
                        "    INTO objreference(attr_id, object_id, reference) /* PROCESS_BY */\n" +
                        "        VALUES(27, object_id_PR.CURRVAL, author_id) \t\t\n" +
                        "\t\t\n" +
                        "SELECT test2author.reference AS author_id\n" +
                        "FROM \n" +
                        "    objects questions,\n" +
                        "    objects tests,\n" +
                        "    objreference test2author\n" +
                        "WHERE\n" +
                        "    questions.object_id = ? /* questionId */\n" +
                        "AND questions.parent_id = tests.object_id\n" +
                        "AND test2author.object_id = tests.object_id\n" +
                        "AND test2author.attr_id = 24 /* CREATE_TEST_BY */";
        jdbcTemplate.update(query, remarkText, questionId, userId, questionId);
    }

    @Transactional
    public void deleteRemark(Integer remarkId) {
        String query =
                "DELETE FROM objects \n" +
                        "\tWHERE object_id = ? /* remarkId */";
        jdbcTemplate.update(query, remarkId);
    }
}
