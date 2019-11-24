package controller.vo;

import java.io.Serializable;
import java.util.Objects;

public class DiskInfo implements Serializable {

    private String name;
    private String url;
    private String type;
    private long size;
    private long use;

    public String getName() {
        return name;
    }

    public DiskInfo setName(String name) {
        this.name = name;
        return this;
    }

    public String getUrl() {
        return url;
    }

    public DiskInfo setUrl(String url) {
        this.url = url;
        return this;
    }

    public String getType() {
        return type;
    }

    public DiskInfo setType(String type) {
        this.type = type;
        return this;
    }

    public long getSize() {
        return size;
    }

    public DiskInfo setSize(long size) {
        this.size = size;
        return this;
    }

    public long getUse() {
        return use;
    }

    public DiskInfo setUse(long use) {
        this.use = use;
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
        DiskInfo diskInfo = (DiskInfo) o;
        return Objects.equals(url, diskInfo.url);
    }

    @Override
    public int hashCode() {
        return Objects.hash(url);
    }

    @Override
    public String toString() {
        return "DiskInfo{" +
                "name='" + name + '\'' +
                ", url='" + url + '\'' +
                ", type='" + type + '\'' +
                ", size=" + size +
                ", use=" + use +
                '}';
    }
}
