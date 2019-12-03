package com.netcracker.testerritto.mappers;

import com.netcracker.testerritto.models.Remark;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

public class RemarkRowMapper implements RowMapper<Remark> {

    @Override
    public Remark mapRow(ResultSet resultSet, int i) throws SQLException {
        Remark remark = new Remark();
        remark.setId(resultSet.getInt("id"));
        remark.setText(resultSet.getString("text"));
        remark.setUserId(resultSet.getInt("userId"));
        remark.setQuestionId(resultSet.getInt("questionId"));
        return remark;
    }
}
