package controller;

import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.FileUploadException;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import service.*;
import controller.vo.*;
import org.springframework.web.bind.annotation.*;
import org.springframework.beans.factory.annotation.Autowired;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/console/")
public class ConsoleController {

    private final SignService signService;
    private final CpuService cpuService;
    private final MemService memService;
    private final FileService fileService;
    private final DiskService diskService;
    private final NetService netService;

    @Autowired
    public ConsoleController(SignService signService,
                             CpuService cpuService,
                             MemService memService,
                             FileService fileService,
                             DiskService diskService,
                             NetService netService) {
        this.signService = signService;
        this.cpuService = cpuService;
        this.memService = memService;
        this.fileService = fileService;
        this.diskService = diskService;
        this.netService = netService;
    }

    @GetMapping("power")
    public Response<List<String>> getPower(@CookieValue(value = "uuid", required = false) String uuid){
        if (uuid == null) {
            List<String> data = new ArrayList<>();
            return Response.fail(data).setMessage("未登录");
        }
        return Response.success(signService.getUser(uuid).getPower());
    }

    @GetMapping("cpu_info")
    public Response<List<String>> cpuInfo() {
        return Response.create(cpuService::cpuInfo);
    }

    @GetMapping("cpu_status")
    public Response<Status> cpuStatus() {
        return Response.create(cpuService::cpuStatus);
    }

    @GetMapping("mem_info")
    public Response<List<MemInfo>> memInfo() {
        return Response.create(memService::memInfo);
    }

    @GetMapping("mem_status")
    public Response<Status> memStatus() {
        return Response.create(memService::memStatus);
    }

    @GetMapping("net_info")
    public Response<List<String>> netInfo() {
        return Response.create(netService::netInfo);
    }

    @GetMapping("net_status")
    public Response<List<Status>> netStatus() {
        return Response.create(netService::netStatus);
    }

    @GetMapping("disk")
    public Response<List<DiskInfo>> disk(){
        return Response.create(diskService::getDiskInfo);
    }

    @GetMapping("root_path")
    public Response<List<String>> rootPath(){
        return Response.create(fileService::getRootPath);
    }

    @PostMapping("file_list")
    public Response<List<FileInfo>> fileList(@RequestParam("url") String url) {
        return Response.create(()->fileService.getFileList(url));
    }

    @PostMapping(value = "file_upload")
    public Response<String> fileUpload(HttpServletRequest request) throws FileUploadException {
        List<FileItem> attr = new ServletFileUpload(new DiskFileItemFactory()).parseRequest(request);
        return Response.create(() -> fileService.upload(attr));
    }
}
