package com.netcracker.testerritto.dao;

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
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

@Repository
public class TestDAO {

    private static final String GET_TEST =
            "select tests.object_id test_id, tests.parent_id group_id, " +
            "test_name.value test_name, creator.object_id creator_id " +
            "from  objects tests, " +
            "    attributes test_name, " +
            "    objreference test2creator, " +
            "    Objects creator " +
            "where " +
            "    tests.object_id = ?  " +
            "    and test_name.attr_id = 9 /*test_name*/ " +
            "    and test_name.object_id = tests.object_id " +
            "    and test2creator.attr_id = 24 /*create_test_by*/ " +
            "    and test2creator.object_id = tests.object_id " +
            "    and creator.object_id = test2creator.reference";

    private static final String GET_EXPERTS =
            "select expert.object_id id, lastname.value last_name,  " +
            "    firstName.value first_name, email.value email,  " +
            "    password.value password, phone.value phone  " +
            "from objects tests,  " +
            "    objreference test2expert,  " +
            "    attributes firstName, " +
            "    attributes lastName, " +
            "    attributes email,  " +
            "    attributes password, " +
            "    attributes phone,   " +
            "    objects expert " +
            "where tests.object_id =  ?  " +
            "    and test2expert.attr_id = 23 /*rate by*/  " +
            "    and test2expert.object_id = tests.object_id  " +
            "    and expert.object_id = test2expert.reference  " +
            "    and firstName.attr_id =  2 /*firstName*/  " +
            "    and firstName.object_id = expert.object_id  " +
            "    and lastName.attr_id = 1 /*lastName*/  " +
            "    and lastName.object_id = expert.object_id  " +
            "    and email.attr_id = 3 /*email*/  " +
            "    and email.object_id = expert.object_id  " +
            "    and password.attr_id = 4 /*password*/  " +
            "    and password.object_id = expert.object_id  " +
            "    and phone.attr_id = 5 /*phone*/  " +
            "    and phone.object_id = expert.object_id";

    private final static String GET_QUESTIONS =
            "select questions.object_id id, questions.parent_id test_id, " +
            "    questions_text.value text,questions_type.list_value_id question_type, " +
            "    question2category.reference as category_id " +
            "from objects questions,  " +
            "    attributes questions_text, " +
            "    attributes questions_type,  " +
            "    objreference question2category " +
            "where questions.parent_id = ? /* testId*/ " +
            "    and questions.object_type_id = 10 /*questions*/  " +
            "    and questions_text.attr_id = 18 /*text_question*/ " +
            "    and questions_text.object_id = questions.object_id  " +
            "    and questions_type.attr_id = 19 /*type_question*/ " +
            "    and questions_type.object_id = questions.object_id "+
            "    and question2category.attr_id = 34 /*question_belongs*/ "+
            "    and question2category.object_id = questions.object_id";

    private final static String GET_CATEGORIES_BY_TEST =
             "select distinct categories.object_id "+
             "from objects gradecategory, "+
             "    objects categories, "+
             "    objects tests, "+
             "    objreference gradecategory2category "+
             "where tests.object_id = ? "+
             "    and tests.object_id = gradecategory.parent_id    "+
             "    and gradecategory2category.attr_id = 33 /*grade_belongs*/ "+
             "    and gradecategory2category.object_id = gradecategory.object_id "+
             "    and gradecategory2category.reference = categories.object_id";


    @Autowired
    private JdbcTemplate jdbcTemplate;
    @Autowired
    private GradeCategoryDAO gradeCategoryDAO;
    @Autowired
    private AnswerDAO answerDAO;

    public Test getTest(BigInteger testId) {
        Test test = jdbcTemplate.queryForObject(GET_TEST, new Object[]{testId.toString()}, new TestRowMapper());

        test.setExperts(getExperts(testId));
        test.setGradesCategory(getGradesCategory(testId));
        test.setQuestions(getQuestions(testId));

        return test;
    }


    private List<User> getExperts(BigInteger testId) {

        return jdbcTemplate.query(GET_EXPERTS, new Object[]{testId.toString()}, new UserRowMapper());
    }

    private List<Question> getQuestions(BigInteger testId) {
        List<Question> questions =   jdbcTemplate.query(GET_QUESTIONS, new Object[]{testId.toString()}, new QuestionRowMapper());
        questions.forEach(question -> question.setAnswers(answerDAO.getAllAnswerInQuestion(question.getId())));
        return questions;
    }

    private List<GradeCategory> getGradesCategory(BigInteger testId) {

        return gradeCategoryDAO.getGradeCategoryByTestId(testId);
    }

    public List<BigInteger> getCategoriesIdByTest (BigInteger testId){
        return jdbcTemplate.query(GET_CATEGORIES_BY_TEST, new Object[]{testId.toString()}, new RowMapper<BigInteger>() {
            @Override
            public BigInteger mapRow(ResultSet rs, int rowNum) throws SQLException {
                return BigInteger.valueOf(rs.getInt(1));
            }
        });
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


    public Test updateTest(Test test) {

        new ObjectEavBuilder.Builder(jdbcTemplate)
            .setObjectId(test.getId())
            .setStringAttribute(AttrtypeProperties.NAME_TEST, test.getNameTest())
            .update();

        return getTest(test.getId());

    }
}

