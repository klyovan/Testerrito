package com.netcracker.testerritto.models;

public class Test {
    protected int id = 0;
    protected String testName = null;
    protected int testCreator = 0;
    protected int testExpert = 0;

    public Test() {
    }

    public Test(int id, String testName, int testCreator, int testExpert) {
        this.id = id;
        this.testName = testName;
        this.testCreator = testCreator;
        this.testExpert = testExpert;
    }


    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTestName() {
        return testName;
    }

    public void setTestName(String testName) {
        this.testName = testName;
    }

    public int getTestCreator() {
        return testCreator;
    }

    public void setTestCreator(int testCreator) {
        this.testCreator = testCreator;
    }

    public int getTestExpert() {
        return testExpert;
    }

    public void setTestExpert(int testExpert) {
        this.testExpert = testExpert;
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
        return id == test.id && testName.equals(test.getTestName());

    }

    @Override
    public String toString() {
        return "Test{" +
                "id=" + id +
                ", testName='" + testName + '\'' +
                '}';
    }
}
