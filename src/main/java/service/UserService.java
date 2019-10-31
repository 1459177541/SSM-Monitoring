package service;

import model.User;

public interface UserService {

    public boolean login(User user);

    public User getUserInfo(long uid);

    public long register(User user);

}
