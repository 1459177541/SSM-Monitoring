package service.impl;

import org.hyperic.sigar.FileSystem;
import org.hyperic.sigar.Sigar;
import org.hyperic.sigar.SigarException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import service.FileService;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class FileServiceImpl implements FileService {

    private final Sigar sigar;

    @Autowired
    public FileServiceImpl(Sigar sigar) {
        this.sigar = sigar;
    }

    @Override
    public List<String> getRootPath() {
        try {
            return Arrays.stream(sigar.getFileSystemList())
                    .map(FileSystem::getDirName)
                    .sorted()
                    .collect(Collectors.toList());
        } catch (SigarException e) {
            throw new RuntimeException(e);
        }
    }

}
