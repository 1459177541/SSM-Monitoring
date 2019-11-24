package service;

import controller.vo.MemInfo;
import controller.vo.Status;

import java.util.List;

public interface MemService {
    public List<MemInfo> memInfo();

    public Status memStatus();
}
