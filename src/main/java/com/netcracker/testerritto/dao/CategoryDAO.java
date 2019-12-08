package com.netcracker.testerritto.dao;

import com.netcracker.testerritto.mappers.CategoryRowMapper;
import com.netcracker.testerritto.models.Category;
import com.netcracker.testerritto.properties.AttrtypeProperties;
import java.math.BigInteger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
@Transactional
public class CategoryDAO {
  @Autowired
  private JdbcTemplate jdbcTemplate;

  @Autowired
  private CategoryRowMapper categoryRowMapper;

  public Category getCategoryById(BigInteger id) {
    String query =
      "select " +
        "category.object_id category_id, " +
        "category_attr.value name_category " +
      "from " +
        "objects category, attributes category_attr " +
      "where " +
        "category.object_id = ? " +
        "and category.object_id = category_attr.object_id " +
        "and category_attr.attr_id = " + AttrtypeProperties.NAME_CATEGORY;
    return jdbcTemplate.queryForObject(query, new Object[]{id.toString()}, categoryRowMapper);
  }

  public void deleteCategoryById(BigInteger id) {
    String query =
      "delete from " +
        "objects category " +
      "where " +
        "category.object_id = ? " +
        "and category.object_type_id = " + AttrtypeProperties.TEXT;
    jdbcTemplate.update(query, new Object[]{id.toString()});
  }

  public Category updateCategory(Category category) {
    String queryUpdateAttributes =
      "update " +
        "attributes category " +
      "set " +
        "category.value = ? " +
      "where " +
        "category.object_id = ? " +
        "and category.attr_id = " + AttrtypeProperties.NAME_CATEGORY;
    jdbcTemplate.update(queryUpdateAttributes, new Object[]{category.getNameCategory(), (category.getId()).toString()});
    return getCategoryById(category.getId());
  }

  public BigInteger createCategory(Category newCategory) {
    String queryInsert =
      "insert all " +
        "into " +
          "objects(object_id, object_type_id, name) " +
          "values (object_id_pr.nextval, "+ AttrtypeProperties.TEXT +", 'Category ' || object_id_pr.currval) /* category */" +
        "into " +
          "attributes(attr_id, object_id, value) " +
          "values (" + AttrtypeProperties.NAME_CATEGORY + ", object_id_pr.currval, ?) /* name_category */" +
      "select * from dual";
    String queryRetrieveId = "select object_id_pr.currval from dual";
    jdbcTemplate.update(queryInsert, new Object[]{newCategory.getNameCategory()});
    return jdbcTemplate.queryForObject(queryRetrieveId, BigInteger.class);
  }
}
