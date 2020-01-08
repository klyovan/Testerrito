package com.netcracker.testerritto.mappers;

import com.netcracker.testerritto.models.Result;
import com.netcracker.testerritto.properties.ListsAttr;
import com.netcracker.testerritto.properties.Status;
import org.springframework.jdbc.core.RowMapper;

import java.math.BigInteger;
import java.sql.ResultSet;
import java.sql.SQLException;

public class ResultRowMapper implements RowMapper<Result> {
  @Override
  public Result mapRow(ResultSet resultSet, int i) throws SQLException {
    Result result = new Result();
    result.setId(new BigInteger(resultSet.getString("id")));
    result.setTestId(new BigInteger(resultSet.getString("test_id")));
    result.setDate(resultSet.getDate("result_date"));
    result.setScore(resultSet.getInt("result_score"));
    result.setStatus(Status.getValueById(BigInteger.valueOf(resultSet.getInt("result_status"))));
    result.setUserId(new BigInteger(resultSet.getString("user_id")));
    result.setCategoryId(new BigInteger(resultSet.getString("category_id")));
    return result;
  }
}
