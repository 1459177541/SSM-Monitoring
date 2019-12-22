package service;

import controller.vo.UserInfo;
import model.User;

import java.util.List;

public interface UserService {

    public boolean login(User user);

    public User getUserInfo(long uid);

    public long register(User user);

    public List<UserInfo> getUserInfoList();

    public Boolean modifyPower(UserInfo userInfo);

    public Boolean resetPassword(long id, String password);
}
