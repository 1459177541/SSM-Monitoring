package service.impl;

import mapper.UserMapper;
import model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import service.SignService;

import java.util.Date;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
public class SignServiceImpl implements SignService {

    private Map<UUID, User> userMap = new ConcurrentHashMap<>();
    private Map<UUID, Date> updateMap = new ConcurrentHashMap<>();
    private final UserMapper userMapper;

    @Autowired
    public SignServiceImpl(UserMapper userMapper) {
        this.userMapper = userMapper;
        Executors
                .newSingleThreadScheduledExecutor()
                .scheduleAtFixedRate(this::scheduled, 0, 10, TimeUnit.MINUTES);
    }

    private void scheduled() {
        long now = System.currentTimeMillis() - 30 * 60 * 1000;
        updateMap
                .entrySet()
                .stream()
                .filter(entry -> entry.getValue().getTime() < now)
                .map(Map.Entry::getKey)
                .collect(Collectors.toSet())
                .forEach(uuid -> {
                    updateMap.remove(uuid);
                    userMap.remove(uuid);
                });
    }

    @Override
    public UUID add(User user) {
        if (user == null) {
            return null;
        }
        UUID uuid = UUID.randomUUID();
        userMap.put(uuid, user);
        updateMap.put(uuid, new Date());
        return uuid;
    }

    @Override
    public User getUser(UUID uuid) {
        User user = uuid == null ? null : userMap.get(uuid);
        if (user != null) {
            updateMap.computeIfPresent(uuid, (uuid1, date) -> {
                date.setTime(System.currentTimeMillis());
                return date;
            });
        }
        return user;
    }

    @Override
    public User getUser(String uuid) {
        return uuid == null ? null : getUser(UUID.fromString(uuid));
    }

    @Override
    public void remove(UUID uuid) {
        if (uuid != null) {
            userMap.remove(uuid);
            updateMap.remove(uuid);
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
                                uuid -> userMapper.findById(userMap.get(uuid).getId()),
                                (a,b)-> a,
                                ConcurrentHashMap::new));
    }
}
