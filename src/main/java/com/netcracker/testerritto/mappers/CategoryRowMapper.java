package com.netcracker.testerritto.mappers;

import com.netcracker.testerritto.models.Category;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;

import java.sql.ResultSet;
import java.sql.SQLException;

@Component
public class CategoryRowMapper implements RowMapper<Category> {
  @Override
  public Category mapRow(ResultSet resultSet, int i) throws SQLException {
    Category category = new Category();
    category.setId(resultSet.getInt("CATEGORY_ID"));
    category.setNameCategory(resultSet.getString("NAME_CATEGORY"));
    return category;
  }
}
