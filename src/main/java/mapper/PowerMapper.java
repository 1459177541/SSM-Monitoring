package mapper;

import model.Power;
import org.apache.ibatis.annotations.*;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@Mapper
public interface PowerMapper {

    @Select({
            "SELECT name",
            "FROM t_power",
            "WHERE uid=#{id}"
    })
    public List<Power> findPower(long id);

    @Insert({
            "INSERT INTO t_power",
            "VALUES (#{id}, #{power})"
    })
    public int addPower(@Param("id") long id, @Param("power") Power power);

    @Delete({
            "DELETE FROM t_power",
            "WHERE uid=#{id} AND name=#{power}"
    })
    public int deletePower(@Param("id") long id, @Param("power") Power power);
}
