package service.impl;

import controller.vo.FileInfo;
import org.hyperic.sigar.FileSystem;
import org.hyperic.sigar.Sigar;
import org.hyperic.sigar.SigarException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import service.FileService;

import java.io.File;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
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

    @Override
    public List<FileInfo> getFileList(String url) {
        return Arrays
                .stream(Objects.requireNonNull(new File(url).listFiles()))
                .map(FileInfo::create)
                .collect(Collectors.toList());
    }

}
