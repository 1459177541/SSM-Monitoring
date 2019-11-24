package service.impl;

import mapper.UserMapper;
import model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import service.UserService;

import java.util.Objects;

@Service
public class UserServiceImpl implements UserService {

    private final UserMapper userMapper;

    @Autowired
    public UserServiceImpl(UserMapper userMapper) {
        this.userMapper = userMapper;
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
        return userMapper.save(user);
    }
}
