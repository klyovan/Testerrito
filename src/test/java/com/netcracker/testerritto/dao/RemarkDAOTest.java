package com.netcracker.testerritto.dao;

import com.netcracker.testerritto.ApplicationConfiguration;
import com.netcracker.testerritto.DataSourceConfig;
import com.netcracker.testerritto.models.Remark;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;

import java.math.BigInteger;
import java.util.Locale;

import static org.junit.Assert.assertEquals;

@RunWith(SpringJUnit4ClassRunner.class)
@WebAppConfiguration
@ContextConfiguration(classes = { ApplicationConfiguration.class, DataSourceConfig.class })
public class RemarkDAOTest {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Autowired
    private RemarkDAO remarkDAO;

    private BigInteger authorId;
    private BigInteger groupId;
    private BigInteger testId;
    private BigInteger questionId;
    private BigInteger remarkerId;
    private BigInteger sequenceId;
    private static String CURRENT_SEQUENCE = "select object_id_pr.currval from dual";

    @Before
    public void setup(){
        Locale.setDefault(Locale.ENGLISH);
        String sql =
                "insert all\n" +
                        "    into objects(object_id, parent_id, object_type_id, name, description)\n" +
                        "        values(object_id_pr.nextval, ?, ?, ?, null)\n" +
                        "select * from dual";

        jdbcTemplate.update(sql,null, 1, "USER_AUTHOR "+"object_id_pr.currval");
        authorId = jdbcTemplate.queryForObject(CURRENT_SEQUENCE, BigInteger.class);

        jdbcTemplate.update(sql, null, 2, "GROUP " + "object_id_pr.currval");
        groupId = jdbcTemplate.queryForObject(CURRENT_SEQUENCE, BigInteger.class);

        jdbcTemplate.update(
                "insert all\n" +
                        "    into objects(object_id, parent_id, object_type_id, name, description)\n" +
                        "        values(object_id_pr.nextval, ?, 4, 'TEST '|| object_id_pr.currval, null)\n" +
                        "    into objreference(attr_id, object_id, reference) /* CREATE_TEST_BY */\n" +
                        "        values(24, object_id_pr.currval, ?)\n" +
                        "select * from dual", groupId.toString(), groupId.toString());
        testId = jdbcTemplate.queryForObject(CURRENT_SEQUENCE, BigInteger.class);

        jdbcTemplate.update(sql, testId.toString(), 10, "QUESTION " + "object_id_pr.currval");
        questionId = jdbcTemplate.queryForObject(CURRENT_SEQUENCE, BigInteger.class);

        jdbcTemplate.update(sql, null, 1, "USER_REMARKER " + "object_id_pr.currval");
        remarkerId = jdbcTemplate.queryForObject(CURRENT_SEQUENCE, BigInteger.class);
    }

    @After
    public void setDown(){
        String sql =
                "delete from\n" +
                        "    objects\n" +
                        "where\n" +
                        "    object_id = ?";
        jdbcTemplate.update(sql, authorId.toString());
        jdbcTemplate.update(sql, groupId.toString());
        jdbcTemplate.update(sql, testId.toString());
        jdbcTemplate.update(sql, questionId.toString());
        jdbcTemplate.update(sql, remarkerId.toString());
        if(sequenceId != null)
            jdbcTemplate.update(sql, sequenceId.toString());
    }

    @Test
    public void insertGetRemarkById() throws Exception{
        remarkDAO.createRemark(remarkerId, questionId,"New Remark Text");
        sequenceId = jdbcTemplate.queryForObject(CURRENT_SEQUENCE, BigInteger.class);

        Remark remark = remarkDAO.getRemarkById(sequenceId);

        Remark remarkExpected = new Remark(sequenceId, "New Remark Text", remarkerId, questionId);

        assertEquals(remarkExpected, remark);
    }

    @Test(expected = IllegalArgumentException.class)
    public void getRemarkByIdNull() throws Exception{
        Remark remark = remarkDAO.getRemarkById(null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void insertRemarkUserIdNull() throws Exception{
        remarkDAO.createRemark(null, questionId, "New Remark Text");
    }

    @Test(expected = IllegalArgumentException.class)
    public void insertRemarkQuestionIdNull() throws Exception{
        remarkDAO.createRemark(authorId, null, "New Remark Text");
    }

    @Test(expected = IllegalArgumentException.class)
    public void insertRemarkRemarkTextNull() throws Exception{
        remarkDAO.createRemark(authorId, questionId, "");
    }

    @Test(expected = EmptyResultDataAccessException.class)
    public void deleteRemarkById() throws Exception{
        remarkDAO.createRemark(remarkerId, questionId,"New Remark Text");
        sequenceId = jdbcTemplate.queryForObject(CURRENT_SEQUENCE, BigInteger.class);

        remarkDAO.deleteRemark(sequenceId);

        Remark remark = remarkDAO.getRemarkById(sequenceId);
    }

    @Test(expected = IllegalArgumentException.class)
    public void deleteRemarkByIdNull() throws Exception{
        remarkDAO.deleteRemark(null);
    }

}
