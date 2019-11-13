package service;

import model.MemInfo;
import model.Status;

import java.util.List;

public interface MemService {
    public List<MemInfo> memInfo();

    public Status memStatus();
}
