package com.netcracker.testerritto.mapper;

import com.netcracker.testerritto.models.GradeCategory;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;

import java.sql.ResultSet;
import java.sql.SQLException;

@Component
public class GradeCategoryMapper implements RowMapper<GradeCategory> {
    @Override
    public GradeCategory mapRow(ResultSet resultSet, int i) throws SQLException {
        GradeCategory gradeCategory = new GradeCategory();
        gradeCategory.setId(resultSet.getInt("GRADE_ID"));
        gradeCategory.setTestId(resultSet.getInt("TEST_ID"));
        gradeCategory.setMinScore(resultSet.getInt("MIN_VALUE"));
        gradeCategory.setMaxScore(resultSet.getInt("MAX_VALUE"));
        gradeCategory.setMeaning(resultSet.getString("MEANING"));
        gradeCategory.setCategoryId(resultSet.getInt("CATEGORY_ID"));
        return gradeCategory;
    }
}
