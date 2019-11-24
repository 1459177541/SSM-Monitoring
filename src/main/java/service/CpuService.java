package service;

import controller.vo.Status;

import java.util.List;

public interface CpuService {
    public List<String> cpuInfo();

    public Status cpuStatus();
}
