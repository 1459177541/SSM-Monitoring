package service.impl;

import model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import service.SignService;

import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
public class SignServiceImpl implements SignService {

    private Map<UUID, User> userMap = new ConcurrentHashMap<>();
    private final UserServiceImpl userService;

    @Autowired
    public SignServiceImpl(UserServiceImpl userService) {
        this.userService = userService;
    }

    @Override
    public UUID add(User user) {
        if (user == null) {
            return null;
        }
        UUID uuid = UUID.randomUUID();
        userMap.put(uuid, user);
        return uuid;
    }

    @Override
    public User getUser(UUID uuid) {
        return uuid == null ? null : userMap.get(uuid);
    }

    @Override
    public User getUser(String uuid) {
        return uuid == null ? null : getUser(UUID.fromString(uuid));
    }

    @Override
    public void remove(UUID uuid) {
        if (uuid != null) {
            userMap.remove(uuid);
        }
    }

    @Override
    public void remove(String uuid) {
        if (uuid != null) {
            remove(UUID.fromString(uuid));
        }
    }

    @Override
    public boolean contain(UUID uuid) {
        return uuid != null && userMap.containsKey(uuid);
    }

    @Override
    public boolean contain(String uuid) {
        return uuid != null && contain(UUID.fromString(uuid));
    }

    @Override
    public void reload() {
        userMap = userMap
                .keySet()
                .stream()
                .collect(Collectors
                        .toMap(Function.identity(),
                                uuid -> userService.getUserInfo(userMap.get(uuid).getId()),
                                (a,b)-> a,
                                ConcurrentHashMap::new));
    }
}
