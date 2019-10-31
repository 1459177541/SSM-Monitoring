package model;

import java.util.List;
import java.util.Objects;

public class UserGroup {

    private Long id;
    private String name;
    private List<String> power;

    public UserGroup() {
    }

    public UserGroup(Long id, String name, List<String> power) {
        this.id = id;
        this.name = name;
        this.power = power;
    }

    public Long getId() {
        return id;
    }

    public UserGroup setId(Long id) {
        this.id = id;
        return this;
    }

    public String getName() {
        return name;
    }

    public UserGroup setName(String name) {
        this.name = name;
        return this;
    }

    public List<String> getPower() {
        return power;
    }

    public UserGroup setPower(List<String> power) {
        this.power = power;
        return this;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        UserGroup userGroup = (UserGroup) o;
        return Objects.equals(id, userGroup.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public String toString() {
        return "Role{" +
                "name='" + name + '\'' +
                '}';
    }
}
