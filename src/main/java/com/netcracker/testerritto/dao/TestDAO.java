package com.netcracker.testerritto.dao;

import com.netcracker.testerritto.mappers.GradeCategoryMapper;
import com.netcracker.testerritto.mappers.QuestionRowMapper;
import com.netcracker.testerritto.mappers.TestRowMapper;
import com.netcracker.testerritto.mappers.UserRowMapper;
import com.netcracker.testerritto.models.GradeCategory;
import com.netcracker.testerritto.models.Question;
import com.netcracker.testerritto.models.Test;
import com.netcracker.testerritto.models.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.math.BigInteger;
import java.util.List;

@Repository
public class TestDAO {

    @Autowired
    JdbcTemplate jdbcTemplate;


    public Test getTest(BigInteger testId) {
        String sql = "select tests.object_id test_id, tests.parent_id group_id, test_name.value test_name, creator.object_id creator_id \n" +
            "from  objects tests,\n" +
            "    attributes test_name,\n" +
            "    objreference test2creator,\n" +
            "    Objects creator\n" +
            "where\n" +
            "    tests.object_id = ? \n" +
            "    and test_name.attr_id = 9\n" +
            "    and test_name.object_id = tests.object_id\n" +
            "    and test2creator.attr_id = 24 /*create_test_by*/\n" +
            "    and test2creator.object_id = tests.object_id\n" +
            "    and creator.object_id = test2creator.reference";


        Test test = jdbcTemplate.queryForObject(sql, new Object[]{testId.toString()}, new TestRowMapper());
        test.setExperts(getExperts(testId));
        test.setGradesCategory(getGradesCategory(testId));
        test.setQuestions(getQuestions(testId));
        return test;
    }


    private List<User> getExperts(BigInteger testId) {
        String sql = "select expert.object_id id, lastname.value lastName, \n" +
            "    firstName.value firstName, email.value email, \n" +
            "    password.value password, phone.value phone \n" +
            "    from objects tests, \n" +
            "    objreference test2expert, \n" +
            "    attributes firstName,\n" +
            "    attributes lastName,\n" +
            "    attributes email, \n" +
            "    attributes password,\n" +
            "    attributes phone,  \n" +
            "    objects expert\n" +
            "where tests.object_id = ?  \n" +
            "    and test2expert.attr_id = 23      /*rate by*/ \n" +
            "    and test2expert.object_id = tests.object_id \n" +
            "    and expert.object_id = test2expert.reference \n" +
            "    and firstName.attr_id = 2      /*firstName*/ \n" +
            "    and firstName.object_id = expert.object_id \n" +
            "    and lastName.attr_id = 1       /*lastName*/ \n" +
            "    and lastName.object_id = expert.object_id \n" +
            "    and email.attr_id = 3          /*email*/ \n" +
            "    and email.object_id = expert.object_id \n" +
            "    and password.attr_id = 4       /*password*/ \n" +
            "    and password.object_id = expert.object_id \n" +
            "    and phone.attr_id = 5          /*phone*/ \n" +
            "    and phone.object_id = expert.object_id\n";

        return jdbcTemplate.query(sql, new Object[]{testId.toString()}, new UserRowMapper());
    }

    private List<Question> getQuestions(BigInteger testId) {
        String sql = "select questions.object_id id, questions.parent_id testId," +
            "    questions_text.value text,questions_type.value type" +
            "    from objects questions, " +
            "    attributes questions_text," +
            "    attributes questions_type " +
            "    where questions.parent_id = ?             /* groupId*/" +
            "    and questions.object_type_id = 10         /*questions*/ " +
            "    and questions_text.attr_id = 18           /*text_question*/" +
            "    and questions_text.object_id = questions.object_id " +
            "    and questions_type.attr_id = 19           /*type_question*/" +
            "    and questions_type.object_id = questions.object_id";

        return jdbcTemplate.query(sql, new Object[]{testId.toString()}, new QuestionRowMapper());
    }

    private List<GradeCategory> getGradesCategory(BigInteger testId) {
        String sql = "select grade_category.object_id grade_id, grade_category.name grade_name, grade_category.parent_id test_id, " +
            "    grade_category_min_value.value min_value, grade_category_max_value.value max_value, grade_category_meaning.value meaning, " +
            "    grade_category_ref_on_category.reference category_id " +
            "    from objects grade_category, " +
            "    attributes grade_category_min_value," +
            "    attributes grade_category_max_value," +
            "    attributes grade_category_meaning, " +
            "    objreference grade_category_ref_on_category " +
            "    where grade_category.parent_id = ? /* group_id*/" +
            "    and grade_category.object_type_id = 9 /*grade_category*/ " +
            "    and grade_category_min_value.attr_id = 15 /*min_score*/" +
            "    and grade_category_min_value.object_id = grade_category.object_id " +
            "    and grade_category_max_value.attr_id = 16 /*max_score*/" +
            "    and grade_category_max_value.object_id = grade_category.object_id" +
            "    and grade_category_meaning.attr_id = 17 /*meaning*/" +
            "    and grade_category_meaning.object_id = grade_category.object_id " +
            "    and grade_category_ref_on_category.object_id = grade_category.object_id " +
            "    and grade_category_ref_on_category.attr_id = 33/*grade_belongs*/";

        return jdbcTemplate.query(sql, new Object[]{testId.toString()}, new GradeCategoryMapper());
    }


    public void deleteTest(BigInteger testId) {
        String sql = "delete from objects where object_id = ?";

        jdbcTemplate.update(sql, testId.toString());
    }

    public BigInteger createTest(Test test) {
        String sql = "insert all  " +
            "    into objects(object_id, parent_id, object_type_id, name, description)" +
            "    values(?, ?, 4,? , null)" +
            "    into attributes(object_id, attr_id, value, date_value, list_value_id)  /*test_name*/" +
            "    values(?, 9, ?, null, null)" +
            "    into objreference(attr_id, object_id, reference) /*test2creator*/" +
            "    values(24,?, ?)" +
            "    select * from dual";

        jdbcTemplate.update(sql, test.getId().toString(), test.getGroupId().toString(), test.getNameTest(), test.getId().toString(), test.getNameTest(), test.getId().toString(), test.getCreatorUserId().toString());

        return test.getId();
    }


    public void updateTest(Test test, BigInteger attrId) {
        String sql = "update attributes \n" +
            "set\n" +
            "    value = ? /* new_name */\n" +
            "where object_id = ? /* group_Id */\n" +
            "and attr_id = ? /* name_test */";

        jdbcTemplate.update(sql, test.getNameTest(), test.getId().toString(), attrId.toString());
    }


    private BigInteger getObjectSequenceCount() {
        String sql = "SELECT object_id_pr.NEXTVAL from dual";

        return jdbcTemplate.queryForObject(sql, BigInteger.class);
    }


}
