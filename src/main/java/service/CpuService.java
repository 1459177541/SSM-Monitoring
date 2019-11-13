package service;

import model.Status;
import org.hyperic.sigar.SigarException;

import java.util.List;

public interface CpuService {
    public List<String> cpuInfo();

    public Status cpuStatus();
}
