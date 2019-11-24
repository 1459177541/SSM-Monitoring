package controller;

import controller.vo.Response;
import model.MemInfo;
import model.Status;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import service.CpuService;
import service.FileService;
import service.MemService;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static controller.inteceptor.UserInterceptor.UUID;

@RestController
public class ConsoleController {

    private final CpuService cpuService;
    private final MemService memService;
    private final FileService fileService;

    @Autowired
    public ConsoleController(CpuService cpuService,
                             MemService memService,
                             FileService fileService) {
        this.cpuService = cpuService;
        this.memService = memService;
        this.fileService = fileService;
    }

    @GetMapping("/console/power")
    public Response<List<String>> getPower(@CookieValue(value = "uuid", required = false) Integer uuid){
        if (uuid == null) {
            List<String> data = new ArrayList<>();
            return Response.fail(data).setMessage("未登录");
        }
        return Response.success(UUID.get(uuid)
                .getUserGroups()
                .stream()
                .flatMap(userGroup -> userGroup.getPower().stream())
                .distinct()
                .collect(Collectors.toList()));
    }

    @GetMapping("/console/cpu_info")
    public Response<List<String>> cpuInfo() {
        return Response.success(cpuService.cpuInfo());
    }

    @GetMapping("/console/cpu_status")
    public Response<Status> cpuStatus() {
        return Response.success(cpuService.cpuStatus());
    }

    @GetMapping("/console/mem_info")
    public Response<List<MemInfo>> memInfo() {
        return Response.success(memService.memInfo());
    }

    @GetMapping("/console/mem_status")
    public Response<Status> memStatus() {
        return Response.success(memService.memStatus());
    }

    @GetMapping("/console/root_path")
    public Response<List<String>> rootPath(){
        return Response.success(fileService.getRootPath());
    }

}
