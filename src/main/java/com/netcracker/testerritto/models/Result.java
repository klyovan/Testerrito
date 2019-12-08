package com.netcracker.testerritto.models;

import java.math.BigInteger;
import java.util.Date;
import java.util.List;

public class Result {

  private BigInteger id;
  private BigInteger testId;
  private Date date;
  private int score;
  private String status;
  private BigInteger userId;


  public Result(BigInteger id, BigInteger testId, Date date, int score, String status,
      BigInteger userId) {
    this.id = id;
    this.testId = testId;
    this.date = date;
    this.score = score;
    this.status = status;
    this.userId = userId;
  }

  public Result(){}

  public BigInteger getId() {
    return id;
  }

  public void setId(BigInteger id) {
    this.id = id;
  }

  public BigInteger getTestId() {
    return testId;
  }

  public void setTestId(BigInteger testId) {
    this.testId = testId;
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

  public String getStatus() {
    return status;
  }

  public void setStatus(String status) {
    this.status = status;
  }

  public BigInteger getUserId() {
    return userId;
  }

  public void setUserId(BigInteger userId) {
    this.userId = userId;
  }
}
