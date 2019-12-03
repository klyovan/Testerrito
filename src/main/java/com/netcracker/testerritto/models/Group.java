package com.netcracker.testerritto.models;

import java.util.List;
import java.util.Objects;

public class Group {
    private Integer group_id;
    private String name;
    private String link;
    private Integer creatorUserId;
    private List<User> users;
    private List<Test> tests;

    public int getGroup_id() {
        return group_id;
    }

    public void setGroup_id(int group_id) {
        this.group_id = group_id;
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

    public int getCreatorUserId() {
        return creatorUserId;
    }

    public void setCreatorUserId(int creatorUserId) {
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
        return group_id.equals(group.group_id) &&
                name.equals(group.name) &&
                link.equals(group.link) &&
                creatorUserId.equals(group.creatorUserId) &&
                Objects.equals(users, group.users) &&
                Objects.equals(tests, group.tests);
    }

    @Override
    public int hashCode() {
        return Objects.hash(group_id, name, link, creatorUserId, users, tests);
    }
}
