package controller.vo;

public class MemInfo {
    private String name;
    private String type = "value";
    private long min = 0;
    private long max;

    public String getName() {
        return name;
    }

    public MemInfo setName(String name) {
        this.name = name;
        return this;
    }

    public String getType() {
        return type;
    }

    public MemInfo setType(String type) {
        this.type = type;
        return this;
    }

    public long getMin() {
        return min;
    }

    public MemInfo setMin(long min) {
        this.min = min;
        return this;
    }

    public long getMax() {
        return max;
    }

    public MemInfo setMax(long max) {
        this.max = max;
        return this;
    }
}
