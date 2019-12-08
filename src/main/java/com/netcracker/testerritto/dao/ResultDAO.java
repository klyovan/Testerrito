package com.netcracker.testerritto.dao;

import com.netcracker.testerritto.models.Result;
import java.math.BigInteger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

@Repository
public class ResultDAO {

  @Autowired
  private JdbcTemplate jdbcTemplate;

  public Result createResult(){

    BigInteger objSequence = getObjectSequenceCount();
    String sql="insert all \n"
        + "    into objects(object_id, parent_id, object_type_id, name, description)\n"
        + "        values(?, null, 5, 'result', null)\n"
        + "    into attributes(object_id, attr_id, value, date_value, list_value_id)\n"
        + "        values(object_id_pr.currval, 10,null, sys.date, null)  --date\n"
        + "    into attributes(object_id, attr_id, value, date_value, list_value_id)\n"
        + "        values(object_id_pr.currval, 11, 5, null, null) --score\n"
        + "    into attributes(object_id, attr_id, value, date_value, list_value_id)\n"
        + "        values(object_id_pr.currval, 12, null, null, 7) --status\n"
        + "    into objreference(attr_id, object_id, reference)  -- look by \n"
        + "        values(29, object_id_, 2) --userid\n"
        + "    into objreference(attr_id, object_id, reference)  --result_belong\n"
        + "        values(30, object_id_pr.currval, 147)--testid\n"
        + "    select * from dual";



    return null;

  }

  private BigInteger getObjectSequenceCount() {
    String sql = " select object_id_pr.NEXTVAL from dual";

    BigInteger result = jdbcTemplate.queryForObject(sql, BigInteger.class);
    return result;
  }

}
