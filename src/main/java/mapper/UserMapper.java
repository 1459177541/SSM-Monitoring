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
            id="powerMap", value = {
                    @Result(column = "id", property = "power",
                            many = @Many(select = "findPower", fetchType = FetchType.EAGER))
    })
    public User findById(long id);


    @Select({
            "SELECT name",
            "FROM t_power",
            "WHERE uid=#{id}"
    })
    public List<String> findPower(long id);

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
            "FROM user"
    })
    @ResultMap({"powerMap"})
    public List<User> findAll();

    @Insert({
            "INSERT INTO user",
            "VALUES (#{user.id},#{user.name},#{user.password})"
    })
    public long save(User user);

}