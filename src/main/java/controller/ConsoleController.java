package controller;

import controller.vo.Response;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static controller.inteceptor.UserInterceptor.UUID;

@RestController
public class ConsoleController {

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

}
