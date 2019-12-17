package service;

import controller.vo.FileInfo;
import org.apache.commons.fileupload.FileItem;

import java.util.List;

public interface FileService {

    public List<String> getRootPath();

    List<FileInfo> getFileList(String url);

    String upload(List<FileItem> attr);
}
