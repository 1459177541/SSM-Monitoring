package service.impl;

import controller.vo.DiskInfo;
import org.hyperic.sigar.FileSystem;
import org.hyperic.sigar.Sigar;
import org.hyperic.sigar.SigarException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import service.DiskService;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class DiskServiceImpl implements DiskService {

    private final Sigar sigar;

    @Autowired
    public DiskServiceImpl(Sigar sigar) {
        this.sigar = sigar;
    }

    @Override
    public List<DiskInfo> getDiskInfo() {
        try {
            return Arrays
                    .stream(sigar.getFileSystemList())
                    .filter(fs -> fs.getType() == FileSystem.TYPE_LOCAL_DISK)
                    .map(fs -> {
                                try {
                                    return new DiskInfo()
                                            .setName(fs.getDevName())
                                            .setUrl(fs.getDirName())
                                            .setType(fs.getTypeName())
                                            .setSize(sigar.getFileSystemUsage(fs.getDirName()).getTotal())
                                            .setUse(sigar.getFileSystemUsage(fs.getDirName()).getUsed());
                                } catch (SigarException e) {
                                    throw new RuntimeException(e);
                                }
                            })
                    .collect(Collectors.toList());
        } catch (SigarException e) {
            throw new RuntimeException(e);
        }
    }
}
