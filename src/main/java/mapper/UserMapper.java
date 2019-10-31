package mapper;

import model.User;
import model.UserGroup;
import org.apache.ibatis.annotations.*;
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
    @Results(id = "userMap", value = {
            @Result(id = true, column = "id", property = "id"),
            @Result(column = "name", property = "name"),
            @Result(column = "password", property = "password"),
            @Result(column = "id", property = "userGroups", many = @Many(select = "findGroup"))
    })
    public User findById(long id);

    @Select({
            "SELECT *",
            "FROM t_group",
            "WHERE id IN (SELECT gid FROM t_user_group WHERE id=#{id})"
    })
    @Results({
            @Result(column = "id", property = "power", many = @Many(select = "mapper.GroupMapper.findPower"))
    })
    public List<UserGroup> findGroup(long id);


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
    @ResultMap("userMap")
    public List<User> findAll();

    @Insert({
            "INSERT INTO user",
            "VALUES (#{user.id},#{user.name},#{user.password})"
    })
    public long save(User user);

}