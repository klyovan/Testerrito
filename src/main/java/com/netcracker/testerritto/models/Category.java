package com.netcracker.testerritto.models;

import java.util.Objects;

public class Category {
    private int id;
    private String nameCategory;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNameCategory() {
        return nameCategory;
    }

    public void setNameCategory(String nameCategory) {
        this.nameCategory = nameCategory;
    }

    public Category() {
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Category category = (Category) o;
        return id == category.id &&
                nameCategory.equals(category.nameCategory);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, nameCategory);
    }
}
