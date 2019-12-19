package com.netcracker.testerritto.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.netcracker.testerritto.properties.ListsAttr;

import java.math.BigInteger;
import java.util.Date;
import java.util.HashMap;
import java.util.Objects;

public class Result extends ObjectEav {

  private Date date;
  private int score;
  private ListsAttr status;
  private BigInteger testId;
  @JsonIgnore
  private HashMap<Reply, Question> replies;
  private BigInteger userId;

  public Result() {
  }

  public Result(Date date, int score, ListsAttr status, BigInteger testId, HashMap<Reply, Question> replies, BigInteger userId) {
    this.date = date;
    this.score = score;
    this.status = status;
    this.testId = testId;
    this.replies = replies;
    this.userId = userId;
  }

  public Date getDate() {
    return date;
  }

  public void setDate(Date date) {
    this.date = date;
  }

  public int getScore() {
    return score;
  }

  public void setScore(int score) {
    this.score = score;
  }


  public ListsAttr getStatus() {
    return status;
  }

  public void setStatus(ListsAttr status) {
    this.status = status;
  }

  public BigInteger getTestId() {
    return testId;
  }

  public void setTestId(BigInteger testId) {
    this.testId = testId;
  }

  public HashMap<Reply, Question> getReplies() {
    return replies;
  }

  public void setReplies(HashMap<Reply, Question> replies) {
    this.replies = replies;
  }

  public BigInteger getUserId() {
    return userId;
  }

  public void setUserId(BigInteger userId) {
    this.userId = userId;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;
    Result result = (Result) o;
    return id.equals(result.id) &&
        date.equals(result.date) &&
        status.equals(result.status) &&
        testId.equals(result.testId) &&
        Objects.equals(replies, result.replies) &&
        userId.equals(result.userId);
  }

  @Override
  public int hashCode() {
    return Objects.hash(id, date, status, testId, replies, userId);
  }

  @Override
  public String toString() {
    return "Result{" +
        "id=" + id +
        ", date=" + date +
        ", status=" + status +
        ", testId=" + testId +
        ", replies=" + replies +
        ", userId=" + userId +
        '}';
  }
}
