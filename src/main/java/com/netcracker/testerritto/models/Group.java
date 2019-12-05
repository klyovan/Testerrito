package com.netcracker.testerritto.models;

import java.util.List;

public class Group {

  private int group_id;
  private String name;
  private String link;
  private int creatorUserId;
  private List<User> users;

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
}
