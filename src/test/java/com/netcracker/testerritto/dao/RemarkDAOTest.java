package com.netcracker.testerritto.dao;

import com.netcracker.testerritto.ApplicationConfiguration;
import com.netcracker.testerritto.DataSourceConfig;
import com.netcracker.testerritto.models.Remark;
import com.netcracker.testerritto.properties.AttrtypeProperties;
import com.netcracker.testerritto.properties.ObjtypeProperties;
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
    private Remark remarkExpected = new Remark();

    @Before
    public void setup(){
        Locale.setDefault(Locale.ENGLISH);
        authorId = new ObjectEavBuilder.Builder(jdbcTemplate)
            .setObjectTypeId(ObjtypeProperties.USER)
            .setName("USER_AUTHOR")
            .create();
        groupId = new ObjectEavBuilder.Builder(jdbcTemplate)
            .setObjectTypeId(ObjtypeProperties.GROUP)
            .setName("GROUP")
            .create();
        testId = new ObjectEavBuilder.Builder(jdbcTemplate)
            .setParentId(groupId)
            .setObjectTypeId(ObjtypeProperties.TEST)
            .setName("TEST")
            .setReference(AttrtypeProperties.CREATE_TEST_BY, authorId)
            .create();
        questionId = new ObjectEavBuilder.Builder(jdbcTemplate)
            .setParentId(testId)
            .setObjectTypeId(ObjtypeProperties.QUESTION)
            .setName("QUESTION")
            .create();
        remarkerId = new ObjectEavBuilder.Builder(jdbcTemplate)
            .setObjectTypeId(ObjtypeProperties.USER)
            .setName("USER_REMARKER")
            .create();
        remarkExpected.setText("New Remark Text");
        remarkExpected.setUserSenderId(remarkerId);
        remarkExpected.setUserRecipientId(authorId);
        remarkExpected.setQuestionId(questionId);
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
        sequenceId = remarkDAO.createRemark(remarkExpected);
        remarkExpected.setId(sequenceId);
        Remark remark = remarkDAO.getRemarkById(sequenceId);
        assertEquals(remarkExpected.getId(), remark.getId());
        assertEquals(remarkExpected.getText(), remark.getText());
        assertEquals(remarkExpected.getQuestionId(), remark.getQuestionId());
        assertEquals(remarkExpected.getUserSenderId(), remark.getUserSenderId());
        assertEquals(remarkExpected.getUserRecipientId(), remark.getUserRecipientId());
    }

    @Test(expected = EmptyResultDataAccessException.class)
    public void deleteRemarkById() throws Exception{
        sequenceId = remarkDAO.createRemark(remarkExpected);
        remarkExpected.setId(sequenceId);
        remarkDAO.deleteRemark(sequenceId);
        Remark remark = remarkDAO.getRemarkById(sequenceId);
    }

}
