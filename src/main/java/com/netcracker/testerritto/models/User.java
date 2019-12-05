package com.netcracker.testerritto.models;

import java.util.List;

public class User {

  private int id;
  private String last_name;
  private String first_name;
  private String email;
  private String password;
  private String phone;
  private List<Group> groups;
  private List<Result> results;
  private List<Group> createdGroups;

  public User() {
  }

  public User(int id, String last_name, String first_name, String email,
      String password, String phone) {
    this.id = id;
    this.last_name = last_name;
    this.first_name = first_name;
    this.email = email;
    this.password = password;
    this.phone = phone;
  }

  public int getId() {
    return id;
  }

  public void setId(int id) {
    this.id = id;
  }

  public String getLast_name() {
    return last_name;
  }

  public void setLast_name(String last_name) {
    this.last_name = last_name;
  }

  public String getFirst_name() {
    return first_name;
  }

  public void setFirst_name(String first_name) {
    this.first_name = first_name;
  }

  public String getEmail() {
    return email;
  }

  public void setEmail(String email) {
    this.email = email;
  }

  public String getPassword() {
    return password;
  }

  public void setPassword(String password) {
    this.password = password;
  }

  public String getPhone() {
    return phone;
  }

  public void setPhone(String phone) {
    this.phone = phone;
  }


  public List<Group> getGroups() {
    return groups;
  }

  public void setGroups(List<Group> groups) {
    this.groups = groups;
  }

  public List<Result> getResults() {
    return results;
  }

  public void setResults(List<Result> results) {
    this.results = results;
  }

  public List<Group> getCreatedGroups() {
    return createdGroups;
  }

  public void setCreatedGroups(List<Group> createdGroups) {
    this.createdGroups = createdGroups;
  }


  @Override
  public boolean equals(Object obj) {
    if (obj == this) {
      return true;
    }
    if (obj == null || obj.getClass() != this.getClass()) {
      return false;
    }

    User user = (User) obj;
    return id == user.id
        && (first_name == user.first_name
        || (first_name != null && first_name.equals(user.getFirst_name())))
        && (last_name == user.last_name
        || (last_name != null && last_name.equals(user.getLast_name())))
        && (email == user.email
        || (email != null && email.equals(user.getEmail())))
        && (password == user.password
        || (password != null && password.equals(user.getPassword())))
        && (phone == user.phone
        || (phone != null && phone.equals(user.getPhone())));
  }
}
