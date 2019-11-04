package controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class ConsoleController {

    @GetMapping({"/console","/console.html"})
    public String console(){
            return "console_page";
    }

}
