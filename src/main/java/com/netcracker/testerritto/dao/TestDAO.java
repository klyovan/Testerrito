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
              "select expert.object_id id, lastname.value lastName,  "
            + "    firstName.value firstName, email.value email,  "
            + "    password.value password, phone.value phone  "
            + "    from objects tests,  "
            + "    objreference test2expert,  "
            + "    attributes firstName, "
            + "    attributes lastName, "
            + "    attributes email,  "
            + "    attributes password, "
            + "    attributes phone,   "
            + "    objects expert "
            + "where tests.object_id =  ?  "
            + "    and test2expert.attr_id = 23 /*rate by*/  "
            + "    and test2expert.object_id = tests.object_id  "
            + "    and expert.object_id = test2expert.reference  "
            + "    and firstName.attr_id =  2 /*firstName*/  "
            + "    and firstName.object_id = expert.object_id  "
            + "    and lastName.attr_id = 1 /*lastName*/  "
            + "    and lastName.object_id = expert.object_id  "
            + "    and email.attr_id = 3 /*email*/  "
            + "    and email.object_id = expert.object_id  "
            + "    and password.attr_id = 4 /*password*/  "
            + "    and password.object_id = expert.object_id  "
            + "    and phone.attr_id = 5 /*phone*/  "
            + "    and phone.object_id = expert.object_id";


    @Autowired
    private JdbcTemplate jdbcTemplate;
    @Autowired
    private QuestionDAO questionDAO;
    @Autowired
    private GradeCategoryDAO gradeCategoryDAO;

    public Test getTest(BigInteger testId) {

        Test test = jdbcTemplate
            .queryForObject(GET_TEST, new Object[]{testId.toString()}, new TestRowMapper());

        test.setExperts(getExperts(testId));
        test.setGradesCategory(getGradesCategory(testId));
        test.setQuestions(getQuestions(testId));

        return test;
    }


    private List<User> getExperts(BigInteger testId) {

        return jdbcTemplate
            .query(GET_EXPERTS, new Object[]{testId.toString()}, new UserRowMapper());
    }

    private List<Question> getQuestions(BigInteger testId) {

        return questionDAO.getAllQuestionInTest(testId);
    }

    private List<GradeCategory> getGradesCategory(BigInteger testId) {

        return gradeCategoryDAO.getGradeCategoryByTestId(testId);
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

