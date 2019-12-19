package com.netcracker.testerritto.models;

import java.math.BigInteger;
import java.util.List;
import java.util.Objects;

public class Reply extends ObjectEav {

    private BigInteger id;
    private BigInteger resultId;
    private BigInteger answerId;
    private String answer;
    private List<Answer> replyList;

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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Reply reply = (Reply) o;
        return id.equals(reply.id) &&
            resultId.equals(reply.resultId) &&
            answerId.equals(reply.answerId) &&
            answer.equals(reply.answer);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, resultId, answerId, answer);
    }
}