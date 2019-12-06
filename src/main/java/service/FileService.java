package service;

import controller.vo.FileInfo;

import java.util.List;

public interface FileService {

    public List<String> getRootPath();

    List<FileInfo> getFileList(String url);
}
