package com.netcracker.testerritto.dao;

import com.netcracker.testerritto.ApplicationConfiguration;
import com.netcracker.testerritto.DataSourceConfig;
import com.netcracker.testerritto.models.Remark;
import com.netcracker.testerritto.services.RemarkService;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.transaction.PlatformTransactionManager;

import javax.sql.DataSource;

import java.util.Locale;

import static org.junit.Assert.assertEquals;

@RunWith(SpringJUnit4ClassRunner.class)
@WebAppConfiguration
@ContextConfiguration(classes = { ApplicationConfiguration.class, DataSourceConfig.class })
public class TestRemarkDAO {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Autowired
    private RemarkDAO remarkDAO;

    private Integer authorId;
    private Integer groupId;
    private Integer testId;
    private Integer questionId;
    private Integer remarkerId;
    private Integer sequenceId;

    @Before
    public void setup(){
        Locale.setDefault(Locale.ENGLISH);
        jdbcTemplate.update(
                "INSERT ALL\n" +
                        "  INTO objects(object_id, parent_id, object_type_id, name, description)\n" +
                        "      VALUES(object_id_PR.NEXTVAL, null, 1, 'USER_AUTHOR '|| object_id_PR.CURRVAL, null)\n" +
                        "SELECT * FROM DUAL");
        authorId = jdbcTemplate.queryForObject("SELECT object_id_pr.CURRVAL FROM DUAL", Integer.class);
        jdbcTemplate.update(
                "INSERT ALL\n" +
                        "    INTO objects(object_id, parent_id, object_type_id, name, description)\n" +
                        "\t\tVALUES(object_id_PR.NEXTVAL, null, 2, 'GROUP '|| object_id_PR.CURRVAL, null)\n" +
                        "SELECT * FROM DUAL");
        groupId = jdbcTemplate.queryForObject("SELECT object_id_pr.CURRVAL FROM DUAL", Integer.class);
        jdbcTemplate.update(
                "INSERT ALL        \n" +
                        "\tINTO objects(object_id, parent_id, object_type_id, name, description)\n" +
                        "        VALUES(object_id_PR.NEXTVAL, object_id_PR.CURRVAL-1, 4, 'TEST '|| object_id_PR.CURRVAL, null)\t \n" +
                        "\tINTO objreference(attr_id, object_id, reference) /* CREATE_TEST_BY */\n" +
                        "        VALUES(24, object_id_PR.CURRVAL, object_id_PR.CURRVAL-1) \n" +
                        "SELECT * FROM DUAL");
        testId = jdbcTemplate.queryForObject("SELECT object_id_pr.CURRVAL FROM DUAL", Integer.class);
        jdbcTemplate.update(
                "INSERT ALL     \n" +
                        "\tINTO objects(object_id, parent_id, object_type_id, name, description)\n" +
                        "\t\tVALUES(object_id_PR.NEXTVAL, object_id_PR.CURRVAL-1, 10, 'QUESTION '|| object_id_PR.CURRVAL, null)\n" +
                        "SELECT * FROM DUAL");
        questionId = jdbcTemplate.queryForObject("SELECT object_id_pr.CURRVAL FROM DUAL", Integer.class);
        jdbcTemplate.update(
                "INSERT ALL     \n" +
                        "\tINTO objects(object_id, parent_id, object_type_id, name, description)\n" +
                        "\t\tVALUES(object_id_PR.NEXTVAL, null, 1, 'USER_REMARKER '|| object_id_PR.CURRVAL, null)\n" +
                        "SELECT * FROM DUAL");
        remarkerId = jdbcTemplate.queryForObject("SELECT object_id_pr.CURRVAL FROM DUAL", Integer.class);
        remarkDAO.createRemark(remarkerId, questionId,"New Remark Text");
        sequenceId = jdbcTemplate.queryForObject("SELECT object_id_pr.CURRVAL FROM DUAL", Integer.class);
    }

    @After
    public void setDown(){
        jdbcTemplate.update("DELETE FROM objects WHERE object_id = ?", authorId);
        jdbcTemplate.update("DELETE FROM objects WHERE object_id = ?", groupId);
        jdbcTemplate.update("DELETE FROM objects WHERE object_id = ?", testId);
        jdbcTemplate.update("DELETE FROM objects WHERE object_id = ?", questionId);
        jdbcTemplate.update("DELETE FROM objects WHERE object_id = ?", remarkerId);
        jdbcTemplate.update("DELETE FROM objects WHERE object_id = ?", sequenceId);
    }

    @Test
    public void insertGetRemark() throws Exception{
        Remark remark = remarkDAO.getRemarkById(sequenceId);

        Remark remarkExpected = new Remark();
        remarkExpected.setText("New Remark Text");
        remarkExpected.setQuestionId(questionId);
        remarkExpected.setUserId(remarkerId);
        remarkExpected.setId(sequenceId);

        assertEquals(remarkExpected, remark);
    }

    @Test(expected = EmptyResultDataAccessException.class)
    public void deleteRemarkById() throws Exception{
        remarkDAO.deleteRemark(sequenceId);
        Remark remark = remarkDAO.getRemarkById(sequenceId);
    }


}
