package com.netcracker.testerritto.dao;

import com.netcracker.testerritto.mappers.CategoryRowMapper;
import com.netcracker.testerritto.models.Category;
import com.netcracker.testerritto.properties.AttrtypeProperties;
import com.netcracker.testerritto.properties.ObjtypeProperties;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigInteger;

@Repository
@Transactional
public class CategoryDAO {
  @Autowired
  private JdbcTemplate jdbcTemplate;
  @Autowired
  private CategoryRowMapper categoryRowMapper;
  private static final String getCategoryByIdQuery =
    "select " +
      "  category.object_id id, " +
      "  category_attr.value name_category " +
      "from " +
      "  objects category, attributes category_attr " +
      "where " +
      "  category.object_id = ? " +
      "  and category.object_id = category_attr.object_id " +
      "  and category_attr.attr_id = 14 /* name_category */";

  public Category getCategoryById(BigInteger id) throws DataAccessException {
    return jdbcTemplate.queryForObject(getCategoryByIdQuery, new Object[]{id.toString()}, categoryRowMapper);
  }

  public void deleteCategoryById(BigInteger id) throws DataAccessException {
    new ObjectEavBuilder.Builder(jdbcTemplate)
      .setObjectId(id)
      .delete();
  }

  public Category updateCategory(Category category) throws DataAccessException {
    new ObjectEavBuilder.Builder(jdbcTemplate)
      .setObjectId(category.getId())
      .setStringAttribute(AttrtypeProperties.NAME_CATEGORY, category.getNameCategory())
      .update();
    return getCategoryById(category.getId());
  }

  public BigInteger createCategory(Category newCategory) throws DataAccessException {
    return new ObjectEavBuilder.Builder(jdbcTemplate)
      .setName(newCategory.getNameCategory())
      .setObjectTypeId(ObjtypeProperties.CATEGORY)
      .setStringAttribute(AttrtypeProperties.NAME_CATEGORY, newCategory.getNameCategory())
      .create();
  }
}
