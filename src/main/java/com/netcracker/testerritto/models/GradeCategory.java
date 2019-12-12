package com.netcracker.testerritto.models;

import java.math.BigInteger;
import java.util.Objects;

public class GradeCategory extends ObjectEav {
    private int minScore;
    private int maxScore;
    private String meaning;
    private BigInteger categoryId;
    private BigInteger testId;

    public int getMinScore() {
        return minScore;
    }

    public void setMinScore(int minScore) {
        this.minScore = minScore;
    }

    public int getMaxScore() {
        return maxScore;
    }

    public void setMaxScore(int maxScore) {
        this.maxScore = maxScore;
    }

    public String getMeaning() {
        return meaning;
    }

    public void setMeaning(String meaning) {
        this.meaning = meaning;
    }

    public BigInteger getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(BigInteger categoryId) {
        this.categoryId = categoryId;
    }

    public BigInteger getTestId() {
        return testId;
    }

    public void setTestId(BigInteger testId) {
        this.testId = testId;
    }

    @Override
    public String toString() {
        return "GradeCategory{" +
            "id=" + id +
            ", minScore=" + minScore +
            ", maxScore=" + maxScore +
            ", meaning='" + meaning + '\'' +
            ", categoryId=" + categoryId +
            ", testId=" + testId +
            '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        GradeCategory that = (GradeCategory) o;
        return minScore == that.minScore &&
            maxScore == that.maxScore &&
            id.equals(that.id) &&
            meaning.equals(that.meaning) &&
            categoryId.equals(that.categoryId) &&
            testId.equals(that.testId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, minScore, maxScore, meaning, categoryId, testId);
    }
}
