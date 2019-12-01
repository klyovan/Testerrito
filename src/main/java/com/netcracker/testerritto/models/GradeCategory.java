package com.netcracker.testerritto.models;

import java.util.Comparator;
import java.util.Objects;

public class GradeCategory {
    private int id;
    private int minScore;
    private int maxScore;
    private String meaning;
    private int categoryId;
    private int testId;

    public GradeCategory() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

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

    public int getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(int categoryId) {
        this.categoryId = categoryId;
    }

    public int getTestId() {
        return testId;
    }

    public void setTestId(int testId) {
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
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        GradeCategory that = (GradeCategory) o;
        return id == that.id &&
                minScore == that.minScore &&
                maxScore == that.maxScore &&
                categoryId == that.categoryId &&
                testId == that.testId &&
                meaning.equals(that.meaning);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, minScore, maxScore, meaning, categoryId, testId);
    }
}
