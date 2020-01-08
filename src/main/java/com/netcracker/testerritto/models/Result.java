package com.netcracker.testerritto.models;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

import com.netcracker.testerritto.properties.Status;
import com.netcracker.testerritto.serializers.ResultMapContentDeserializer;
import com.netcracker.testerritto.serializers.ResultMapContentSerializer;
import com.netcracker.testerritto.serializers.ResultMapKeyDeserializer;
import com.netcracker.testerritto.serializers.ResultMapKeySerializer;

import java.math.BigInteger;
import java.util.Date;
import java.util.Map;
import java.util.Objects;

public class Result extends ObjectEav {

    private Date date;
    private int score;
    private Status status;
    private BigInteger testId;
    @JsonSerialize(keyUsing = ResultMapKeySerializer.class, contentUsing = ResultMapContentSerializer.class)
    @JsonDeserialize(keyUsing = ResultMapKeyDeserializer.class, contentUsing = ResultMapContentDeserializer.class)
    private Map<Question, Reply> replies;
    private BigInteger userId;
    private BigInteger categoryId;

    public Result() {
    }

    public Result(Date date, int score, Status status, BigInteger testId, Map<Question, Reply> replies, BigInteger userId) {
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


    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public BigInteger getTestId() {
        return testId;
    }

    public void setTestId(BigInteger testId) {
        this.testId = testId;
    }

    public Map<Question, Reply> getReplies() {
        return replies;
    }

    public void setReplies(Map<Question, Reply> replies) {
        this.replies = replies;
    }

    public BigInteger getUserId() {
        return userId;
    }

    public void setUserId(BigInteger userId) {
        this.userId = userId;
    }

    public BigInteger getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(BigInteger categoryId) {
        this.categoryId = categoryId;
    }

    @Override
    public boolean equals(Object o) {
      if (this == o) {
        return true;
      }
      if (o == null || getClass() != o.getClass()) {
        return false;
      }
        Result result = (Result) o;
        return id.equals(result.id) &&
            date.equals(result.date) &&
            status.equals(result.status) &&
            testId.equals(result.testId) &&
            Objects.equals(replies, result.replies) &&
            userId.equals(result.userId) &&
            categoryId.equals(result.categoryId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, date, status, testId, replies, userId, categoryId);
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
            ", categoryId ="+ categoryId +
            '}';
    }
}
