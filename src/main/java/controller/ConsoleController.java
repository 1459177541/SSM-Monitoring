package controller;

import model.Power;
import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.FileUploadException;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import service.*;
import controller.vo.*;
import org.springframework.web.bind.annotation.*;
import org.springframework.beans.factory.annotation.Autowired;

import javax.imageio.ImageIO;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/console/")
public class ConsoleController {

    private final SignService signService;
    private final CpuService cpuService;
    private final MemService memService;
    private final FileService fileService;
    private final DiskService diskService;
    private final NetService netService;
    private final RemoteDesktopService desktopService;
    private final UserService userService;

    @Autowired
    public ConsoleController(SignService signService,
                             CpuService cpuService,
                             MemService memService,
                             FileService fileService,
                             DiskService diskService,
                             NetService netService,
                             RemoteDesktopService desktopService,
                             UserService userService) {
        this.signService = signService;
        this.cpuService = cpuService;
        this.memService = memService;
        this.fileService = fileService;
        this.diskService = diskService;
        this.netService = netService;
        this.desktopService = desktopService;
        this.userService = userService;
    }

    @GetMapping("power")
    public Response<List<String>> getPower(@CookieValue(value = "uuid", required = false) String uuid) {
        if (uuid == null) {
            List<String> data = new ArrayList<>();
            return Response.fail(data).setMessage("未登录");
        }
        return Response.success(
                signService
                        .getUser(uuid)
                        .getPower()
                        .stream()
                        .map(Power::name)
                        .collect(Collectors.toList()));
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
    public Response<List<DiskInfo>> disk() {
        return Response.create(diskService::getDiskInfo);
    }

    @GetMapping("file_root_path")
    public Response<List<String>> rootPath() {
        return Response.create(fileService::getRootPath);
    }

    @PostMapping("file_list")
    public Response<List<FileInfo>> fileList(@RequestParam("url") String url) {
        return Response.create(() -> fileService.getFileList(url));
    }

    @PostMapping(value = "file_upload")
    public Response<String> fileUpload(HttpServletRequest request) throws FileUploadException {
        List<FileItem> attr = new ServletFileUpload(new DiskFileItemFactory()).parseRequest(request);
        return Response.create(() -> fileService.upload(attr));
    }

    @PostMapping("file_download/{fileName}")
    public void fileDownload(HttpServletResponse response,
                             @RequestParam("url") String url,
                             @PathVariable String fileName) throws IOException {
        fileService.transfer(URLDecoder.decode(url, StandardCharsets.UTF_8), response.getOutputStream());
    }

    @PostMapping("file_delete")
    public Response<Boolean> fileDelete(@RequestParam("url") String url) {
        return Response.create(() -> fileService.delete(url));
    }

    @PostMapping("file_rename")
    public Response<Boolean> fileRename(@RequestParam("url") String url, @RequestParam("name") String name) {
        return Response.create(() -> fileService.reName(url, name));
    }

    @PostMapping("file_mkdir")
    public Response<Boolean> fileMkdir(@RequestParam("url") String url, @RequestParam("name") String name) {
        return Response.create(() -> fileService.mkdir(url, name));
    }

    @PostMapping("file_watch")
    public Response<Boolean> fileWatch(@RequestParam("url") String url) {
        return Response.create(() -> fileService.watch(url));
    }

    @PostMapping("file_add_watch")
    public Response<Boolean> fileAddWatch(@RequestParam("url") String url) {
        return Response.create(() -> fileService.addWatch(url));
    }

    @PostMapping("file_remove_watch")
    public Response<Boolean> fileRemoveWatch(@RequestParam("url") String url) {
        return Response.create(() -> fileService.removeWatch(url));
    }

    @GetMapping("desktop")
    public void desktop(HttpServletResponse response) throws IOException {
        ImageIO.write(desktopService.screenshots(), "jpeg", response.getOutputStream());
    }

    @GetMapping("user_list")
    public Response<List<UserInfo>> userList() {
        return Response.create(userService::getUserInfoList);
    }

    @PostMapping("user_modify_power")
    public Response<Boolean> modifyPower(UserInfo userInfo) {
        return Response.create(() -> userService.modifyPower(userInfo));
    }

    @PostMapping("user_reset_password")
    public Response<Boolean> resetPassword(@RequestParam("id") long id, @RequestParam("password") String password) {
        return Response.create(() -> userService.resetPassword(id, password));
    }
}
