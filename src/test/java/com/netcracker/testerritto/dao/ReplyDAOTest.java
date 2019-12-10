package com.netcracker.testerritto.dao;

import static org.junit.Assert.assertTrue;

import com.netcracker.testerritto.ApplicationConfiguration;
import com.netcracker.testerritto.models.Question;
import com.netcracker.testerritto.models.Reply;
import com.netcracker.testerritto.models.Result;
import com.netcracker.testerritto.properties.ListsAttr;
import java.math.BigInteger;
import java.util.Date;
import java.util.HashMap;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@SpringBootTest(classes = ApplicationConfiguration.class)
@RunWith(SpringRunner.class)
public class ReplyDAOTest {

  @Autowired
  private ReplyDAO replyDAO;

  @Autowired
  private ResultDAO resultDAO;

  private BigInteger resultId;
  private BigInteger replyId;


  @Before
  public void init() {

    replyId = replyDAO.createReply(BigInteger.valueOf(-69), BigInteger.valueOf(-911));
  }

  @After
  public void tearDown() {
    replyDAO.deleteReply(replyId);
  }

  @Test
  public void createReplyAndGetReplyTest() {

    // BigInteger replyId = replyDAO.createReply(resultId, BigInteger.valueOf(-911));
    Reply reply = replyDAO.getReply(replyId);
    assertTrue("да".equals(reply.getAnswer()));
    // Result result = resultDAO.createResult(date);
  }


  @Test
  public void updateReplyTest() {
    replyDAO.updateReply(replyId,BigInteger.valueOf(-912));
    Reply reply = replyDAO.getReply(replyId);
    assertTrue("нет".equals(reply.getAnswer()));

  }


}

