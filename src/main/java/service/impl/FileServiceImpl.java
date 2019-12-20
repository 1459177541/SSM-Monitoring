package service.impl;

import controller.vo.FileInfo;
import org.apache.commons.fileupload.FileItem;
import org.hyperic.sigar.FileSystem;
import org.hyperic.sigar.Sigar;
import org.hyperic.sigar.SigarException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import service.FileService;

import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.nio.file.*;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

import static java.nio.file.StandardWatchEventKinds.*;

@Service
public class FileServiceImpl implements FileService {

    private final Sigar sigar;
    private static final String SEPARATOR = System.getProperty("file.separator");
    private final Map<Path, WatchService> watchServiceMap = new ConcurrentHashMap<>();
    private final Map<Path, AtomicInteger> pathCount = new ConcurrentHashMap<>();

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

    @Override
    public String upload(List<FileItem> attr) {
        String url = attr.stream()
                .filter(FileItem::isFormField)
                .filter(fileItem -> "url".equals(fileItem.getFieldName()))
                .map(fileItem -> {
                    try {
                        return fileItem.getString("UTF-8");
                    } catch (UnsupportedEncodingException e) {
                        throw new RuntimeException(e);
                    }
                })
                .findFirst()
                .orElseThrow();
        attr.stream()
                .filter(fileItem -> !fileItem.isFormField())
                .forEach(fileItem -> {
                    try {
                        fileItem.write(new File(url+SEPARATOR+fileItem.getName()));
                    } catch (Exception e) {
                        throw new RuntimeException(e);
                    }
                });
        return "success";
    }

    @Override
    public boolean delete(String url) {
        return new File(url).delete();
    }

    @Override
    public boolean reName(String url, String name) {
        File old = new File(url);
        return old.renameTo(new File(old.getParent() + SEPARATOR + name));
    }

    @Override
    public boolean mkdir(String url, String name) {
        return new File(url + SEPARATOR + name).mkdir();
    }

    @Override
    public boolean watch(String url) {
        Path path =Paths.get(url);
        if (watchServiceMap.containsKey(path)) {
            WatchKey watchKey = watchServiceMap.get(path).poll();
            if (watchKey == null) {
                return false;
            }
            watchKey.pollEvents();
            watchKey.reset();
            return true;
        }
        throw new RuntimeException("未添加的监听");
    }

    @Override
    public boolean addWatch(String url) {
        Path path = Paths.get(url);
        try {
            WatchService watchService = FileSystems.getDefault().newWatchService();
            path.register(watchService, ENTRY_CREATE, ENTRY_DELETE, ENTRY_MODIFY);
            watchServiceMap.put(path, watchService);
            pathCount.compute(path, ((path1, atomicInteger) -> {
                if (atomicInteger != null) {
                    atomicInteger.addAndGet(1);
                    return atomicInteger;
                } else {
                    return new AtomicInteger(1);
                }
            }));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return watch(url);
    }

    @Override
    public boolean removeWatch(String url) {
        Path path = Paths.get(url);
        if (watchServiceMap.containsKey(path)) {
            AtomicInteger count = pathCount.get(path);
            count.addAndGet(-1);
            if (count.get() > 0) {
                return true;
            }
            pathCount.remove(path);
            try {
                watchServiceMap.get(path).close();
                watchServiceMap.remove(path);
                return true;
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
        return true;
    }

}
