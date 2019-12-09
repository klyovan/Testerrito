package com.netcracker.testerritto.mappers;

import com.netcracker.testerritto.models.Reply;
import java.math.BigInteger;
import java.sql.ResultSet;
import java.sql.SQLException;
import org.springframework.jdbc.core.RowMapper;

public class ReplyRowMapper implements RowMapper<Reply> {

  @Override
  public Reply mapRow(ResultSet resultSet, int i) throws SQLException {
    Reply reply = new Reply();
    reply.setId(new BigInteger(resultSet.getString("id")));
    reply.setResultId(new BigInteger(resultSet.getString("result_id")));
    reply.setAnswerId(new BigInteger(resultSet.getString("answer_id")));

    return reply;
  }
}
