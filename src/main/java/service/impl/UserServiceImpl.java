package service.impl;

import controller.vo.UserInfo;
import mapper.PowerMapper;
import mapper.UserMapper;
import model.Power;
import model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import service.UserService;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import static model.Power.*;

@Service
public class UserServiceImpl implements UserService {

    private final UserMapper userMapper;
    private final PowerMapper powerMapper;

    @Autowired
    public UserServiceImpl(UserMapper userMapper,
                           PowerMapper powerMapper) {
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
            powerMapper.addPower(id, index);
            return id;
        } else {
            throw new RuntimeException("添加失败");
        }
    }

    @Override
    public List<UserInfo> getUserInfoList() {
        return userMapper
                .findAll()
                .stream()
                .map(UserInfo::create)
                .collect(Collectors.toList());
    }

    private void modifyPower(Long id, List<Power> powers, Power power, boolean modify) {
        if (powers.contains(power) ^ modify) {
            if (modify) {
                powerMapper.addPower(id, power);
            } else {
                powerMapper.deletePower(id, power);
            }
        }
    }

    @Override
    @Transactional(rollbackFor = RuntimeException.class)
    public Boolean modifyPower(UserInfo userInfo) {
        List<Power> power = userMapper.findById(userInfo.getId()).getPower();
        modifyPower(userInfo.getId(), power, cpu, userInfo.isCpu());
        modifyPower(userInfo.getId(), power, mem, userInfo.isMem());
        modifyPower(userInfo.getId(), power, net, userInfo.isNet());
        modifyPower(userInfo.getId(), power, disk, userInfo.isDisk());
        modifyPower(userInfo.getId(), power, file, userInfo.isFile());
        modifyPower(userInfo.getId(), power, desktop, userInfo.isDesktop());
        modifyPower(userInfo.getId(), power, user, userInfo.isUser());
        return true;
    }

    @Override
    public Boolean resetPassword(long id, String password) {
        return userMapper.setPassword(id, password) == 1;
    }

}
