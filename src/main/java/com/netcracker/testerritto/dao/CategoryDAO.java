package com.netcracker.testerritto.dao;

import com.netcracker.testerritto.mapper.CategoryRowMapper;
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
        String query = "SELECT category.object_id, category_attr.value " +
                "FROM objects category, attributes category_attr " +
                "WHERE category.object_id = ? AND " +
                "category.object_id = category_attr.object_id AND " +
                "category_attr.attr_id = 14";
        return jdbcTemplate.queryForObject(query, new Object[]{id}, categoryRowMapper);
    }

    public void deleteCategoryById(int id) {
        String query = "DELETE FROM objects category " +
                "WHERE category.object_id = ? AND category.object_type_id = 8";
        jdbcTemplate.update(query, new Object[]{id});
    }

    public Category updateCategory(Category category) {
        String queryUpdateAttributes = "UPDATE attributes category SET category.value = ? " +
                "WHERE category.object_id = ? AND category.attr_id = 14";
        jdbcTemplate.update(queryUpdateAttributes, new Object[]{category.getNameCategory(), category.getId()});
        return getCategoryById(category.getId());
    }

    public int createCategory(Category newCategory) {
        String queryInsert = "INSERT ALL " +
                "INTO objects(object_id, object_type_id, name) VALUES(object_id_pr.nextval, 8 , 'Category ' || object_id_pr.currval) " +
                "INTO attributes(attr_id, object_id, value) VALUES (14, object_id_pr.currval, ?)" +
                "SELECT * FROM dual";
        String queryRetrieveId = "SELECT object_id_pr.currval FROM dual";
        jdbcTemplate.update(queryInsert, new Object[]{newCategory.getNameCategory()});
        return jdbcTemplate.queryForObject(queryRetrieveId, Integer.class);
    }
}
