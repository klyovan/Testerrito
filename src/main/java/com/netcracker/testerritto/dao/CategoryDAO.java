package com.netcracker.testerritto.dao;

import com.netcracker.testerritto.mappers.CategoryRowMapper;
import com.netcracker.testerritto.models.Category;
import com.netcracker.testerritto.properties.AttrtypeProperties;
import com.netcracker.testerritto.properties.ObjtypeProperties;
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
    String getCategoryByIdQuery =
      "select " +
      "  category.object_id id, " +
      "  category_attr.value name_category " +
      "from " +
      "  objects category, attributes category_attr " +
      "where " +
      "  category.object_id = ? " +
      "  and category.object_id = category_attr.object_id " +
      "  and category_attr.attr_id = " + AttrtypeProperties.NAME_CATEGORY;
    return jdbcTemplate.queryForObject(getCategoryByIdQuery, new Object[]{id.toString()}, categoryRowMapper);
  }

  public void deleteCategoryById(BigInteger id) {
    new ObjectEavBuilder.Builder(jdbcTemplate)
      .setObjectId(id)
      .delete();
  }

  public Category updateCategory(Category category) {
    new ObjectEavBuilder.Builder(jdbcTemplate)
      .setObjectId(category.getId())
      .setStringAttribute(new BigInteger(String.valueOf(AttrtypeProperties.NAME_CATEGORY)), category.getNameCategory())
      .update();
    return getCategoryById(category.getId());
  }

  public BigInteger createCategory(Category newCategory) {
    BigInteger newCategoryId = new ObjectEavBuilder.Builder(jdbcTemplate)
      .setName(newCategory.getNameCategory())
      .setObjectTypeId(new BigInteger(String.valueOf(ObjtypeProperties.CATEGORY)))
      .setStringAttribute(new BigInteger(String.valueOf(AttrtypeProperties.NAME_CATEGORY)), newCategory.getNameCategory())
      .create();
    return newCategoryId;
  }
}
