package com.netcracker.testerritto.dao;

import com.netcracker.testerritto.mappers.GradeCategoryMapper;
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
    String query =
        "select " +
            "grade_category.object_id grade_id, grade_category.name grade_name, grade_category.parent_id test_id, " +
            "grade_category_min_value.value min_value, grade_category_max_value.value max_value, grade_category_meaning.value meaning, " +
            "grade_category_ref_on_category.reference category_id " +
            "from " +
            "objects grade_category, " +
            "attributes grade_category_min_value, attributes grade_category_max_value, attributes grade_category_meaning, " +
            "objreference grade_category_ref_on_category " +
            "where " +
            "grade_category.parent_id = ? and " +
            "grade_category.object_type_id = 9 /*9 is object_type of \"grade_category\"*/and " +
            "grade_category_min_value.attr_id = 15 /*15 is attr_id of \"min_score\"*/and " +
            "grade_category_min_value.object_id = grade_category.object_id and " +
            "grade_category_max_value.attr_id = 16 /*16 is attr_id of \"max_score\"*/and " +
            "grade_category_max_value.object_id = grade_category.object_id and " +
            "grade_category_meaning.attr_id = 17 /*17 is attr_id of \"meaning\"*/and " +
            "grade_category_meaning.object_id = grade_category.object_id and " +
            "grade_category_ref_on_category.object_id = grade_category.object_id and " +
            "grade_category_ref_on_category.attr_id = 33/*33 is attr_id of \"grade_belongs\"*/";
    return jdbcTemplate.query(query, new Object[]{id}, gradeCategoryMapper);
  }

  public List<GradeCategory> getGradeCategoryByCategoryId(int id) {
    String query =
        "select " +
            "grade_category.object_id grade_id, grade_category.name grade_name, grade_category.parent_id test_id, " +
            "grade_category_min_value.value min_value, grade_category_max_value.value max_value, grade_category_meaning.value meaning, " +
            "grade_category_ref_on_category.reference category_id " +
            "from " +
            "objects grade_category, " +
            "attributes grade_category_min_value, attributes grade_category_max_value, attributes grade_category_meaning, " +
            "objreference grade_category_ref_on_category " +
            "where " +
            "grade_category_ref_on_category.reference = ? and " +
            "grade_category_ref_on_category.attr_id = 33 /*33 is attr_id of \"grade_belongs\"*/and " +
            "grade_category_ref_on_category.object_id = grade_category.object_id and " +
            "grade_category.object_type_id = 9 /*9 is object_type of \"grade_category\"*/and " +
            "grade_category_min_value.attr_id = 15 /*15 is attr_id of \"min_score\"*/and " +
            "grade_category_min_value.object_id = grade_category.object_id and " +
            "grade_category_max_value.attr_id = 16 /*16 is attr_id of \"max_score\"*/and " +
            "grade_category_max_value.object_id = grade_category.object_id and " +
            "grade_category_meaning.attr_id = 17 /*17 is attr_id of \"meaning\"*/and " +
            "grade_category_meaning.object_id = grade_category.object_id";
    return jdbcTemplate.query(query, new Object[]{id}, gradeCategoryMapper);
  }

  public GradeCategory getGradeCategoryById(int id) {
    String query =
        "select " +
            "grade_category.object_id grade_id, grade_category.parent_id test_id, " +
            "grade_category_min_value.value min_value, grade_category_max_value.value max_value, grade_category_meaning.value meaning, " +
            "grade_category_ref_on_category.reference category_id " +
            "from " +
            "objects grade_category, " +
            "attributes grade_category_min_value, attributes grade_category_max_value, attributes grade_category_meaning, " +
            "objreference grade_category_ref_on_category " +
            "where " +
            "grade_category.object_id = ? and " +
            "grade_category.object_type_id = 9 /*9 is object_type of \"grade_category\"*/and " +
            "grade_category_min_value.attr_id = 15 /*15 is attr_id of \"min_score\"*/and " +
            "grade_category_min_value.object_id = grade_category.object_id and " +
            "grade_category_max_value.attr_id = 16 /*16 is attr_id of \"max_score\"*/and " +
            "grade_category_max_value.object_id = grade_category.object_id and " +
            "grade_category_meaning.attr_id = 17 /*17 is attr_id of \"meaning\"*/and " +
            "grade_category_meaning.object_id = grade_category.object_id and " +
            "grade_category_ref_on_category.object_id = grade_category.object_id and " +
            "grade_category_ref_on_category.attr_id = 33/*33 is attr_id of \"grade_belongs\"*/";
    return jdbcTemplate.queryForObject(query, new Object[]{id}, gradeCategoryMapper);
  }

  public int createGradeCategory(GradeCategory newGradeCategory) {
    String queryInsert =
        "insert all " +
            "into " +
            "objects(object_id, parent_id, object_type_id, name) " +
            "values(object_id_pr.nextval, :test_id, 9, 'Grade ' || object_id_pr.currval) /*9 is object_type of \"grade_category\"*/" +
            "into " +
            "attributes(attr_id, object_id, value) " +
            "values(15, object_id_pr.currval, :min_value) /*15 is attr_id of \"min_score\"*/" +
            "into " +
            "attributes(attr_id, object_id, value) " +
            "values(16, object_id_pr.currval, :max_value) /*16 is attr_id of \"max_score\"*/" +
            "into " +
            "attributes(attr_id, object_id, value) " +
            "values(17, object_id_pr.currval, :meaning) /*17 is attr_id of \"meaning\"*/" +
            "into " +
            "objreference(attr_id, object_id, reference) " +
            "values(33, object_id_pr.currval, :category_id) /*33 is attr_id of \"grade_belongs\"*/" +
            "select * from dual";
    SqlParameterSource namedParameters = new MapSqlParameterSource().addValue("test_id",  newGradeCategory.getTestId())
        .addValue("min_value", newGradeCategory.getMinScore())
        .addValue("max_value", newGradeCategory.getMaxScore())
        .addValue("meaning", newGradeCategory.getMeaning())
        .addValue("category_id", newGradeCategory.getCategoryId());
    String queryRetrieveId =
        "select " +
            "object_id_pr.currval " +
            "from " +
            "dual";
    namedParameterJdbcTemplate.update(queryInsert, namedParameters);
    return jdbcTemplate.queryForObject(queryRetrieveId, Integer.class);
  }

  public void deleteGradeCategoryById(int id) {
    String query =
        "delete from " +
            "objects grade_category " +
            "where " +
            "grade_category.object_id = ? and " +
            "grade_category.object_type_id = 9 /*9 is object_type of \"grade_category\"*/";
    jdbcTemplate.update(query, new Object[]{id});
  }

  public void deleteGradeCategoryByTestId(int id) {
    String query =
        "delete from " +
            "objects grade_category " +
            "where " +
            "grade_category.parent_id = ? and " +
            "grade_category.object_type_id = 9 /*9 is object_type of \"grade_category\"*/";
    jdbcTemplate.update(query, new Object[]{id});
  }

  public GradeCategory updateGradeCategory(GradeCategory gradeCategory) {
    String queryUpdateMinValue =
        "update " +
            "attributes grade_category_min_value " +
            "set " +
            "grade_category_min_value.value = :min_value " +
            "where " +
            "grade_category_min_value.object_id = :grade_category_id and " +
            "grade_category_min_value.attr_id = 15 /*15 is attr_id of \"min_score\"*/";
    String queryUpdateMaxValue =
        "update " +
            "attributes grade_category_max_value " +
            "set " +
            "grade_category_max_value.value = :max_value " +
            "where " +
            "grade_category_max_value.object_id = :grade_category_id and " +
            "grade_category_max_value.attr_id = 16 /*16 is attr_id of \"max_score\"*/";
    String queryUpdateMeaningValue =
        "update " +
            "attributes grade_category_meaning_value " +
            "set " +
            "grade_category_meaning_value.value = :meaning " +
            "where " +
            "grade_category_meaning_value.object_id = :grade_category_id and " +
            "grade_category_meaning_value.attr_id = 17 /*17 is attr_id of \"meaning\"*/";
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
