package com.netcracker.testerritto.models;

import java.util.Objects;

public class Category extends ObjectEav {
  private String nameCategory;

  public String getNameCategory() {
    return nameCategory;
  }

  public void setNameCategory(String nameCategory) {
    this.nameCategory = nameCategory;
  }

  @Override
  public String toString() {
    return "Category{" +
        "id=" + id +
        ", nameCategory='" + nameCategory + '\'' +
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
    Category category = (Category) o;
    return id.equals(category.id) &&
        nameCategory.equals(category.nameCategory);
  }

  @Override
  public int hashCode() {
    return Objects.hash(id, nameCategory);
  }
}
