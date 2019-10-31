package mapper;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@Mapper
public interface GroupMapper {

    @Select({
            "SELECT power",
            "FROM t_group_power",
            "WHERE gid=#{id}"
    })
    public List<String> findPower(long id);

}
