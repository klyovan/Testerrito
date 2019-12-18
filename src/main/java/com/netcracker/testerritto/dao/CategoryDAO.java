package com.netcracker.testerritto.dao;

import com.netcracker.testerritto.mappers.CategoryRowMapper;
import com.netcracker.testerritto.models.Category;
import com.netcracker.testerritto.properties.AttrtypeProperties;
import com.netcracker.testerritto.properties.ObjtypeProperties;
import org.springframework.beans.factory.annotation.Autowired;
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
            "  attr.value name_category " +
            "from " +
            "  objects category, attributes attr " +
            "where " +
            "  category.object_id = ? " +
            "  and category.object_id = attr.object_id " +
            "  and attr.attr_id = 14 /* name_category */";

    public Category getCategoryById(BigInteger id) {
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
            .setStringAttribute(AttrtypeProperties.NAME_CATEGORY, category.getNameCategory())
            .update();
        return getCategoryById(category.getId());
    }

    public BigInteger createCategory(Category newCategory) {
        return new ObjectEavBuilder.Builder(jdbcTemplate)
            .setName(newCategory.getNameCategory())
            .setObjectTypeId(ObjtypeProperties.CATEGORY)
            .setStringAttribute(AttrtypeProperties.NAME_CATEGORY, newCategory.getNameCategory())
            .create();
    }
}
