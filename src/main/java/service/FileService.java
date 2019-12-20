package service;

import controller.vo.FileInfo;
import org.apache.commons.fileupload.FileItem;

import java.io.OutputStream;
import java.util.List;

public interface FileService {

    public List<String> getRootPath();

    public List<FileInfo> getFileList(String url);

    public String upload(List<FileItem> attr);

    public boolean delete(String url);

    public boolean reName(String url, String name);

    public boolean mkdir(String url, String name);

    public boolean watch(String url);

    public boolean addWatch(String url);

    public boolean removeWatch(String url);

    public int transfer(String url, OutputStream outputStream);

}
