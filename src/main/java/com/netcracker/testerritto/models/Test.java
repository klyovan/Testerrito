package com.netcracker.testerritto.models;

import java.math.BigInteger;
import java.util.List;
import java.util.Objects;

public class Test {
    private BigInteger id;
    private BigInteger groupId;
    private String nameTest;
    private BigInteger creatorUserId;
    private List<GradeCategory> gradesCategory;
    private List<User> experts;
    private List<Question> questions;

    public Test() {
    }

    public Test(BigInteger id, BigInteger groupId, String nameTest, BigInteger creatorUserId, List<GradeCategory> gradesCategory, List<User> experts, List<Question> questions) {
        this.id = id;
        this.groupId = groupId;
        this.nameTest = nameTest;
        this.creatorUserId = creatorUserId;
        this.gradesCategory = gradesCategory;
        this.experts = experts;
        this.questions = questions;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == this) {
            return true;
        }
        if (obj == null || obj.getClass() != this.getClass()) {
            return false;
        }

        Test test = (Test) obj;
        return id.equals(test.id) &&
            groupId.equals( test.groupId) &&
            nameTest.equals(test.nameTest) &&
            creatorUserId.equals(test.creatorUserId) &&
            Objects.equals(experts, test.experts) &&
            Objects.equals(questions, test.questions) &&
            Objects.equals(gradesCategory, test.gradesCategory);

    }

    @Override
    public int hashCode() {
        return Objects.hash(id, groupId, nameTest, creatorUserId, experts, questions, gradesCategory);
    }

    public BigInteger getId() {
        return id;
    }

    public void setId(BigInteger id) {
        this.id = id;
    }

    public BigInteger getGroupId() {
        return groupId;
    }

    public void setGroupId(BigInteger groupId) {
        this.groupId = groupId;
    }

    public String getNameTest() {
        return nameTest;
    }

    public void setNameTest(String nameTest) {
        this.nameTest = nameTest;
    }

    public BigInteger getCreatorUserId() {
        return creatorUserId;
    }

    public void setCreatorUserId(BigInteger creatorUserId) {
        this.creatorUserId = creatorUserId;
    }

    public List<GradeCategory> getGradesCategory() {
        return gradesCategory;
    }

    public void setGradesCategory(List<GradeCategory> gradesCategory) {
        this.gradesCategory = gradesCategory;
    }

    public List<User> getExperts() {
        return experts;
    }

    public void setExperts(List<User> experts) {
        this.experts = experts;
    }

    public List<Question> getQuestions() {
        return questions;
    }

    public void setQuestions(List<Question> questions) {
        this.questions = questions;
    }

    @Override
    public String toString() {
        return "Test{" +
            "id=" + id +
            ", groupId=" + groupId +
            ", nameTest='" + nameTest + '\'' +
            ", creatorUserId=" + creatorUserId +
            ", gradesCategory=" + gradesCategory +
            ", experts=" + experts +
            ", questions=" + questions +
            '}';
    }
}
