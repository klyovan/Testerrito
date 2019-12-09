package com.netcracker.testerritto.models;

import java.math.BigInteger;
import java.util.List;
import java.util.Objects;

public class Question {
    private BigInteger id;
    private String textQuestion;
    private String typeQuestion;
    private BigInteger testId;
    private List<Answer> answers;

    public Question() {
    }

    public Question(BigInteger id, String textQuestion, String typeQuestion, BigInteger testId, List<Answer> answers) {
        this.id = id;
        this.textQuestion = textQuestion;
        this.typeQuestion = typeQuestion;
        this.testId = testId;
        this.answers = answers;
    }

    public void setId(BigInteger id) {
        this.id = id;
    }

    public BigInteger getId() {
        return this.id;
    }

    public void setTextQuestion(String textQuestion) {
        this.textQuestion = textQuestion;
    }

    public String getTextQuestion() {
        return this.textQuestion;
    }

    public void setTypeQuestion(String typeQuestion) {
        this.typeQuestion = typeQuestion;
    }

    public String getTypeQuestion() {
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Question question = (Question) o;
        return id.equals(question.id) &&
            textQuestion.equals(question.textQuestion) &&
            typeQuestion.equals(question.typeQuestion) &&
            testId.equals(question.testId) &&
            Objects.equals(answers, question.answers);
    }


    @Override
    public int hashCode() {
        return Objects.hash(id, textQuestion, typeQuestion, testId, answers);
    }

}
