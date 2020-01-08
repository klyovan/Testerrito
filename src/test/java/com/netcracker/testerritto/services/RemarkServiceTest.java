package com.netcracker.testerritto.services;

import com.netcracker.testerritto.ApplicationConfiguration;
import com.netcracker.testerritto.DataSourceConfig;
import com.netcracker.testerritto.dao.ObjectEavBuilder;
import com.netcracker.testerritto.exceptions.ServiceException;
import com.netcracker.testerritto.models.Remark;
import com.netcracker.testerritto.properties.AttrtypeProperties;
import com.netcracker.testerritto.properties.ObjtypeProperties;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
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
@ContextConfiguration(classes = {ApplicationConfiguration.class, DataSourceConfig.class})
public class RemarkServiceTest {

    @Autowired
    private RemarkService remarkService;

    @Autowired
    private JdbcTemplate jdbcTemplate;

    private BigInteger authorId;
    private BigInteger groupId;
    private BigInteger testId;
    private BigInteger questionId;
    private BigInteger remarkerId;
    private BigInteger sequenceId;
    private Remark remarkExpected = new Remark();

    @After
    public void setDown(){
        ArrayList<BigInteger> listForDelete = new ArrayList<>();
        if (sequenceId != null)
            listForDelete.add(authorId);
        if (sequenceId != null)
            listForDelete.add(groupId);
        if (sequenceId != null)
            listForDelete.add(testId);
        if (sequenceId != null)
            listForDelete.add(questionId);
        if (sequenceId != null)
            listForDelete.add(remarkerId);
        if (sequenceId != null)
            listForDelete.add(sequenceId);
        for (BigInteger id : listForDelete) {
            new ObjectEavBuilder.Builder(jdbcTemplate)
                .setObjectId(id)
                .delete();
        }
    }

    @Test(expected = IllegalArgumentException.class)
    public void getRemarkByIdNull() throws Exception {
        remarkService.getRemarkById(null);
    }

    @Test(expected = ServiceException.class)
    public void getRemarkByIdWrongId() throws Exception {
        remarkService.getRemarkById(new BigInteger("-666"));
    }

    @Test
    public void createAndGetRemarkById() throws Exception {
        createTestValues();
        sequenceId = remarkService.createRemark(remarkExpected);
        remarkExpected.setId(sequenceId);
        Remark remark = remarkService.getRemarkById(sequenceId);
        assertEquals(remarkExpected.getId(), remark.getId());
        assertEquals(remarkExpected.getText(), remark.getText());
        assertEquals(remarkExpected.getQuestionId(), remark.getQuestionId());
        assertEquals(remarkExpected.getUserSenderId(), remark.getUserSenderId());
        assertEquals(remarkExpected.getUserRecipientId(), remark.getUserRecipientId());
    }

    @Test(expected = IllegalArgumentException.class)
    public void createRemarkNull() throws Exception {
        Remark remark = new Remark();
        remarkService.createRemark(remark);
    }

    @Test(expected = IllegalArgumentException.class)
    public void createRemarkNullQuestionId() throws Exception {
        Remark remark = new Remark();
        remark.setQuestionId(null);
        remark.setUserSenderId(new BigInteger("1"));
        remark.setText("New Text");
        remarkService.createRemark(remark);
    }

    @Test(expected = IllegalArgumentException.class)
    public void createRemarkNullSenderId() throws Exception {
        Remark remark = new Remark();
        remark.setQuestionId(new BigInteger("1"));
        remark.setUserSenderId(null);
        remark.setText("New Text");
        remarkService.createRemark(remark);
    }

    @Test(expected = IllegalArgumentException.class)
    public void createRemarkNullText() throws Exception {
        Remark remark = new Remark();
        remark.setQuestionId(new BigInteger("1"));
        remark.setUserSenderId(new BigInteger("2"));
        remark.setText(null);
        remarkService.createRemark(remark);
    }

    @Test(expected = IllegalArgumentException.class)
    public void createRemarkEmptyText() throws Exception {
        Remark remark = new Remark();
        remark.setQuestionId(new BigInteger("1"));
        remark.setUserSenderId(new BigInteger("2"));
        remark.setText("");
        remarkService.createRemark(remark);
    }

    @Test(expected = ServiceException.class)
    public void createRemarkWrongSenderId() throws Exception {
        Remark remark = new Remark();
        remark.setQuestionId(new BigInteger("1"));
        remark.setUserSenderId(new BigInteger("-666"));
        remark.setUserRecipientId(new BigInteger("1"));
        remark.setText("New Next");
        remarkService.createRemark(remark);
    }

    @Test(expected = ServiceException.class)
    public void createRemarkWrongQuestionId() throws Exception {
        Remark remark = new Remark();
        remark.setQuestionId(new BigInteger("-666"));
        remark.setUserSenderId(new BigInteger("2"));
        remark.setUserRecipientId(new BigInteger("2"));
        remark.setText("New Text");
        remarkService.createRemark(remark);
    }

    @Test(expected = IllegalArgumentException.class)
    public void deleteRemarkNull() throws Exception {
        remarkService.deleteRemark(null);
    }

    @Test(expected = ServiceException.class)
    public void deleteRemark() throws Exception {
        createTestValues();
        sequenceId = remarkService.createRemark(remarkExpected);
        remarkExpected.setId(sequenceId);
        remarkService.deleteRemark(sequenceId);
        Remark remark = remarkService.getRemarkById(sequenceId);
    }

    private void createTestValues(){
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
}
