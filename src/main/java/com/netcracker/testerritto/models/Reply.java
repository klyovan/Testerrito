package com.netcracker.testerritto.models;

import java.math.BigInteger;
import java.util.List;

public class Reply  extends ObjectEav{

  private BigInteger id;
  private BigInteger resultId;
  private BigInteger answerId;
  private String answer;

  public String getAnswer() {
    return answer;
  }

  @Override
  public BigInteger getId() {
    return id;
  }

  @Override
  public void setId(BigInteger id) {
    this.id = id;
  }

  public void setAnswer(String answer) {
    this.answer = answer;
  }

  private List<Answer> replyList;

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



  public BigInteger getAnswerId() {
    return answerId;
  }

  public void setAnswerId(BigInteger answerId) {
    this.answerId = answerId;
  }
}