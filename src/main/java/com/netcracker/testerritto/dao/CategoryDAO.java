package com.netcracker.testerritto.dao;

import com.netcracker.testerritto.mappers.CategoryRowMapper;
import com.netcracker.testerritto.models.Category;
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

  public Category getCategoryById(int id) {
    String query =
      "select " +
        "category.object_id category_id, " +
        "category_attr.value name_category " +
      "from " +
        "objects category, attributes category_attr " +
      "where " +
        "category.object_id = ? " +
        "and category.object_id = category_attr.object_id " +
        "and category_attr.attr_id = 14 /* name_category */";
    return jdbcTemplate.queryForObject(query, new Object[]{id}, categoryRowMapper);
  }

  public void deleteCategoryById(int id) {
    String query =
      "delete from " +
        "objects category " +
      "where " +
        "category.object_id = ? " +
        "and category.object_type_id = 8 /* category */";
    jdbcTemplate.update(query, new Object[]{id});
  }

  public Category updateCategory(Category category) {
    String queryUpdateAttributes =
      "update " +
        "attributes category " +
      "set " +
        "category.value = ? " +
      "where " +
        "category.object_id = ? " +
        "and category.attr_id = 14 /* name_category */";
    jdbcTemplate.update(queryUpdateAttributes, new Object[]{category.getNameCategory(), category.getId()});
    return getCategoryById(category.getId());
  }

  public int createCategory(Category newCategory) {
    String queryInsert =
      "insert all " +
        "into " +
          "objects(object_id, object_type_id, name) " +
          "values (object_id_pr.nextval, 8 , 'Category ' || object_id_pr.currval) /* category */" +
        "into " +
          "attributes(attr_id, object_id, value) " +
          "values (14, object_id_pr.currval, ?) /* name_category */" +
      "select * from dual";
    String queryRetrieveId = "select object_id_pr.currval from dual";
    jdbcTemplate.update(queryInsert, new Object[]{newCategory.getNameCategory()});
    return jdbcTemplate.queryForObject(queryRetrieveId, Integer.class);
  }
}
