package controller.vo;

import java.io.Serializable;
import java.util.Date;
import java.util.Map;

public class Status implements Serializable {

    private Date time;
    private Map<String, Double> status;

    public Date getTime() {
        return time;
    }

    public Status setTime(Date time) {
        this.time = time;
        return this;
    }

    public Map<String, Double> getStatus() {
        return status;
    }

    public Status setStatus(Map<String, Double> status) {
        this.status = status;
        return this;
    }
}
