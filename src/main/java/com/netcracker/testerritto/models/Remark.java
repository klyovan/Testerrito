package com.netcracker.testerritto.models;

import java.math.BigInteger;
import java.util.Objects;

public class Remark extends ObjectEav{

    private String text;
    private BigInteger userSenderId;
    private BigInteger userRecipientId;
    private BigInteger questionId;

    public void setText(String text) {
        this.text = text;
    }

    public String getText() {
        return text;
    }

    public BigInteger getUserSenderId() {
        return userSenderId;
    }

    public void setUserSenderId(BigInteger userSenderId) {
        this.userSenderId = userSenderId;
    }

    public BigInteger getQuestionId() {
        return questionId;
    }

    public void setQuestionId(BigInteger questionId) {
        this.questionId = questionId;
    }

    public BigInteger getUserRecipientId() {
        return userRecipientId;
    }

    public void setUserRecipientId(BigInteger userRecipientId) {
        this.userRecipientId = userRecipientId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Remark remark = (Remark) o;
        return id.equals(remark.id) &&
                text.equals(remark.text) &&
                userSenderId.equals(remark.userSenderId) &&
                questionId.equals(remark.questionId)&&
                userRecipientId.equals(remark.userRecipientId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, text, userSenderId, questionId, userRecipientId);
    }

    @Override
    public String toString() {
        return "Remark{" +
                "id=" + id +
                ", text='" + text + '\'' +
                ", userSenderId=" + userSenderId +
                ", userRecipientId=" + userRecipientId+
                ", questionId=" + questionId +
                '}';
    }
}

