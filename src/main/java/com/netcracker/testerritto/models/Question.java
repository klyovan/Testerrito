package com.netcracker.testerritto.models;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.netcracker.testerritto.properties.ListsAttr;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class Question extends ObjectEav {

    private String textQuestion;
    private ListsAttr typeQuestion;
    private BigInteger testId;
    @JsonDeserialize(as = ArrayList.class, contentAs = Answer.class)
    private List<Answer> answers;
    private BigInteger categoryId;

    public void setTextQuestion(String textQuestion) {
        this.textQuestion = textQuestion;
    }

    public String getTextQuestion() {
        return this.textQuestion;
    }

    public void setTypeQuestion(ListsAttr typeQuestion) {
        this.typeQuestion = typeQuestion;
    }

    public ListsAttr getTypeQuestion() {
        return this.typeQuestion;
    }

    public void setTestId(BigInteger testId) {
        this.testId = testId;
    }

    public BigInteger getTestId() {
        return this.testId;
    }

    public void setAnswers(List<Answer> answers) {
        this.answers = answers;
    }

    public List<Answer> getAnswers() {
        return this.answers;
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
        Question question = (Question) o;
        return id.equals(question.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, textQuestion, typeQuestion, testId, answers, categoryId);
    }
}
