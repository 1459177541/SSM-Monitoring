package controller;

import controller.vo.Response;
import model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import service.UserService;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ThreadLocalRandom;

@Controller
public class UserController {

    private final UserService userService;

    static ConcurrentHashMap<Integer, User> UUID = new ConcurrentHashMap<>();
    static ThreadLocalRandom random = ThreadLocalRandom.current();

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping({"/sign_in"})
    @ResponseBody
    public Response<String> sign_in(User user, HttpServletResponse response){
        if (userService.login(user)) {
            user = userService.getUserInfo(user.getId());
            int uuid;
            do{
                uuid = random.nextInt(Integer.MAX_VALUE);
            } while (UUID.putIfAbsent(uuid, user) != null);
            Cookie cookie = new Cookie("uuid", String.valueOf(uuid));
            cookie.setMaxAge(30*60);
            response.addCookie(cookie);
            return Response.success("/console");
        } else {
            return Response.fail("").setMessage("用户名或密码错误");
        }
    }

}
