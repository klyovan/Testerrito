package com.netcracker.testerritto.models;

import java.math.BigInteger;
import java.util.List;
import java.util.Objects;

public class Group {
    private BigInteger id;
    private String name;
    private String link;
    private BigInteger creatorUserId;
    private List<User> users;
    private List<Test> tests;

    public Group(){}

    public Group(BigInteger id, BigInteger creatorUserId, String name, String link, List<User> users, List<Test> tests){
        this.id = id;
        this.creatorUserId = creatorUserId;
        this.name = name;
        this.link = link;
        this.users = users;
        this.tests = tests;
    }

    public BigInteger getId() {
        return id;
    }

    public void setId(BigInteger id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }

    public BigInteger getCreatorUserId() {
        return creatorUserId;
    }

    public void setCreatorUserId(BigInteger creatorUserId) {
        this.creatorUserId = creatorUserId;
    }

    public List<User> getUsers() {
        return users;
    }

    public void setUsers(List<User> users) {
        this.users = users;
    }

    public List<Test> getTests() {
        return tests;
    }

    public void setTests(List<Test> tests) {
        this.tests = tests;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Group group = (Group) o;
        return id.equals(group.id) &&
                name.equals(group.name) &&
                link.equals(group.link) &&
                creatorUserId.equals(group.creatorUserId) &&
                Objects.equals(users, group.users) &&
                Objects.equals(tests, group.tests);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, link, creatorUserId, users, tests);
    }

    @Override
    public String toString() {
        return "Group{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", link='" + link + '\'' +
                ", creatorUserId=" + creatorUserId +
                ", users=" + users +
                ", tests=" + tests +
                '}';
    }
}
