package service;

import model.User;

import java.util.UUID;

public interface SignService {

    public UUID add(User user);
    public User getUser(UUID uuid);
    public User getUser(String uuid);
    public void remove(UUID uuid);
    public void remove(String uuid);
    public boolean contain(UUID uuid);
    public boolean contain(String uuid);

}
