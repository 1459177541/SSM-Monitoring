package controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.GetMapping;

import javax.servlet.http.HttpServletResponse;

import static controller.UserController.UUID;

@Controller
public class ConsoleController {

    @GetMapping({"/console","/console.html"})
    public String console(@CookieValue("uuid") String uuid, HttpServletResponse response){
        if (uuid != null && UUID.containsKey(Integer.parseInt(uuid))) {
            return "console_page";
        } else {
            response.setStatus(HttpServletResponse.SC_FORBIDDEN);
            return null;
        }
    }

}
