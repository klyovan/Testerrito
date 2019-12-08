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
import java.util.ArrayList;
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

    @Before
    public void setup(){
        Locale.setDefault(Locale.ENGLISH);
        authorId = new ObjectEavBuilder.Builder(jdbcTemplate)
                .setObjectTypeId(new BigInteger("1"))
                .setName("USER_AUTHOR")
                .create();
        groupId = new ObjectEavBuilder.Builder(jdbcTemplate)
                .setObjectTypeId(new BigInteger("2"))
                .setName("GROUP")
                .create();
        testId = new ObjectEavBuilder.Builder(jdbcTemplate)
                .setParentId(groupId)
                .setObjectTypeId(new BigInteger("4"))
                .setName("TEST")
                .setReference(new BigInteger("24"), groupId)
                .create();
        questionId = new ObjectEavBuilder.Builder(jdbcTemplate)
                .setParentId(testId)
                .setObjectTypeId(new BigInteger("10"))
                .setName("QUESTION")
                .create();
        remarkerId = new ObjectEavBuilder.Builder(jdbcTemplate)
                .setObjectTypeId(new BigInteger("1"))
                .setName("USER_REMARKER")
                .create();
    }

    @After
    public void setDown(){
        ArrayList<BigInteger> listForDelete = new ArrayList<>();
        listForDelete.add(authorId);
        listForDelete.add(groupId);
        listForDelete.add(testId);
        listForDelete.add(questionId);
        listForDelete.add(remarkerId);
        if(sequenceId != null)
            listForDelete.add(sequenceId);
        for(BigInteger id : listForDelete){
            new ObjectEavBuilder.Builder(jdbcTemplate)
                    .setObjectId(id)
                    .delete();
        }
    }

    @Test
    public void insertGetRemarkById() throws Exception{
        sequenceId = remarkDAO.createRemark(remarkerId, questionId,"New Remark Text");
        Remark remark = remarkDAO.getRemarkById(sequenceId);
        Remark remarkExpected = new Remark(sequenceId, "New Remark Text", remarkerId, questionId);
        assertEquals(remarkExpected.getId(), remark.getId());
        assertEquals(remarkExpected.getText(), remark.getText());
        assertEquals(remarkExpected.getQuestionId(), remark.getQuestionId());
        assertEquals(remarkExpected.getUserSenderId(), remark.getUserSenderId());
    }

    @Test(expected = EmptyResultDataAccessException.class)
    public void deleteRemarkById() throws Exception{
        sequenceId = remarkDAO.createRemark(remarkerId, questionId,"New Remark Text");
        remarkDAO.deleteRemark(sequenceId);
        Remark remark = remarkDAO.getRemarkById(sequenceId);
    }

}
