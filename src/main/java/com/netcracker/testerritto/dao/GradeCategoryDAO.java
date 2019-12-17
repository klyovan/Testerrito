package com.netcracker.testerritto.dao;

import com.netcracker.testerritto.mappers.GradeCategoryMapper;
import com.netcracker.testerritto.models.GradeCategory;
import com.netcracker.testerritto.properties.AttrtypeProperties;
import com.netcracker.testerritto.properties.ObjtypeProperties;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigInteger;
import java.util.List;

@Repository
@Transactional
public class GradeCategoryDAO {
  @Autowired
  private JdbcTemplate jdbcTemplate;
  @Autowired
  private GradeCategoryMapper gradeCategoryMapper;
  private static final String getGradeCategoryByTestIdQuery =
    "select " +
      "  grade_category.object_id id, " +
      "  grade_category.name grade_name, " +
      "  grade_category.parent_id test_id, " +
      "  min_value.value min_value, " +
      "  max_value.value max_value, " +
      "  meaning.value meaning, " +
      "  ref_on_category.reference category_id " +
      "from " +
      "  objects grade_category, " +
      "  attributes min_value, " +
      "  attributes max_value, " +
      "  attributes meaning, " +
      "  objreference ref_on_category " +
      "where " +
      "  grade_category.parent_id = ? " +
      "  and grade_category.object_type_id = 9 /* grade_category */ " +
      "  and min_value.attr_id = 15 /* min_score */ " +
      "  and min_value.object_id = grade_category.object_id " +
      "  and max_value.attr_id = 16 /* max_score */ " +
      "  and max_value.object_id = grade_category.object_id  " +
      "  and meaning.attr_id = 17 /* meaning */ " +
      "  and meaning.object_id = grade_category.object_id " +
      "  and ref_on_category.object_id = grade_category.object_id " +
      "  and ref_on_category.attr_id = 33/* grade_belongs */";
  private static final String getGradeCategoryByCategoryIdQuery =
      "select " +
      "  grade_category.object_id id, " +
      "  grade_category.name grade_name, " +
      "  grade_category.parent_id test_id, " +
      "  min_value.value min_value, " +
      "  max_value.value max_value, " +
      "  meaning.value meaning, " +
      "  ref_on_category.reference category_id " +
      "from " +
      "  objects grade_category, " +
      "  attributes min_value, " +
      "  attributes max_value, " +
      "  attributes meaning, " +
      "  objreference ref_on_category " +
      "where " +
      "  ref_on_category.reference = ? " +
      "  and ref_on_category.attr_id = 33 /* grade_belongs */ " +
      "  and ref_on_category.object_id = grade_category.object_id " +
      "  and grade_category.object_type_id = 9 /* grade_category */ " +
      "  and min_value.attr_id = 15 /* min_score */ " +
      "  and min_value.object_id = grade_category.object_id " +
      "  and max_value.attr_id = 16 /* max_score */ " +
      "  and max_value.object_id = grade_category.object_id " +
      "  and meaning.attr_id = 17 /* meaning */ " +
      "  and meaning.object_id = grade_category.object_id";
  private static final String getGradeCategoryByIdQuery =
    "select " +
      "  grade_category.object_id id, " +
      "  grade_category.parent_id test_id, " +
      "  min_value.value min_value, " +
      "  max_value.value max_value, " +
      "  meaning.value meaning, " +
      "  ref_on_category.reference category_id " +
      "from " +
      "  objects grade_category, " +
      "  attributes min_value, " +
      "  attributes max_value, " +
      "  attributes meaning, " +
      "  objreference ref_on_category " +
      "where " +
      "  grade_category.object_id = ? " +
      "  and grade_category.object_type_id = 9 /* grade_category */ " +
      "  and min_value.attr_id = 15 /* min_score */ " +
      "  and min_value.object_id = grade_category.object_id " +
      "  and max_value.attr_id = 16 /* max_score */ " +
      "  and max_value.object_id = grade_category.object_id  " +
      "  and meaning.attr_id = 17 /* meaning */ " +
      "  and meaning.object_id = grade_category.object_id " +
      "  and ref_on_category.object_id = grade_category.object_id " +
      "  and ref_on_category.attr_id = 33/* grade_belongs */";

  public List<GradeCategory> getGradeCategoryByTestId(BigInteger id) {
    return jdbcTemplate
      .query(getGradeCategoryByTestIdQuery, new Object[]{id.toString()}, gradeCategoryMapper);
  }

  public List<GradeCategory> getGradeCategoryByCategoryId(BigInteger id) {
    return jdbcTemplate
      .query(getGradeCategoryByCategoryIdQuery, new Object[]{id.toString()}, gradeCategoryMapper);
  }

  public GradeCategory getGradeCategoryById(BigInteger id) {
    return jdbcTemplate.queryForObject(getGradeCategoryByIdQuery, new Object[]{id.toString()},
      gradeCategoryMapper);
  }

  public BigInteger createGradeCategory(GradeCategory newGradeCategory) {
    return new ObjectEavBuilder.Builder(jdbcTemplate)
      .setName(newGradeCategory.getMeaning())
      .setObjectTypeId(ObjtypeProperties.GRADE_CATEGORY)
      .setParentId(newGradeCategory.getTestId())
      .setStringAttribute(AttrtypeProperties.MIN_SCORE,
        String.valueOf(newGradeCategory.getMinScore()))
      .setStringAttribute(AttrtypeProperties.MAX_SCORE,
        String.valueOf(newGradeCategory.getMaxScore()))
      .setStringAttribute(AttrtypeProperties.MEANING,
        String.valueOf(newGradeCategory.getMeaning()))
      .setReference(new BigInteger(String.valueOf(AttrtypeProperties.GRADE_BELONGS)),
        newGradeCategory.getCategoryId())
      .create();
  }

  public void deleteGradeCategoryById(BigInteger id) {
    new ObjectEavBuilder.Builder(jdbcTemplate)
      .setObjectId(id)
      .delete();
  }

  public void deleteGradeCategoryByTestId(BigInteger id) {
    new ObjectEavBuilder.Builder(jdbcTemplate)
      .setParentId(id)
      .setObjectTypeId(ObjtypeProperties.GRADE_CATEGORY)
      .delete();
  }

  public GradeCategory updateGradeCategory(GradeCategory gradeCategory) {
    new ObjectEavBuilder.Builder(jdbcTemplate)
      .setObjectId(gradeCategory.getId())
      .setStringAttribute(AttrtypeProperties.MIN_SCORE,
        String.valueOf(gradeCategory.getMinScore()))
      .setStringAttribute(AttrtypeProperties.MAX_SCORE,
        String.valueOf(gradeCategory.getMaxScore()))
      .setStringAttribute(AttrtypeProperties.MEANING, gradeCategory.getMeaning())
      .update();
    return getGradeCategoryById(gradeCategory.getId());
  }
}
