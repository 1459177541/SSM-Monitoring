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
import java.io.UnsupportedEncodingException;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
public class FileServiceImpl implements FileService {

    private final Sigar sigar;
    private static final String SEPARATOR = System.getProperty("file.separator");

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

}
