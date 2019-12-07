package com.netcracker.testerritto.mappers;

import com.netcracker.testerritto.models.GradeCategory;
import java.math.BigInteger;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;

import java.sql.ResultSet;
import java.sql.SQLException;

@Component
public class GradeCategoryMapper implements RowMapper<GradeCategory> {
  @Override
  public GradeCategory mapRow(ResultSet resultSet, int i) throws SQLException {
    GradeCategory gradeCategory = new GradeCategory();
    gradeCategory.setId(BigInteger.valueOf(resultSet.getInt("grade_id")));
    gradeCategory.setTestId(BigInteger.valueOf(resultSet.getInt("test_id")));
    gradeCategory.setMinScore(resultSet.getInt("min_value"));
    gradeCategory.setMaxScore(resultSet.getInt("max_value"));
    gradeCategory.setMeaning(resultSet.getString("meaning"));
    gradeCategory.setCategoryId(BigInteger.valueOf(resultSet.getInt("category_id")));
    return gradeCategory;
  }
}
