package controller.vo;

import java.io.File;

public class FileInfo {
    private String name;
    private boolean file;
    private String suffix;
    private String url;


    public String getName() {
        return name;
    }

    public FileInfo setName(String name) {
        this.name = name;
        return this;
    }

    public boolean isFile() {
        return file;
    }

    public FileInfo setFile(boolean file) {
        this.file = file;
        return this;
    }

    public String getSuffix() {
        return suffix;
    }

    public FileInfo setSuffix(String suffix) {
        this.suffix = suffix;
        return this;
    }

    public String getUrl() {
        return url;
    }

    public FileInfo setUrl(String url) {
        this.url = url;
        return this;
    }

    public static FileInfo create(File file) {
        FileInfo fileInfo = new FileInfo()
                .setName(file.getName())
                .setUrl(file.getAbsolutePath());
        int last = fileInfo.getName().lastIndexOf('.');
        if (last != -1) {
            fileInfo.setFile(true).setSuffix(fileInfo.getName().substring(last+1));
        }
        return fileInfo;
    }
}
