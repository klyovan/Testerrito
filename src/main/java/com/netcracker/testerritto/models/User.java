package com.netcracker.testerritto.models;

import java.math.BigInteger;
import java.util.List;

public class User {

  private BigInteger id;
  private String lastName;
  private String firstName;
  private String email;
  private String password;
  private String phone;
  private List<Group> groups;
  private List<Result> results;
  private List<Group> createdGroups;

  public User() {
  }

  public User(BigInteger id, String lastName, String firstName, String email,
      String password, String phone) {
    this.id = id;
    this.lastName = lastName;
    this.firstName = firstName;
    this.email = email;
    this.password = password;
    this.phone = phone;
  }

  public BigInteger getId() {
    return id;
  }

  public void setId(BigInteger id) {
    this.id = id;
  }

  public String getLastName() {
    return lastName;
  }

  public void setLastName(String lastName) {
    this.lastName = lastName;
  }

  public String getFirstName() {
    return firstName;
  }

  public void setFirstName(String firstName) {
    this.firstName = firstName;
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
    return (id != null && id.compareTo(user.getId()) == 0)
        && (firstName != null && firstName.equals(user.getFirstName()))
        && (lastName != null && lastName.equals(user.getLastName()))
        && (email != null && email.equals(user.getEmail()))
        && (password != null && password.equals(user.getPassword()))
        && (phone != null && phone.equals(user.getPhone()));
  }
}
