package com.netcracker.testerritto.mappers;

import com.netcracker.testerritto.models.Remark;
import org.springframework.jdbc.core.RowMapper;

import java.math.BigInteger;
import java.sql.ResultSet;
import java.sql.SQLException;

public class RemarkRowMapper implements RowMapper<Remark> {

    @Override
    public Remark mapRow(ResultSet resultSet, int i) throws SQLException {
        Remark remark = new Remark();
        remark.setId(new BigInteger(resultSet.getString("id")));
        remark.setText(resultSet.getString("text"));
        remark.setUserSenderId(new BigInteger(resultSet.getString("userId")));
        remark.setQuestionId(new BigInteger(resultSet.getString("questionId")));
        return remark;
    }
}
