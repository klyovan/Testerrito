package com.netcracker.testerritto.models;

import java.math.BigInteger;
import java.util.HashMap;
import java.util.List;

public class Reply {

  private BigInteger id;
  private BigInteger resultId;
  private BigInteger answerId;
  private List<Answer> replyList;

 public Reply(BigInteger id, BigInteger resultId, BigInteger answerId) {
    this.id = id;
    this.resultId = resultId;
    this.answerId = answerId;
  }

  public Reply(){};

  public BigInteger getResultId() {
    return resultId;
  }

  public void setResultId(BigInteger resultId) {
    this.resultId = resultId;
  }

  public List<Answer> getReplyList() {
    return replyList;
  }

  public void setReplyList(List<Answer> replyList) {
    this.replyList = replyList;
  }

  public BigInteger getReplyId() {
    return id;
  }

  public void setId(BigInteger id) {
    this.id = id;
  }

  public BigInteger getAnswerId() {
    return answerId;
  }

  public void setAnswerId(BigInteger answerId) {
    this.answerId = answerId;
  }
}
