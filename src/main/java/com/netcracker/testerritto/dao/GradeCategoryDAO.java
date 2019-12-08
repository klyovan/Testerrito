package com.netcracker.testerritto.dao;

import com.netcracker.testerritto.mappers.GradeCategoryMapper;
import com.netcracker.testerritto.models.GradeCategory;
import com.netcracker.testerritto.properties.AttrtypeProperties;
import com.netcracker.testerritto.properties.ObjtypeProperties;
import java.math.BigInteger;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
@Transactional
public class GradeCategoryDAO {
  @Autowired
  private JdbcTemplate jdbcTemplate;

  @Autowired
  private NamedParameterJdbcTemplate namedParameterJdbcTemplate;

  @Autowired
  private GradeCategoryMapper gradeCategoryMapper;

  public List<GradeCategory> getGradeCategoryByTestId(BigInteger id) {
    String getGradeCategoryByTestIdQuery =
      "select " +
      "  grade_category.object_id id, " +
      "  grade_category.name grade_name, " +
      "  grade_category.parent_id test_id, " +
      "  grade_category_min_value.value min_value, " +
      "  grade_category_max_value.value max_value, " +
      "  grade_category_meaning.value meaning, " +
      "  grade_category_ref_on_category.reference category_id " +
      "from " +
      "  objects grade_category, " +
      "  attributes grade_category_min_value, " +
      "  attributes grade_category_max_value, " +
      "  attributes grade_category_meaning, " +
      "  objreference grade_category_ref_on_category " +
      "where " +
      "  grade_category.parent_id = ? " +
      "  and grade_category.object_type_id = " + ObjtypeProperties.GRADE_CATEGORY +
      "  and grade_category_min_value.attr_id = " + AttrtypeProperties.MIN_SCORE +
      "  and grade_category_min_value.object_id = grade_category.object_id " +
      "  and grade_category_max_value.attr_id = " + AttrtypeProperties.MAX_SCORE +
      "  and grade_category_max_value.object_id = grade_category.object_id  " +
      "  and grade_category_meaning.attr_id = " + AttrtypeProperties.MEANING +
      "  and grade_category_meaning.object_id = grade_category.object_id " +
      "  and grade_category_ref_on_category.object_id = grade_category.object_id " +
      "  and grade_category_ref_on_category.attr_id = " + AttrtypeProperties.GRADE_BELONGS;
    return jdbcTemplate.query(getGradeCategoryByTestIdQuery, new Object[]{id.toString()}, gradeCategoryMapper);
  }

  public List<GradeCategory> getGradeCategoryByCategoryId(BigInteger id) {
    String getGradeCategoryByCategoryIdQuery =
      "select " +
      "  grade_category.object_id id, " +
      "  grade_category.name grade_name, " +
      "  grade_category.parent_id test_id, " +
      "  grade_category_min_value.value min_value, " +
      "  grade_category_max_value.value max_value, " +
      "  grade_category_meaning.value meaning, " +
      "  grade_category_ref_on_category.reference category_id " +
      "from " +
      "  objects grade_category, " +
      "  attributes grade_category_min_value, " +
      "  attributes grade_category_max_value, " +
      "  attributes grade_category_meaning, " +
      "  objreference grade_category_ref_on_category " +
      "where " +
      "  grade_category_ref_on_category.reference = ? " +
      "  and grade_category_ref_on_category.attr_id = " + AttrtypeProperties.GRADE_BELONGS +
      "  and grade_category_ref_on_category.object_id = grade_category.object_id " +
      "  and grade_category.object_type_id = " + ObjtypeProperties.GRADE_CATEGORY +
      "  and grade_category_min_value.attr_id = " + AttrtypeProperties.MIN_SCORE +
      "  and grade_category_min_value.object_id = grade_category.object_id " +
      "  and grade_category_max_value.attr_id = " + AttrtypeProperties.MAX_SCORE +
      "  and grade_category_max_value.object_id = grade_category.object_id " +
      "  and grade_category_meaning.attr_id = " + AttrtypeProperties.MEANING +
      "  and grade_category_meaning.object_id = grade_category.object_id";
    return jdbcTemplate.query(getGradeCategoryByCategoryIdQuery, new Object[]{id.toString()}, gradeCategoryMapper);
  }

  public GradeCategory getGradeCategoryById(BigInteger id) {
    String getGradeCategoryByIdQuery =
      "select " +
      "  grade_category.object_id id, " +
      "  grade_category.parent_id test_id, " +
      "  grade_category_min_value.value min_value, " +
      "  grade_category_max_value.value max_value, " +
      "  grade_category_meaning.value meaning, " +
      "  grade_category_ref_on_category.reference category_id " +
      "from " +
      "  objects grade_category, " +
      "  attributes grade_category_min_value, " +
      "  attributes grade_category_max_value, " +
      "  attributes grade_category_meaning, " +
      "  objreference grade_category_ref_on_category " +
      "where " +
      "  grade_category.object_id = ? " +
      "  and grade_category.object_type_id = " + ObjtypeProperties.GRADE_CATEGORY +
      "  and grade_category_min_value.attr_id = " + AttrtypeProperties.MIN_SCORE +
      "  and grade_category_min_value.object_id = grade_category.object_id  " +
      "  and grade_category_max_value.attr_id = " + AttrtypeProperties.MAX_SCORE +
      "  and grade_category_max_value.object_id = grade_category.object_id " +
      "  and grade_category_meaning.attr_id = " + AttrtypeProperties.MEANING +
      "  and grade_category_meaning.object_id = grade_category.object_id " +
      "  and grade_category_ref_on_category.object_id = grade_category.object_id " +
      "  and grade_category_ref_on_category.attr_id = " + AttrtypeProperties.GRADE_BELONGS;
    return jdbcTemplate.queryForObject(getGradeCategoryByIdQuery, new Object[]{id.toString()}, gradeCategoryMapper);
  }

  public BigInteger createGradeCategory(GradeCategory newGradeCategory) {
    BigInteger newGradeCategoryId = new ObjectEavBuilder.Builder(jdbcTemplate)
      .setName(newGradeCategory.getMeaning())
      .setObjectTypeId(new BigInteger(String.valueOf(ObjtypeProperties.GRADE_CATEGORY)))
      .setParentId(newGradeCategory.getTestId())
      .setStringAttribute(new BigInteger(String.valueOf(AttrtypeProperties.MIN_SCORE)), String.valueOf(newGradeCategory.getMinScore()))
      .setStringAttribute(new BigInteger(String.valueOf(AttrtypeProperties.MAX_SCORE)), String.valueOf(newGradeCategory.getMaxScore()))
      .setStringAttribute(new BigInteger(String.valueOf(AttrtypeProperties.MEANING)), String.valueOf(newGradeCategory.getMeaning()))
      .setReference(new BigInteger(String.valueOf(AttrtypeProperties.GRADE_BELONGS)), newGradeCategory.getCategoryId())
      .create();
    return newGradeCategoryId;
  }

  public void deleteGradeCategoryById(BigInteger id) {
    new ObjectEavBuilder.Builder(jdbcTemplate)
        .setObjectId(id)
        .delete();
  }

  public void deleteGradeCategoryByTestId(BigInteger id) {
    String query =
      "delete from " +
        "objects grade_category " +
      "where " +
        "grade_category.parent_id = ? " +
        "and grade_category.object_type_id = 9 /* grade_category */";
    jdbcTemplate.update(query, new Object[]{id.toString()});
  }

  public GradeCategory updateGradeCategory(GradeCategory gradeCategory) {
    String queryUpdateMinValue =
      "update " +
        "attributes grade_category_min_value " +
      "set " +
        "grade_category_min_value.value = :min_value " +
      "where " +
        "grade_category_min_value.object_id = :grade_category_id " +
        "and grade_category_min_value.attr_id = 15 /* min_score */";
    String queryUpdateMaxValue =
      "update " +
        "attributes grade_category_max_value " +
      "set " +
        "grade_category_max_value.value = :max_value " +
      "where " +
        "grade_category_max_value.object_id = :grade_category_id " +
        "and grade_category_max_value.attr_id = 16 /* max_score */";
    String queryUpdateMeaningValue =
      "update " +
        "attributes grade_category_meaning_value " +
      "set " +
        "grade_category_meaning_value.value = :meaning " +
      "where " +
        "grade_category_meaning_value.object_id = :grade_category_id " +
        "and grade_category_meaning_value.attr_id = 17 /* meaning */";
    SqlParameterSource namedParameters = new MapSqlParameterSource()
        .addValue("min_value", gradeCategory.getMinScore())
        .addValue("max_value", gradeCategory.getMaxScore())
        .addValue("meaning", gradeCategory.getMeaning())
        .addValue("grade_category_id", (gradeCategory.getId()).toString());
    namedParameterJdbcTemplate.update(queryUpdateMinValue, namedParameters);
    namedParameterJdbcTemplate.update(queryUpdateMaxValue, namedParameters);
    namedParameterJdbcTemplate.update(queryUpdateMeaningValue, namedParameters);
    return getGradeCategoryById(gradeCategory.getId());
  }
}
