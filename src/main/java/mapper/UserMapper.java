package mapper;

import model.User;
import org.apache.ibatis.annotations.*;
import org.apache.ibatis.mapping.FetchType;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@Mapper
public interface UserMapper {

    @Select({
            "SELECT *",
            "FROM t_user",
            "WHERE id=#{id}"
    })
    @Results(
            id = "powerMap", value = {
            @Result(id = true, column = "id", property = "id"),
            @Result(column = "name", property = "name"),
            @Result(column = "password", property = "password"),
            @Result(column = "id", property = "power",
                    many = @Many(select = "mapper.PowerMapper.findPower",
                            fetchType = FetchType.EAGER))
    })
    public User findById(long id);

    @Select({
            "SELECT id, name, password",
            "FROM t_user",
            "WHERE id=#{id}"
    })
    @Results(id = "userBaseMap", value = {
            @Result(id = true, column = "id", property = "id"),
            @Result(column = "name", property = "name"),
            @Result(column = "password", property = "password"),
    })
    public User findBaseById(long id);

    @Select({
            "SELECT *",
            "FROM t_user"
    })
    @ResultMap({"powerMap"})
    public List<User> findAll();

    @Insert({
            "INSERT INTO t_user(name, password)",
            "VALUES (#{name},#{password})"
    })
    @Options(useGeneratedKeys = true, keyProperty = "id", keyColumn = "id")
    public int save(User user);

    @Update({
            "UPDATE t_user",
            "SET password=#{password}",
            "WHERE id=#{id}"
    })
    public int setPassword(@Param("id") long id, @Param("password") String password);
}