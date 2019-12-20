package service;

import controller.vo.FileInfo;
import org.apache.commons.fileupload.FileItem;

import java.util.List;

public interface FileService {

    List<String> getRootPath();

    List<FileInfo> getFileList(String url);

    String upload(List<FileItem> attr);

    boolean delete(String url);

    boolean reName(String url, String name);

    boolean mkdir(String url, String name);

    boolean watch(String url);

    boolean addWatch(String url);

    boolean removeWatch(String url);
}
