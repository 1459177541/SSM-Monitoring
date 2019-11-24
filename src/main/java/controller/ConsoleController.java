package controller;

import controller.vo.DiskInfo;
import controller.vo.Response;
import controller.vo.MemInfo;
import controller.vo.Status;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import service.CpuService;
import service.DiskService;
import service.FileService;
import service.MemService;

import java.util.ArrayList;
import java.util.List;

import static controller.inteceptor.UserInterceptor.UUID;

@RestController
@RequestMapping("/console/")
public class ConsoleController {

    private final CpuService cpuService;
    private final MemService memService;
    private final FileService fileService;
    private final DiskService diskService;

    @Autowired
    public ConsoleController(CpuService cpuService,
                             MemService memService,
                             FileService fileService,
                             DiskService diskService) {
        this.cpuService = cpuService;
        this.memService = memService;
        this.fileService = fileService;
        this.diskService = diskService;
    }

    @GetMapping("power")
    public Response<List<String>> getPower(@CookieValue(value = "uuid", required = false) Integer uuid){
        if (uuid == null) {
            List<String> data = new ArrayList<>();
            return Response.fail(data).setMessage("未登录");
        }
        return Response.success(UUID.get(uuid).getPower());
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

    @GetMapping("disk")
    public Response<List<DiskInfo>> disk(){
        return Response.create(diskService::getDiskInfo);
    }

    @GetMapping("root_path")
    public Response<List<String>> rootPath(){
        return Response.create(fileService::getRootPath);
    }

}
