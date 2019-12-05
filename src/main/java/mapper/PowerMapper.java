package mapper;

import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
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
    public List<String> findPower(long id);

    @Insert({
            "INSERT INTO t_power",
            "VALUES (#{id}, #{power})"
    })
    public int addPower(@Param("id") long id, @Param("power") String power);
}
