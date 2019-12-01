package com.netcracker.testerritto.dao;

import com.netcracker.testerritto.mapper.GradeCategoryMapper;
import com.netcracker.testerritto.models.GradeCategory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository
@Transactional
public class GradeCategoryDAO {
    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Autowired
    private NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    @Autowired
    private GradeCategoryMapper gradeCategoryMapper;

    public List<GradeCategory> getGradeCategoryByTestId(int id) {
        String query = "SELECT grade_category.object_id grade_id, grade_category.name grade_name, grade_category.parent_id test_id, " +
                "grade_category_min_value.value min_value, grade_category_max_value.value max_value, grade_category_meaning.value meaning, " +
                "grade_category_ref_on_category.reference category_id " +
                "FROM objects grade_category, " +
                "attributes grade_category_min_value, attributes grade_category_max_value, attributes grade_category_meaning, " +
                "objreference grade_category_ref_on_category " +
                "WHERE grade_category.parent_id = ? AND " +
                "grade_category.object_type_id = 9 AND " +
                "grade_category_min_value.attr_id = 15 AND grade_category_min_value.object_id = grade_category.object_id AND " +
                "grade_category_max_value.attr_id = 16 AND grade_category_max_value.object_id = grade_category.object_id AND " +
                "grade_category_meaning.attr_id = 17 AND grade_category_meaning.object_id = grade_category.object_id AND " +
                "grade_category_ref_on_category.object_id = grade_category.object_id AND grade_category_ref_on_category.attr_id = 33";
        return jdbcTemplate.query(query, new Object[]{id}, gradeCategoryMapper);
    }

    public List<GradeCategory> getGradeCategoryByCategoryId(int id) {
        String query = "SELECT grade_category.object_id grade_id, grade_category.name grade_name, grade_category.parent_id test_id, " +
                "grade_category_min_value.value min_value, grade_category_max_value.value max_value, grade_category_meaning.value meaning, " +
                "grade_category_ref_on_category.reference category_id " +
                "FROM objects grade_category, " +
                "attributes grade_category_min_value, attributes grade_category_max_value, attributes grade_category_meaning, " +
                "objreference grade_category_ref_on_category " +
                "WHERE grade_category_ref_on_category.reference = ? AND grade_category_ref_on_category.attr_id = 33 AND " +
                "grade_category_ref_on_category.object_id = grade_category.object_id AND " +
                "grade_category.object_type_id = 9 AND " +
                "grade_category_min_value.attr_id = 15 AND grade_category_min_value.object_id = grade_category.object_id AND " +
                "grade_category_max_value.attr_id = 16 AND grade_category_max_value.object_id = grade_category.object_id AND " +
                "grade_category_meaning.attr_id = 17 AND grade_category_meaning.object_id = grade_category.object_id";
        return jdbcTemplate.query(query, new Object[]{id}, gradeCategoryMapper);
    }

    public GradeCategory getGradeCategoryById(int id) {
        String query = "SELECT grade_category.object_id grade_id, grade_category.parent_id test_id, " +
                "grade_category_min_value.value min_value, grade_category_max_value.value max_value, grade_category_meaning.value meaning, " +
                "grade_category_ref_on_category.reference category_id " +
                "FROM objects grade_category, " +
                "attributes grade_category_min_value, attributes grade_category_max_value, attributes grade_category_meaning, " +
                "objreference grade_category_ref_on_category " +
                "WHERE grade_category.object_id = ? AND " +
                "grade_category.object_type_id = 9 AND " +
                "grade_category_min_value.attr_id = 15 AND grade_category_min_value.object_id = grade_category.object_id AND " +
                "grade_category_max_value.attr_id = 16 AND grade_category_max_value.object_id = grade_category.object_id AND " +
                "grade_category_meaning.attr_id = 17 AND grade_category_meaning.object_id = grade_category.object_id AND " +
                "grade_category_ref_on_category.object_id = grade_category.object_id AND grade_category_ref_on_category.attr_id = 33";
        return jdbcTemplate.queryForObject(query, new Object[]{id}, gradeCategoryMapper);
    }

    public int createGradeCategory(GradeCategory newGradeCategory) {
        String queryInsert = "INSERT ALL " +
                "INTO objects(object_id, parent_id, object_type_id, name) VALUES(object_id_pr.nextval, :test_id, 9, 'Grade ' || object_id_pr.currval) " +
                "INTO attributes(attr_id, object_id, value) VALUES(15, object_id_pr.currval, :min_value) " +
                "INTO attributes(attr_id, object_id, value) VALUES(16, object_id_pr.currval, :max_value) " +
                "INTO attributes(attr_id, object_id, value) VALUES(17, object_id_pr.currval, :meaning) " +
                "INTO objreference(attr_id, object_id, reference) VALUES(33, object_id_pr.currval, :category_id) " +
                "SELECT * FROM dual";
        SqlParameterSource namedParameters = new MapSqlParameterSource().addValue("test_id",  newGradeCategory.getTestId())
                                                                        .addValue("min_value", newGradeCategory.getMinScore())
                                                                        .addValue("max_value", newGradeCategory.getMaxScore())
                                                                        .addValue("meaning", newGradeCategory.getMeaning())
                                                                        .addValue("category_id", newGradeCategory.getCategoryId());
        String queryRetrieveId = "SELECT object_id_pr.currval FROM dual";
        namedParameterJdbcTemplate.update(queryInsert, namedParameters);
        return jdbcTemplate.queryForObject(queryRetrieveId, Integer.class);
    }

    public void deleteGradeCategoryById(int id) {
        String query = "DELETE FROM objects grade_category " +
                "WHERE grade_category.object_id = ? AND grade_category.object_type_id = 9";
        jdbcTemplate.update(query, new Object[]{id});
    }

    public void deleteGradeCategoryByTestId(int id) {
        String query = "DELETE FROM objects grade_category " +
                "WHERE grade_category.parent_id = ? AND grade_category.object_type_id = 9";
        jdbcTemplate.update(query, new Object[]{id});
    }

    public GradeCategory updateGradeCategory(GradeCategory gradeCategory) {
        String queryUpdateMinValue = "UPDATE attributes grade_category_min_value " +
                "SET grade_category_min_value.value = :min_value " +
                "WHERE grade_category_min_value.object_id = :grade_category_id AND " +
                "grade_category_min_value.attr_id = 15";
        String queryUpdateMaxValue = "UPDATE attributes grade_category_max_value " +
                "SET grade_category_max_value.value = :max_value " +
                "WHERE grade_category_max_value.object_id = :grade_category_id AND " +
                "grade_category_max_value.attr_id = 16";
        String queryUpdateMeaningValue = "UPDATE attributes grade_category_meaning_value " +
                "SET grade_category_meaning_value.value = :meaning " +
                "WHERE grade_category_meaning_value.object_id = :grade_category_id AND " +
                "grade_category_meaning_value.attr_id = 17";
        SqlParameterSource namedParameters = new MapSqlParameterSource().addValue("min_value", gradeCategory.getMinScore())
                                                                        .addValue("max_value", gradeCategory.getMaxScore())
                                                                        .addValue("meaning", gradeCategory.getMeaning())
                                                                        .addValue("grade_category_id", gradeCategory.getId());
        namedParameterJdbcTemplate.update(queryUpdateMinValue, namedParameters);
        namedParameterJdbcTemplate.update(queryUpdateMaxValue, namedParameters);
        namedParameterJdbcTemplate.update(queryUpdateMeaningValue, namedParameters);
        return getGradeCategoryById(gradeCategory.getId());
    }
}
