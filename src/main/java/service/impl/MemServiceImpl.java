package service.impl;

import model.MemInfo;
import model.Status;
import org.hyperic.sigar.Sigar;
import org.hyperic.sigar.SigarException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import service.MemService;

import java.util.Date;
import java.util.List;
import java.util.Map;

@Service
public class MemServiceImpl implements MemService {

    private final Sigar sigar;

    private static final String MEM = "memory";
    private static final String SWAP = "swap";

    @Autowired
    public MemServiceImpl(Sigar sigar) {
        this.sigar = sigar;
    }

    @Override
    public List<MemInfo> memInfo() {
        try {
            return List.of(
                    new MemInfo().setName(MEM).setMax(sigar.getMem().getTotal()/1000),
                    new MemInfo().setName(SWAP).setMax(sigar.getSwap().getTotal()/1000)
            );
        } catch (SigarException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public Status memStatus() {
        try {
            return new Status()
                    .setTime(new Date())
                    .setStatus(Map.of(
                            MEM, (double)sigar.getMem().getUsed()/1000.0D,
                            SWAP, (double)sigar.getSwap().getUsed()/1000.0D
                    ));
        } catch (SigarException e) {
            throw new RuntimeException(e);
        }
    }

}
