package service.impl;

import model.Status;
import org.hyperic.sigar.Sigar;
import org.hyperic.sigar.SigarException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import service.CpuService;

import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Service
public class CpuServiceImpl implements CpuService {

    private final Sigar sigar;

    @Autowired
    public CpuServiceImpl(Sigar sigar) {
        this.sigar = sigar;
    }

    @Override
    public List<String> cpuInfo() {
        try {
            return IntStream
                    .range(0, sigar.getCpuInfoList().length)
                    .mapToObj(i -> "cpu"+i)
                    .collect(Collectors.toList());
        } catch (SigarException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public Status cpuStatus() {
        AtomicInteger i = new AtomicInteger(-1);
        try {
            return new Status()
                    .setTime(new Date())
                    .setStatus(Arrays
                            .stream(sigar.getCpuPercList())
                            .map(cpuPerc -> cpuPerc.getCombined()*100.0D)
                            .collect(Collectors
                                    .toMap(o -> "cpu"+i.addAndGet(1), Function.identity()))
                    );
        } catch (SigarException e) {
            throw new RuntimeException(e);
        }
    }
}
