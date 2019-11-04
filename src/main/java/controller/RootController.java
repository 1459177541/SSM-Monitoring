package controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class RootController {

    @GetMapping({"/","/index","/index.html"})
    public String index(){
        return "index_page";
    }

    @GetMapping({"/sign_in","sign_in.html"})
    public String login(){
        return "sign_in_page";
    }

    @GetMapping({"/sign_up","sign_in.html"})
    public String signUp(){
        return "sign_up_page";
    }

    @GetMapping({"/err"})
    public String err(@RequestParam("err") int err){
        return "err_page";
    }

}
