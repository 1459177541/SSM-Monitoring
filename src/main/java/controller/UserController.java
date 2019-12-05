package controller;

import controller.vo.Response;
import model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import service.SignService;
import service.UserService;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;

@Controller
public class UserController {

    private final UserService userService;
    private final SignService signService;

    @Autowired
    public UserController(UserService userService, SignService signService) {
        this.userService = userService;
        this.signService = signService;
    }

    @PostMapping({"/sign_in"})
    @ResponseBody
    public Response<String> signIn(User user, HttpServletResponse response){
        if (userService.login(user)) {
            user = userService.getUserInfo(user.getId());
            Cookie cookie = new Cookie("uuid", signService.add(user).toString());
            cookie.setMaxAge(30 * 60);
            response.addCookie(cookie);
            return Response.success("/console");
        } else {
            return Response.fail("").setMessage("用户名或密码错误");
        }
    }

    @GetMapping({"/sign_out"})
    public String signOut(@CookieValue("uuid") String uuid) {
        signService.remove(uuid);
        return "redirect:/";
    }


    @PostMapping({"/sign_up"})
    @ResponseBody
    public Response<Long> signUp(User user, HttpServletResponse response) {
        try {
            long id = userService.register(user);
            Cookie cookie = new Cookie("uuid", signService.add(userService.getUserInfo(id)).toString());
            cookie.setMaxAge(30 * 60);
            response.addCookie(cookie);
            return Response.success(id);
        } catch (Exception e) {
            return Response.fail(0L).setMessage(e.getMessage());
        }
    }
}
