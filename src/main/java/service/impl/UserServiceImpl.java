package service.impl;

import mapper.PowerMapper;
import mapper.UserMapper;
import model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import service.UserService;

import java.util.Objects;

@Service
public class UserServiceImpl implements UserService {

    private final UserMapper userMapper;
    private final PowerMapper powerMapper;

    @Autowired
    public UserServiceImpl(UserMapper userMapper, PowerMapper powerMapper) {
        this.userMapper = userMapper;
        this.powerMapper = powerMapper;
    }

    @Override
    public boolean login(User user) {
        if (user == null) {
            return false;
        }
        User user1 = userMapper.findBaseById(user.getId());
        if (user1 == null) {
            return false;
        }
        return Objects.equals(user.getPassword(), user1.getPassword());
    }

    @Override
    public User getUserInfo(long uid) {
        return userMapper.findById(uid);
    }

    @Override
    public long register(User user) {
        if (1==userMapper.save(user)) {
            long id = user.getId();
            powerMapper.addPower(id, "index");
            return id;
        } else {
            throw new RuntimeException("添加失败");
        }
    }
}
