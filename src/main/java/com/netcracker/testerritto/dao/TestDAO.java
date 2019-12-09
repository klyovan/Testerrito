package com.netcracker.testerritto.dao;

import com.netcracker.testerritto.mappers.GradeCategoryMapper;
import com.netcracker.testerritto.mappers.QuestionRowMapper;
import com.netcracker.testerritto.mappers.TestRowMapper;
import com.netcracker.testerritto.mappers.UserRowMapper;
import com.netcracker.testerritto.models.GradeCategory;
import com.netcracker.testerritto.models.Question;
import com.netcracker.testerritto.models.Test;
import com.netcracker.testerritto.models.User;
import com.netcracker.testerritto.properties.AttrtypeProperties;
import com.netcracker.testerritto.properties.ObjtypeProperties;
import java.math.BigInteger;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

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
            "    and test_name.attr_id = ? /*test_name*/\n" +
            "    and test_name.object_id = tests.object_id\n" +
            "    and test2creator.attr_id = ? /*create_test_by*/\n" +
            "    and test2creator.object_id = tests.object_id\n" +
            "    and creator.object_id = test2creator.reference";


        Test test = jdbcTemplate.queryForObject(sql, new Object[]{testId.toString(), AttrtypeProperties.NAME_TEST.toString(),
            AttrtypeProperties.CREATE_TEST_BY.toString()}, new TestRowMapper());

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
            "    and test2expert.attr_id = ?      /*rate by*/ \n" +
            "    and test2expert.object_id = tests.object_id \n" +
            "    and expert.object_id = test2expert.reference \n" +
            "    and firstName.attr_id =  ?     /*firstName*/ \n" +
            "    and firstName.object_id = expert.object_id \n" +
            "    and lastName.attr_id = ?       /*lastName*/ \n" +
            "    and lastName.object_id = expert.object_id \n" +
            "    and email.attr_id = ?          /*email*/ \n" +
            "    and email.object_id = expert.object_id \n" +
            "    and password.attr_id = ?       /*password*/ \n" +
            "    and password.object_id = expert.object_id \n" +
            "    and phone.attr_id = ?          /*phone*/ \n" +
            "    and phone.object_id = expert.object_id\n";

        return jdbcTemplate.query(sql, new Object[]{testId.toString(), AttrtypeProperties.RATE_BY.toString(), AttrtypeProperties.FIRST_NAME.toString(),
            AttrtypeProperties.LAST_NAME.toString(), AttrtypeProperties.EMAIL.toString(),
            AttrtypeProperties.PASSWORD.toString(), AttrtypeProperties.PHONE.toString()}, new UserRowMapper());
    }

    private List<Question> getQuestions(BigInteger testId) {
        String sql = "select questions.object_id id, questions.parent_id testId," +
            "    questions_text.value text,questions_type.value type" +
            "    from objects questions, " +
            "    attributes questions_text," +
            "    attributes questions_type " +
            "    where questions.parent_id = ?             /* groupId*/" +
            "    and questions.object_type_id = ?         /*questions*/ " +
            "    and questions_text.attr_id = ?           /*text_question*/" +
            "    and questions_text.object_id = questions.object_id " +
            "    and questions_type.attr_id = ?           /*type_question*/" +
            "    and questions_type.object_id = questions.object_id";

        return jdbcTemplate.query(sql, new Object[]{testId.toString(), ObjtypeProperties.QUESTION.toString(),
            AttrtypeProperties.TEXT_QUESTION.toString(), AttrtypeProperties.TYPE_QUESTION.toString()}, new QuestionRowMapper());
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
            "    and grade_category.object_type_id = ? /*grade_category*/ " +
            "    and grade_category_min_value.attr_id = ? /*min_score*/" +
            "    and grade_category_min_value.object_id = grade_category.object_id " +
            "    and grade_category_max_value.attr_id = ? /*max_score*/" +
            "    and grade_category_max_value.object_id = grade_category.object_id" +
            "    and grade_category_meaning.attr_id = ? /*meaning*/" +
            "    and grade_category_meaning.object_id = grade_category.object_id " +
            "    and grade_category_ref_on_category.object_id = grade_category.object_id " +
            "    and grade_category_ref_on_category.attr_id = ?/*grade_belongs*/";

        return jdbcTemplate.query(sql, new Object[]{testId.toString(), ObjtypeProperties.GRADE_CATEGORY.toString(), AttrtypeProperties.MIN_SCORE.toString(),
            AttrtypeProperties.MAX_SCORE.toString(), AttrtypeProperties.MEANING.toString(), AttrtypeProperties.GRADE_BELONGS.toString()}, new GradeCategoryMapper());
    }


    public void deleteTest(BigInteger testId) {
        new ObjectEavBuilder.Builder(jdbcTemplate)
            .setObjectId(testId)
            .delete();
    }

    public BigInteger createTest(Test test) {

        return new ObjectEavBuilder.Builder(jdbcTemplate)
            .setName(test.getNameTest())
            .setObjectTypeId(ObjtypeProperties.TEST)
            .setParentId(test.getGroupId())
            .setStringAttribute(AttrtypeProperties.NAME_TEST, test.getNameTest())
            .setReference(AttrtypeProperties.CREATE_TEST_BY, test.getCreatorUserId())
            .create();
    }


    public BigInteger updateTest(Test test) {

        new ObjectEavBuilder.Builder(jdbcTemplate)
            .setObjectId(test.getId())
            .setStringAttribute(AttrtypeProperties.NAME_TEST, test.getNameTest())
            .update();

        return test.getId();

    }
}

