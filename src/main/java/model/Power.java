package model;

public enum Power {
    index("index"),
    cpu("cpu"),
    mem("mem"),
    net("net"),
    disk("disk"),
    file("file"),
    desktop("desktop"),
    user("user");

    public String name;
    Power(String name) {
        this.name = name;
    }
}
