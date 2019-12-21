package controller.vo;

import model.Power;
import model.User;

import java.io.Serializable;
import java.util.Objects;

public class UserInfo implements Serializable {
    private Long id;
    private String name;
    private boolean cpu;
    private boolean mem;
    private boolean net;
    private boolean disk;
    private boolean file;
    private boolean desktop;
    private boolean user;

    public Long getId() {
        return id;
    }

    public static UserInfo create(User user) {
        UserInfo userInfo = new UserInfo()
                .setId(user.getId())
                .setName(user.getName());
        if (user.getPower() == null) {
            return userInfo;
        }
        user.getPower().forEach(s -> {
            userInfo.cpu = userInfo.cpu || Power.cpu == s;
            userInfo.mem = userInfo.mem || Power.mem == s;
            userInfo.net = userInfo.net || Power.net == s;
            userInfo.disk = userInfo.disk || Power.disk == s;
            userInfo.file = userInfo.file || Power.file == s;
            userInfo.desktop = userInfo.desktop || Power.desktop == s;
            userInfo.user = userInfo.user || Power.user == s;
        });
        return userInfo;
    }

    public UserInfo setId(Long id) {
        this.id = id;
        return this;
    }

    public String getName() {
        return name;
    }

    public UserInfo setName(String name) {
        this.name = name;
        return this;
    }

    public boolean isCpu() {
        return cpu;
    }

    public UserInfo setCpu(boolean cpu) {
        this.cpu = cpu;
        return this;
    }

    public boolean isMem() {
        return mem;
    }

    public UserInfo setMem(boolean mem) {
        this.mem = mem;
        return this;
    }

    public boolean isNet() {
        return net;
    }

    public UserInfo setNet(boolean net) {
        this.net = net;
        return this;
    }

    public boolean isDisk() {
        return disk;
    }

    public UserInfo setDisk(boolean disk) {
        this.disk = disk;
        return this;
    }

    public boolean isFile() {
        return file;
    }

    public UserInfo setFile(boolean file) {
        this.file = file;
        return this;
    }

    public boolean isDesktop() {
        return desktop;
    }

    public UserInfo setDesktop(boolean desktop) {
        this.desktop = desktop;
        return this;
    }

    public boolean isUser() {
        return user;
    }

    public UserInfo setUser(boolean user) {
        this.user = user;
        return this;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        UserInfo userInfo = (UserInfo) o;
        return Objects.equals(id, userInfo.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public String toString() {
        return "UserInfo{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", cpu=" + cpu +
                ", mem=" + mem +
                ", net=" + net +
                ", disk=" + disk +
                ", file=" + file +
                ", desktop=" + desktop +
                ", user=" + user +
                '}';
    }
}
