package com.netcracker.testerritto.mappers;

import com.netcracker.testerritto.models.Result;
import java.sql.ResultSet;
import java.sql.SQLException;
import org.springframework.jdbc.core.RowMapper;

public class ResultRowMapper implements RowMapper<Result> {
  @Override
  public Result mapRow(ResultSet resultSet, int i) throws SQLException {
    Result result = new Result();
    result.setId(resultSet.getBigDecimal("id").toBigInteger());
    result.setTestId(resultSet.getBigDecimal("test_id").toBigInteger());
    result.setDate(resultSet.getDate("result_date"));
    result.setScore(resultSet.getInt("result_score"));
    result.setStatus(resultSet.getString("result_status"));
    result.setUserId(resultSet.getBigDecimal("user_id").toBigInteger());
    return result;
  }


}
