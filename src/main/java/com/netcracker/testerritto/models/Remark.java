package com.netcracker.testerritto.models;

import java.util.Objects;

public class Remark {

    private Integer id;
    private String text;
    private Integer userId;
    private Integer questionId;

    public void setId(Integer id) {
        this.id = id;
    }

    public void setText(String text) {
        this.text = text;
    }

    public Integer getId() {
        return id;
    }

    public String getText() {
        return text;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public Integer getQuestionId() {
        return questionId;
    }

    public void setQuestionId(Integer questionId) {
        this.questionId = questionId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Remark remark = (Remark) o;
        return id.equals(remark.id) &&
                text.equals(remark.text) &&
                userId.equals(remark.userId) &&
                questionId.equals(remark.questionId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, text, userId, questionId);
    }
}

